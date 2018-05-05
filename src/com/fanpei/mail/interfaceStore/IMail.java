package com.fanpei.mail.interfaceStore;

import javax.mail.Message;

import com.fanpei.mail.entity.MailContent;
import com.fanpei.mail.entity.MailType;
import com.fanpei.mail.entity.PageInfo;
import com.fanpei.mail.entity.User;

/**
 * @Description 邮件处理接口
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:10:09
 */
public interface IMail {
	/**
	 * @param message
	 *            邮件主体信息
	 * @throws Exception
	 */
	void send(MailContent message) throws Exception;

	/**
	 * 保存成草稿
	 * 
	 * @param message
	 * @throws Exception
	 */
	void saveDraft(MailContent message) throws Exception;

	/**
	 * 检测新邮件
	 * 
	 * @return 0:没有 >0:新邮件个数
	 */
	int checkNewMail() throws Exception;

	/**
	 * 删除指定的邮件
	 * 
	 * @param user
	 * @param type
	 * @param msgs
	 * @throws Exception
	 */
	void delete(User user, MailType type, Message[] msgs) throws Exception;

	/**
	 * 获取指定邮件
	 * 
	 * @param user
	 * @param type
	 * @param pageinfo
	 *            分页信息
	 * @throws Exception
	 */
	Message[] getMails(User user, MailType type, PageInfo pageinfo) throws Exception;
}
