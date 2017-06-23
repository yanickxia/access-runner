package in.daocloud.java.nev.access.runner;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by misha on 2017/4/24.
 * command type enum
 */
public enum CommandType {
    LOGIN((byte) 0x01), REAL_TIME_INFO((byte) 0x02), DELAY_INFO((byte) 0x03), LOGOUT((byte) 0x04), HEARTBEAT((byte) 0x07), CHECK_TIME((byte) 0x08), RESERVE((byte) 0x00);

    private byte content;

    CommandType(byte content) {
        this.content = content;
    }

    private static Map<Byte, CommandType> COMMAND_CACHE = new HashMap<Byte, CommandType>() {
        private Map<Byte, CommandType> init() {
            Arrays.stream(CommandType.values())
                    .forEach(val -> this.put(val.content, val));
            return this;
        }
    }.init();

    public static CommandType form(byte content) {
        return COMMAND_CACHE.getOrDefault(content, RESERVE);
    }

    public byte getContent() {
        return content;
    }
}
