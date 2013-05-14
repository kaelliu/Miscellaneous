package com.service;

import java.util.Map;

public class UserServiceImpl implements UserService{

	private UserDao userDao;
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public int login(Map param) {
		// TODO Auto-generated method stub
		return userDao.login(param);
	}

	@Override
	public int register(Map param) {
		// TODO Auto-generated method stub
//		userDao
		if(param.containsKey("username"))
		{
			int result = userDao.checkName((String) param.get("username"));
			if(result == 0){
				userDao.register(param);
				return Integer.parseInt(String.valueOf(param.get("uid")));
			}
			else
				return -1;
		}
		else return -1;
	}

	@Override
	public void insertUser(Map param) {
		// TODO Auto-generated method stub
		userDao.insertUser(param);
	}

	@Override
	public int getuid(String un) {
		// TODO Auto-generated method stub
		return userDao.getuid(un);
	}

	@Override
	public int checkName(String un) {
		// TODO Auto-generated method stub
		return userDao.checkName(un);
	}

	@Override
	public int countAll() {
		// TODO Auto-generated method stub
		return userDao.countAll();
	}

	@Override
	public void changepasswd(Map param) {
		// TODO Auto-generated method stub
		userDao.changepasswd(param);
	}

	@Override
	public void deleteuser(int id) {
		// TODO Auto-generated method stub
		userDao.deleteuser(id);
	}

	@Override
	public void changeUser(Map param) {
		// TODO Auto-generated method stub
		userDao.changeUser(param);
	}

	@Override
	public UserDetailDto getuserDetail(int uid) {
		// TODO Auto-generated method stub
		return userDao.getuserDetail(uid);
	}

	@Override
	public int login2(Map param) {
		// TODO Auto-generated method stub
		Integer res = userDao.login2(param);
		if(res == null)
			return -1;
		else
			return res.intValue();
	}

}
