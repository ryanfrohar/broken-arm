package main.ca.carleton.sysc.message.strategy;

import main.ca.carleton.sysc.communication.ArduinoTransceiver;
import main.ca.carleton.sysc.message.Input;
import main.ca.carleton.sysc.types.Command;
import main.ca.carleton.sysc.util.ConfigManager;
import main.ca.carleton.sysc.util.GCodeTransformer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

/**
 * Evaluates a text message into executable g-code and saves it to be executed on the START_JOB command
 */
public class SendTextCommandStrategy implements CommandProcessingStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(SendTextCommandStrategy.class);

    private static final String FONT_PATH = "../../../../Python/F-Engrave-1.68_src/fonts/";

    private static final String F_ENGRAVE_PATH = "../../../../Python/F-Engrave-1.68_src/f-engrave.py";

    private static final String CXF = ".cxf";

    private static final String START = "G90";

    private final Input input;

    private final GCodeTransformer converter;

    private final ArduinoTransceiver arduinoIO;

    public SendTextCommandStrategy(final Input input) {
        this.input = input;
        this.arduinoIO = ArduinoTransceiver.getInstance();
        this.converter = new GCodeTransformer();
    }

    @Override
    public String execute() {
        List<String> parameters = input.getParameters();
        final String font = parameters.get(0);
        final String text = String.join("", parameters.subList(1, parameters.size()));
        boolean started = false;

        try {
            final String fontPath = new File(FONT_PATH + font + CXF).getCanonicalPath();
            final String scriptPath = new File(F_ENGRAVE_PATH).getCanonicalPath();

            final String[] args = new String[] {"python", scriptPath, "-b", "-f", fontPath, "-t " + text};
            LOG.debug("Executing engrave g-code generate command: {}", Arrays.toString(args));
            final ProcessBuilder pb = new ProcessBuilder(args);

            final Process process = pb.start();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            this.killAlarms();
            this.setCurrentLocationAsZero();
            this.gotoTopLeft();

            String response = "";
            for (String line = ""; line != null; line = reader.readLine()) {
                if(started) {
                    String processedLine = converter.cartesianToVPlot(line);
                    LOG.info(processedLine);
                    response = this.writeLine(processedLine);
                    LOG.info(response);
                } else {
                    started = line.contains(START);
                }
            }

            this.gotoZero();

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String writeLine(final String line) {
        this.arduinoIO.write(line);

        String response;
        do {
            LOG.info(response = this.arduinoIO.read());
        } while(StringUtils.isEmpty(response));
        return response;
    }

    private void killAlarms() {
        final String line = Command.KILL_ALARMS.getCode();
        LOG.info("killing any alarms present: {}", line);
        this.writeLine(line);
    }

    private void setCurrentLocationAsZero() {
        final String line = Command.SET_CUR_AS_ZERO.getCode();
        LOG.info("setting current location at bottom left as zero position: {}", line);
        this.writeLine(line);
    }

    private void gotoTopLeft() {
        this.writeLine(Command.LIFT.getCode());

        final String line = String.format(Command.GOTO.getCode(), this.converter.calcInitialA(), this.converter.calcInitialB());
        LOG.info("setting location to top left position: {}", line);
        this.writeLine(line);
    }

    private void gotoZero() {
        final String line = Command.ZERO.getCode();
        LOG.info("setting location to bottom left zero position: {}", line);
        this.writeLine(line);
    }

}