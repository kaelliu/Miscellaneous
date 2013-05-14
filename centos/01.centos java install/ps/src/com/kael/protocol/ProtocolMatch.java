package com.kael.protocol;

public class ProtocolMatch
{
	// see interface file for detail
	// ps->c
	// no data send ,just tell client it can login
	public final static short MAIN_CMD_CONNECTED   = 2989;
	// c->ps
	public final static short PS_CMD_REGISTER   = 10;
	public final static short PS_CMD_LOGIN	    = 11;
	public final static short PS_CMD_MODIFY     = 12;// optional
	public final static short PS_CMD_DELETEUSR  = 13;
	
	public final static short PS_CMD_GM_LIST    = 20;
	public final static short PS_CMD_ROOMSTATUS = 21;// optional
	public final static short PS_CMD_USERCOME   = 22;// 
	public final static short PS_CMD_USERGO     = 23;
	
	public final static short PS_CMD_GS_START   = 30;
	// 进入房间，发送玩家具体信息给房间服务器
	public final static short PS_CMD_INTOROOM   = 31;
	
	// gs->ps,registe user detail info success
	public final static short PS_CMD_USER_CANIN   = 70;
	// ps->c
	// data send back:operate command id
	public final static short MAIN_CMD_OPERATE_RESULT  = 1000;
	public final static short MAIN_CMD_OPERATE_SUCCESS = 1001;
	// data send back:error code
	public final static short MAIN_CMD_OPERATE_FAILED  = 1002;
}
