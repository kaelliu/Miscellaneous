package com.kael.p2p.client.instance;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.kael.p2p.common.ConnectionData;
import com.kael.p2p.common.Data;
import com.kael.p2p.common.HandshakeData;
import com.kael.p2p.common.JsonData;
import com.kael.p2p.common.ModeData;
import com.kael.p2p.common.PingData;

public class UdpP2pServer implements Runnable{
	private final Long timeout = 300000L;
	private DatagramSocket socket;
	private SocketAddress server = new InetSocketAddress("localhost", 12345);
	private SocketAddress target = null;
	private UdpP2pServerAdapter adapter;
	private Long lastMessageTime = null;
	private static Long id = null;
	private boolean localDataSend = false;
	private Byte type = null;

	public static Long getClientId() {
		return id;
	}
	
	public boolean isSystemClient() {
		return type == (byte)0xFF;
	}
	
	public UdpP2pServer(UdpP2pServerAdapter adapter) {
		this.adapter = adapter;
		try {
			// Constructs a datagram socket and binds it to any available port on the local host machine
			socket = new DatagramSocket();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		Thread t = new Thread(this);
		t.setName("UdpP2p receive thread");
		t.start();
	}
	
	public UdpP2pServer(SocketAddress server) {
		super();
		this.server = server;
	}
	
	public void setLocalDataSend(boolean flg) {
		localDataSend = flg;
	}
	
	public void connect() {
		ConnectionData connectionData = new ConnectionData();
		addLocalData(connectionData);
		sendData(connectionData);
	}
	
	public void connect(Long id) {
		ConnectionData connectionData = new ConnectionData();
		addLocalData(connectionData);
		connectionData.setId(id);
		sendData(connectionData);
	}
	
	public void connectSystem(Long id) {
		if(id == null) {
			return;
		}
		ConnectionData connectionData = new ConnectionData();
		addLocalData(connectionData);
		connectionData.setId(id);
		connectionData.setTarget(-1L);
		sendData(connectionData);
	}
	
	public void connectClient(Long id, Long other) {
		if(id == null || other == null) {
			return;
		}
		ConnectionData connectionData = new ConnectionData();
		addLocalData(connectionData);
		connectionData.setId(id);
		connectionData.setTarget(other);
		sendData(connectionData);
	}
	
	private void addLocalData(ConnectionData connectionData) {
		if(localDataSend) {
			try {
				connectionData.setLocalAddress(InetAddress.getLocalHost().getHostAddress());
				connectionData.setLocalPort(socket.getLocalPort());
				System.out.println(connectionData.encode());
			}
			catch (Exception e) {
			}
		}
	}
	
	private void sendData(Data data) {
		try {
			byte[] bytes = data.encode().getBytes();
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, server);
			socket.send(packet);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean sendPing() {
		if(lastMessageTime < System.currentTimeMillis() - timeout) {
			return false;
		}
		PingData pingData = new PingData();
		sendData(pingData);
		return true;
	}
	
	public void event(DatagramPacket packet) {
		lastMessageTime = System.currentTimeMillis();
		JsonData recvData = new JsonData(new String(packet.getData()));
		System.out.println(recvData);
		if("handshake".equals(recvData.get("message"))) {
			handshakeEvent(new HandshakeData(recvData));
			return;
		}
		if("mode".equals(recvData.get("message"))) {
			modeEvent(new ModeData(recvData));
			return;
		}
		System.out.println("soreigai");
	}

	private void handshakeEvent(HandshakeData handshakeData) {
		JsonData sendData = new JsonData();
		System.out.println("handshake");
		handshakeData.setStringToken(Long.toHexString(handshakeData.getToken()));
		sendData(handshakeData);
	}
	
	private void modeEvent(ModeData modeData) {
		System.out.println(modeData.encode());
		id = modeData.getId();
		type = 0x01;
		if(modeData.getTarget() <= 0) {
			switch (modeData.getTarget().intValue()) {
			case -1:
				type = (byte)0xFF; 
				break;
			case 0:
				type = (byte)0x00; 
				break;
			default:
				break;
			}
		}
		else {
		}
		adapter.setupNextClient(this);
	}
	@Override
	public void run() {
		try {
			while(true) {
				DatagramPacket packet = new DatagramPacket(new byte[1024], 1024);
				socket.receive(packet);
				event(packet);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
