package com.kael.udp.holepunch;

import java.util.concurrent.ConcurrentHashMap;

public class Room {
	public int id;
	public byte curPeople;
	public final byte maxPeople = 100;
	public final byte minPeople = 2;
	public String chiefUser = null;// 房主
	public ConcurrentHashMap<String,RoomUser> users;
}
