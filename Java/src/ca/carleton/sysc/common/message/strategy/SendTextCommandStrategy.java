package ca.carleton.sysc.common.message.strategy;

import ca.carleton.sysc.common.CommandProcessor;
import ca.carleton.sysc.common.message.Input;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Evaluates a text message into executable g-code and saves it to be executed on the START_JOB command
 */
public class SendTextCommandStrategy implements CommandProcessingStrategy {

    private static final Logger LOG = LoggerFactory.getLogger(CommandProcessor.class);

    private static final String FONT_PATH = "../python/F-Engrave-1.68_src/fonts/";

    private static final String F_ENGRAVE_PATH = "../python/F-Engrave-1.68_src/f-engrave.py";

    private static final String CXF = ".cxf";

    private final Input input;

    public SendTextCommandStrategy(final Input input) {
        this.input = input;
    }

    @Override
    public String execute() {
        List<String> parameters = input.getParameters();
        final String font = parameters.get(0);
        final String text = parameters.get(1);

        try {
            final String fontPath = new File(FONT_PATH + font + CXF).getCanonicalPath();
            final String scriptPath = new File(F_ENGRAVE_PATH).getCanonicalPath();

            String command = String.format("python %s -b -f %s -t %s", scriptPath, fontPath, '"' + text + '"');
            LOG.debug("Executing engrave g-code generate command: {}", command);
            final ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectOutput(ProcessBuilder.Redirect.INHERIT);
            pb.redirectError(ProcessBuilder.Redirect.INHERIT);
            Process p = pb.start();
            Thread.sleep(2_000);
            return new String(p.getInputStream().readAllBytes());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }
}
