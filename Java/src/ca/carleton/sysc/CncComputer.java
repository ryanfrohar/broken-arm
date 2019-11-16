package ca.carleton.sysc;

import ca.carleton.sysc.common.types.Command;
import ca.carleton.sysc.communication.ArduinoTransceiver;
import ca.carleton.sysc.communication.MessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CncComputer {

    private static final Logger LOG = LoggerFactory.getLogger(CncComputer.class);

    public static void main(final String[] args) {

        CncComputer.initArduino();

        // Begin the main thread that will listen to new inputs
        final MessageListener messageListener = new MessageListener();
        LOG.info("Starting message listener");
        messageListener.run();
    }

    /**
     * Checks if the arduino is online
     */
    private static void initArduino() {
        final ArduinoTransceiver arduinoIO = new ArduinoTransceiver();
        arduinoIO.write(Command.WAKE_UP.getCode());
        final String msg = arduinoIO.read();
        LOG.info("Arduino GRBL online {}", msg);
    }
}
