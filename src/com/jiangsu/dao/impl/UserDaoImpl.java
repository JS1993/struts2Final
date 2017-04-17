package com.jiangsu.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.lang3.StringUtils;

import com.jiangsu.dao.IUserDao;
import com.jiangsu.domain.User;
import com.jiangsu.utils.JNDIUtil;


public class UserDaoImpl implements IUserDao {

private QueryRunner qr = new QueryRunner(JNDIUtil.getDataSource());
	
	
	public int addUser(User user) {
		try{
			return qr.update("insert into S_User(userID,userName,logonName,logonPwd,sex,birthday,education,telephone,hobby,path,filename,remark)values(?,?,?,?,?,?,?,?,?,?,?,?)",
					user.getUserID(),user.getUserName(),user.getLogonName(),user.getLogonPwd(),user.getSex(),user.getBirthday(),user.getEducation(),user.getTelephone(),
					user.getHobby(),user.getPath(),user.getFilename(),user.getRemark());
		}catch(Exception e){
			throw new RuntimeException(e);
		}	}

	
	public int updateUser(User user) {
		try{
			return qr.update("update S_User set userName=?,logonName=?,logonPwd=?,sex=?,birthday=?,education=?,telephone=?,hobby=?,path=?,filename=?,remark=? where userID = ?",
					user.getUserName(),user.getLogonName(),user.getLogonPwd(),user.getSex(),user.getBirthday(),user.getEducation(),user.getTelephone(),
					user.getHobby(),user.getPath(),user.getFilename(),user.getRemark(),user.getUserID());
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	public int deleteUser(Integer userid) {
		try{
			return qr.update("delete from S_User where userID = ?",userid);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	public User selectUserById(Integer userid) {
		try{
			return qr.query("select * from S_User where userID = ? ", new BeanHandler<User>(User.class),userid);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}

	
	public List<User> selectAllUser() {
		try{
			return qr.query("select * from S_User ", new BeanListHandler<User>(User.class));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	
	public User selectUserByInfo(String logonName, String logonPwd) {
		try{
			return qr.query("select * from S_User where logonName=? and logonPwd=? ", new BeanHandler<User>(User.class),logonName,logonPwd);
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}


	public List<User> selectUserByCondition(String userName, String gender,
			String education, String isUpload) {
		
		//如果上面的所有条件都没选的话
		if(StringUtils.isBlank(userName) && StringUtils.isBlank(gender) && StringUtils.isBlank(education) && StringUtils.isBlank(isUpload)){
			return selectAllUser();
		}else{
			try {
				List<Object> parameters = new ArrayList<Object>();
				String sql = " select * from S_User ";
				StringBuffer ss = new StringBuffer(sql);
				ss.append(" where 1=1 ");
				if(StringUtils.isNotBlank(userName)){
					ss.append(" and userName like ? ");
					parameters.add("%"+userName+"%");
				}
				if(StringUtils.isNotBlank(gender)){
					ss.append(" and gender = ? ");
					parameters.add(gender);
				}
				if(StringUtils.isNotBlank(education)){
					ss.append(" and education = ? ");
					parameters.add(education);
				}
				if(StringUtils.isNotBlank(isUpload)){
					//用户需要使用此条件
					if("true".equals(isUpload)){
						ss.append(" and filename is not null ");//数据库中判断是否为null 不能用!=  =。用的是is not  /is
					}else{
						ss.append(" and filename is null ");
					}
				}
				sql = ss.toString();
				return qr.query(sql, new BeanListHandler<User>(User.class),parameters.toArray());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

	}
	

}
