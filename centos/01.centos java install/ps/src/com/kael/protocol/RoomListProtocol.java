package com.kael.protocol;

import java.util.List;

import lib.kael.ProtocolBase;

import com.data.GameRoomData;

public class RoomListProtocol extends ProtocolBase{
	private List<GameRoomData> lst;
	public List<GameRoomData> getLst() {
		return lst;
	}
	public void setLst(List<GameRoomData> lst) {
		this.lst = lst;
	}
	public RoomListProtocol()
	{
		this.Cmd = ProtocolMatch.PS_CMD_GM_LIST;
	}
}
