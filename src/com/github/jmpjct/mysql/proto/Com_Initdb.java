package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Initdb extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Initdb");
    
    public String schema = "";
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_INIT_DB));
        payload.add(Proto.build_fixed_str(this.schema.length(), this.schema));
        
        return payload;
    }
    
    public static Com_Initdb loadFromPacket(byte[] packet) {
        Logger.getLogger("MySQL.Com.Initdb").trace("loadFromPacket");
        Com_Initdb obj = new Com_Initdb();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        // Header
        proto.get_fixed_int(1);
        
        obj.schema = proto.get_eop_str();
        
        return obj;
    }
    
}
