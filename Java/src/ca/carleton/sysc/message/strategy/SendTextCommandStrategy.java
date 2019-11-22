package ca.carleton.sysc.message.strategy;

import ca.carleton.sysc.message.CommandProcessor;
import ca.carleton.sysc.message.Input;
import ca.carleton.sysc.communication.ArduinoTransceiver;
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

    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private static final String FONT_PATH = "../../../../Python/F-Engrave-1.68_src/fonts/";

    private static final String F_ENGRAVE_PATH = "../../../../Python/F-Engrave-1.68_src/f-engrave.py";

    private static final String CXF = ".cxf";

    private static final String START = "G90";

    private final Input input;

    private final ArduinoTransceiver arduinoIO;

    public SendTextCommandStrategy(final Input input) {
        this.input = input;
        this.arduinoIO = ArduinoTransceiver.getInstance();
    }

    @Override
    public String execute() {
        List<String> parameters = input.getParameters();
        final String font = parameters.get(0);
        final String text = parameters.get(1);
        boolean started = false;


        try {
            final String fontPath = new File(FONT_PATH + font + CXF).getCanonicalPath();
            final String scriptPath = new File(F_ENGRAVE_PATH).getCanonicalPath();

            final String[] args = new String[] {"python", scriptPath, "-b", "-f", fontPath, "-t", '"' + text + '"'};
            LOG.debug("Executing engrave g-code generate command: {}", Arrays.toString(args));
            final ProcessBuilder pb = new ProcessBuilder(args);

            final Process process = pb.start();

            final BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String response = "";
            for (String line = ""; line != null; line = reader.readLine()) {
                if(started) {
                    final GCodeConverter converter = new GCodeConverter(525, 100, 200);
                    String processedLine = converter.cartesianToVPlot(line);

                    LOG.info("{}", processedLine);
                    this.arduinoIO.write(processedLine);

                    do {
                        LOG.info(response = this.arduinoIO.read());
                    } while(StringUtils.isEmpty(response));
                } else {
                    started = line.contains(START);
                }
            }

            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class GCodeConverter {

        private final List<String> CONVERTIBLE_CODES = List.of("G0", "G1");

        private final double width;

        private final double x0;

        private final double y0;

        private double previousX;

        private double previousY;

        private double minusA = 0;

        private double minusB = 0;

        public GCodeConverter(final double width, final double x0, final double y0) {
            this.width = width;
            this.x0 = x0;
            this.y0 = y0;
            this.previousX = x0;
            this.previousY = y0;
        }

        private String cartesianToVPlot(final String line) {
            final String code = this.getCode(line);
            if (line.contains("Z") || !CONVERTIBLE_CODES.contains(code)) {
                return line;
            }

            final double x = this.getX(line) + x0;
            final double y = this.getY(line) + y0;
            double a = this.calcA(x, y);
            double b = this.calcB(x, y);

            if(this.minusA == 0) {
                this.minusA = a;
                this.minusB = b;
            }

            a = Math.round((a - this.minusA) * 1000d) / 1000d;
            b = Math.round((b - this.minusB) * 1000d) / 1000d;

            return String.format("%s X%s Y%s", code, a, b);
        }

        private String getCode(final String line) {
            return line.split("\\s+")[0];
        }

        private double getX(final String line) {
            final String[] split = line.split("\\s+");
            final double x = split[1].contains("X") ? Double.parseDouble(split[1].substring(1)) : this.previousX;
            this.previousX = x;
            return x;
        }

        private double getY(final String line) {
            final String[] split = line.split("\\s+");
            final double y;

            if(split[1].contains("Y")) {
                y = Double.parseDouble(split[1].substring(1));
            } else if(split.length > 2 && split[2].contains("Y")) {
                y = Double.parseDouble(split[2].substring(1));
            } else {
                y = this.previousY;
            }
            this.previousY = y;
            return y;
        }

        private double calcA(final double x, final double y) {
            final double a;

            a = Math.sqrt(x * x + y * y);
            return Math.round(a * 1000d) / 1000d;
        }

        private double calcB(final double x, final double y) {
            final double b;

            b = Math.sqrt((this.width - x) * (this.width - x) + y * y);
            return Math.round(b * 1000d) / 1000d;
        }

    }

}
