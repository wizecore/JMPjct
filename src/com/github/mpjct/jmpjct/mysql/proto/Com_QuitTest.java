package com.github.mpjct.jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Com_QuitTest {
    @Test
    public void test1() {
        byte[] packet = new byte[] {
            (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01,
        };
        Com_Quit com_quit = Com_Quit.loadFromPacket(packet);
        assertArrayEquals(packet, com_quit.toPacket());
    }
}
