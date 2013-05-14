package com.kael.protocol;

import lib.kael.ProtocolBase;

import com.service.UserDetailDto;

// ps->gs
public class IntoGameProtocol extends ProtocolBase{
	private UserDetailDto ud;
	public UserDetailDto getUd() {
		return ud;
	}
	public void setUd(UserDetailDto ud) {
		this.ud = ud;
	}
	public IntoGameProtocol()
	{
		this.Cmd= ProtocolMatch.PS_CMD_INTOROOM;
	}
}
