package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Setoption extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Setoption");
    
    public long operation = 0;
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_SET_OPTION));
        payload.add(Proto.build_fixed_int(2, this.operation));
        
        return payload;
    }

    public static Com_Setoption loadFromPacket(byte[] packet) {
        Logger.getLogger("MySQL.Com.Setoption").trace("loadFromPacket");
        Com_Setoption obj = new Com_Setoption();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        // Header
        proto.get_fixed_int(1);
        
        obj.operation = proto.get_fixed_int(2);
        
        return obj;
    }
    
}
