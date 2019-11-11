package ca.carleton.sysc.communication;

import ca.carleton.sysc.common.MessageProcessor;

public class MessageListener implements Runnable {

    @Override
    public void run() {

//        while(true) {
            // Listen for UDP messages and assign...
            // Using arbitrary value for now
            final String[] messageArgs = new String[] {"STATUS"};

            new MessageProcessor(messageArgs).run();
//        }
    }
}
