package main.ca.carleton.sysc.message.strategy;

import main.ca.carleton.sysc.message.Input;
import main.ca.carleton.sysc.communication.ArduinoTransceiver;

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
