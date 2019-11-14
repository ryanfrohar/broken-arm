package ca.carleton.sysc.communication;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpTransceiver {
// TODO Public receive and send
    private final static int PACKET_SIZE = 508; // Size of the datagram packet
    private final static int port = 1010;

    public DatagramPacket receive() {
        // Check the argument
        try
        {
            // Construct the socket
            DatagramSocket socket = new DatagramSocket( port ) ;

            for( ;; )
            {
                System.out.println( "Receiving on port " + port ) ;
                DatagramPacket packet = new DatagramPacket( new byte[PACKET_SIZE], PACKET_SIZE ) ;
                socket.receive( packet ) ;

                System.out.println( packet.getAddress() + " " + packet.getPort() + ": " + new String(packet.getData()).trim() ) ;
                return packet;
            }
        }
        catch( Exception e )
        {
            System.out.println( e ) ;
            return null;
        }
    }


    public void send( final DatagramPacket packet){

    }

}
