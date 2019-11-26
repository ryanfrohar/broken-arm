package test.ca.carleton.sysc.util;

import main.ca.carleton.sysc.util.PacketDataSupport;
import org.apache.commons.lang3.ArrayUtils;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PacketDataSupportTest {

    private final PacketDataSupport packetDataSupport = new PacketDataSupport();

    @Test
    public void canBuildPacketData() {
        final List<String> expected = Arrays.asList("TEXT", "font", "message");

        // Get bytes from test message
        final byte[] messageInBytes = this.packetDataSupport.buildPacketData(expected);
        // get back message from bytes
        final List<String> actual = this.packetDataSupport.getMessageArguments(messageInBytes);
        assertThat("Assert the message turned into bytes and back is the same as the original",actual, is(expected));
    }

    @Test
    public void canFailBuildPacketData() {
        final List<String> invalidMessage = new ArrayList<>();
        invalidMessage.add(null);

        // Get bytes from test message
        final byte[] messageInBytes = this.packetDataSupport.buildPacketData(invalidMessage);
        // get back message from bytes
        final List<String> messageFromBytes = this.packetDataSupport.getMessageArguments(messageInBytes);
        assertThat("Assert returned invalid list is not what is returned", invalidMessage, is(not(messageFromBytes)));
        assertThat("Assert the method returned an empty array as specified to do on invalid inputs", messageInBytes, is(ArrayUtils.EMPTY_BYTE_ARRAY));
    }

    @Test
    public void canInterpretPacketData() {
        final List<String> expected = Arrays.asList("TEXT", "font", "message");
        final byte[] delimiter = new byte[]{0x0};
        byte[] text = ArrayUtils.addAll("TEXT".getBytes(), delimiter);
        byte[] font = ArrayUtils.addAll("font".getBytes(), delimiter);
        byte[] message = ArrayUtils.addAll("message".getBytes(), delimiter);
        byte[] bytes = ArrayUtils.addAll(ArrayUtils.addAll(text, font), message);

        // get back message from bytes
        final List<String> actual = this.packetDataSupport.getMessageArguments(bytes);
        assertThat("Assert the message turned into bytes and back is the same as the original", actual, is(expected));
    }

    @Test
    public void canFailInterpretPacketData() {
        final byte[] invalidMessage = new byte[]{0x0,0x0,0x0,0x0,0x0};

        // get back message from bytes
        final List<String> actual = this.packetDataSupport.getMessageArguments(invalidMessage);
        assertThat("Assert the message of null bytes was interpreted as nothing", actual, is(Collections.emptyList()));
    }
}
