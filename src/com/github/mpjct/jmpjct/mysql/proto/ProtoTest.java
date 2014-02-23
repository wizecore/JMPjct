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
        
        payload.add( Proto.build_fixed_str(packet.length, proto.get_fixed_str(packet.length, true), true));
        
        assertArrayEquals(packet, Proto.arraylist_to_array(payload));
    }
    
    @Test
    public void test1() {
        byte[] expected = new byte[] {
            (byte)0x00
        };
        assertArrayEquals(expected, Proto.build_fixed_int(1, 0));
    }
    
    @Test
    public void test2() {
        byte[] expected = new byte[] {
            (byte)0xFF
        };
        assertArrayEquals(expected, Proto.build_fixed_int(1, 255));
    }
    
    @Test
    public void test3() {
        byte[] expected = new byte[] {
            (byte)0x00, (byte)0x00
        };
        assertArrayEquals(expected, Proto.build_fixed_int(2, 0));
    }
    
    @Test
    public void test4() {
        byte[] expected = new byte[] {
            (byte)0xFF, (byte)0xFF
        };
        assertArrayEquals(expected, Proto.build_fixed_int(2, 0xFFFF));
    }
    
    @Test
    public void test5() {
        byte[] expected = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(expected, Proto.build_fixed_int(3, 0));
    }
    
    @Test
    public void test6() {
        byte[] expected = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00
        };
        assertArrayEquals(expected, Proto.build_fixed_int(4, 0));
    }
    
    @Test
    public void test7() {
        byte[] expected = new byte[] {
            (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_fixed_int(8, 0));
    }
    
    @Test
    public void test8() {
        byte[] expected = new byte[] {
            (byte)0xFF, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_fixed_int(8, 255));
    }
    
    @Test
    public void test9() {
        byte[] expected = new byte[] {
            (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(0));
    }
    
    @Test
    public void test10() {
        byte[] expected = new byte[] {
            (byte)0xFC, (byte)0xFB, (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(251));
    }
    
    @Test
    public void test11() {
        byte[] expected = new byte[] {
            (byte)0xFC, (byte)0xFC, (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(252));
    }
    
    @Test
    public void test12() {
        byte[] expected = new byte[] {
            (byte)0xFD, (byte)0x00, (byte)0x00, (byte)0x01,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(65536));
    }
    
    @Test
    public void test13() {
        byte[] expected = new byte[] {
            (byte)0xfe, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(16777216));
    }
    
    @Test
    public void test14() {
        byte[] expected = new byte[] {
            (byte)0xfe, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x02, (byte)0x00, (byte)0x00, (byte)0x00,
            (byte)0x00,
        };
        assertArrayEquals(expected, Proto.build_lenenc_int(33554432));
    }

}
