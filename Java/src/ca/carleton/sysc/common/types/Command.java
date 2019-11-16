package ca.carleton.sysc.common.types;

/**
 * Valid commands the CNC computer can accept to relay to GRBL
 */
public enum Command {

    DEVELOPER("%s", CommandType.PARAMETERIZED),
    WAKE_UP("\n\r\n\r", CommandType.NO_PARAMETER),
    STATUS("?", CommandType.NO_PARAMETER),
    PAUSE("!", CommandType.NO_PARAMETER),
    ABORT("M00", CommandType.NO_PARAMETER),
    RESUME("~", CommandType.NO_PARAMETER),
    ESTOP("M112", CommandType.NO_PARAMETER),
    SEND_TEXT("", CommandType.PARAMETERIZED);

    private static final String NEW_LINE = "\r\n";

    private final String code;

    private final CommandType commandType;

    public byte[] getBytes() {
        return (this.code + NEW_LINE).getBytes();
    }

    Command(final String code, final CommandType commandType) {
        this.code = code;
        this.commandType = commandType;
    }

    public String getCode() {
        return code;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
