package in.daocloud.java.nev.access.runner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class TestUtils {

    public static ByteBuf fromString(String str) {
        ByteBuf byteBuf = Unpooled.buffer();

        Arrays.stream(str.split(" "))
                .forEach(c -> byteBuf.writeByte(Integer.parseInt(c, 16)));

        return byteBuf;
    }
}
