package ca.carleton.sysc.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.commons.lang3.time.StopWatch;

public class ArduinoTransceiver {

    private static final int TIME_OUT = 10000;

    private static final int SLEEP = 20;

    public void write(final String message) {
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

        final byte[] bytes = message.getBytes();

        comPort.writeBytes(bytes, bytes.length);

        comPort.closePort();
    }

    public String read() {
        final SerialPort comPort = SerialPort.getCommPorts()[1];
        final StopWatch stopWatch = new StopWatch();
        String message;

        stopWatch.start();
        comPort.openPort();
        comPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[comPort.bytesAvailable()];
                int numRead = comPort.readBytes(newData, newData.length);
                System.out.println("Read " + numRead + " bytes.");
                System.out.println(new String(newData));
            }
        });

        while (comPort.bytesAvailable() == 0 && stopWatch.getTime() < TIME_OUT) {
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                System.out.println("Thread interrupted, aborting read.");
                break;
            }
        }

        if (comPort.bytesAvailable() != 0) {
            byte[] readBuffer = new byte[comPort.bytesAvailable()];
            int numRead = comPort.readBytes(readBuffer, readBuffer.length);
            System.out.println("Read " + numRead + " bytes.");
            message = new String(readBuffer);
        } else {
            System.out.println("no bytes to read.");
            message = "";
        }

        return message;
    }

}
