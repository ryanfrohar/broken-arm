package ca.carleton.sysc.communication;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transceiver for the GRBL Arduino
 * Opens the predefined port on creation and will close the port on shutdown
 */
public class ArduinoTransceiver {

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoTransceiver.class);

    private static final int TIME_OUT = 10000;

    private static final int SLEEP = 20;

    private static final int BAUD_RATE = 115200;

    private static final int DATA_BITS = 8;

    private static final int STOP_BITS = 1;

    private static final int PARITY = 0;

    private static final String PORT = "COM4";

    private final SerialPort commPort;

    public ArduinoTransceiver() {
        this.commPort = SerialPort.getCommPort(PORT);
        this.initComPort();
    }

    public boolean connected() {
        return this.commPort.isOpen();
    }

    /**
     * Writes the given message to the comm port
     * @param message message to write
     */
    public void write(final String message) {
        final SerialPort comPort = SerialPort.getCommPorts()[1];

        final byte[] bytes = message.getBytes();
        comPort.writeBytes(bytes, bytes.length);
    }

    /**
     * Reads the available message from the comm port.
     * Empty String will be returned if no bytes are available to read
     * before the set time out {@link ArduinoTransceiver#TIME_OUT}
     * @return Available message on the comm port, empty string other wise
     */
    public String read() {
        final String message;

        this.awaitBytesAvailable();

        if (this.commPort.bytesAvailable() != 0) {
            byte[] buffer = new byte[this.commPort.bytesAvailable()];
            this.commPort.readBytes(buffer, buffer.length);
            message = new String(buffer);
        } else {
            LOG.warn("no bytes to read.");
            message = "";
        }

        return message;
    }

    private void awaitBytesAvailable() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        while (this.commPort.bytesAvailable() == 0 && stopWatch.getTime() < TIME_OUT) {
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                LOG.info("read sleep interrupted.");
                break;
            }
        }
    }

    private void initComPort() {
        this.commPort.setComPortParameters(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
        this.commPort.openPort();

        if(LOG.isDebugEnabled()) {
            this.addWriteListener();
            this.addReadListener();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                commPort.closePort();
            }
        }, "Shutdown-thread"));
    }

    private void addWriteListener() {
        this.commPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_WRITTEN; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() == SerialPort.LISTENING_EVENT_DATA_WRITTEN) {
                    LOG.debug("All bytes were successfully transmitted!");
                }
            }
        });
    }

    private void addReadListener() {
        this.commPort.addDataListener(new SerialPortDataListener() {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE) {
                    return;
                }

                byte[] newData = new byte[commPort.bytesAvailable()];
                int numRead = commPort.readBytes(newData, newData.length);
                LOG.debug("Found {} bytes to read", numRead);
            }
        });
    }

}
