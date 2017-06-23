package io.daocloud.java.nev.access.runner.utils;

import io.daocloud.java.nev.access.runner.bean.AskCommandType;
import io.daocloud.java.nev.access.runner.bean.CommandType;
import io.daocloud.java.nev.access.runner.bean.EncryptType;
import io.daocloud.java.nev.access.runner.bean.TransferData;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;
import java.util.Arrays;


public class DataReader {


    /**
     * BCC check for data
     *
     * @param data input data
     * @return ok is true, other is false
     */
    private static boolean check(byte[] data) {
        byte bcc = data[0];
        for (int i = 1; i < data.length - 1; i++) {
            bcc = (byte) (bcc ^ data[i]);
        }

        return bcc == data[data.length - 1];
    }


    public static TransferData read(ByteBuf data) {
        byte[] source = BytesUtil.byteBuf2Bytes(data);
        return read(source);
    }

    public static TransferData read(byte[] data) {

        String begin = new String(Arrays.copyOfRange(data, 0, 2)); //起始符
        byte command = data[2]; //命令单元 - 命令标志
        byte askCommand = data[3]; // 命令单元 - 应答标志
        String uniqueNo = new String(Arrays.copyOfRange(data, 4, 21));//唯一识别码
        byte encryptType = data[22]; //加密方式
        Integer dataLen = (int) ByteBuffer.wrap(Arrays.copyOfRange(data, 22, 24)).getShort(); //数据长度
        byte[] dataBody = Arrays.copyOfRange(data, 24, 24 + dataLen); // 数据单元
        byte bccIndex = data[data.length - 1]; // 检测位置


        return TransferData.builder()
                .begin(begin)
                .command(CommandType.form(command))
                .askCommand(AskCommandType.from(askCommand))
                .uniqueNo(uniqueNo)
                .encryptType(EncryptType.from(encryptType))
                .len(dataLen)
                .data(dataBody)
                .bbc(bccIndex)
                .source(data)
                .build();
    }


}

