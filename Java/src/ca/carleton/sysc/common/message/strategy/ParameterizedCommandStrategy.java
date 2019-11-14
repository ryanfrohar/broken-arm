package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.message.Input;
import ca.carleton.sysc.common.types.Command;
import ca.carleton.sysc.communication.ArduinoTransceiver;

/**
 * Evaluates a parameterized command into executable g-code
 */
public class ParameterizedCommandStrategy implements MessageProcessingStrategy {

    private final Input input;

    private final ArduinoTransceiver arduinoTransceiver;

    public ParameterizedCommandStrategy(final Input input) {
        this.input = input;
        this.arduinoTransceiver = new ArduinoTransceiver();
    }

    @Override
    public String execute() {
        String gCode = String.format(this.input.getCommand().getCode(), this.input.getParameters().toArray());

        this.arduinoTransceiver.write(gCode);
//        return this.arduinoTransceiver.read();
        return "test";
    }
}
