package ca.carleton.sysc.common;

import ca.carleton.sysc.common.message.strategy.MessageProcessingStrategy;
import ca.carleton.sysc.common.message.strategy.ParameterizedCommandStrategy;
import ca.carleton.sysc.common.message.strategy.SendTextCommandStrategy;
import ca.carleton.sysc.common.message.strategy.SimpleCommandStrategy;
import ca.carleton.sysc.common.types.Command;
import ca.carleton.sysc.communication.ArduinoTransceiver;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;

/**
 * Processes the input message into g-code for the Arduino
 */
public class MessageProcessor implements Runnable {

    private final String[] messageArgs;

    private final ArduinoTransceiver arduinoTransceiver;

    // TODO needs the return address of the UDP packet such that it can return results
    public MessageProcessor(String[] messageArgs) {
        this.messageArgs = messageArgs;
        this.arduinoTransceiver = new ArduinoTransceiver();
    }

    @Override
    public void run() {

        if (messageArgs.length < 1 || !EnumUtils.isValidEnum(Command.class, messageArgs[0])) {
            System.out.println(String.format("Command not recognized: %s", Arrays.toString(messageArgs)));
            // TODO return an error message back to the application
            return;
        }

        Command command = Command.valueOf(messageArgs[0]);
        System.out.println(String.format("Command recognized: %s", command.name()));

        final MessageProcessingStrategy strategy = this.getStrategy(command);

        // FIXME This is a gross over simplification. There could be multiple lines of g-code to write
        String gCode = strategy.processMessage();
        this.arduinoTransceiver.write(gCode);

        // TODO return via udp the result
    }

    private MessageProcessingStrategy getStrategy(final Command command) {
        final MessageProcessingStrategy strategy;

        if(messageArgs.length == 1) {
            strategy = new SimpleCommandStrategy(command);
        } else if(command.equals(Command.SEND_TEXT)) {
            strategy = new SendTextCommandStrategy(command, messageArgs[1]);
        } else {
            strategy = new ParameterizedCommandStrategy(command, Arrays.copyOfRange(messageArgs, 1, messageArgs.length));
        }

        return strategy;
    }
}
