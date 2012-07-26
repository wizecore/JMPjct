package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Changeuser extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Changeuser");
    
    public String user = "";
    public String authResponse = "";
    public String schema = "";
    public long characterSet = 0;
    public long capabilities = 0;
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_CHANGE_USER));
        payload.add(Proto.build_null_str(this.user));
        if (this.capabilities != Flags.CLIENT_SECURE_CONNECTION)
            payload.add(Proto.build_lenenc_str(this.authResponse));
        else
            payload.add(Proto.build_null_str(this.authResponse));
        payload.add(Proto.build_null_str(this.schema));
        payload.add(Proto.build_fixed_int(2, this.characterSet));
        
        return payload;
    }
}
