package com.jiangsu.web.interceptor;

import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.jiangsu.domain.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * 检查登陆的拦截器
 * @author jiangsu
 *
 */
public class CheckLoginInterceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		//1.获取session对象
		HttpSession session = ServletActionContext.getRequest().getSession();
		//2.在session域中找user对象
		User user = (User)session.getAttribute("user");
		//3.有就放行
		if (user == null) {
			return "login";
		}
		//4.没有就前往登陆页面
		return invocation.invoke();
	}

}
