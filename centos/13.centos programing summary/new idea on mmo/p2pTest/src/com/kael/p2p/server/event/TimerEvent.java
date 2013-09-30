package com.kael.p2p.server.event;

import com.kael.p2p.server.adapter.ServerAdapter;

public class TimerEvent extends Thread{
	private final ServerAdapter adapter;
	
	private final long interval = 30000L;
	public TimerEvent(ServerAdapter adapter) {
		this.adapter = adapter;
	}
	@Override
	public void run() {
		while(true) {
			try {
				Thread.sleep(interval);
				adapter.scheduledJob();
			}
			catch (Exception e) {
			}
		}
	}
}
