package sysc.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sysc.common.message.Input;
import sysc.common.message.strategy.CommandProcessingStrategy;
import sysc.common.message.strategy.ParameterizedCommandStrategy;
import sysc.common.message.strategy.SendTextCommandStrategy;
import sysc.common.message.strategy.SimpleCommandStrategy;
import sysc.common.types.Command;

import java.util.ArrayList;
import java.util.List;

/**
 * Processes the input bean and relays the command to the Arduino's GRBL software
 */
public class CommandProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private final Input input;

    public CommandProcessor(final Input input) {
        this.input = input;
    }

    public String execute() {
        List<String> result = this.validate();
        if(!result.isEmpty()) {
            final String errorMessage = "Given message contains validation errors, reporting to user and ignoring message\n" + String.join("\n", result);
            LOG.error(errorMessage);
            return errorMessage;
        }

        LOG.info("Command recognized: {}", this.input.getCommand().name());

        final CommandProcessingStrategy strategy = this.getStrategy(this.input);
        return strategy.execute();
    }

    private List<String> validate() {
        List<String> errors = new ArrayList<>();

        if (this.input.getCommand() == null) {
            errors.add("Command not recognized");
        }

        return errors;
    }

    private CommandProcessingStrategy getStrategy(final Input input) {
        final CommandProcessingStrategy strategy;

        // Find a strategy by specific commands first
        if (input.getCommand() == Command.SEND_TEXT) {
            return new SendTextCommandStrategy(input);
        }

        // Find a strategy by the command type second
        switch (input.getCommand().getCommandType()) {
            case NO_PARAMETER:
                return new SimpleCommandStrategy(input);
            case PARAMETERIZED:
                return new ParameterizedCommandStrategy(input);
            default:
                throw new IllegalStateException("Unexpected value: " + input.getCommand().getCommandType());
        }
    }

}
