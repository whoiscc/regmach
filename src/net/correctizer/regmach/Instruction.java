package net.correctizer.regmach;

public class Instruction {
    int bytes;

    Instruction(int bytes) {
        this.bytes = bytes;
    }

    static int loadProgram(Machine machine, Instruction[] program) {
        long address = 0x00000000_008000;
        for (Instruction instruction : program) {
            int page = (int) (address >> 24);
            int offset = (int) (address & 0xffffff);
            machine.setMemory32(page, offset, instruction.bytes);
            address += 4;
        }
        return program.length;
    }

    static Instruction nop() {
        return new Instruction(0);
    }

    private static int opCode(int type) {
        return type << 24;
    }

    private static int reg32A(int registerIndex) {
        return registerIndex << 19;
    }

    private static int ctl32(boolean ctl1, boolean ctl2, boolean ctl3) {
        return ((ctl1 ? 4 : 0) + (ctl2 ? 2 : 0) + (ctl3 ? 1 : 0)) << 16;
    }

    static Instruction loadImmediate32High16(int registerIndex, int value) {
        assert registerIndex >= 0 && registerIndex < 32;
        assert value >= 0 && value < 1 << 16;
        return new Instruction(opCode(1) | reg32A(registerIndex) | ctl32(false, false, true) | value);
    }

    static Instruction loadImmediate32Low16(int registerIndex, int value) {
        assert registerIndex >= 0 && registerIndex < 32;
        assert value >= 0 && value < 1 << 16;
        return new Instruction(opCode(1) | reg32A(registerIndex) | ctl32(false, false, false) | value);
    }
}
