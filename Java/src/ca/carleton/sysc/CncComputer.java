package ca.carleton.sysc;

import ca.carleton.sysc.communication.MessageListener;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

public class CncComputer {

    public static void main(final String[] args) {

        // Check if Arduino is online
        final SerialPort comPort = SerialPort.getCommPorts()[1];
        comPort.openPort();

        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_WRITTEN; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN)
                    System.out.println("All bytes were successfully transmitted!");
            }
        });

        comPort.addDataListener(new SerialPortMessageListener()
        {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_RECEIVED; }

            @Override
            public byte[] getMessageDelimiter() { return new byte[] { (byte)0x0D }; }

            @Override
            public boolean delimiterIndicatesEndOfMessage() { return true; }

            @Override
            public void serialEvent(SerialPortEvent event)
            {
                byte[] delimitedMessage = event.getReceivedData();
                System.out.println("Received the following delimited message: " + delimitedMessage);
            }
        });

        // wake up grbl
        byte[] bytes = "\r\n\r\n".getBytes();
        comPort.writeBytes(bytes, bytes.length);
        try {
            Thread.sleep(2_000);
        } catch (InterruptedException e) {
            System.out.println("Thread interrupted, aborting read.");
        }

        comPort.closePort();


        // Begin the main thread that will listen to new inputs
        final MessageListener messageListener = new MessageListener();
        System.out.println("Starting message listener...");
        messageListener.run();

    }
}
