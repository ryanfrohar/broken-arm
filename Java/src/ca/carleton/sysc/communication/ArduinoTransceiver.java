package ca.carleton.sysc.communication;

import com.fazecast.jSerialComm.SerialPort;
import org.apache.commons.lang3.time.StopWatch;

public class ArduinoTransceiver {

    private static final int TIME_OUT = 500;

    private static final int SLEEP = 20;

    public void write(final String message) {
        final SerialPort comPort = SerialPort.getCommPorts()[0];
        final byte[] bytes = message.getBytes();

        comPort.writeBytes(bytes, bytes.length);

        comPort.closePort();
    }

    public String read() {
        final SerialPort comPort = SerialPort.getCommPorts()[0];
        final StopWatch stopWatch = new StopWatch();
        String message;

        stopWatch.start();
        comPort.openPort();

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
