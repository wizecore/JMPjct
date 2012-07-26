package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Processkill extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Processkill");
    
    public long connectionId = 0;
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_PROCESS_KILL));
        payload.add(Proto.build_fixed_int(4, this.connectionId));
        
        return payload;
    }
}
