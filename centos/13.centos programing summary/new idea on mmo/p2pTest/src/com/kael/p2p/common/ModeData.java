package com.kael.p2p.common;

public class ModeData implements Data{
	private Long id = null;
	private Long target = null;
	public ModeData() {
	}

	public ModeData(JsonData data) {
		this();
		id = Long.parseLong((String)data.get("id"));
		target = Long.parseLong((String)data.get("target"));
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
	@Override
	public String encode() {
		JsonData data = new JsonData();
		data.put("message", "mode");
		if(id != null) {
			data.put("id", id.toString());
		}
		if(target != null) {
			data.put("target", target.toString());
		}
		return data.encode();
	}
}
