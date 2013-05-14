package com.kael;

import lib.kael.LogicHandler;
import lib.kael.PojoMapper;
import lib.kael.ServerApp;

import org.codehaus.jackson.JsonNode;
import org.jboss.netty.channel.Channel;
import com.kael.protocol.ProtocolMatch;
import com.toplogic.GatewayLogic;
import com.toplogic.UserLogic;

public class GameServerLogicHandler implements LogicHandler
{
	@Override
	public boolean handle(Object data, Channel ch) {
		// get command first
		String hash = (String)data;
		if(hash.length() == 0)
			return false;
		JsonNode rootNode = PojoMapper.toNode(hash);
		if(rootNode==null){
			System.out.println("JsonNode wrong!");
			return false;
		}
//		short command = (short) jo.getInt("cmd");System.out.println("cmd:"+command);
		int command = rootNode.get("cmd").getIntValue();
//		if(!rootNode.has("ke"))return false;
//		int k = rootNode.get("ke").getIntValue();
		
		try{
			switch(command)
			{
				// c -> s handler part
				case ProtocolMatch.PS_CMD_REGISTER:
					return ((UserLogic)getBean("userLogic")).handleRegiste(hash,ch);
				case ProtocolMatch.PS_CMD_LOGIN:
					return ((UserLogic)getBean("userLogic")).handleLogin(hash, ch);
				case ProtocolMatch.PS_CMD_DELETEUSR:
					return ((UserLogic)getBean("userLogic")).handleDelete(hash, ch);
				case ProtocolMatch.PS_CMD_INTOROOM:
					return ((UserLogic)getBean("userLogic")).handleIntoGameRoom(hash, ch);
				case ProtocolMatch.PS_CMD_GM_LIST:
					return ((GatewayLogic)getBean("gatewayLogic")).handleGameServerList(hash, ch);
				case ProtocolMatch.PS_CMD_USERCOME:
				case ProtocolMatch.PS_CMD_USERGO:
					return ((GatewayLogic)getBean("gatewayLogic")).handleGameServerUserAction(hash, ch);
				case ProtocolMatch.PS_CMD_GS_START:
					return ((GatewayLogic)getBean("gatewayLogic")).handleGameServerStart(hash, ch);
				case ProtocolMatch.PS_CMD_USER_CANIN:
					return ((GatewayLogic)getBean("gatewayLogic")).handleGameServerCanIn(hash, ch);
				default:
					return false;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	private Object getBean(String name){
		return ServerApp.getInstance().m_context.getBean(name);
	}
}