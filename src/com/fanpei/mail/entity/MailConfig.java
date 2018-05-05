package com.fanpei.mail.entity;

/**
 * @Description 邮件常量
 * @Author fanpei
 * @CreateTime 2018年4月26日 下午3:02:07
 */
public class MailConfig {

	public static String defaulDomain = "bzmocha.com";
	public static String serverAddr = "127.0.0.1";// 服务器地址

	public static final String POP3 = "pop3";
	public static final String SMTP = "smtp";
	public static final String IMAP = "imap";

	/**
	 * 邮件检测时间间隔 单位:毫秒
	 */
	public static int CHECKMAILINTERVAL = 3000;

}
