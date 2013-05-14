package com.kael.protocol;

import lib.kael.ProtocolBase;

public class UserIntoGameRoom extends ProtocolBase{
	private int uid;
	private int rid;
	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public UserIntoGameRoom()
	{
		this.Cmd= ProtocolMatch.PS_CMD_INTOROOM;
	}
}
