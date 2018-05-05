package com.fanpei.mail.entity;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

/**
 * @Description 类说明：SMTP/POP3授权验证类
 * @Author fanpei
 * @CreateTime 2018年4月25日 下午6:13:45
 */
public class SmtpPop3Auth extends Authenticator {
	String user, password;

	// 设置帐号信息
	public SmtpPop3Auth(String user, String password) {
		this.user = user;
		this.password = password;
	}

	// 取得PasswordAuthentication对象
	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password);
	}
}
