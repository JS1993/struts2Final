package com.jiangsu.web.interceptor;

import javax.interceptor.InvocationContext;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.jiangsu.domain.User;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.MethodFilterInterceptor;

/**
 * ����½��������
 * @author jiangsu
 *
 */
public class CheckLoginInterceptor extends MethodFilterInterceptor {

	@Override
	protected String doIntercept(ActionInvocation invocation) throws Exception {
		//1.��ȡsession����
		HttpSession session = ServletActionContext.getRequest().getSession();
		//2.��session������user����
		User user = (User)session.getAttribute("user");
		//3.�оͷ���
		if (user == null) {
			return "login";
		}
		//4.û�о�ǰ����½ҳ��
		return invocation.invoke();
	}

}
