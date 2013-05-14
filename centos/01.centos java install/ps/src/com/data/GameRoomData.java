package com.data;

public class GameRoomData {
	private int roomid;
	public int getRoomid() {
		return roomid;
	}
	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}
	public short getRoomType() {
		return roomType;
	}
	public void setRoomType(short roomType) {
		this.roomType = roomType;
	}
	public short getRoomUserCount() {
		return roomUserCount;
	}
	public void setRoomUserCount(short roomUserCount) {
		this.roomUserCount = roomUserCount;
	}
	
	private short roomType;
	private short roomUserCount;
	private String ip;// ip:port
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public short getP() {
		return p;
	}
	public void setP(short p) {
		this.p = p;
	}

	private short p;// port
}
