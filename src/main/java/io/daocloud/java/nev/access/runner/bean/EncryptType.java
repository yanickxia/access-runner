package io.daocloud.java.nev.access.runner.bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum EncryptType {
    NONE((byte) 0x01), RSA((byte) 0x02), AES128((byte) 0x03), ERROR((byte) 0xFE), INVALID((byte) 0xFF), RESERVE((byte) 0x00);

    private byte content;

    private static Map<Byte, EncryptType> ENCRYPT_CACHED = new HashMap<Byte, EncryptType>() {
        private Map<Byte, EncryptType> init() {
            Arrays.stream(EncryptType.values())
                    .forEach(item -> this.put(item.content, item));
            return this;
        }
    }.init();

    EncryptType(byte content) {
        this.content = content;
    }

    public static EncryptType from(byte content) {
        return ENCRYPT_CACHED.getOrDefault(content, RESERVE);
    }

    public byte getContent() {
        return this.content;
    }

}
