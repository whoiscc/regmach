package net.correctizer.regmach;

import java.util.HashMap;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Machine {
    HashMap<Integer, Byte[]> pages;
    Byte[] registerFile;

    static final int IP_PAGE_REGISTER = 0;
    static final int IP_OFFSET_REGISTER = 1;

    static final Logger LOGGER = Logger.getLogger(Machine.class.getName());

    Machine(Instruction[] program) {
        pages = new HashMap<>();
        registerFile = new Byte[128];
        // initialize IP to 0x[00000000]+008000
        setRegister32(IP_PAGE_REGISTER, 0x00000000);
        setRegister32(IP_OFFSET_REGISTER, 0x00008000);
        // end
        int instructionCount = Instruction.loadProgram(this, program);
        LOGGER.log(Level.INFO, "load {0} instructions into memory 0x[00000000]+008000", instructionCount);
    }

    void setRegister32(int registerIndex, int value) {
        assert registerIndex >= 0 && registerIndex < 32;
        int offset = registerIndex << 2;
        registerFile[offset] = (byte) (value >> 24);
        registerFile[offset + 1] = (byte) (value >> 16 & 0xff);
        registerFile[offset + 2] = (byte) (value >> 8 & 0xff);
        registerFile[offset + 3] = (byte) (value & 0xff);
    }

    int getRegister32(int registerIndex) {
        assert registerIndex >= 0 && registerIndex < 32;
        int offset = registerIndex << 2;
        Function<Byte, Integer> extendByte = (value) -> (int) value & 0xff;
        return (extendByte.apply(registerFile[offset]) << 24) |
                (extendByte.apply(registerFile[offset + 1]) << 16) |
                (extendByte.apply(registerFile[offset + 2]) << 8) |
                extendByte.apply(registerFile[offset + 3]);
    }

    void setMemory32(int page, int offset, int value) {
        assert offset >= 0 && offset < 1 << 24;
        assert offset % 4 == 0;
        pages.putIfAbsent(page, new Byte[1 << 24]);
        pages.get(page)[offset] = (byte) (value >> 24);
        pages.get(page)[offset + 1] = (byte) (value >> 16 & 0xff);
        pages.get(page)[offset + 2] = (byte) (value >> 8 & 0xff);
        pages.get(page)[offset + 3] = (byte) (value & 0xff);
    }

    int getMemory32(int page, int offset) {
        assert offset >= 0 && offset < 1 << 24;
        assert offset % 4 == 0;
        pages.putIfAbsent(page, new Byte[1 << 24]);
        Function<Byte, Integer> extendByte = (value) -> (int) value & 0xff;
        return (extendByte.apply(pages.get(page)[offset]) << 24) |
                (extendByte.apply(pages.get(page)[offset + 1]) << 16) |
                (extendByte.apply(pages.get(page)[offset + 2]) << 8) |
                extendByte.apply(pages.get(page)[offset + 3]);
    }
}
