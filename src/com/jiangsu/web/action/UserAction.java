package com.jiangsu.web.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.apache.struts2.util.TokenHelper;

import com.jiangsu.domain.User;
import com.jiangsu.service.IUserService;
import com.jiangsu.service.impl.UserServiceImpl;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.util.ValueStack;

public class UserAction extends ActionSupport implements ModelDriven<User> {
	
	private IUserService service = new UserServiceImpl();

	private User user = new User();
	
	@Override
	public User getModel() {
		return user;
	}
	
	public String login() throws Exception{
		User dbUser = service.login(user.getLogonName(), user.getLogonPwd());
		if (dbUser==null) {
			addActionError("登录失败，用户名密码不匹配");
			return "input";
		}
		//登录成功
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("user", dbUser);
		return SUCCESS;
	}
	
	//保存文件的file
	private File upload;
	//文件名
	private String uploadFileName;
	
	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}
	
	//添加用户
	public String add() throws Exception {
		//1.文件保存的路径
		String filePath = ServletActionContext.getServletContext().getRealPath("/files");
		String dir = generateChildPath(filePath);
		//2.简历.doc,生成带有随机性的文件名
		String fileName = TokenHelper.generateGUID()+"_"+uploadFileName; //不带--
		//3.填充user缺少的信息
		user.setPath(dir);
		user.setFilename(fileName);//保存的文件名必须是带有guid的文件名，因为下载的时候还要用
		//4.上传文件操作
		upload.renameTo(new File(filePath+File.separator+dir,fileName));
		//5.保存用户
		int res = service.saveUser(user);
		if (res>0) {
			return SUCCESS;
		}
		return null;
	}

	//生成一个以yyyy-MM-dd格式的文件夹
	public String generateChildPath(String filePath) {
		Date date = new Date();
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String dir = format.format(date);
		File file = new File(filePath,dir);
		if(!file.exists()){
			file.mkdirs();
		}
		return dir;
	}
	
	

	//查询所有用户
	private List<User> users;  //用于存放查询到的所有用户
	
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	private String findAll() throws Exception {
		users = service.findAllUser();
		return SUCCESS;
	}
	
	//根据id查询用户的详细信息
	public String findUserById() throws Exception{
		//根据userID获取用户 对象
		user = service.findUserById(user.getUserID());
		//把user压入栈顶
		ValueStack vs = ActionContext.getContext().getValueStack();
		vs.push(user);
		return SUCCESS;
	}
	
	//文件下载
	private InputStream inputStream;
	private String oldFileName;
	
	public InputStream getInputStream() {
		return inputStream;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	
	public String getOldFileName() {
		return oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	public String download() throws Exception {
		//1.获取用户信息
		User dbUser = service.findUserById(user.getUserID());
		//2.得到文件存放的路径
		String filePath = ServletActionContext.getServletContext().getRealPath("/files");
		//给原始文件名赋值
		oldFileName = dbUser.getFilename().substring(dbUser.getFilename().indexOf("_"+1));
		//3.给字节输入流赋值
		inputStream = new FileInputStream(filePath+File.separator+dbUser.getPath()+File.separator+dbUser.getFilename());
		//4.返回成功
		return SUCCESS;
		//5.剩下的交给stream类型的结果视图
		
	}
	
	//删除用户
	public String delete() {
		int res = service.removeUser(user.getUserID());
		if (res>0) {
			return SUCCESS;
		}
		return null;
	}
	
	//显示编辑页面的动作方法
	public String editUI() {
		//根据userID获取用户 对象
		user = service.findUserById(user.getUserID());
		//把user压入栈顶
		ValueStack vs = ActionContext.getContext().getValueStack();
		vs.push(user);
		return SUCCESS;
	}
	
	//编辑用户
	public String edit() throws Exception{
		
		//1.判断用户是否重新选择了文件
		if (upload==null) {  //没有选择就用原来的
			User dbUser = service.findUserById(user.getUserID());  //根据id将用户查询出来
			user.setFilename(dbUser.getFilename());
			user.setPath(dbUser.getPath());
			int res = service.modifyUser(user);
			if (res>0) {
				return SUCCESS;
			}
		}else{
			//1.文件保存的路径
			String filePath = ServletActionContext.getServletContext().getRealPath("/files");
			String dir = generateChildPath(filePath);
			//2.简历.doc,生成带有随机性的文件名
			String fileName = TokenHelper.generateGUID()+"_"+uploadFileName; //不带--
			//3.填充user缺少的信息
			user.setPath(dir);
			user.setFilename(fileName);//保存的文件名必须是带有guid的文件名，因为下载的时候还要用
			//4.上传文件操作
			upload.renameTo(new File(filePath+File.separator+dir,fileName));
			//5.保存用户
			int res = service.saveUser(user);
			if (res>0) {
				return SUCCESS;
			}
		}
		
		return null;
	}
	
	//多条件查询用户
	private String isUpload;
	
	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String findUserByCondition() {
		//注意isUpload有三种情况：1.请选择：忽略此条件；2.有简历：只查询有简历的条件；3.无简历：只查询没有简历的条件
		users = service.findUserByCondition(user.getUserName(), user.getSex(), user.getEducation(), isUpload);
		
		return SUCCESS;
	}

}

















