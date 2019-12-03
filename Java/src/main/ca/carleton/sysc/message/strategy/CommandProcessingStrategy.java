package main.ca.carleton.sysc.message.strategy;

public interface CommandProcessingStrategy {

    /**
     * Processes the incoming message and return a result of the operation
     * @return return value to relay back to the user
     */
    String execute();

}
