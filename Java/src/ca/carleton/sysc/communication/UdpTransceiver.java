package ca.carleton.sysc.communication;

import javax.xml.crypto.Data;
import java.net.DatagramPacket;

public class UdpTransceiver {

    public DatagramPacket receive() {
        try {
            Thread.sleep(600_000_000);
        } catch (InterruptedException e) {
            //LOG.info("Main thread interrupted.");
        }
        return null;
    }

    public void send(final DatagramPacket packet) {

    }

}
