package main.ca.carleton.sysc.types;

/**
 * Valid commands the CNC computer can accept to relay to GRBL
 */
public enum Command {

    DEV("%s", CommandType.PARAMETERIZED),
    WAKE_UP("\n\r\n\r", CommandType.NO_PARAMETER),
    MOVE_UP("$J=G21G91X-5F5000\n $J=G21G91Y-5F5000", CommandType.NO_PARAMETER),
    MOVE_LEFT("$J=G21G91X-5F5000\n $J=G21G91Y5F5000", CommandType.NO_PARAMETER),
    MOVE_RIGHT("$J=G21G91X5F5000\n $J=G21G91Y-5F5000", CommandType.NO_PARAMETER),
    MOVE_DOWN("$J=G21G91X5F5000\n $J=G21G91Y5F5000", CommandType.NO_PARAMETER),
    STATUS("?", CommandType.NO_PARAMETER),
    PAUSE("!", CommandType.NO_PARAMETER),
    ABORT("M00", CommandType.NO_PARAMETER),
    RESUME("~", CommandType.NO_PARAMETER),
    ESTOP("M112", CommandType.NO_PARAMETER),
    LIFT("G0 Z5", CommandType.NO_PARAMETER),
    GOTO("G0 X%s Y%s", CommandType.PARAMETERIZED),
    ZERO("G90 G0 X0 Y0", CommandType.NO_PARAMETER),
    KILL_ALARMS("$X", CommandType.NO_PARAMETER),
    SET_CUR_AS_ZERO("G10 P0 L20 X0 Y0 Z0", CommandType.NO_PARAMETER),
    TEXT("", CommandType.PARAMETERIZED);

    private final String code;

    private final CommandType commandType;

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
