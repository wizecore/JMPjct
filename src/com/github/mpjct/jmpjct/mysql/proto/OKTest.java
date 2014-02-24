package com.github.mpjct.jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class OKTest {
    @Test
    public void test1() {
        byte[] packet = new byte[] {
            (byte)0x07, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00,
        };
        OK ok = OK.loadFromPacket(packet);
        assertArrayEquals(packet, ok.toPacket());
        assertEquals(ok.affectedRows, 0);
        assertEquals(ok.lastInsertId, 0);
        assertEquals(ok.hasStatusFlag(Flags.SERVER_STATUS_AUTOCOMMIT), true);
        assertEquals(ok.hasStatusFlag(Flags.SERVER_STATUS_IN_TRANS_READONLY), false);
        assertEquals(ok.warnings, 0);
    }
}
