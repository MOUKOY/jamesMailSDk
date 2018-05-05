package com.fanpei.mail.entity;

/**
 * @Description 邮件类型枚举
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:11:34
 */
public enum MailType {

	/**
	 * 发送
	 */
	SEND("Sent"),

	/**
	 * 接收
	 */
	RECIVE("INBOX"),

	/**
	 * 草稿
	 */
	DRAF("DRAF");

	private String value;

	private MailType(String v) {
		this.value = v;
	}

	@Override
	public String toString() {
		return value;
	}

}
