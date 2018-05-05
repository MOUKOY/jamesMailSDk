package com.fanpei.mail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Transport;

import com.fanpei.mail.entity.MailContent;
import com.fanpei.mail.entity.MailType;
import com.fanpei.mail.entity.PageInfo;
import com.fanpei.mail.entity.User;
import com.fanpei.mail.interfaceStore.IMail;
import com.fanpei.mail.util.SendAndGetMail;
import com.sun.mail.smtp.SMTPMessage;

/**
 * @Description 邮件传输助手
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:32:49
 */
public class MailsImp extends MailsBase implements IMail {

	@Override
	public void send(MailContent message) throws Exception {
		try {
			SMTPMessage msg = initMessage(message);
			/*
			 * 3. 发邮件
			 */
			Transport.send(msg, msg.getAllRecipients());

		} catch (MessagingException mex) {
			System.out.println("\n--Exception handling in MailsImp.java");
			mex.printStackTrace();
			System.out.println();
			Exception ex = mex;
			do {
				if (ex instanceof SendFailedException) {
					SendFailedException sfex = (SendFailedException) ex;
					Address[] invalid = sfex.getInvalidAddresses();
					if (invalid != null) {
						System.out.println("    ** Invalid Addresses");
						for (int i = 0; i < invalid.length; i++)
							System.out.println("         " + invalid[i]);
					}
					Address[] validUnsent = sfex.getValidUnsentAddresses();
					if (validUnsent != null) {
						System.out.println("    ** ValidUnsent Addresses");
						for (int i = 0; i < validUnsent.length; i++)
							System.out.println("         " + validUnsent[i]);
					}
					Address[] validSent = sfex.getValidSentAddresses();
					if (validSent != null) {
						System.out.println("    ** ValidSent Addresses");
						for (int i = 0; i < validSent.length; i++)
							System.out.println("         " + validSent[i]);
					}
				}
				System.out.println();
				if (ex instanceof MessagingException)
					ex = ((MessagingException) ex).getNextException();
				else
					ex = null;
			} while (ex != null);
		}
	}

	@Override
	public void saveDraft(MailContent message) throws Exception {
		SMTPMessage msg = initMessage(message);
		saveEmailToBox(message.getFromU(), MailType.DRAF, new Message[] { msg });
	}

	@Override
	public void delete(User user, MailType type, Message[] msgs) throws Exception {
		SendAndGetMail.deleteMail(user, type, msgs);
	}

	@Override
	public Message[] getMails(User user, MailType type, PageInfo pageinfo) throws Exception {
		return SendAndGetMail.getMails(user, type, pageinfo);
	}

	@Override
	public int checkNewMail() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

}
