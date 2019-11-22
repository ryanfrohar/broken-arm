package ca.carleton.sysc.util;

import ca.carleton.sysc.message.Input;
import ca.carleton.sysc.types.Command;
import org.apache.commons.lang3.EnumUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacketDataSupport {

    private static final byte DELIMITER = 0x0;

    /**
     * Create a Input bean from the given data byte[]
     * @param data data from the DatagramPacket
     * @return Input bean, command and parameters will be empty if invalid command is entered
     */
    public Input getInputFromData(final byte[] data) {
        final List<String> messageArguments = this.getMessageArguments(data);
        return messageArguments.isEmpty() || !EnumUtils.isValidEnum(Command.class, messageArguments.get(0))?
            new Input(null, Collections.emptyList()) :
            new Input(Command.valueOf(messageArguments.get(0)), messageArguments.subList(1, messageArguments.size()));
    }

    /**
     * Fetches the string arguments from the data byte[] of DatagramPacket
     * @param data the byte[]
     * @return String[] containing the received string arguments
     */
    public List<String> getMessageArguments(final byte[] data) {
        final List<String> messageArgs = new ArrayList<>();

        for (int i = 0, cursor = 0; i < data.length; i++) {
            if(data[i] == DELIMITER) {
                messageArgs.add(new String(Arrays.copyOfRange(data, cursor, i), StandardCharsets.UTF_8));
                cursor += i;
            }
        }

        return messageArgs;
    }

    /**
     * Builds packet data byte[] to send in a DatagramPacket
     * @param messages the String arguments to place in the byte[]
     * @return a byte[] containing each string delimited by a null byte
     */
    public byte[] buildPacketData(final List<String> messages) {

        return new byte[]{};
    }


    /**
     * @see PacketDataSupport#buildPacketData(List)
     */
    public byte[] buildPacketData(final String... messages) {
        return this.buildPacketData(Arrays.asList(messages));
    }

}
