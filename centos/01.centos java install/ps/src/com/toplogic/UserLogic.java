package com.toplogic;

import java.util.HashMap;
import java.util.Map;

import lib.kael.ProtocolBase;

import org.jboss.netty.channel.Channel;

import com.kael.PServer;
import com.kael.protocol.DeleteUserProtocol;
import com.kael.protocol.ErrorCode;
import com.kael.protocol.IntoGameProtocol;
import com.kael.protocol.LoginProtocol;
import com.kael.protocol.OperateResultProtocol;
import com.kael.protocol.ProtocolMatch;
import com.kael.protocol.RegisterLoginSuccessProtocol;
import com.kael.protocol.RegisterProtocol;
import com.kael.protocol.UserIntoGameRoom;
import com.service.UserDetailDto;
import com.service.UserService;

public class UserLogic {
	private UserService userService;
	public UserService getUserService() {
		return userService;
	}
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	public boolean handleRegiste(String msg, Channel ch) {
		
		RegisterProtocol rp = (RegisterProtocol) new RegisterProtocol().fromJObj(msg);
		Map param = new HashMap();
		param.put("username", rp.getUn());
		param.put("password", rp.getPass());
		param.put("sex", 1);//rp.getSex());
		param.put("ico", 1);//rp.getIco());
		param.put("nm", "kael");//rp.getNm());
		int result = userService.register(param);
		
		OperateResultProtocol op=null;
		if(result == -1)// failed
		{
			op = new OperateResultProtocol(ErrorCode.ERROR_REGISTE_FAILED,false);
			ch.write(op);
		}
		else
		{
			// SUCCESS,UID
			RegisterLoginSuccessProtocol rsp = new RegisterLoginSuccessProtocol();
			rsp.setUid(result);
			ch.write(rsp);
		}
		return (op == null);
	}
	
	public boolean handleDelete(String msg, Channel ch) {
		
		DeleteUserProtocol dp = (DeleteUserProtocol) new DeleteUserProtocol().fromJObj(msg);
		userService.deleteuser(dp.getUid());
		OperateResultProtocol op = new OperateResultProtocol(dp.getCmd(), true);
		ch.write(op);
		return false;
	}
	
	public boolean handleLogin(String msg, Channel ch) {
		
		LoginProtocol lp = (LoginProtocol) new LoginProtocol().fromJObj(msg);
		Map param = new HashMap();
		param.put("username", lp.getUn());
		param.put("password", lp.getPass());
		int result = userService.login(param);
		
		OperateResultProtocol op = null;
		if(result == 1)// extractly match
		{
			int uid = userService.getuid(lp.getUn());
			RegisterLoginSuccessProtocol rsp = new RegisterLoginSuccessProtocol();
			rsp.setUid(uid);
			rsp.setCmd(ProtocolMatch.PS_CMD_LOGIN);
			ch.write(rsp);
		}
		else
		{
			op = new OperateResultProtocol(ErrorCode.ERROR_LOGIN_FAILED,false);
		}
		if(op!=null)
			ch.write(op);
		return (op == null);
	}
	
	public boolean handleIntoGameRoom(String msg, Channel ch) {
		UserIntoGameRoom gs = (UserIntoGameRoom) new UserIntoGameRoom().fromJObj(msg);
		int uid = gs.getUid();
		int rid = gs.getRid();
		UserDetailDto ud = userService.getuserDetail(uid);
		if(ud == null)
			return false;
		else
		{
			Channel gameserver = PServer.ridWithChannels.get(rid);
			if(gameserver==null)
				return false;
			else
			{
				IntoGameProtocol igp = new IntoGameProtocol();
				UserDetailDto udn = new UserDetailDto();
				udn.setIcon(ud.getIcon());
				udn.setSex(ud.getSex());
				udn.setUid(uid);
				udn.setNm(ud.getNm());
				igp.setUd(udn);
//				igp.getUd().setUid(uid);
				gameserver.write(igp);
				// problem :need wait gameserver write back and write back to user
				// here write back successful msg directly
//				ch.write(gs);
				// mod: keep channel,wait gameserver successful msg back
				PServer.uidWithChannels.put(uid, ch);
				return true;
			}
		}
	}
}
