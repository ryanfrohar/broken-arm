package main.ca.carleton.sysc.message.strategy;

import main.ca.carleton.sysc.message.Input;

/**
 * Evaluates a text message into executable g-code and saves it to be executed on the START_JOB command
 */
public class SendTextCommandStrategy implements CommandProcessingStrategy {

    private final Input input;

    public SendTextCommandStrategy(final Input input) {
        this.input = input;
    }

    @Override
    public String execute() {
        return null;
    }
}
