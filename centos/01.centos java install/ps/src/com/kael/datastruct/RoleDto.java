package com.kael.datastruct;

import lib.kael.CommonDto;

public class RoleDto extends CommonDto{
	private int id;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public short getRolex() {
		return rolex;
	}
	public void setRolex(short rolex) {
		this.rolex = rolex;
	}
	public short getRoley() {
		return roley;
	}
	public void setRoley(short roley) {
		this.roley = roley;
	}
	public int getRolehp() {
		return rolehp;
	}
	public void setRolehp(int rolehp) {
		this.rolehp = rolehp;
	}
	public String getRolename() {
		return rolename;
	}
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
	private short rolex;
	private short roley;
	private int rolehp;
	private String rolename;
}
