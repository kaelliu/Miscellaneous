package com.kael.protocol;

import lib.kael.ProtocolBase;

public class RegisterLoginSuccessProtocol extends ProtocolBase{
	private int uid;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public RegisterLoginSuccessProtocol()
	{
		this.Cmd = ProtocolMatch.PS_CMD_REGISTER;
	}
}
