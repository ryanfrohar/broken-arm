package main.ca.carleton.sysc.message;

import main.ca.carleton.sysc.types.Command;

import java.util.List;

/**
 * Input bean to represent the shape of the received message from the user
 */
public class Input {

    private Command command;

    private List<String> parameters;

    public Input(Command command, List<String> parameters) {
        this.command = command;
        this.parameters = parameters;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }
}
