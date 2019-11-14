package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.message.Input;

/**
 * Evaluates simple commands with no parameters straight from the Command enum
 */
public class SimpleCommandStrategy implements MessageProcessingStrategy {

    private final Input input;

    public SimpleCommandStrategy(final Input input) {
        this.input = input;
    }

    @Override
    public String execute() {
        return this.input.getCommand().getCode();
    }
}
