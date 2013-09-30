package com.kael.p2p.client.event;

import com.kael.p2p.client.instance.UdpP2pServerAdapter;

public class TimerEvent extends Thread{
	private final UdpP2pServerAdapter adapter;
	private final long interval = 30000L;
	public TimerEvent(UdpP2pServerAdapter adapter) {
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
