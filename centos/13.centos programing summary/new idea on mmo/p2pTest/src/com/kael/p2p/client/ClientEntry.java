package com.kael.p2p.client;

import com.kael.p2p.client.instance.UdpP2pServerAdapter;

public class ClientEntry {
	public static void main(String[] args) throws Exception {
		new ClientEntry();
	}
	public ClientEntry() throws Exception {
		UdpP2pServerAdapter adapter = new UdpP2pServerAdapter();
		adapter.start();
	}
}
