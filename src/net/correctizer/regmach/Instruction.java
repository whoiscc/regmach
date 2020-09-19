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
            int offset = (int)(address & 0xffffff);
            machine.setMemory32(page, offset, instruction.bytes);
            address += 4;
        }
        return program.length;
    }

    static Instruction nop() {
        return new Instruction(0);
    }
}
