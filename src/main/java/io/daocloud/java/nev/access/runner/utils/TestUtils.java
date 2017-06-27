package io.daocloud.java.nev.access.runner.utils;

import io.daocloud.java.nev.access.runner.bean.TransferData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.Arrays;

public class TestUtils {

    public static TransferData LoginSampleData = DataReader.read(TestUtils.fromString("23 23 01 fe 4c 44 50 55 4e 41 53 43 31 47 43 31 39 30 38 38 30 01 00 2e 11 02 07 0a 03 1b 00 14 38 39 38 36 30 36 31 36 30 32 30 30 30 34 37 36 36 30 31 35 01 10 00 00 00 10 00 2b 00 00 05 00 04 00 00 00 00 01 cc", true));
    public static TransferData realSampleData = DataReader.read(
            TestUtils.fromString("23 23 02 fe 4c 44 50 55 4e 41 53 43 31 47 43 31 39 30 38 38 30 01 01 4d 11 02 07 0a 02 00 01 01 03 01 01 ea 00 00 18 8e 11 23 27 b2 5e 01 2e 4e 20 13 00 02 01 01 01 34 52 c1 4e de 4a 10 cc 27 1a 05 00 06 af 0f f8 01 e9 93 ca 06 01 13 0f c3 01 60 0f b5 01 04 32 01 0a 30 07 00 00 00 00 00 00 00 00 00 09 01 01 00 18 30 30 31 32 31 31 30 31 31 30 30 30 2e 2f 2e 2e 2f 2f 30 30 30 30 2f 2f 08 01 01 11 23 27 b2 00 6c 00 01 6c 0f be 0f bf 0f bf 0f b8 0f b6 0f b7 0f 9a 0f 98 0f 96 0f 71 0f 6f 0f 67 0f 7f 0f 83 0f 88 0f 9c 0f 9c 0f 9b 0f ab 0f a9 0f ab 0f a5 0f a1 0f 9f 0f 81 0f 7f 0f 83 0f 8e 0f 91 0f 91 0f c0 0f be 0f bf 0f ab 0f a9 0f a8 0f 83 0f 84 0f 84 0f a4 0f a6 0f a7 0f b8 0f b7 0f ba 0f b5 0f b6 0f b4 0f 91 0f 90 0f 94 0f a5 0f a7 0f a8 0f be 0f bf 0f be 0f be 0f bf 0f bc 0f a4 0f a5 0f a5 0f 9e 0f 9e 0f a2 0f b8 0f b7 0f b7 0f b9 0f ba 0f b7 0f 71 0f 70 0f 72 0f 6b 0f 6d 0f 6c 0f 58 0f 57 0f 51 0f 4c 0f 53 0f 58 0f 5a 0f 56 0f 57 0f 50 0f 4d 0f 49 0f 45 0f 4c 0f 50 0f 63 0f 65 0f 61 0f 53 0f 49 0f 3c 0f 3e 0f 4c 0f 4d 0f a2 0f a2 0f a1 0f ac 0f aa 0f a9 0c", true));


    public static ByteBuf fromString(String str, boolean isHasSpace) {
        ByteBuf byteBuf = Unpooled.buffer();

        if (isHasSpace) {
            Arrays.stream(str.split(" "))
                    .forEach(c -> byteBuf.writeByte(Integer.parseInt(c, 16)));
        } else {
            for (int i = 0; i < str.length(); i += 3) {
                byteBuf.writeByte(Integer.parseInt(str.substring(i, i + 2), 16));
            }
        }

        return byteBuf;
    }
}
