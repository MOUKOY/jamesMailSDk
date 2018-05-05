package com.fanpei.mail.util;

/**
 * 字符串处理助手
 * 
 * @author fanpei
 * @version 创建时间:2018年4月20日 上午11:13:36
 */
public class MyStringUtil {

	/**
	 * 组装助手
	 * 
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	public static String format(String arg0, Object... arg1) {
		return new StringBuilder().append(arg0).append(":").append(arg1).toString();
	}

}
