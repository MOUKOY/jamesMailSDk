package com.fanpei.mail.util;

import com.fanpei.mail.entity.MailConfig;

/**
 * 类说明：检测新邮件类
 * 
 * @author 作者: LiuJunGuang
 * @version 创建时间：2011-2-26 下午04:26:45
 */
public class CheckNewMail extends Thread {
	private static int MailCount = 0;// 新邮件总数计数器
	private int num = 0;// 得到邮箱中新邮件的个数
	private CheckNewMialUtil check = null;

	public CheckNewMail() {
		check = new CheckNewMialUtil();
	}

	public void run() {
		while (true) {
			try {
				num = check.checkNewMail();
				sleep(MailConfig.CHECKMAILINTERVAL);// 暂停3秒
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 设置显示新邮件个数
	public static void setNewMailCount(int count) {
		MailCount = count;
	}
}
