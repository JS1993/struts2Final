package com.jiangsu.service.impl;

import java.util.List;

import com.jiangsu.dao.IUserDao;
import com.jiangsu.dao.impl.UserDaoImpl;
import com.jiangsu.domain.User;
import com.jiangsu.service.IUserService;

public class UserServiceImpl implements IUserService {
	
	private IUserDao dao = new UserDaoImpl(); 

	public User login(String logonName, String logonPwd) {
		return dao.selectUserByInfo(logonName,logonPwd);
	}

	
	public int saveUser(User user) {
		return dao.addUser(user);
	}

	
	public int modifyUser(User user) {
		return dao.updateUser(user);
	}

	
	public int removeUser(Integer userID) {
		return dao.deleteUser(userID);
	}

	
	public User findUserById(Integer userID) {
		return dao.selectUserById(userID);
	}

	
	public List<User> findAllUser() {
		return dao.selectAllUser();
	}

	
	public List<User> findUserByCondition(String userName, String gender,
			String education, String isUpload) {
		return dao.selectUserByCondition(userName,gender,education,isUpload);
	}

}
