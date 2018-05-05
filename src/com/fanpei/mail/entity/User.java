package com.fanpei.mail.entity;

import com.fanpei.mail.interfaceStore.IDataCheck;

/**
 * @Description 用户信息
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:37:25
 */
public class User implements IDataCheck {

	/**
	 * 用户名
	 */
	String name;

	/**
	 * 密码
	 */
	String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}

	/**
	 * @param nameWithDomain
	 *            邮件地址全称
	 * @param password
	 *            密码
	 */

	public User() {
		// TODO Auto-generated constructor stub
	}

	public User(String name) {
		this.name = name;
	}

	public User(String nameWithDomain, String password) {
		if (null == nameWithDomain) {
			throw new NullPointerException("MailUser 构造函数初始化时,nameWithDomain为空");
		}
		this.name = nameWithDomain;
		this.password = password;
	}

	@Override
	public void check() throws Exception {
		if (name == null || "".equals(name))
			throw new NullPointerException("name 或者为空");

	}
}
