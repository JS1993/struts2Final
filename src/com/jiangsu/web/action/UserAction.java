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
			addActionError("��¼ʧ�ܣ��û������벻ƥ��");
			return "input";
		}
		//��¼�ɹ�
		HttpSession session = ServletActionContext.getRequest().getSession();
		session.setAttribute("user", dbUser);
		return SUCCESS;
	}
	
	//�����ļ���file
	private File upload;
	//�ļ���
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
	
	//����û�
	public String add() throws Exception {
		//1.�ļ������·��
		String filePath = ServletActionContext.getServletContext().getRealPath("/files");
		String dir = generateChildPath(filePath);
		//2.����.doc,���ɴ�������Ե��ļ���
		String fileName = TokenHelper.generateGUID()+"_"+uploadFileName; //����--
		//3.���userȱ�ٵ���Ϣ
		user.setPath(dir);
		user.setFilename(fileName);//������ļ��������Ǵ���guid���ļ�������Ϊ���ص�ʱ��Ҫ��
		//4.�ϴ��ļ�����
		upload.renameTo(new File(filePath+File.separator+dir,fileName));
		//5.�����û�
		int res = service.saveUser(user);
		if (res>0) {
			return SUCCESS;
		}
		return null;
	}

	//����һ����yyyy-MM-dd��ʽ���ļ���
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
	
	

	//��ѯ�����û�
	private List<User> users;  //���ڴ�Ų�ѯ���������û�
	
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
	
	//����id��ѯ�û�����ϸ��Ϣ
	public String findUserById() throws Exception{
		//����userID��ȡ�û� ����
		user = service.findUserById(user.getUserID());
		//��userѹ��ջ��
		ValueStack vs = ActionContext.getContext().getValueStack();
		vs.push(user);
		return SUCCESS;
	}
	
	//�ļ�����
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
		//1.��ȡ�û���Ϣ
		User dbUser = service.findUserById(user.getUserID());
		//2.�õ��ļ���ŵ�·��
		String filePath = ServletActionContext.getServletContext().getRealPath("/files");
		//��ԭʼ�ļ�����ֵ
		oldFileName = dbUser.getFilename().substring(dbUser.getFilename().indexOf("_"+1));
		//3.���ֽ���������ֵ
		inputStream = new FileInputStream(filePath+File.separator+dbUser.getPath()+File.separator+dbUser.getFilename());
		//4.���سɹ�
		return SUCCESS;
		//5.ʣ�µĽ���stream���͵Ľ����ͼ
		
	}
	
	//ɾ���û�
	public String delete() {
		int res = service.removeUser(user.getUserID());
		if (res>0) {
			return SUCCESS;
		}
		return null;
	}
	
	//��ʾ�༭ҳ��Ķ�������
	public String editUI() {
		//����userID��ȡ�û� ����
		user = service.findUserById(user.getUserID());
		//��userѹ��ջ��
		ValueStack vs = ActionContext.getContext().getValueStack();
		vs.push(user);
		return SUCCESS;
	}
	
	//�༭�û�
	public String edit() throws Exception{
		
		//1.�ж��û��Ƿ�����ѡ�����ļ�
		if (upload==null) {  //û��ѡ�����ԭ����
			User dbUser = service.findUserById(user.getUserID());  //����id���û���ѯ����
			user.setFilename(dbUser.getFilename());
			user.setPath(dbUser.getPath());
			int res = service.modifyUser(user);
			if (res>0) {
				return SUCCESS;
			}
		}else{
			//1.�ļ������·��
			String filePath = ServletActionContext.getServletContext().getRealPath("/files");
			String dir = generateChildPath(filePath);
			//2.����.doc,���ɴ�������Ե��ļ���
			String fileName = TokenHelper.generateGUID()+"_"+uploadFileName; //����--
			//3.���userȱ�ٵ���Ϣ
			user.setPath(dir);
			user.setFilename(fileName);//������ļ��������Ǵ���guid���ļ�������Ϊ���ص�ʱ��Ҫ��
			//4.�ϴ��ļ�����
			upload.renameTo(new File(filePath+File.separator+dir,fileName));
			//5.�����û�
			int res = service.saveUser(user);
			if (res>0) {
				return SUCCESS;
			}
		}
		
		return null;
	}
	
	//��������ѯ�û�
	private String isUpload;
	
	public String getIsUpload() {
		return isUpload;
	}

	public void setIsUpload(String isUpload) {
		this.isUpload = isUpload;
	}

	public String findUserByCondition() {
		//ע��isUpload�����������1.��ѡ�񣺺��Դ�������2.�м�����ֻ��ѯ�м�����������3.�޼�����ֻ��ѯû�м���������
		users = service.findUserByCondition(user.getUserName(), user.getSex(), user.getEducation(), isUpload);
		
		return SUCCESS;
	}

}

















