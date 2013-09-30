package com.kael.p2p.server.adapter;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.kael.p2p.server.client.Client;
import com.kael.p2p.server.client.ClientManager;
import com.kael.p2p.server.event.SocketEvent;
import com.kael.p2p.server.event.TimerEvent;

public class ServerAdapter {
	private TimerEvent timer;
	private SocketEvent socket;
	
	public void start() {
		try {
			timer = new TimerEvent(this);
			timer.start();
			socket = new SocketEvent(this);
			socket.start();
			ClientManager clientManager = ClientManager.getInstance();
			clientManager.setAdapter(this);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void scheduledJob() {
		ClientManager clientManager = ClientManager.getInstance();
		clientManager.pingJob();
	}
	public void dataJob(DatagramPacket packet) {
		ClientManager clientManager = ClientManager.getInstance();
		Client client = clientManager.getTargetClient(packet);
		client.receiveMessage(packet);
	}
	public DatagramSocket getSocket() {
		return socket.getSocket();
	}
}
