package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.types.Command;

/**
 * Evaluates simple commands with no parameters straight from the Command enum
 */
public class SimpleCommandStrategy implements MessageProcessingStrategy {

    private final Command command;

    public SimpleCommandStrategy(final Command command) {
        this.command = command;
    }

    @Override
    public String processMessage() {
        return command.getCode();
    }
}
