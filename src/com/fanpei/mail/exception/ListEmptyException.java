package com.fanpei.mail.exception;

/**
 * list为null或者长度为0
 * 
 * @Description
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午4:47:25
 */
public class ListEmptyException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SuppressWarnings("unused")
	private static String mString = "list为null或者长度为0";

	public ListEmptyException(String dscr) {
		super(dscr);
	}
}
