package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Packet
 */

import java.util.ArrayList;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.InputStream;
import org.apache.commons.io.HexDump;
import org.apache.log4j.Logger;

public abstract class Packet {
    
    public long sequenceId = 0;
    
    public abstract ArrayList<byte[]> getPayload();
    
    public byte[] toPacket() {
        ArrayList<byte[]> payload = this.getPayload();
        
        int size = 0;
        for (byte[] field: payload)
            size += field.length;
        
        byte[] packet = new byte[size+4];
        
        System.arraycopy(Proto.build_fixed_int(3, size), 0, packet, 0, 3);
        System.arraycopy(Proto.build_fixed_int(1, this.sequenceId), 0, packet, 3, 1);
        
        int offset = 4;
        for (byte[] field: payload) {
            System.arraycopy(field, 0, packet, offset, field.length);
            offset += field.length;
        }
        
        return packet;
    }
    
    public static int getSize(byte[] packet) {
        int size = (int) new Proto(packet).get_fixed_int(3);
        return size;
    }
    
    public static byte getType(byte[] packet) {
        return packet[4];
    }
    
    public static long getSequenceId(byte[] packet) {
        return new Proto(packet, 3).get_fixed_int(1);
    }
    
    public static final void dump(byte[] packet) {
        Logger logger = Logger.getLogger("MySQL.Packet");
        
        if (!logger.isTraceEnabled())
            return;
        
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HexDump.dump(packet, 0, out, 0);
            logger.trace("Dumping packet\n"+out.toString());
        }
        catch (IOException e) {
            return;
        }
    }
    
    public static byte[] read_packet(InputStream in) throws IOException {
        int b = 0;
        int size = 0;
        byte[] packet = new byte[3];
        
        // Read size (3)
        int offset = 0;
        int target = 3;
        do {
            b = in.read(packet, offset, (target - offset));
            if (b == -1) {
                throw new IOException();
            }
            offset += b;
        } while (offset != target);
        
        size = Packet.getSize(packet);
        
        byte[] packet_tmp = new byte[size+4];
        System.arraycopy(packet, 0, packet_tmp, 0, 3);
        packet = packet_tmp;
        packet_tmp = null;
        
        target = packet.length;
        do {
            b = in.read(packet, offset, (target - offset));
            if (b == -1) {
                throw new IOException();
            }
            offset += b;
        } while (offset != target);
        
        return packet;
    }
    
    public static ArrayList<byte[]> read_full_result_set(InputStream in, OutputStream out, ArrayList<byte[]> buffer, boolean bufferResultSet) throws IOException {
        // Assume we have the start of a result set already
        
        byte[] packet = buffer.get((buffer.size()-1));
        long colCount = ColCount.loadFromPacket(packet).colCount;
        
        // Read the columns and the EOF field
        for (int i = 0; i < (colCount+1); i++) {
            // Evil optimization
            if (!bufferResultSet) {
                Packet.write(out, buffer);
                buffer = new ArrayList<byte[]>();
            }
                
            packet = Packet.read_packet(in);
            if (packet == null) {
                throw new IOException();
            }
            buffer.add(packet);
        }
        
        do {
            // Evil optimization
            if (!bufferResultSet) {
                Packet.write(out, buffer);
                buffer = new ArrayList<byte[]>();
            }
            
            packet = Packet.read_packet(in);
            if (packet == null) {
                throw new IOException();
            }
            buffer.add(packet);
        } while (Packet.getType(packet) != Flags.EOF && Packet.getType(packet) != Flags.ERR);
        
        // Evil optimization
        if (!bufferResultSet) {
            Packet.write(out, buffer);
            buffer = new ArrayList<byte[]>();
        }
        
        if (Packet.getType(packet) == Flags.ERR)
            return buffer;
        
        if (EOF.loadFromPacket(packet).hasStatusFlag(Flags.SERVER_MORE_RESULTS_EXISTS)) {
            buffer.add(Packet.read_packet(in));
            buffer = Packet.read_full_result_set(in, out, buffer, bufferResultSet);
        }
        return buffer;
    }
    
    public static void write(OutputStream out, ArrayList<byte[]> buffer) throws IOException {
        for (byte[] packet: buffer) {
            out.write(packet);
        }
    }
    
}
