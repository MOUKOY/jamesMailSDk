package com.fanpei.mail;

import java.io.IOException;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.james.domainlist.api.DomainListManagementMBean;
import org.apache.james.user.api.UsersRepositoryManagementMBean;

import com.fanpei.mail.entity.MailConfig;
import com.fanpei.mail.entity.MailServerInfo;
import com.fanpei.mail.exception.DataIsNullExcepetion;
import com.fanpei.mail.interfaceStore.IJamesAction;
import com.fanpei.mail.util.MyStringUtil;
import com.sun.jdmk.comm.HtmlAdaptorServer;

/**
 * @author fanpei
 * @version 创建时间:2018年4月19日 下午3:58:03
 */
public class JamesImpl implements IJamesAction {
	private static IJamesAction james;
	private static final Object mylock = new Object();

	private static MBeanServerConnection server = null;
	private static UsersRepositoryManagementMBean usersBean;
	private static DomainListManagementMBean domainBean;

	public static IJamesAction getJames() {
		return james;
	}

	private JamesImpl() {
	}

	/**
	 * @param serverAddress_
	 * @param defaulDomain
	 * @throws IOException
	 * @throws MalformedObjectNameException
	 */
	public static void init(String serverAddress_, String defaulDomain_, boolean auth)
			throws IOException, MalformedObjectNameException {

		if (james == null) {
			synchronized (mylock) {
				try {
					checkinit();
				} catch (ExceptionInInitializerError e) {
					MailServerInfo serverInfo = new MailServerInfo(serverAddress_, -1, serverAddress_, -1,
							serverAddress_, -1);
					MailsBase.inti(serverAddress_, defaulDomain_, auth, serverInfo);
					startRMIConnector(MailConfig.serverAddr);
					String beanUser = "org.apache.james:type=component,name=usersrepository";
					String beanDomain = "org.apache.james:type=component,name=domainlist";
					usersBean = MBeanServerInvocationHandler.newProxyInstance(server, new ObjectName(beanUser),
							UsersRepositoryManagementMBean.class, false);
					domainBean = MBeanServerInvocationHandler.newProxyInstance(server, new ObjectName(beanDomain),
							DomainListManagementMBean.class, false);
				}
				james = new JamesImpl();
			}
		}
	}

	/**
	 * 校验是否初始化
	 */
	private static void checkinit() {
		if (usersBean == null || domainBean == null)
			throw new ExceptionInInitializerError("未经初始化的,usersBean或usersBean为空");
	}

	/**
	 * 返回完整组装地址
	 * 
	 * @param userName
	 * @param domainName
	 * @return
	 */
	private static String getMailDress(String userName, String domainName) {
		return new StringBuilder().append(userName).append("@").append(domainName).toString();
	}

	/**
	 * 处理域名
	 * 
	 * @param domainName
	 * @return
	 */
	private static String domainformat(String domainName) {
		return "" == domainName ? MailConfig.defaulDomain : domainName;
	}

	/**
	 * 启动远程连接jmx
	 * 
	 * @param ip
	 * @throws IOException
	 */
	private static void startRMIConnector(String ip) throws IOException {
		JMXServiceURL serviceURL = new JMXServiceURL(
				String.format("service:jmx:rmi:///jndi/rmi://%s:9999/jmxrmi", ip, ip));
		// LocateRegistry.createRegistry(999);
		JMXConnector conect = JMXConnectorFactory.connect(serviceURL);
		server = conect.getMBeanServerConnection();
	}

	@SuppressWarnings("unused")
	@Deprecated
	private static void startHTMLAdapter() {
		HtmlAdaptorServer adapter = new HtmlAdaptorServer();
		ObjectName adapterName = null;
		try {
			adapter.setPort(9092);
			// create the HTML adapter
			adapterName = new ObjectName("JMXBookAgent:name=html,port=9092");
			// server.registerMBean(adapter, adapterName);
			adapter.start();
		} catch (Exception e) {
			System.out.println("Error Starting HTML Adapter for Agent");
		}
	}

	@SuppressWarnings("unused")
	@Override
	public void addUser(String userName, String password) throws VerifyError, Exception {

		checkinit();

		if (null == userName || null == password)
			throw new DataIsNullExcepetion("userName or passwd is null");

		usersBean.addUser(userName, password);
		if (1 > 0) {
			return;
		}

		String email = userName;
		String domainName = getDomainFromUserName(userName);
		if (null == domainName) {
			domainName = MailConfig.defaulDomain;
			email = getMailDress(userName, domainName);
		}
		String user = getUserFromUserName(userName);
		if (domainBean.containsDomain(domainName) && !usersBean.verifyExists(user)) {
			usersBean.addUser(email, password);
		} else
			throw new VerifyError(MyStringUtil.format("%s不存在此domain，或%s已存在此用户名", domainName, userName));
	}

	/**
	 * 从用户名中获取 domainName
	 * 
	 * @param userName
	 * @return
	 */
	private String getDomainFromUserName(String userName) {
		String domainName;
		if (userName.contains("@")) {
			int index = userName.indexOf("@");
			domainName = userName.substring(index + 1, userName.length());
		} else
			domainName = null;
		return domainName;
	}

	private String getUserFromUserName(String userName) {
		String user;
		if (userName.contains("@")) {
			int index = userName.indexOf("@");
			user = userName.substring(0, index);
		} else
			user = userName;
		return user;
	}

	@Override
	public void removeUser(String userName) throws VerifyError, Exception {
		checkinit();
		if (null == userName)
			throw new DataIsNullExcepetion("userName is null");

		if (usersBean.verifyExists(userName)) {// && domainBean.containsDomain(domainName)
			usersBean.deleteUser(userName);
		} else
			throw new VerifyError(MyStringUtil.format("不存在此用户名：%s", userName));

	}

	@Override
	public long countUsers() throws Exception {
		checkinit();
		return usersBean.countUsers();
	}

	@Override
	public String[] listUsers() throws Exception {
		checkinit();
		return usersBean.listAllUsers();
	}

	@Override
	public void addDomain(String... arg) throws VerifyError, Exception {
		checkinit();
		String value = null == arg ? null : arg[0];
		String domainName = domainformat(value);
		if (!domainBean.containsDomain(domainName))
			domainBean.addDomain(domainName);
		else
			throw new VerifyError(MyStringUtil.format("此domain已存在", domainName));

	}

	@Override
	public void removeDomain(String... arg) throws NoSuchFieldError, Exception {
		checkinit();
		String value = null == arg ? null : arg[0];
		String domainName = domainformat(value);
		domainName = domainformat(domainName);
		if (domainBean.containsDomain(domainName)) {
			domainBean.removeDomain(domainName);
		} else
			throw new NoSuchFieldError(String.format("此domain不存在%s", domainName));
	}

	@Override
	public List<String> listDomains() throws Exception {
		checkinit();
		return domainBean.getDomains();
	}

	@Override
	public String getDeafultDomain() throws Exception {
		checkinit();
		return domainBean.getDefaultDomain();
	}

}