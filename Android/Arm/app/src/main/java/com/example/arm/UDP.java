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
            final InetAddress host = InetAddress.getByName("192.168.43.197"); //Static IP of the Raspberry Pi, 192.168.2.82 my computer "192.168.43.197" Raspberry Pi's
            final int port = 9090; // Port the Raspberry Pi is listening on
            final int numSend = 1;
            socket = new DatagramSocket();
            String message = strings[0];// This is the string inputed

            while (true) {

                byte[] convert = message.getBytes();
                //Replacing all the space is a message with null
                for(int j = 0; j < convert.length; j++) {
                    if (convert[j] == 32) {
                        convert[j] = 0;
                    }
                }


                for (int i = 0; i < numSend; i++) {
                    DatagramPacket packet1 = new DatagramPacket(convert, convert.length, host, port);
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
