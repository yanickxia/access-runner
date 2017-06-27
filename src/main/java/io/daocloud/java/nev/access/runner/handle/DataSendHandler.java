package io.daocloud.java.nev.access.runner.handle;

import io.daocloud.java.nev.access.runner.bean.AskCommandType;
import io.daocloud.java.nev.access.runner.bean.CommandType;
import io.daocloud.java.nev.access.runner.bean.EncryptType;
import io.daocloud.java.nev.access.runner.bean.TransferData;
import io.daocloud.java.nev.access.runner.service.ScheduledService;
import io.daocloud.java.nev.access.runner.service.VinPoolService;
import io.daocloud.java.nev.access.runner.utils.ConfigProperties;
import io.daocloud.java.nev.access.runner.utils.DataReader;
import io.daocloud.java.nev.access.runner.utils.TestUtils;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by yann on 2017/6/23.
 */

@Slf4j
public class DataSendHandler extends ChannelInboundHandlerAdapter {

    private VinPoolService vinPoolService = VinPoolService.vinPool();
    private String vin;
    private static ConfigProperties configProperties = ConfigProperties.configProperties();
    private static ScheduledExecutorService scheduledService = ScheduledService.scheduledService();
    private ScheduledFuture scheduledFuture;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        vin = vinPoolService.getEnableVin();
        log.info("get enable vin from redis {}", vin);

        scheduledFuture = scheduledService.scheduleAtFixedRate(
                new SendDataJob(vin, ctx), 0, configProperties.connectPeriod(), TimeUnit.SECONDS);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        vinPoolService.backVin(vin);
        log.error("channelInactive, back vin {}", vin);
        scheduledFuture.cancel(false);
        ctx.close();
    }

}


@Slf4j
class SendDataJob implements Runnable {
    private String vin;
    private ChannelHandlerContext ctx;

    public SendDataJob(String vin, ChannelHandlerContext ctx) {
        this.vin = vin;
        this.ctx = ctx;
    }

    @Override
    public void run() {
        TransferData transferData = TestUtils.LoginSampleData;

        TransferData fakeLogin = TransferData.builder()
                .data(transferData.getData())
                .askCommand(AskCommandType.COMMAND)
                .command(CommandType.LOGIN)
                .encryptType(EncryptType.NONE)
                .len(transferData.getLen())
                .uniqueNo(vin)
                .build();

        byte[] newDatas = fake(fakeLogin);
        ctx.writeAndFlush(Unpooled.copiedBuffer(newDatas));
        log.info("vin {} send data ", vin);

        TransferData realData = TestUtils.realSampleData;
        TransferData fakeReal = TransferData.builder()
                .data(realData.getData())
                .askCommand(AskCommandType.COMMAND)
                .command(CommandType.REAL_TIME_INFO)
                .encryptType(EncryptType.NONE)
                .len(realData.getLen())
                .uniqueNo(vin)
                .build();
        newDatas = fake(fakeReal);
        ctx.writeAndFlush(Unpooled.copiedBuffer(newDatas));
        log.info("vin {} send real data ", vin);
    }

    public static byte[] fake(TransferData transferData) {

        byte[] s = new byte[]{0x23, 0x23};
        byte command = transferData.getCommand().getContent();
        byte ask = AskCommandType.SUCCESS.getContent();
        byte[] vin = transferData.getUniqueNo().getBytes(Charset.defaultCharset());
        byte en = transferData.getEncryptType().getContent();
        byte[] size = Arrays.copyOfRange(intToBytes2(transferData.getData().length), 2, 4);
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
        DataReader.read(byteBuffer.array());

        return byteBuffer.array();
    }

    public static byte[] intToBytes2(int value) {
        byte[] src = new byte[4];
        src[0] = (byte) ((value >> 24) & 0xFF);
        src[1] = (byte) ((value >> 16) & 0xFF);
        src[2] = (byte) ((value >> 8) & 0xFF);
        src[3] = (byte) (value & 0xFF);
        return src;
    }
}