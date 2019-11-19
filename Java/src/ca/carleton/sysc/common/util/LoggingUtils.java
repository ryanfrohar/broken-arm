package ca.carleton.sysc.common.util;

import ca.carleton.sysc.common.cli.CLI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;

/**
 * Util class to set the verbose and quite mode logging for the running application.
 */
public final class LoggingUtils {

    private LoggingUtils() {
        //Util class can not be instantiated
    }

    public static String getLogMode() {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        return (root.getLevel().equals(Level.TRACE) ? CLI.VERBOSE:CLI.QUIET);
    }

    /**
     * Sets log level to info and show logging to console.
     */
    public static void setLogToVerboseMode() {
        LoggingUtils.setLogLevel(Level.TRACE);
    }

    /**
     * Sets log level to warn and suppress info logging but still show warnings and errors to console.
     */
    public static void setLogToQuietMode() {
        LoggingUtils.setLogLevel(Level.WARN);
    }

    /**
     * Sets the logger level at run time.
     * @param level Level to set loger
     */
    private static void setLogLevel(final Level level) {
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.trace("Setting Log level to {}", level);
        root.setLevel(level);
        root.trace("Log level set to {}", level);
    }
}