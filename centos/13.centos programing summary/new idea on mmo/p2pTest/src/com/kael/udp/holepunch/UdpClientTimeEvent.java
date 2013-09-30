package com.kael.udp.holepunch;

public class UdpClientTimeEvent extends Thread{
	private final UdpClient adapter;
	private final long interval = 600L;
	public UdpClientTimeEvent(UdpClient adapter) {
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
