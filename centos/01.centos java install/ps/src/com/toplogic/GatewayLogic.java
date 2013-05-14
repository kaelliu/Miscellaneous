package com.toplogic;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import lib.kael.ExternalIpAddressFetcher;
import lib.kael.ProtocolBase;
import lib.kael.ServerConstant;
import lib.kael.util.IPUtils;

import org.jboss.netty.channel.Channel;

import com.data.GameRoomData;
import com.kael.PServer;
import com.kael.protocol.GameServerStart;
import com.kael.protocol.GameServerUserChange;
import com.kael.protocol.IntoGameProtocol;
import com.kael.protocol.ProtocolMatch;
import com.kael.protocol.RoomListProtocol;
import com.kael.protocol.UserIntoGameRoom;
import com.service.UserService;

public class GatewayLogic {
	public boolean handleGameServerList(String msg,Channel ch)
	{
		RoomListProtocol glp = new RoomListProtocol();
		List lst = new ArrayList<GameRoomData>();
		Set entrys = PServer.sessionDatas.entrySet();
		{
			Iterator it = entrys.iterator();
			while(it.hasNext()) 
			{
				Entry entry = (Entry) it.next();
				GameRoomData roomdata = (GameRoomData) entry.getValue();
				lst.add(roomdata);
			}
		}
		glp.setLst(lst);
		ch.write(glp);
		return true;
	}
	
	public boolean handleGameServerUserAction(String msg,Channel ch)
	{
		GameServerUserChange gc = (GameServerUserChange) new GameServerUserChange().fromJObj(msg);
		GameRoomData grd = PServer.sessionDatas.get(ch);
		if(grd == null)
			return false;
		grd.setRoomUserCount((short) (grd.getRoomUserCount() + ((gc.getTp() == 1)?1:-1)));
		return true;
	}
	
	public boolean handleGameServerStart(String msg,Channel ch)
	{
		int roomid = PServer.ids.incrementAndGet();
		GameServerStart gs = (GameServerStart) new GameServerStart().fromJObj(msg);
		GameRoomData grd = new GameRoomData();
		// save the room config and make a room id
//		StringBuffer sb = new StringBuffer();
		String ip = ((InetSocketAddress) ch.getRemoteAddress()).getAddress().toString();
		if(ip.compareTo("/127.0.0.1")==0)
		{
			if(ServerConstant.isOnLine)
				ip = new ExternalIpAddressFetcher().getMyExternalIpAddress();
			else
				ip = IPUtils.getFirstNoLoopbackAddress();
		}
		System.out.println("ip register:"+ip);
		grd.setIp(ip);
		grd.setP(gs.getGrd().getP());
		grd.setRoomid(roomid);
		grd.setRoomType(gs.getGrd().getRoomType());
		grd.setRoomUserCount(gs.getGrd().getRoomUserCount());
		// save room
		PServer.sessionDatas.put(ch, grd);
		PServer.ridWithChannels.put(grd.getRoomid(), ch);
		// send room id to room
		gs.getGrd().setRoomid(grd.getRoomid());
		ch.write(gs);
		// need send to other user?
		return true;
	}
	
	public boolean handleGameServerCanIn(String msg,Channel ch)
	{
		// take off that user
		UserIntoGameRoom gs = (UserIntoGameRoom) new UserIntoGameRoom().fromJObj(msg);
		gs.setCmd(ProtocolMatch.PS_CMD_INTOROOM);
		Channel chuser = PServer.uidWithChannels.get(gs.getUid());
		chuser.write(gs);
		// take off from buffer
		PServer.uidWithChannels.remove(gs.getUid());
		return true;
	}
}
