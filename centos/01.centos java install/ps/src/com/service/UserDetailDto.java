package com.service;

public class UserDetailDto {
	private int uid;
	private byte sex;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public byte getSex() {
		return sex;
	}
	public void setSex(byte sex) {
		this.sex = sex;
	}
	public String getNm() {
		return nm;
	}
	public void setNm(String nm) {
		this.nm = nm;
	}
	public byte getIcon() {
		return icon;
	}
	public void setIcon(byte icon) {
		this.icon = icon;
	}
	private String nm;// nick name
	private byte icon;
}
