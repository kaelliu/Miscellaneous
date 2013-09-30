package com.kael.p2p.common;

public class ConnectTargetData implements Data{
	// 目标nat外部地址,端口
	private String address;
	private Integer port;
	// 目标nat内部地址,端口
	private String localAddress;
	private Integer localPort;
	private Long id;
	public ConnectTargetData() {
	}
	public ConnectTargetData(JsonData data) {
		this();
		address = (String)data.get("address");
		port = Integer.parseInt((String)data.get("port"));
		id = Long.parseLong((String)data.get("id"));
		localAddress = (String)data.get("localAddress");
		localPort = Integer.parseInt((String)data.get("localPort"));
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
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
	public void setLocalPort(Integer localPort) {
		this.localPort = localPort;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("address", address);
		data.put("port", port.toString());
		data.put("id", id.toString());
		if(localAddress != null && localPort != null) {
			data.put("localAddress", localAddress);
			data.put("localPort", localPort.toString());
		}
		return null;
	}
}
