package com.jiangsu.dao;

import java.util.List;

import com.jiangsu.domain.User;

/**
 * �û�������س־ò�ӿ�
 * @author jiangsu
 *
 */

public interface IUserDao {


	/**
	 * �����û��ĵ�¼���������ѯ�û�
	 * @param logonName
	 * @param logonPwd
	 * @return
	 */
	User selectUserByInfo(String logonName, String logonPwd);
	
	/**
	 * ����û�
	 * @param user
	 * @return
	 */
	int addUser(User user);
	
	/**
	 * �����û���Ϣ
	 * @param user
	 * @return
	 */
	int updateUser(User user);
	
	/**
	 * �����û�id��ɾ���û���Ϣ
	 * @param userID
	 * @return
	 */
	int deleteUser(Integer userID);
	
	/**
	 * �����û�id����ѯ�û���Ϣ
	 * @param userID
	 * @return
	 */
	User selectUserById(Integer userID);
	
	/**
	 * ��ѯ�����û���Ϣ
	 * @return
	 */
	List<User> selectAllUser();
	
	/**
	 * ����ָ��������ȡ�û���Ϣ
	 * ��������null��ʱ�򣬱�ʾ��������
	 * @param userName
	 * @param gender
	 * @param education
	 * @param isUpload
	 * @return
	 */
	List<User> selectUserByCondition(String userName, String gender,String education, String isUpload);


}
