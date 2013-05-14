package com.kael.protocol;

import lib.kael.ProtocolBase;

public class DeleteUserProtocol extends ProtocolBase
{
	/**
	 * 
	 */
	private int uid;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public DeleteUserProtocol()
	{
		this.Cmd= ProtocolMatch.PS_CMD_DELETEUSR;
	}
}