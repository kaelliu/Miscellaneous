package com.kael;

import lib.kael.ConnectionHandler;
import lib.kael.ProtocolBase;
import lib.kael.ServerApp;

import org.jboss.netty.channel.Channel;

import com.data.GameRoomData;
import com.kael.protocol.ProtocolMatch;

public class GameConnectionHandler implements ConnectionHandler
{

	@Override
	public boolean handleConnect(Channel ch) {
		//
		ProtocolBase ccp = new ProtocolBase();
		ccp.setCmd(ProtocolMatch.MAIN_CMD_CONNECTED);
		ch.write(ccp);
		return true;
	}

	@Override
	public boolean handleDisConnect(Channel ch) {
		// clean a game server
		GameRoomData grd = PServer.sessionDatas.get(ch);
		if(grd != null)
		{
			PServer.ridWithChannels.remove(grd.getRoomid());
			PServer.sessionDatas.remove(ch);
		}
		
		return true;
	}

	private Object getBean(String name){
		return ServerApp.getInstance().m_context.getBean(name);
	}
}