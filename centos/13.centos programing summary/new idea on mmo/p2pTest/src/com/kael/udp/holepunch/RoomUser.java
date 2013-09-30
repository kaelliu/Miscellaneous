package com.kael.udp.holepunch;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class RoomUser {
	public int uid;
	public DatagramSocket client;
	public byte status;
	public InetAddress natAddr;
	public int natPort;
	public InetAddress localAddr;
	public short local;
}
