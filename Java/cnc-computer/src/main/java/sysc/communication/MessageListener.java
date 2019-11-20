package sysc.communication;

import sysc.common.MessageProcessor;

import java.net.DatagramPacket;

/**
 * Main thread which will constantly listen for messages to delegate to processing threads
 */
public class MessageListener implements Runnable {

    private final UdpTransceiver udpTransceiver;

    private boolean shutdown;

    public MessageListener() {
        this.udpTransceiver = new UdpTransceiver();
    }

    @Override
    public void run() {
        while(!shutdown) {
            DatagramPacket packet = this.udpTransceiver.receive();
            new Thread(new MessageProcessor(packet)).start();
        }
    }
}
