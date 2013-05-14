package com.kael.protocol;

import lib.kael.ProtocolBase;

public class RegisterProtocol extends ProtocolBase 
{
	/**
	 * 
	 */
	private String un;//userName;
	private String pass;//passwd;
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	private String nm;// nickname;
	private int    sex;
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	public int getIco() {
		return ico;
	}
	public void setIco(int ico) {
		this.ico = ico;
	}
	private int    ico;// icon
//	private boolean id;// 身份证验证是否通过
	public RegisterProtocol()
	{
		this.Cmd= ProtocolMatch.PS_CMD_REGISTER;
	}
	public RegisterProtocol(String un,String pw)
	{
		this.un=un;
		pass=pw;
		Cmd= ProtocolMatch.PS_CMD_REGISTER;
	}
	
	public String getUn() {
		return un;
	}
	public void setUn(String un) {
		this.un = un;
	}
	
}