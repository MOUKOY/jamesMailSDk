package com.fanpei.mail;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;

import org.apache.james.mailbox.MailboxManager;

import com.fanpei.mail.entity.AttachFile;
import com.fanpei.mail.entity.MailConfig;
import com.fanpei.mail.entity.MailContent;
import com.fanpei.mail.entity.MailServerInfo;
import com.fanpei.mail.entity.MailType;
import com.fanpei.mail.entity.SmtpPop3Auth;
import com.fanpei.mail.entity.User;
import com.fanpei.mail.exception.DataIsNullExcepetion;
import com.sun.mail.smtp.SMTPMessage;

/**
 * @Description jamse服务器对象基础信息包含类
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午6:24:35
 */
public abstract class MailsBase {

	protected static MailboxManager mailboxManager = null;

	protected static Properties smtpProps = null;// smtp属性
	protected static Properties pop3Props = null;// pop3属性
	protected static Properties imapProps = null;// pop3属性

	// protected static boolean auth = false;// 是否启用密码验证
	protected static MailServerInfo serverIno = null;
	protected static SimpleDateFormat dateFormat;// 日期格式化

	public static SimpleDateFormat getDateFormat() {
		return dateFormat;
	}

	public static MailServerInfo getServerIno() {
		return serverIno;
	}

	public static Properties getProps(String protocol) {
		switch (protocol) {
		case MailConfig.IMAP:
			return imapProps;
		case MailConfig.SMTP:
			return smtpProps;
		case MailConfig.POP3:
			return pop3Props;
		default:
			return null;
		}
	}

	/**
	 * 初始化服务器信息，若参数为空，采用默认信息
	 * 
	 * @param server
	 *            邮件服务器后台管理地址
	 * @param domain
	 * @param auth
	 *            是否启用认证
	 * @param serverInfo
	 *            提供邮件服务的服务器地址信息
	 */
	protected static void inti(String server, String domain, boolean auth, MailServerInfo serverInfo_) {
		MailConfig.defaulDomain = null == domain ? MailConfig.defaulDomain : domain;
		MailConfig.serverAddr = null == server ? MailConfig.serverAddr : server;
		serverIno = serverInfo_;
		smtpProps = new Properties();
		smtpProps.setProperty("mail.store.protocol", "smtp");
		smtpProps.setProperty("mail.smtp.host", serverIno.getSmtpip());
		smtpProps.setProperty("mail.smtp.port", Long.toString(serverInfo_.getSmtpPort()));

		pop3Props = new Properties();
		pop3Props.setProperty("mail.store.protocol", "pop3");
		pop3Props.setProperty("mail.pop3.host", serverIno.getPop3ip());
		pop3Props.setProperty("mail.pop3.port", Long.toString(serverInfo_.getPop3Port()));

		imapProps = new Properties();
		imapProps.setProperty("mail.store.protocol", "imap");
		imapProps.setProperty("mail.imap.host", serverIno.getImapip());
		imapProps.setProperty("mail.imap.port", Long.toString(serverInfo_.getImapPort()));

		dateFormat = new SimpleDateFormat("yyyy年MM月dd日 hh:mm-ss");

	}

	/**
	 * 连接邮件服务器
	 * 
	 * @param user
	 * @param protocol
	 *            连接协议
	 * @return
	 * @throws Exception
	 */
	protected static Store connect(User user, String protocol) throws Exception {
		// 创建一个授权验证对象
		SmtpPop3Auth auth = new SmtpPop3Auth(user.getName(), user.getPassword());

		// 取得一个Session对象
		Properties prop = MailsBase.getProps(protocol);
		Session session = Session.getInstance(prop, auth);
		session.setDebug(true);// 调试模式

		// 创建Session实例对象 
		Store store = session.getStore(protocol);
		store.connect();
		return store;
	}

	/**
	 * 关闭连接
	 * 
	 * @param folder
	 * @param store
	 */
	protected static void closeConnect(Folder folder, Store store) {
		try {
			if (folder != null && folder.isOpen())
				folder.close(true);// 关闭连接时是否删除邮件，true删除邮件
		} catch (MessagingException e) {
			e.printStackTrace();
		} finally {
			try {
				if (store != null && store.isConnected())
					store.close();// 关闭收件箱连接
			} catch (MessagingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 初始化待发送邮件信息
	 * 
	 * @param message
	 * @return
	 * @throws Exception
	 */
	protected static SMTPMessage initMessage(MailContent message) throws Exception {
		/*
		 * 1. 得到session
		 */
		SmtpPop3Auth auth = new SmtpPop3Auth(message.getFromU().getName(), message.getFromU().getPassword());

		Session session = Session.getInstance(MailsBase.getProps(MailConfig.SMTP), auth);
		/*
		 * 2. 创建MimeMessage
		 */
		SMTPMessage msg = new SMTPMessage(session);
		message.initMsg(msg);
		return msg;
	}

	/**
	 * 创建指定的文件夹
	 * 
	 * @param user
	 * @param folder
	 * @param folderName
	 * @return
	 * @throws Exception
	 */
	protected static boolean createFolder(User user, Folder folder, String folderName) throws Exception {
		boolean created = false;
		if (folder == null) {
			throw new DataIsNullExcepetion("folder is null ");
		}
		if (!folder.exists())
			created = folder.create(Folder.HOLDS_MESSAGES);
		return created;
	}

	/**
	 * 保存邮件信息到指定的邮箱文件夹下
	 * 
	 * @param user
	 *            用户
	 * @param type
	 *            邮箱类型
	 * @param msgs
	 *            待保存邮件信息
	 * @throws Exception
	 */
	protected static void saveEmailToBox(User user, MailType type, Message[] msgs) throws Exception {
		Store store = null;
		Folder folder = null;
		try {
			store = connect(user, MailConfig.IMAP);
			folder = store.getFolder(type.toString());
			createFolder(user, folder, type.toString());
			folder.open(Folder.READ_WRITE);
			for (Message message : msgs) {
				message.setFlag(Flag.SEEN, true);
			}

			folder.appendMessages(msgs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeConnect(folder, store);
		}
	}

	/**
	 * 解析邮件
	 * 
	 * @param msgElement
	 * @return
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	protected static MailContent praseMail(Message msgElement)
			throws MessagingException, UnsupportedEncodingException, IOException {
		MailContent mail;
		mail = new MailContent();
		// 读取邮件ID
		String[] ids = msgElement.getHeader("Message-ID");
		if (ids != null) {
			mail.setMessageID(ids[0]);
		}
		// 读取邮件标题
		mail.setTitle(msgElement.getSubject());

		// 读取发件人
		User fromU = new User(MimeUtility.decodeText(msgElement.getFrom()[0].toString()));
		mail.setFromU(fromU);

		// 读取邮件发送日期
		if (null != msgElement.getSentDate())
			mail.setSenddate(MailsBase.getDateFormat().format(msgElement.getSentDate()));

		// 读取邮件大小
		mail.setSize(new Integer(msgElement.getSize()));

		// 取得邮件内容
		if (msgElement.isMimeType("text/*")) {
			mail.setContent(msgElement.getContent().toString());
		}

		// 判断是否有附件
		if (msgElement.isMimeType("multipart/*")) {
			Multipart mp = null;
			BodyPart part = null;
			String disp = null;
			mail.setHasAttach(true);
			mp = (Multipart) msgElement.getContent();
			// 遍历每个Miltipart对象
			for (int j = 0; j < mp.getCount(); j++) {
				part = mp.getBodyPart(j);
				disp = part.getDisposition();
				// 如果有附件
				if (disp != null && (disp.equals(Part.ATTACHMENT) || disp.equals(Part.INLINE))) {
					String filename = MimeUtility.decodeText(part.getFileName());// 解决中文附件名的问题
					AttachFile file = new AttachFile();
					file.setName(filename);
					mail.addFile(file);
				}
			}
		}
		return mail;
	}

}
