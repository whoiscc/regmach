package net.correctizer.regmach;

import java.util.HashMap;
import java.util.function.Function;

public class Machine {
    HashMap<Long, Byte[]> pages;
    Byte[] registerFile;

    static final int IP_PAGE_REGISTER = 0;
    static final int IP_OFFSET_REGISTER = 1;

    Machine() {
        pages = new HashMap<>();
        registerFile = new Byte[128];
        // initialize IP to 0x8000
        setRegister32(IP_PAGE_REGISTER, 0x00000000);
        setRegister32(IP_OFFSET_REGISTER, 0x00008000);
    }

    void setRegister32(int registerIndex, long value) {
        assert value < 1L << 32;
        assert registerIndex >= 0 && registerIndex < 32;
        int offset = registerIndex << 2;
        registerFile[offset] = (byte) (value >> 24);
        registerFile[offset + 1] = (byte) (value >> 16 & 0xff);
        registerFile[offset + 2] = (byte) (value >> 8 & 0xff);
        registerFile[offset + 3] = (byte) (value & 0xff);
    }

    long getRegister32(int registerIndex) {
        assert registerIndex >= 0 && registerIndex < 32;
        int offset = registerIndex << 2;

        Function<Byte, Integer> extendByte = (value) -> (int) value & 0xff;

        return (extendByte.apply(registerFile[offset]) << 24) |
                (extendByte.apply(registerFile[offset + 1]) << 16) |
                (extendByte.apply(registerFile[offset + 2]) << 8) |
                extendByte.apply(registerFile[offset + 3]);
    }
}
