package ca.carleton.sysc.common;

import ca.carleton.sysc.common.message.Input;
import ca.carleton.sysc.common.message.strategy.MessageProcessingStrategy;
import ca.carleton.sysc.common.message.strategy.ParameterizedCommandStrategy;
import ca.carleton.sysc.common.message.strategy.SendTextCommandStrategy;
import ca.carleton.sysc.common.message.strategy.SimpleCommandStrategy;
import ca.carleton.sysc.common.types.ResultType;
import ca.carleton.sysc.common.util.PacketDataSupport;
import ca.carleton.sysc.communication.UdpTransceiver;
import org.apache.commons.lang3.ArrayUtils;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes the input message into g-code for the Arduino
 */
public class MessageProcessor implements Runnable {

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
            final String errorMessage = "Given message contains validation errors, reporting to user and ignoring message\n" + String.join("\n", result);
            System.out.println(errorMessage);
            byte[] bytes = this.packetDataSupport.buildPacketData(ResultType.ERROR.name(), errorMessage);
            this.udpTransceiver.send(new DatagramPacket(bytes, bytes.length, this.packet.getAddress(), this.packet.getPort()));
            return;
        }

        final Input input = this.packetDataSupport.getInputFromData(packet.getData());
        System.out.println(String.format("Command recognized: %s", input.getCommand().name()));

        final MessageProcessingStrategy strategy = this.getStrategy(input);
        final String returnValue = strategy.execute();

        // return result via udp
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

        final Input input = this.packetDataSupport.getInputFromData(data);

        if (input.getCommand() == null) {
            errors.add(String.format("Command not recognized: %s", new String(data, StandardCharsets.UTF_8)));
        }

        return errors;
    }

    private MessageProcessingStrategy getStrategy(final Input input) {
        final MessageProcessingStrategy strategy;

        // Find a strategy by specific commands first
        switch(input.getCommand()) {
            case SEND_TEXT:
                return new SendTextCommandStrategy(input);
        }

        // Find a strategy by the command type second
        switch (input.getCommand().getCommandType()) {
            case NO_PARAMETER:
                return new SimpleCommandStrategy(input);
            case PARAMETERIZED:
                return new ParameterizedCommandStrategy(input);
            default:
                throw new IllegalStateException("Unexpected value: " + input.getCommand().getCommandType());
        }
    }
}
