package main.ca.carleton.sysc.message.strategy;

import main.ca.carleton.sysc.message.Input;
import main.ca.carleton.sysc.communication.ArduinoTransceiver;

/**
 * Evaluates a parameterized command into executable g-code
 */
public class ParameterizedCommandStrategy implements CommandProcessingStrategy {

    private final Input input;

    private final ArduinoTransceiver arduinoTransceiver;

    public ParameterizedCommandStrategy(final Input input) {
        this.input = input;
        this.arduinoTransceiver = ArduinoTransceiver.getInstance();
    }

    @Override
    public String execute() {
        String gCode = String.format(this.input.getCommand().getCode(), this.input.getParameters().toArray());

        this.arduinoTransceiver.write(gCode);
        return this.arduinoTransceiver.read();
    }
}
