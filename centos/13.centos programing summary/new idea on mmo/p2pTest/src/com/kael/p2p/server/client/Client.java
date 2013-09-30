package com.kael.p2p.server.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Random;

import com.kael.p2p.common.ConnectionData;
import com.kael.p2p.common.Data;
import com.kael.p2p.common.HandshakeData;
import com.kael.p2p.common.JsonData;
import com.kael.p2p.common.ModeData;
import com.kael.p2p.common.PingData;

public class Client {
	private final Long timeout = 300000L;
	private SocketAddress address = null;
	private SocketAddress localAddress = null;
	private Long lastMessageTime = null;
	private Long id = null;
	private Long handshakeToken = null;
	private boolean udpCheck = false;
	private boolean udpHolePunchingCheck = false;
	private Long target = null;
	
	public boolean isReadyClient() {
		return udpCheck;
	}
	
	public Client(DatagramPacket packet, Long id) {
		lastMessageTime = System.currentTimeMillis();
		address = packet.getSocketAddress();
		this.id = id;
	}
	
	public void setSystemClient() {
		target = -1L;
	}
	
	public void setTarget(Long targetId) {
		target = targetId;
	}
	
	public Long getTarget() {
		return target;
	}
	
	public Long getId() {
		return id;
	}
	
	public SocketAddress getAddress() {
		return address;
	}
	
	public String getAddressKey() {
		return address.toString();
	}
	
	private void generateHandshakeToken() {
		Random randam = new Random(System.currentTimeMillis());
		handshakeToken = randam.nextLong();
		handshakeToken = (handshakeToken < 0 ? -handshakeToken : handshakeToken);
	}
	public boolean sendPing(DatagramSocket socket) {
		if(lastMessageTime < System.currentTimeMillis() - timeout) {
			return false;
		}
		PingData pingData = new PingData();
		sendData(pingData);
		return true;
	}

	public void receiveMessage(DatagramPacket packet) {
		if(udpCheck) {
			lastMessageTime = System.currentTimeMillis();
		}
		JsonData recvData = new JsonData(new String(packet.getData()));
		if("ping".equals(recvData.get("message"))) {
			return;
		}
		if("conn".equals(recvData.get("message"))) {
			connectionEvent(new ConnectionData(recvData));
			return;
		}
		if("handshake".equals(recvData.get("message"))) {
			handshakeEvent(new HandshakeData(recvData));
			return;
		}
		if("demand".equals(recvData.get("message"))) {
			demandEvent(recvData);
			return;
		}
		otherEvent(recvData);
	}
	
	private void otherEvent(JsonData data) {

	}
	private void demandEvent(JsonData data) {

	}
	
	private void connectionEvent(ConnectionData connectionData) {
		if(connectionData.getId() != null) {
			id = connectionData.getId();
		}
		if(connectionData.getTarget() != null) {
			target = connectionData.getTarget();
		}
		if(connectionData.getLocalAddress() != null && connectionData.getLocalPort() != null) {
			localAddress = new InetSocketAddress(connectionData.getLocalAddress(), connectionData.getLocalPort());
		}
		sendHandshake();
	}

	private void sendHandshake() {
		HandshakeData handshakeData = new HandshakeData();
		generateHandshakeToken();
		handshakeData.setToken(handshakeToken);
		sendData(handshakeData);
	}
	
	private void handshakeEvent(HandshakeData handshakeData) {
		String token = handshakeData.getStringToken();
		System.out.println(token);
		System.out.println(handshakeToken);
		if(token != null && token.equals(Long.toHexString(handshakeToken))) {
			System.out.println("handshaketoken is ok");
			ClientManager clientManager = ClientManager.getInstance();
			clientManager.setupClient(this);
			udpCheck = true;
		}
		else {
			System.out.println("handshaketoken is ng");
		}
	}
	
	public void sendMode() {
		ModeData modeData = new ModeData();
		modeData.setId(id);
		modeData.setTarget(target);
		sendData(modeData);
	}
	
	public void sendConnectTargetData() {

	}
	
	private void sendData(String data) {
		try {
			byte[] bytes = data.getBytes();
			DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address);
			ClientManager clientManager = ClientManager.getInstance();
			clientManager.getSocket().send(packet);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendData(Data data) {
		sendData(data.encode());
	}
}
