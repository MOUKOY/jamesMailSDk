package com.fanpei.mail.entity;

import java.util.List;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;

import com.fanpei.mail.exception.DataIsNullExcepetion;
import com.fanpei.mail.exception.ListEmptyException;
import com.fanpei.mail.interfaceStore.IDataCheck;

/**
 * 邮件接收者
 * 
 * @Description
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午4:55:33
 */
public class MailUsers implements IDataCheck {

	protected User fromU;// 发送者
	protected List<User> recvUs;// 接收者
	protected List<User> ccUs;// 抄送者
	protected List<User> secretUs;// 密送者

	public User getFromU() {
		return fromU;
	}

	public List<User> getRecvUs() {
		return recvUs;
	}

	public void setRecvUs(List<User> recvUs) {
		this.recvUs = recvUs;
	}

	public List<User> getCcUs() {
		return ccUs;
	}

	public void setCcUs(List<User> ccUs) {
		this.ccUs = ccUs;
	}

	public List<User> getSecretUs() {
		return secretUs;
	}

	public void setSecretUs(List<User> secretUs) {
		this.secretUs = secretUs;
	}

	public void setFromU(User fromU) {
		this.fromU = fromU;
	}

	public MailUsers() {
		// TODO Auto-generated constructor stub
	}

	public MailUsers(User fromU, List<User> recvUs, List<User> ccUs, List<User> secretUs) {
		this.fromU = fromU;
		this.ccUs = ccUs;
		this.recvUs = recvUs;
		this.secretUs = secretUs;
	}

	/**
	 * 初始化邮件发送者接收者用户信息
	 * 
	 * @param msg
	 * @throws Exception
	 */
	public void initMsg(MimeMessage msg) throws Exception {
		check();

		// 设置发件人
		msg.setFrom(new InternetAddress(MimeUtility.encodeText(fromU.getName())));
		if (recvUs == null || recvUs.isEmpty()) {
			throw new DataIsNullExcepetion("the recivers is empty!");
		} else {
			InternetAddress[] addrs = new InternetAddress[recvUs.size()];
			int i = 0;
			for (User user : recvUs) {
				addrs[i] = new InternetAddress(MimeUtility.encodeText(user.getName()));
				i++;
			}
			msg.setRecipients(RecipientType.TO, addrs);
		}

		// 设置抄送
		if (ccUs != null) {
			InternetAddress[] addrs = new InternetAddress[ccUs.size()];
			int i = 0;
			for (User user : ccUs) {
				addrs[i] = new InternetAddress(MimeUtility.encodeText(user.getName()));
				i++;
			}
			msg.setRecipients(RecipientType.CC, addrs);
		}

		// 设置暗送
		if (secretUs != null) {
			InternetAddress[] addrs = new InternetAddress[secretUs.size()];
			int i = 0;
			for (User user : secretUs) {
				addrs[i] = new InternetAddress(MimeUtility.encodeText(user.getName()));
				i++;
			}
			msg.setRecipients(RecipientType.BCC, addrs);
		}
	}

	@Override
	public void check() throws Exception {
		if (fromU == null)
			throw new NullPointerException("发送者为空");
		fromU.check();

		if (recvUs == null || recvUs.isEmpty()) {
			throw new ListEmptyException("邮件接收者为空");
		}
	}

}
