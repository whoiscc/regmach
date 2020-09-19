package net.correctizer.regmach;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MachineTest {
    @Test
    void setRegisterGetRegister() {
        Machine machine = new Machine(new Instruction[0]);
        machine.setRegister32(10, 0x11223344);
        assertEquals(machine.getRegister32(10), 0x11223344);
        machine.setRegister32(9, 0x55667788);
        machine.setRegister32(11, 0x99aabbcc);
        assertEquals(machine.getRegister32(9), 0x55667788);
        assertEquals(machine.getRegister32(10), 0x11223344);
        assertEquals(machine.getRegister32(11), 0x99aabbcc);
    }

    @Test
    void loadNopProgram() {
        Machine machine = new Machine(new Instruction[]{Instruction.nop()});
        assertEquals(machine.getMemory32(0x00000000, 0x008000), 0x0);
    }
}