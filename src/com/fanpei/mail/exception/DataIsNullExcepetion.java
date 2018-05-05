package com.fanpei.mail.exception;

/**
 * @Description 数据为null 替代 nullexception
 * @Author fanpei
 * @CreateTime 2018年4月27日 下午6:30:47
 */
public class DataIsNullExcepetion extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8066130595120316028L;

	public DataIsNullExcepetion(String dscr) {
		super(dscr);
	}
}
