package com.github.jmpjct.mysql.proto;

/*
 * A MySQL Command Packet
 */

import java.util.ArrayList;
import org.apache.log4j.Logger;

public class Com_Fieldlist extends Packet {
    public Logger logger = Logger.getLogger("MySQL.Com.Fieldlist");
    
    public String table = "";
    public String fields = "";
    
    public ArrayList<byte[]> getPayload() {
        this.logger.trace("getPayload");
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        payload.add(Proto.build_byte(Flags.COM_FIELD_LIST));
        payload.add(Proto.build_null_str(this.table));
        payload.add(Proto.build_fixed_str(this.fields.length(), this.fields));
        
        return payload;
    }
}
