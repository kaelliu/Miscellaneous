package com.kael.protocol;

import lib.kael.ProtocolBase;

public class ConnectedOrDisConnectProtocol extends ProtocolBase{
	// id make by server
	private int id;
	public ConnectedOrDisConnectProtocol()
	{
		// default is connected,if some user logout,set command to 
		// ProtocolMatch.MAIN_CMD_USER_GONE
		this.Cmd = ProtocolMatch.MAIN_CMD_CONNECTED;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
}
