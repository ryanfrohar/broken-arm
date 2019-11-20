package sysc.common;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sysc.common.message.Input;
import sysc.common.types.ResultType;
import sysc.common.util.PacketDataSupport;
import sysc.communication.UdpTransceiver;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes the input UDP message to be processed by the {@link CommandProcessor}
 * and returns the to the received address via UDP
 */
public class MessageProcessor implements Runnable {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessor.class);

    private final DatagramPacket packet;

    private final UdpTransceiver udpTransceiver;

    private final PacketDataSupport packetDataSupport;

    public MessageProcessor(final DatagramPacket packet) {
        this.packet = packet;
        this.udpTransceiver = new UdpTransceiver();
        this.packetDataSupport = new PacketDataSupport();
    }

    @Override
    public void run() {
        List<String> result = this.validate();
        if(!result.isEmpty()) {
            final String errorMessage = "Given message contains validation errors, ignoring message\n" + String.join("\n", result);
            LOG.error(errorMessage);
            byte[] bytes = this.packetDataSupport.buildPacketData(ResultType.ERROR.name(), errorMessage);
            this.udpTransceiver.send(new DatagramPacket(bytes, bytes.length, this.packet.getAddress(), this.packet.getPort()));
            return;
        }

        final Input input = this.packetDataSupport.getInputFromData(packet.getData());
        final String returnValue = new CommandProcessor(input).execute();

        final byte[] returnBytes = returnValue.getBytes();
        this.udpTransceiver.send(new DatagramPacket(returnBytes, returnBytes.length, this.packet.getAddress(), this.packet.getPort()));
    }

    /**
     * Validate the given DatagramPacket
     * @return list of error caught in validation
     */
    private List<String> validate() {
        List<String> errors = new ArrayList<>();
        final byte[] data = packet.getData();

        if (ArrayUtils.isEmpty(data)) {
             errors.add("packet data is empty");
        }

        return errors;
    }

}
