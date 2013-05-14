package com.kael.protocol;

import lib.kael.ProtocolBase;

public class GameServerUserChange extends ProtocolBase{
	private int tp;// 1-come 2-go
public int getTp() {
		return tp;
	}
	public void setTp(int tp) {
		this.tp = tp;
	}
	//	private int rid;// room id
	public GameServerUserChange()
	{
		this.Cmd = ProtocolMatch.PS_CMD_USERCOME;
	}
}
