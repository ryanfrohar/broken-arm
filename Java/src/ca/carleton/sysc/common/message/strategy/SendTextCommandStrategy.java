package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.types.Command;

/**
 * Evaluates a text message into executable g-code and saves it to be executed on the START_JOB command
 */
public class SendTextCommandStrategy implements MessageProcessingStrategy {

    private final Command command;

    private final String text;

    public SendTextCommandStrategy(final Command command, final String text) {
        this.command = command;
        this.text = text;
    }

    @Override
    public String processMessage() {
        return null;
    }
}
