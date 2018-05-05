package com.fanpei.mail.entity;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.fanpei.mail.interfaceStore.IDataCheck;

/**
 * @Description 邮件主体信息
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午4:32:10
 */
public class MailContent extends MailUsers implements IDataCheck {

	int priority = 1;// 优先级等级 1：紧急 3：普通 5：缓慢
	String messageID;// 邮件ID
	String title;// 主题 标题
	String senddate;// 发送日期
	long size;// 邮件大小
	String content;// 邮件主体内容
	boolean hasAttach;// 是否含有附件
	List<AttachFile> files;// 附件列表

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSenddate() {
		return senddate;
	}

	public void setSenddate(String senddate) {
		this.senddate = senddate;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isHasAttach() {
		return hasAttach;
	}

	public void setHasAttach(boolean hasAttach) {
		this.hasAttach = hasAttach;
	}

	public List<AttachFile> getFiles() {
		return files;
	}

	public void addFile(AttachFile file) {
		if (files == null) {
			files = new LinkedList<>();
		}
		files.add(file);
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public MailContent() {
	}

	public MailContent(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public void cloneUsersInfo(MailUsers users) {
		this.ccUs = users.ccUs;
		this.fromU = users.fromU;
		this.recvUs = users.recvUs;
		this.secretUs = users.secretUs;
	}

	/**
	 * 初始化邮件内容信息
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void initMsg(MimeMessage msg) throws Exception {
		check();
		super.initMsg(msg);
		// msg.setFlag(Flag.SEEN, true);
		// 指定邮件优先级 1：紧急 3：普通 5：缓慢
		msg.setHeader("X-Priority", Integer.toString(priority));
		msg.saveChanges();
		msg.setSentDate(new Date());
		msg.setSubject(MimeUtility.encodeText(title));

		// 判断附件是否为空
		if (null != files && !files.isEmpty()) {
			// 新建一个MimeMultipart对象用来存放多个BodyPart对象
			Multipart container = new MimeMultipart();
			// 新建一个存放信件内容的BodyPart对象
			BodyPart textBodyPart = new MimeBodyPart();
			// 给BodyPart对象设置内容和格式/编码方式
			textBodyPart.setContent(content, "text/html;charset=utf-8");
			// 将含有信件内容的BodyPart加入到MimeMultipart对象中
			container.addBodyPart(textBodyPart);
			Iterator<AttachFile> fileIterator = files.iterator();
			while (fileIterator.hasNext()) {// 迭代所有附件
				AttachFile file = fileIterator.next();
				// 新建一个存放信件附件的BodyPart对象
				BodyPart fileBodyPart = new MimeBodyPart();
				// 将本地文件作为附件
				FileDataSource fds = new FileDataSource(file.getFilePath());
				fileBodyPart.setDataHandler(new DataHandler(fds));
				// 处理邮件中附件文件名的中文问题
				String attachName = fds.getName();
				attachName = MimeUtility.encodeText(attachName);
				// 设定附件文件名
				fileBodyPart.setFileName(attachName);
				// 将附件的BodyPart对象加入到container中
				container.addBodyPart(fileBodyPart);
			}
			// 将container作为消息对象的内容
			msg.setContent(container);
		} else {// 没有附件的情况
			msg.setContent(content, "text/html;charset=utf-8");
		}

	}

	@Override
	public void check() throws Exception {
		super.check();
		if (title == null)
			title = "";
		if (null == content)
			content = "";
		// throw new NullPointerException("邮件内容为空");
	}

}
