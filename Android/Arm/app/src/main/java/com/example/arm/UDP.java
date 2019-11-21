package com.example.arm;

import android.os.AsyncTask;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


class UDP extends AsyncTask<String,Void,Void> {

    @Override
    protected Void doInBackground(String... strings) {


        DatagramSocket socket = null;
        try {
            // Convert the arguments first, to ensure that they are valid
            final InetAddress host = InetAddress.getByName("172.17.60.136");
            final int port = 8080;
            final int numSend = 1;
            socket = new DatagramSocket();
            String hi = strings[0];

            while (true) {
                //if (.length() == 0) break;// Here i will insert what ever i wanna send

                byte[] what = hi.getBytes();
                //byte[] fontUsed = font.getBytes();
                //byte[] data = text.getBytes();
                //byte[] message = new byte[what.length + fontUsed.length + data.length];

                //System.arraycopy(what,0,message,0,what.length);
                //System.arraycopy(fontUsed,0,message,what.length,fontUsed.length);
                //System.arraycopy(data,0,message,fontUsed.length,data.length);


                for (int i = 0; i < numSend; i++) {
                    DatagramPacket packet1 = new DatagramPacket(what, what.length, host, port);
                    socket.send(packet1);

                }
                break;
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            if (socket != null)
                socket.close();
        }
        return null;

    }
}
