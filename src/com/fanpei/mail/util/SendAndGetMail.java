package com.fanpei.mail.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.search.MessageIDTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;

import com.fanpei.mail.MailsBase;
import com.fanpei.mail.entity.MailConfig;
import com.fanpei.mail.entity.MailContent;
import com.fanpei.mail.entity.MailType;
import com.fanpei.mail.entity.PageInfo;
import com.fanpei.mail.entity.User;

/**
 * @Description
 * @Author fanpei
 * @CreateTime 2018年4月26日 下午4:32:20
 */
public class SendAndGetMail extends MailsBase {

	/**
	 * 获取所有消息
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public static List<Message> getAllMsgs(User user) throws Exception {
		List<Message> messages = new LinkedList<Message>();
		Store store = null;
		Folder folder = null;
		try {
			// 建立IMAP连接
			store = connect(user, MailConfig.IMAP);
			Folder[] folders = store.getDefaultFolder().list();

			for (Folder f : folders) {
				try {
					f.open(Folder.READ_ONLY);
					Message[] msgs = f.getMessages();
					if (msgs != null && msgs.length > 0) {
						for (Message message : msgs) {
							messages.add(message);
						}
					}
				} finally {
					closeConnect(f, null);
				}
			}
		} finally {
			closeConnect(folder, store);// 关闭连接邮件服务器
		}
		return messages;
	}

	/**
	 * 删除用户所有文件夹
	 * 
	 * @param user
	 * @throws Exception
	 */
	public static void deleteAllFolders(User user) throws Exception {
		Store store = null;
		Folder folder = null;
		try {
			// 建立IMAP连接
			store = connect(user, MailConfig.IMAP);
			Folder[] folders = store.getDefaultFolder().list();

			for (Folder folder2 : folders) {
				folder2.delete(true);
			}
			Folder[] folders2 = store.getDefaultFolder().list();
			System.err.println(folders2.length);
		} finally {
			closeConnect(folder, store);// 关闭连接邮件服务器
		}
	}

	/**
	 * 分页获取文件夹里邮件
	 * 
	 * @param user
	 * @param type
	 * @param pageinfo
	 * @return
	 * @throws Exception
	 */
	public static Message[] getMails(User user, MailType type, PageInfo pageinfo) throws Exception {
		Message[] msgs = null;
		Store store = null;
		Folder folder = null;
		try {
			// 建立IMAP连接
			store = connect(user, MailConfig.IMAP);
			// Folder[] folders = store.getDefaultFolder().list();
			/*
			 * for (Folder folder2 : folders) { folder2.delete(true); } Folder[] folders2 =
			 * store.getDefaultFolder().list();
			 */
			// 取得一个Folder对象
			String folderName = type.toString();
			folder = store.getFolder(folderName);
			createFolder(user, folder, folderName);
			folder.open(Folder.READ_ONLY);
			// 取得邮件总数
			int toltal = folder.getMessageCount();
			if (toltal < 1) {
				return msgs;
			}
			pageinfo.setPageSum(toltal);

			// 取得所有的Message对象
			msgs = folder.getMessages((int) pageinfo.getStart(), (int) pageinfo.getEnd());
			if (msgs != null) {
				for (Message message : msgs) {
					praseMail(message);
				}
			}

		} finally {
			closeConnect(folder, store);// 关闭连接邮件服务器
		}

		return msgs;
	}

	/**
	 * @param user
	 * @param type
	 * @param msgs
	 * @throws Exception
	 */
	public static void deleteMail(User user, MailType type, Message[] msgs) throws Exception {

		if (null == msgs || msgs.length < 0)
			return;

		Store store = null;
		Folder folder = null;
		try {
			store = connect(user, MailConfig.IMAP);// 连接邮件服务器
			// 取得一个Folder对象
			folder = store.getFolder(type.toString());
			folder.open(Folder.READ_WRITE);
			SearchTerm term = new OrTerm(genDelTerm(msgs));
			Message[] mes = folder.search(term);
			if (mes != null && mes.length > 0) {
				for (int i = 0; i < mes.length; i++) {
					mes[i].setFlag(Flag.DELETED, true);
				}
			}

		} finally {
			closeConnect(folder, store);// 关闭邮件服务器的连接并删除邮件
		}
	}

	/**
	 * 删除邮件-搜索
	 * 
	 * @param user
	 * @param ids
	 *            待删除id集合
	 * @throws Exception
	 */
	public static void deleteMailbySearch(User user, MailType type, String[] ids) throws Exception {

		if (null == ids || ids.length < 0)
			return;

		Store store = null;
		Folder folder = null;
		try {
			store = connect(user, MailConfig.IMAP);// 连接邮件服务器
			// 取得一个Folder对象
			folder = store.getFolder(type.toString());
			folder.open(Folder.READ_WRITE);
			SearchTerm term = new OrTerm(genDelTerm(ids));
			Message[] mes = folder.search(term);
			if (mes != null) {
				for (int i = 0; i < mes.length; i++) {
					mes[i].setFlag(Flags.Flag.DELETED, true);
				}
			}
		} finally {
			closeConnect(folder, store);// 关闭邮件服务器的连接并删除邮件
		}
	}

	/**
	 * 组装搜索条件
	 * 
	 * @param ids
	 * @return
	 */
	private static SearchTerm[] genGetTerm(User user, MailType type, PageInfo pageinfo) {
		SearchTerm[] terms = new SearchTerm[4];
		// terms[0] = new RecipientStringTerm(RecipientType.CC, pattern);

		return terms;
	}

	/**
	 * 组装搜索条件
	 * 
	 * @param ids
	 * @return
	 */
	private static SearchTerm[] genDelTerm(String[] ids) {
		SearchTerm[] terms = new SearchTerm[ids.length];
		int index = 0;
		while (index < ids.length) {
			MessageIDTerm term = new MessageIDTerm(ids[index]);
			terms[index] = term;
			index++;
		}
		return terms;
	}

	@SuppressWarnings("unused")
	private static SearchTerm[] genDelTerm(Message[] mgs) throws MessagingException {
		SearchTerm[] terms = new SearchTerm[mgs.length];
		int index = 0;
		while (index < mgs.length) {
			MessageIDTerm term = new MessageIDTerm(mgs[index].getHeader("Message-ID")[0]);
			terms[index] = term;
			index++;
		}
		return terms;
	}

	/**
	 * 显示复杂邮件的正文内容
	 * 
	 * @param part
	 * @param partNum
	 * @param x
	 *            格式 1:text/html 2:text/plain
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private String getPart(Part part, int partNum, int x) throws MessagingException, IOException {
		String s = "";
		String s1 = "";
		String s2 = "";
		String s5 = "";
		String sct = part.getContentType();
		if (sct == null) {
			s = "part 无效";
			return s;
		}
		ContentType ct = new ContentType(sct);
		if (ct.match("text/html") || ct.match("text/plain")) {
			// display text/plain inline
			s1 = "" + (String) part.getContent() + "";
		} else if (partNum != 0) {
			String temp = "";
			if ((temp = part.getFileName()) != null) {
				s2 = "Filename: " + temp + "";
			}
		}
		if (part.isMimeType("multipart/alternative")) {
			String s6 = "";
			String s7 = "";
			Multipart mp = (Multipart) part.getContent();
			int count = mp.getCount();
			for (int i = 0; i < count; i++) {
				if (mp.getBodyPart(i).isMimeType("text/plain"))
					s7 = getPart(mp.getBodyPart(i), i, 2);
				else if (mp.getBodyPart(i).isMimeType("text/html"))
					s6 = getPart(mp.getBodyPart(i), i, 1);
			}
			if (x == 1) {// html格式的字符串
				s5 = s6;
			}
			if (x == 2) {// paint类型的字符串
				s5 = s7;
			}
			return s5;
		}
		s = s1 + s2;
		return s;
	}

	@SuppressWarnings("unused")
	private void name() throws UnsupportedEncodingException, MessagingException, IOException {

		FetchProfile profile = new FetchProfile();
		profile.add(FetchProfile.Item.ENVELOPE);
		Folder folder = null;
		Message[] msg = null;
		// profile.add(FetchProfile.Item.FLAGS);
		folder.fetch(msg, profile);
		if (msg != null && msg.length > 0) {
			ArrayList result = new ArrayList<>(msg.length);
			MailContent mail = null;
			// 取出每个邮件的信息
			for (int i = 0; i < msg.length; i++) {
				Message msgElement = msg[i];
				mail = praseMail(msgElement);
				result.add(mail);
			}
		}
	}
}
