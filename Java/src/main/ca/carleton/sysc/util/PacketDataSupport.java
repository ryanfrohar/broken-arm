package main.ca.carleton.sysc.util;

import main.ca.carleton.sysc.message.Input;
import main.ca.carleton.sysc.types.Command;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacketDataSupport {

    private static final Logger LOG = LoggerFactory.getLogger(PacketDataSupport.class);

    private static final byte DELIMITER = 0x0;

    /**
     * Create a Input bean from the given data byte[]
     * @param data data from the DatagramPacket
     * @return Input bean, command and parameters will be empty if invalid command is entered
     */
    public Input getInputFromData(final byte[] data) {
        final List<String> messageArguments = this.getMessageArguments(data);
        LOG.info(ArrayUtils.toString(messageArguments.toArray()));
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

        for (int i = 0, cursor = -1; i < data.length; i++) {
            if(data[i] == DELIMITER) {
                if(i != 0 && cursor != i-1) {// check for consecutive null bytes
                    messageArgs.add(new String(Arrays.copyOfRange(data, cursor+1, i), StandardCharsets.UTF_8));
                }
                cursor = i;
            }
        }

        return messageArgs;
    }

    /**
     * Builds packet data byte[] to send in a DatagramPacket
     * returns an empty array if null or empty list is passed
     * @param messages the String arguments to place in the byte[]
     * @return a byte[] containing each string delimited by a null byte
     */
    public byte[] buildPacketData(final List<String> messages) {
        if (messages == null || messages.size() < 1 || messages.contains(null)) {
            LOG.error("Invalid null passed to build packet data. Returning empty byte array");
            return new byte[]{};
        }

        final Byte[] data = messages.stream()
                .map(msg -> ArrayUtils.add(msg.getBytes(), DELIMITER))
                .map(ArrayUtils::toObject)
                .flatMap(Arrays::stream)
                .toArray(Byte[]::new);
        return ArrayUtils.toPrimitive(data);
    }


    /**
     * @see PacketDataSupport#buildPacketData(List)
     */
    public byte[] buildPacketData(final String... messages) {
        return this.buildPacketData(Arrays.asList(messages));
    }

}