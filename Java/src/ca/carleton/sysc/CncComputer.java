package ca.carleton.sysc;

import ca.carleton.sysc.communication.MessageListener;

import javax.naming.Context;

public class CncComputer {

    public static void main(final String[] args) {

        // Begin the main thread that will listen to new inputs
        final MessageListener messageListener = new MessageListener();
        System.out.println("Starting message listener...");
        messageListener.run();

    }
}
