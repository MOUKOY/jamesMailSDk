package com.fanpei.mail.interfaceStore;

import java.util.List;

/**
 * james操作接口
 * 
 * @author fanpei
 * @version 创建时间:2018年4月19日 下午3:49:46
 */
public interface IJamesAction {

	/**
	 * 添加用户
	 * 
	 * @param userName
	 */
	void addUser(String userName, String passwd) throws Exception;

	/**
	 * 移除用户
	 * 
	 * @param userName
	 */
	void removeUser(String userName) throws Exception;

	/**
	 * 用户总个数
	 * 
	 * @return
	 * @throws Exception
	 */
	long countUsers() throws Exception;

	/**
	 * 获取所有用户
	 */
	String[] listUsers() throws Exception;

	/**
	 * 添加Domian
	 * 
	 * @param arg
	 *            最大支持一个参数
	 * @param domainName
	 *            可为空,为空使用sdk默认域名
	 */
	void addDomain(String... arg) throws Exception;

	/**
	 * 移除domain
	 * 
	 * @param arg
	 *            最大支持一个参数
	 * @param domainName
	 *            可为空,为空使用sdk默认域名
	 */
	void removeDomain(String... arg) throws Exception;

	/**
	 * 获取所有domian
	 */
	List<String> listDomains() throws Exception;

	/**
	 * 获取默认的域名，此域名为james服务配置文件中的默认域名，非本SDK域名
	 * 
	 * @return
	 * @throws Exception
	 */
	String getDeafultDomain() throws Exception;

}
