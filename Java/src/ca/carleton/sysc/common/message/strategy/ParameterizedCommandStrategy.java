package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.types.Command;

/**
 * Evaluates a parameterized command into executable g-code
 */
public class ParameterizedCommandStrategy implements MessageProcessingStrategy {

    private final Command command;

    private final String[] parameters;

    public ParameterizedCommandStrategy(final Command command, final String[] parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    @Override
    public String processMessage() {
        return String.format(this.command.getCode(), (Object[]) this.parameters);
    }
}
