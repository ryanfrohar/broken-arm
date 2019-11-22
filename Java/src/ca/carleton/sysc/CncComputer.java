package ca.carleton.sysc;

import ca.carleton.sysc.cli.CLI;
import ca.carleton.sysc.types.Command;
import ca.carleton.sysc.communication.ArduinoTransceiver;
import ca.carleton.sysc.communication.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CncComputer {

    private static final Logger LOG = LoggerFactory.getLogger(CncComputer.class);

    private static final int SLEEP = 5_000;

    public static void main(final String[] args) {

        final boolean success = CncComputer.initArduino();

        if (success) {
            LOG.info("Starting application");
            new Thread(new MessageListener()).start();
            new CLI().prompt();
        } else {
            LOG.error("Arduino could not be connected to. Please check connection and try again. Exiting ");
        }
    }

    /**
     * Checks if the arduino is online
     */
    private static boolean initArduino() {
        LOG.info("Connecting to GRBL");
        final ArduinoTransceiver arduinoIO = ArduinoTransceiver.getInstance();

        while (!arduinoIO.isConnected()) {
            arduinoIO.connect();
            try {
                Thread.sleep(SLEEP);
            } catch (InterruptedException e) {
                LOG.info("Main thread interrupted.");
                break;
            }
            LOG.info("Failed to connect, retiring...");
        }

        if(arduinoIO.isConnected()) {
            arduinoIO.write(Command.WAKE_UP.getCode());
            final String msg = arduinoIO.read();
            LOG.info("Connected to GRBL {}", msg);
            return true;
        } else {
            return false;
        }
    }
}
