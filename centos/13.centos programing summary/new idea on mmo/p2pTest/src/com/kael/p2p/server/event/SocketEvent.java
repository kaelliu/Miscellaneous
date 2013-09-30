package com.kael.p2p.server.event;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.kael.p2p.server.adapter.ServerAdapter;

public class SocketEvent extends Thread{
	private final ServerAdapter adapter;
	private final int port = 12345;
	private DatagramSocket socket = null;
	
	public SocketEvent(ServerAdapter adapter) throws SocketException {
		this.adapter = adapter;
		socket = new DatagramSocket(port);
	}
	
	@Override
	public void run() {
		DatagramPacket recvPacket;
		ExecutorService execService = Executors.newCachedThreadPool();
		while(true) {
			try {
				recvPacket = new DatagramPacket(new byte[1024], 1024);
				socket.receive(recvPacket); 
				execService.execute(new SocketJob(recvPacket));
			}
			catch (IOException e) {
			}
		}
	}
	
	private class SocketJob implements Runnable {
		private DatagramPacket packet;
		public SocketJob(DatagramPacket packet) {
			this.packet = packet;
		}
		@Override
		public void run() {
			adapter.dataJob(packet);
		}
	}
	public DatagramSocket getSocket() {
		return socket;
	}
}
