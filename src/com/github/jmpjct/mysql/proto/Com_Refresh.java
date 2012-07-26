package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Refresh extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Refresh");
    
    public long flags = 0x00;
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_REFRESH));
        payload.add(Proto.build_fixed_int(1, this.flags));
        
        return payload;
    }
    
    public static Com_Refresh loadFromPacket(byte[] packet) {
        Logger.getLogger("MySQL.Com.Refresh").trace("loadFromPacket");
        Com_Refresh obj = new Com_Refresh();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        // Header
        proto.get_fixed_int(1);
        
        obj.flags = proto.get_fixed_int(1);
        
        return obj;
    }
}
