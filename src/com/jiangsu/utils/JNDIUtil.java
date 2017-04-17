package com.jiangsu.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * JNDI数据源的工具类
 * @author jiangsu
 *
 */
public class JNDIUtil {

	//1.定义个数据源
	private static DataSource ds;
	
	//2.给数据源赋值
	static{
		try {
			Context initCtx = new InitialContext();
			ds = (DataSource)initCtx.lookup("java:comp/env/jdbc/day28");//"jdbc/day28"它是数据源的名称
		} catch (NamingException e) {
			throw new ExceptionInInitializerError("初始化连接失败");
		}
	}
	
	//3.提供一个获取数据源的方法
	public static DataSource getDataSource(){
		return ds;
	}
	
	//4.提供一个获取连接的方法，注意，以后获取连接，必须使用该方法
	public static Connection getConnection(){
		try {
			return ds.getConnection();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
