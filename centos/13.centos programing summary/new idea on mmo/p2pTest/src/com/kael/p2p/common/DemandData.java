package com.kael.p2p.common;

public class DemandData implements Data{
	public DemandData() {

	}
	public DemandData(JsonData data) {
		this();
	}
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("message", "demand");
		return data.encode();
	}
	public static int hash() {
		return "demand".hashCode();
	}
}
