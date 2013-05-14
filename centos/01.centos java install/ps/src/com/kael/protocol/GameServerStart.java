package com.kael.protocol;

import com.data.GameRoomData;

import lib.kael.ProtocolBase;

public class GameServerStart extends ProtocolBase{
	private GameRoomData grd;
	public GameRoomData getGrd() {
		return grd;
	}
	public void setGrd(GameRoomData grd) {
		this.grd = grd;
	}
	public GameServerStart()
	{
		this.Cmd = ProtocolMatch.PS_CMD_GS_START;
	}
}
