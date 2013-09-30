package com.kael.p2p.common;

public class ConnectionData implements Data{
	private Long id = null;
	private Long target = null;
	private String localAddress = null;
	private Integer localPort = null;

	public ConnectionData() {
	}
	public ConnectionData(JsonData data) {
		this();
		localAddress = (String)data.get("localAddress");
		localPort = Integer.parseInt((String)data.get("localPort"));
		if(data.get("id") == null) {
			return;
		}
		id = Long.parseLong((String)data.get("id"));
		if(data.get("target") == null) {
			return;
		}
		if("system".equals(data.get("target"))) {
			target = -1L;
		}
		else {
			target = Long.parseLong((String)data.get("target"));
		}
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getTarget() {
		return target;
	}
	public void setTarget(Long target) {
		this.target = target;
	}
	public String getLocalAddress() {
		return localAddress;
	}
	public void setLocalAddress(String localAddress) {
		this.localAddress = localAddress;
	}
	public Integer getLocalPort() {
		return localPort;
	}
	public void setLocalPort(Integer port) {
		this.localPort = port;
	}
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("message", "conn");
		if(id != null) {
			data.put("id", id.toString());
		}
		if(target != null) {
			if(target == -1L) { 
				data.put("target", "system");
			}
			else {
				data.put("target", target.toString());
			}
		}
		if(localPort != null) {
			data.put("localPort", localPort.toString());
		}
		if(localAddress != null) {
			data.put("localAddress", localAddress);
		}
		return data.encode();
	}
}
