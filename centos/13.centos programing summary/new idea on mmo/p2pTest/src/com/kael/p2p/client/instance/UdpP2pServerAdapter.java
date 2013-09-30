package com.kael.p2p.client.instance;

import java.util.ArrayList;
import java.util.List;

import com.kael.p2p.client.Server;
import com.kael.p2p.client.event.TimerEvent;

public class UdpP2pServerAdapter implements Server{
	private final boolean allowLocalConnection = true;
	private TimerEvent timer;

	private UdpP2pServer systemServer = null;
	private List<UdpP2pServer> clientServer = null;
	public UdpP2pServerAdapter() {
		clientServer = new ArrayList<UdpP2pServer>();
	}
	public void start() {
		try {
			timer = new TimerEvent(this);
			timer.start();

			setupDefaultClient();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setApplication() {
	}
	public void scheduledJob() {
		System.out.println("TimerEvent");
	}

	public void setupDefaultClient() {
		UdpP2pServer server = new UdpP2pServer(this);
		server.setLocalDataSend(allowLocalConnection);
		server.connect();
	}
	public void setupNextClient(UdpP2pServer server) {
		if(server.isSystemClient()) {
			systemServer = server;
			UdpP2pServer nextServer = new UdpP2pServer(this);
			nextServer.setLocalDataSend(allowLocalConnection);
			nextServer.connect(UdpP2pServer.getClientId());
		}
		else {
			clientServer.add(server);
		}
	}
}
