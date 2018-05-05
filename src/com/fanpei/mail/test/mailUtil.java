package com.fanpei.mail.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Message;
import javax.management.MalformedObjectNameException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.fanpei.mail.JamesImpl;
import com.fanpei.mail.MailsImp;
import com.fanpei.mail.entity.MailContent;
import com.fanpei.mail.entity.MailType;
import com.fanpei.mail.entity.MailUsers;
import com.fanpei.mail.entity.PageInfo;
import com.fanpei.mail.entity.User;
import com.fanpei.mail.util.SendAndGetMail;

/**
 * 测试用例
 * 
 * @author MOUKOY
 *
 */
class mailUtil {
	static MailsImp mailUtil = null;
	User fromU = new User("fanpei@zytm.com", "123");
	User secU = new User("liulu@zytm.com", "123");
	User thirdU = new User("fanpei2@zytm.com", "123");

	@BeforeAll
	static void setUp() throws Exception {

		try {
			String ip = "192.168.0.244";
			JamesImpl.init(ip, "zytm.com", false);
			mailUtil = new MailsImp();

		} catch (MalformedObjectNameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	// @Test
	void deleteUser() {
		try {
			String[] users1 = JamesImpl.getJames().listUsers();
			JamesImpl.getJames().removeUser("fanpei@zytm.com");
			JamesImpl.getJames().removeUser("fanpei2@zytm.com");
			JamesImpl.getJames().removeUser("liulu@zytm.com");

			String[] users2 = JamesImpl.getJames().listUsers();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// @Test
	void addUser() {
		try {
			JamesImpl.getJames().addUser("fanpei@zytm.com", "123");
			JamesImpl.getJames().addUser("fanpei2@zytm.com", "123");
			JamesImpl.getJames().addUser("liulu@zytm.com", "123");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	void testSend() {
		try {

			List<User> recvUs = new ArrayList<>();

			recvUs.add(secU);
			recvUs.add(thirdU);
			List<User> secretUs = new ArrayList<>();
			// secretUs.add(thirdU);
			MailUsers users = new MailUsers(fromU, recvUs, null, secretUs);
			MailContent message = new MailContent("娴嬭瘯閭欢2", "浣犱滑閮芥槸濂藉瀛�");
			message.cloneUsersInfo(users);

			mailUtil.send(message);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	void testGetMails() {

		try {
			PageInfo pageInfo = new PageInfo(1, 10);
			Message[] mails = mailUtil.getMails(thirdU, MailType.RECIVE, pageInfo);
			if (mails == null) {
				System.err.println();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// @Test
	void deleMail() {
		try {
			PageInfo pageInfo = new PageInfo(1, 10);
			Message[] mails = mailUtil.getMails(thirdU, MailType.DRAF, pageInfo);
			if (mails != null && mails.length > 0) {
				mailUtil.delete(thirdU, MailType.RECIVE, mails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// @Test
	void MailFolder() {
		try {
			// 3.鍙戦�侀偖浠�
			List<User> recvUs = new ArrayList<>();
			recvUs.add(secU);
			recvUs.add(thirdU);
			List<User> secretUs = new ArrayList<>();
			// secretUs.add(thirdU);
			MailUsers users = new MailUsers(fromU, recvUs, null, secretUs);
			MailContent message = new MailContent("娴嬭瘯閭欢2", "浣犱滑閮芥槸濂藉瀛�");
			message.cloneUsersInfo(users);
			mailUtil.send(message);

			List<Message> msgs2 = SendAndGetMail.getAllMsgs(secU);
			if (null != msgs2) {
				System.out.println(msgs2.size());
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Test
	void folder() {
		try {
			// 1.鏌ョ湅閭欢
			PageInfo pageInfo = new PageInfo(1, 10);
			Message[] mails1 = mailUtil.getMails(fromU, MailType.DRAF, pageInfo);
			if (mails1 != null && mails1.length > 0) {
				mailUtil.delete(thirdU, MailType.SEND, mails1);
			}
			// 2.鍒犻櫎鏂囦欢澶�
			SendAndGetMail.deleteAllFolders(fromU);

			// 3.鍙戦�侀偖浠�
			List<User> recvUs = new ArrayList<>();
			recvUs.add(secU);
			recvUs.add(thirdU);
			List<User> secretUs = new ArrayList<>();
			// secretUs.add(thirdU);
			MailUsers users = new MailUsers(fromU, recvUs, null, secretUs);
			MailContent message = new MailContent("娴嬭瘯閭欢2", "浣犱滑閮芥槸濂藉瀛�");
			message.cloneUsersInfo(users);
			mailUtil.send(message);

			// 4鏌ョ湅閭欢
			Message[] mails2 = mailUtil.getMails(thirdU, MailType.RECIVE, pageInfo);
			if (mails2 != null && mails2.length > 0) {

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}