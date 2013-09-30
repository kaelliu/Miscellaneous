package com.kael.p2p.server;

import com.kael.p2p.server.adapter.ServerAdapter;

public class ServerEntry {
	public static void main(String[] args) {
		new ServerEntry();
	}
	public ServerEntry() {
		ServerAdapter adapter = new ServerAdapter();
		adapter.start();
		System.out.println("startup end...");
	}
}
