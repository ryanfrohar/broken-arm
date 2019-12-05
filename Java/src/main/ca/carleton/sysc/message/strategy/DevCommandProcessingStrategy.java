package main.ca.carleton.sysc.message.strategy;

import main.ca.carleton.sysc.communication.ArduinoTransceiver;
import main.ca.carleton.sysc.message.Input;
import main.ca.carleton.sysc.util.GCodeTransformer;

public class DevCommandProcessingStrategy implements CommandProcessingStrategy {

    private final Input input;

    private final ArduinoTransceiver arduinoIO;

    public DevCommandProcessingStrategy(final Input input) {
        this.input = input;
        this.arduinoIO = ArduinoTransceiver.getInstance();
    }

    @Override
    public String execute() {
        final String gCode = String.join(" ", this.input.getParameters());
        final String newGCode = new GCodeTransformer(10,0,0).cartesianToVPlot(gCode);
        this.arduinoIO.write(newGCode);
        return this.arduinoIO.read();
    }

}
