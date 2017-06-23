package in.daocloud.java.nev.access.runner;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by yann on 2017/6/23.
 */
public class DataSendHandler extends ChannelInboundHandlerAdapter {

    private static String vin = VinPool.getEnableVin();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        TransferData transferData =
                DataReader.read(TestUtils.fromString("23 23 01 fe 4c 44 50 55 4e 41 53 43 31 47 43 31 39 30 38 38 30 01 00 2e 11 02 07 0a 03 1b 00 14 38 39 38 36 30 36 31 36 30 32 30 30 30 34 37 36 36 30 31 35 01 10 00 00 00 10 00 2b 00 00 05 00 04 00 00 00 00 01 cc"));


        TransferData loginData = TransferData.builder()
                .command(CommandType.LOGIN)
                .askCommand(AskCommandType.SUCCESS)
                .uniqueNo(vin)
                .encryptType(EncryptType.NONE)
                .len(transferData.getData().length)
                .data(transferData.getData())
                .build();

        ctx.writeAndFlush(fakeLogin(loginData));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        VinPool.back(vin);
        ctx.close();
    }


    public static byte[] fakeLogin(TransferData transferData) {

        byte[] s = new byte[]{0x23, 0x23};
        byte command = transferData.getCommand().getContent();
        byte ask = AskCommandType.SUCCESS.getContent();
        byte[] vin = transferData.getUniqueNo().getBytes(Charset.defaultCharset());
        byte en = transferData.getEncryptType().getContent();
        byte[] size = new byte[]{0x00, (byte) transferData.getData().length};
        byte[] data = transferData.getData();

        int sizeOfPackage = s.length + 1 + 1 + vin.length + 1 + size.length + data.length;
        ByteBuffer byteBuffer = ByteBuffer.allocate(sizeOfPackage + 1);
        byteBuffer.put(s)
                .put(command)
                .put(ask)
                .put(vin)
                .put(en)
                .put(size)
                .put(data);

        byte bcc = byteBuffer.get(0);

        for (int i = 1; i < sizeOfPackage; i++) {
            bcc = (byte) (bcc ^ byteBuffer.get(i));
        }

        byteBuffer.put(bcc);

        return byteBuffer.array();
    }
}
