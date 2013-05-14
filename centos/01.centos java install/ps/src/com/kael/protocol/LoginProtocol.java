package com.kael.protocol;

import lib.kael.ProtocolBase;

public class LoginProtocol extends ProtocolBase
{
	/**
	 * 
	 */
	private String un;
	public String getUn() {
		return un;
	}
	public void setUn(String un) {
		this.un = un;
	}
	
	private String pass;
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public LoginProtocol()
	{
		this.Cmd= ProtocolMatch.PS_CMD_LOGIN;
	}
}