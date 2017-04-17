package com.jiangsu.service;

import java.util.List;

import com.jiangsu.domain.User;

	
	/**
	 * �û�������ص�ҵ���ӿ�
	 * @author jiangsu
	 *
	 */
public interface IUserService {
		
		/**
		 * �û���¼
		 * @param logonName
		 * @param logonPwd
		 * @return
		 */
		User login(String logonName,String logonPwd);
		
		/**
		 * �����û�
		 * @param user
		 * @return
		 */
		int saveUser(User user);
		
		/**
		 * �޸��û�
		 * @param user
		 * @return
		 */
		int modifyUser(User user);
		
		/**
		 * �����û�id��ɾ���û�
		 * @param userID
		 * @return
		 */
		int removeUser(Integer userID);
		
		/**
		 * �����û�id����ȡ�û���Ϣ
		 * @param userID
		 * @return
		 */
		User findUserById(Integer userID);
		
		/**
		 * ��ѯ�����û�
		 * @return
		 */
		List<User> findAllUser();
		
		/**
		 * ����������ѯ�û���Ϣ
		 * @param userName
		 * @param gender
		 * @param education
		 * @param isUpload
		 * @return
		 */
		List<User> findUserByCondition(String userName,String gender,String education,String isUpload);
		
		
		
		
		
		
}
