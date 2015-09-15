package com.github.mpjct.jmpjct.mysql.proto;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class Row extends Packet {
	public Logger logger = Logger.getLogger("Engine");
	 
    public int type = Flags.ROW_TYPE_TEXT;
    public int colType = Flags.MYSQL_TYPE_VAR_STRING;
    public ArrayList<Object> data = new ArrayList<Object>();
    
    public Row () {}
    
    public Row (String data1) {
        this.addData(data1);
    }
    
    public void addData(String data) {
        this.data.add(data);
    }
    
    public Row (String data1, Integer data2) {
        this.addData(data1);
        this.addData(data2);
    }
    
    public Row (String data1, long data2) {
        this.addData(data1);
        this.addData(data2);
    }
    
    public Row (String data1, float data2) {
        this.addData(data1);
        this.addData(data2);
    }
    
    public Row (String data1, boolean data2) {
        this.addData(data1);
        this.addData(data2);
    }
    
    public Row (String data1, String data2) {
        this.addData(data1);
        this.addData(data2);
    }
    
    public void addData(Integer data) {
        this.data.add(Integer.toString(data));
    }
    
    public void addData(long data) {
        this.data.add(String.valueOf(data));
    }
    
    public void addData(float data) {
        this.data.add(String.valueOf(data));
    }
    
    public void addData(boolean data) {
        this.data.add(String.valueOf(data));
    }
    
    // Add other addData for other types here
    
    public ArrayList<byte[]> getPayload() {
        ArrayList<byte[]> payload = new ArrayList<byte[]>();
        
        for (Object obj: this.data) {
            switch (this.type) {
                case Flags.ROW_TYPE_TEXT: 
                    if (obj instanceof String)
                        payload.add(Proto.build_lenenc_str((String)obj));
                    else
                    if (obj instanceof Integer)
                        payload.add(Proto.build_lenenc_int((Integer)obj));
                    else 
                    if (obj == null) {
                    	// If a field is NULL `0xfb` is sent as described in `length encoded integer`_.
                    	payload.add(Proto.build_lenenc_int((Integer)0xfb));
                    } else {
                        // trigger error
                    	logger.warn("Invalid column value: " + obj);
                    }
                    break;
                case Flags.ROW_TYPE_BINARY:
                    break;
                default:
                    break;
            }
        }
        
        return payload;
    }
    
    public static Row loadFromPacket(byte[] packet) {
        Row obj = new Row();
        Proto proto = new Proto(packet, 3);
        
        obj.sequenceId = proto.get_fixed_int(1);
        
        return obj;
    }
}
