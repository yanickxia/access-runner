package in.daocloud.java.nev.access.runner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by misha on 2017/4/24.
 * ask command enum
 */
public enum AskCommandType {

    SUCCESS((byte) 0x01), ERROR((byte) 0x02), VIN_REPEAT((byte) 0x03), COMMAND((byte) 0xFE), RESERVE((byte) 0x00);

    AskCommandType(byte content) {
        this.content = content;
    }

    private byte content;
    private static Map<Byte, AskCommandType> ASK_COMMAND_CACHED = new HashMap<Byte, AskCommandType>() {
        private Map<Byte, AskCommandType> init() {
            Arrays.stream(AskCommandType.values())
                    .forEach(val -> this.put(val.content, val));
            return this;
        }
    }.init();

    public static AskCommandType from(byte content) {
        return ASK_COMMAND_CACHED.getOrDefault(content, RESERVE);
    }

    public byte getContent() {
        return content;
    }
}