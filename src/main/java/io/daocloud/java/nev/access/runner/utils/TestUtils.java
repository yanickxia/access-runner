package io.daocloud.java.nev.access.runner.utils;

import io.daocloud.java.nev.access.runner.bean.TransferData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class TestUtils {

    public static TransferData LoginSampleData = DataReader.read(TestUtils.fromString("23 23 01 fe 4c 44 50 55 4e 41 53 43 31 47 43 31 39 30 38 38 30 01 00 2e 11 02 07 0a 03 1b 00 14 38 39 38 36 30 36 31 36 30 32 30 30 30 34 37 36 36 30 31 35 01 10 00 00 00 10 00 2b 00 00 05 00 04 00 00 00 00 01 cc"));

    public static ByteBuf fromString(String str) {
        ByteBuf byteBuf = Unpooled.buffer();

        Arrays.stream(str.split(" "))
                .forEach(c -> byteBuf.writeByte(Integer.parseInt(c, 16)));

        return byteBuf;
    }
}
