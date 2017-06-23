package in.daocloud.java.nev.access.runner;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class TransferData {
    private byte[] source;

    private String begin;
    private CommandType command;
    private AskCommandType askCommand;
    private String uniqueNo;
    private EncryptType encryptType;
    private Integer len;
    private byte[] data;
    private Byte bbc;
}
