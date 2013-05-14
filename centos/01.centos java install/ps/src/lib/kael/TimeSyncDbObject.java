package lib.kael;

import java.util.Map;

public class TimeSyncDbObject {
	public static final byte I=1;
	public static final byte U=2;
	public static final byte D=3;
	public byte type;
	// data for operation,table,fields,value etc...
	public Map data;
	
	public TimeSyncDbObject(){
		type = I;
	}
}
