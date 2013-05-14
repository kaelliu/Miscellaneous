package com.service;

import java.util.Map;

public interface UserService {
	public int login(Map param);
	public int login2(Map param);
	public int register(Map param);
	public void insertUser(Map param);
	public int getuid(String un);
	public int checkName(String un);
	public int countAll();
	public void changepasswd(Map param);
	public void deleteuser(int id);
	public void changeUser(Map param);
	public UserDetailDto getuserDetail(int uid);
}
