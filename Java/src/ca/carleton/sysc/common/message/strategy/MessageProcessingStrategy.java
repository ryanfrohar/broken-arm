package ca.carleton.sysc.common.message.strategy;

public interface MessageProcessingStrategy {

    /**
     * Processes the incoming message into executable g-code
     * @return executable g-code
     */
    String processMessage();

}
