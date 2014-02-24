package com.github.mpjct.jmpjct.mysql.proto;

import org.junit.*;
import static org.junit.Assert.*;

public class Com_QueryTest {
    @Test
    public void test1() {
        byte[] packet = Proto.packet_string_to_bytes(""
            + "2e 00 00 00 03 73 65 6c    65 63 74 20 22 30 31 32"
            + "33 34 35 36 37 38 39 30    31 32 33 34 35 36 37 38"
            + "39 30 31 32 33 34 35 36    37 38 39 30 31 32 33 34"
            + "35 22                                             "
        );

        Com_Query pkt = Com_Query.loadFromPacket(packet);
        assertArrayEquals(packet, pkt.toPacket());
        assertEquals(pkt.query, "select \"012345678901234567890123456789012345\"");
    }
}
