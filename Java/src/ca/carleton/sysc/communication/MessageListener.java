package ca.carleton.sysc.communication;

import ca.carleton.sysc.common.MessageProcessor;
import ca.carleton.sysc.common.types.Command;
import org.apache.commons.lang3.ArrayUtils;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageListener implements Runnable {

    private final UdpTransceiver udpTransceiver;

    private boolean shutdown;

    public MessageListener() {
        this.udpTransceiver = new UdpTransceiver();
    }

    @Override
    public void run() {

        while(!shutdown) {

            this.udpTransceiver.receive();

            // Using arbitrary values for now
            final List<Byte> testMessage = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(Command.DEVELOPER.name().getBytes())));
            testMessage.add((byte)0x0);
            testMessage.addAll(Arrays.asList(ArrayUtils.toObject("$$\r\n".getBytes())));
            testMessage.add((byte)0x0);

            byte[] testBytes = ArrayUtils.toPrimitive(testMessage.toArray(new Byte[0]));
            final DatagramPacket packet = new DatagramPacket(testBytes, testBytes.length);

            new MessageProcessor(packet).run();
        }
    }
}
