package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Query extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Query");
    
    public String query = "";
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_QUERY));
        payload.add(Proto.build_fixed_str(this.query.length(), this.query));
        
        return payload;
    }

    public static Com_Query loadFromPacket(byte[] packet) {
        Logger.getLogger("MySQL.Com.Query").trace("loadFromPacket");
        Com_Query obj = new Com_Query();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        // Header
        proto.get_fixed_int(1);
        
        obj.query = proto.get_eop_str();
        
        return obj;
    }
    
}
