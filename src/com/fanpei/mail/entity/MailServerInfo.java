package com.fanpei.mail.entity;

/**
 * @Description 邮件服务器信息
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:48:50
 */
public class MailServerInfo {

	/**
	 * pop3服务器地址
	 */
	private String pop3ip = "127.0.0.1";

	/**
	 * pop3端口 默认110
	 */
	private int pop3Port = 110;

	/**
	 * smtp服务器地址
	 */
	private String smtpip = "127.0.0.1";

	/**
	 * smtp端口 默认25
	 */
	private int smtpPort = 25;

	/**
	 * imap服务器地址
	 */
	private String imapip = "127.0.0.1";

	/**
	 * imap端口 默认25
	 */
	private int imapPort = 143;

	public int getPop3Port() {
		return pop3Port;
	}

	public int getSmtpPort() {
		return smtpPort;
	}

	public String getPop3ip() {
		return pop3ip;
	}

	public String getSmtpip() {
		return smtpip;
	}

	public String getImapip() {
		return imapip;
	}

	public int getImapPort() {
		return imapPort;
	}

	public String getSmtpAddr() {
		return new StringBuilder().append(smtpip).append(":").append(smtpPort).toString();
	}

	public String getPop3Addr() {
		return new StringBuilder().append(pop3ip).append(":").append(pop3Port).toString();
	}

	public String getImapAddr() {
		return new StringBuilder().append(imapip).append(":").append(imapPort).toString();
	}

	public MailServerInfo(String pop3ip, int pop3Port, String smtpip, int smtpPort, String imapip, int imapPort) {
		this.pop3ip = pop3ip != null ? pop3ip : this.pop3ip;
		this.pop3Port = pop3Port > 0 ? pop3Port : this.pop3Port;
		this.smtpip = smtpip != null ? smtpip : this.smtpip;
		this.smtpPort = smtpPort > 0 ? smtpPort : this.smtpPort;
		this.imapip = imapip != null ? imapip : this.imapip;
		this.imapPort = imapPort > 0 ? imapPort : this.imapPort;
	}

}
