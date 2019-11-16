package ca.carleton.sysc.common.cli;

import ca.carleton.sysc.common.CommandProcessor;
import ca.carleton.sysc.common.message.Input;
import ca.carleton.sysc.common.types.Command;
import ca.carleton.sysc.common.util.LoggingUtils;
import org.apache.commons.lang3.EnumUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CLI {

    public static final String QUIET = "quiet";
    public static final String VERBOSE = "verbose";

    private Scanner inputScanner;

    public CLI() {
        this.inputScanner = new Scanner(System.in);
        LoggingUtils.setLogToVerboseMode();
    }

    /**
     * Main prompt loop. Wait for input and process.
     */
    public void prompt() {
        System.out.println("Type 'help' for available commands.");

        while(true) {
            System.out.print("> ");
            if (!this.processInput()) {
                System.out.println("Command not found. Available commands:");
                help();
            }
        }
    }

    /**
     * Process an input.
     * @return true if process succeeded (i.e, command exists or input is empty)
     */
    private boolean processInput() {
        String line = inputScanner.nextLine();

        String[] args = line.trim().split("\\s+");
        if (args[0].isEmpty()) {
            return true;
        }

        if (EnumUtils.isValidEnum(Command.class, args[0])) {
            final Input input = new Input(Command.valueOf(args[0]), Arrays.asList(Arrays.copyOfRange(args, 1, args.length)));
            System.out.println(new CommandProcessor(input).execute());
            return true;
        }
        return false;
    }

    /**
     * Print all possible commands
     */
    protected void help() {
        for (Command command: Command.values()) {
            System.out.println(command.name());
        }
    }
}
