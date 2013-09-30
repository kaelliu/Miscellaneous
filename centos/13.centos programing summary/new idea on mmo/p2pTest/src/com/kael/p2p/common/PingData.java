package com.kael.p2p.common;

public class PingData implements Data{
	private String ping;
	public PingData() {
		ping = "ping";
	}
	
	public PingData(JsonData data) {
		this();
	}
	
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("message", ping);
		return data.encode();
	}

}
