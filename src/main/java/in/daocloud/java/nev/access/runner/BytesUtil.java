package in.daocloud.java.nev.access.runner;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

/**
 * Created by yann on 2017/4/19.
 */
public abstract class BytesUtil {

    public static byte[] byteBuf2Bytes(ByteBuf data) {
        byte[] bytes = new byte[data.readableBytes()];
        data.readBytes(bytes);
        return bytes;
    }

    public static ByteBuf fromString(String str) {
        ByteBuf byteBuf = Unpooled.buffer();

        Arrays.stream(str.split(" "))
                .forEach(c -> byteBuf.writeByte(Integer.parseInt(c, 16)));

        return byteBuf;
    }
}
