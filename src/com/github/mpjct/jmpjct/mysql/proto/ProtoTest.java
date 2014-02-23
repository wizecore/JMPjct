package com.github.mpjct.jmpjct.mysql.proto;

import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;

public class ProtoTest {
    
    @Test
    public void test_all_byte_values() {
        byte[] packet = new byte[256];
        int offset = 0;
        for (byte b = (byte)0x00; b <= (byte)0x7E; b++) {
            packet[offset] = (byte)b;
            offset = offset + 1;
        }
        
        packet[offset] = (byte)0x7F;
        offset = offset + 1;
        
        for (byte b = (byte)0x80; b <= (byte)0xFF; b++) {
            packet[offset] = (byte)b;
            offset = offset + 1;
        }
        
        Proto proto = new Proto(packet);
        
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add( Proto.build_fixed_str(packet.length, proto.get_fixed_str(packet.length)));
        
        assertArrayEquals(packet, Proto.arraylist_to_array(payload));
    }

}
