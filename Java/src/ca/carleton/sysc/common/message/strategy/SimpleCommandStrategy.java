package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.message.Input;
import ca.carleton.sysc.communication.ArduinoTransceiver;

/**
 * Evaluates simple commands with no parameters straight from the Command enum
 */
public class SimpleCommandStrategy implements CommandProcessingStrategy {

    private final Input input;

    private ArduinoTransceiver arduinoIO;

    public SimpleCommandStrategy(final Input input) {
        this.input = input;
        this.arduinoIO = ArduinoTransceiver.getInstance();
    }

    @Override
    public String execute() {
        this.arduinoIO.write(this.input.getCommand().getCode());
        return this.arduinoIO.read();
    }
}
