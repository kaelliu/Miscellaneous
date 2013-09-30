package com.kael.p2p.common;

public class HandshakeData implements Data{
	private Long token = null;
	private String stringToken = null;
	
	public HandshakeData() {
	}
	
	public HandshakeData(JsonData data) {
		this();
		if(data.get("token") != null) {
			token = Long.parseLong((String)data.get("token"));
		}
		if(data.get("stringToken") != null) {
			stringToken = (String)data.get("stringToken");
		}
	}
	
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("message", "handshake");
		if(token != null) {
			data.put("token", token.toString());
		}
		if(stringToken != null) {
			data.put("stringToken", stringToken);
		}
		return data.encode();
	}

	public Long getToken() {
		return token;
	}

	public void setToken(Long token) {
		this.token = token;
	}

	public String getStringToken() {
		return stringToken;
	}

	public void setStringToken(String stringToken) {
		this.stringToken = stringToken;
	}
}
