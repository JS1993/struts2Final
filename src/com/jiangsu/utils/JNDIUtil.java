package com.jiangsu.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * JNDI����Դ�Ĺ�����
 * @author jiangsu
 *
 */
public class JNDIUtil {

	//1.���������Դ
	private static DataSource ds;
	
	//2.������Դ��ֵ
	static{
		try {
			Context initCtx = new InitialContext();
			ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/day28");//"jdbc/day28"��������Դ������
		} catch (NamingException e) {
			throw new ExceptionInInitializerError("��ʼ������ʧ��");
		}
	}
	
	//3.�ṩһ����ȡ����Դ�ķ���
	public static DataSource getDataSource(){
		return ds;
	}
	
	//4.�ṩһ����ȡ���ӵķ�����ע�⣬�Ժ��ȡ���ӣ�����ʹ�ø÷���
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
