package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Quit extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Quit");
    
    public String query = "";
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_QUIT));
        
        return payload;
    }
    
    public static Com_Quit loadFromPacket(byte[] packet) {
        Logger.getLogger("MySQL.Com.Quit").trace("loadFromPacket");
        Com_Quit obj = new Com_Quit();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        // Header
        proto.get_fixed_int(1);
        
        return obj;
    }
}
