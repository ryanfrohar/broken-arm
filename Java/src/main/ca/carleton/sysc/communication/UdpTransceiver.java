package main.ca.carleton.sysc.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpTransceiver {
// TODO Public receive and send
    private final static int PACKET_SIZE = 508; // Size of the datagram packet
    private final static int port = 1010;



    public DatagramPacket receive() {
        // Check the argument
        try (DatagramSocket socket = new DatagramSocket(port)) {
            // Construct the socket

            System.out.println("Receiving on port " + port);
            DatagramPacket packet = new DatagramPacket(new byte[PACKET_SIZE], PACKET_SIZE);
            socket.receive(packet);

            System.out.println(packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim());
            return packet;

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("There is an error i am returning null");
      return null;

    }


    public void send( final DatagramPacket packet){
        try (DatagramSocket socket = new DatagramSocket()) {
            // Convert the arguments first, to ensure that they are valid
            socket.send(packet);
            System.out.println("Closing down");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
