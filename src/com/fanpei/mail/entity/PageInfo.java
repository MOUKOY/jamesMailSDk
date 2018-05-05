package com.fanpei.mail.entity;

/**
 * 分页信息
 * 
 * @Description
 * @Author fanpei
 * @CreateTime 2018年4月20日 下午3:15:02
 */
public class PageInfo {
	/**
	 * 当前页序号
	 */
	long index;

	/**
	 * 分页个数
	 */
	long perCount;

	/**
	 * 页码总数
	 */
	long pageSum;

	public PageInfo(long index_, long perCount_) {
		this.perCount = perCount_;
		this.index = index_;
	}

	public long getIndex() {
		return index;
	}

	public long getPerCount() {
		return perCount;
	}

	public long getPageSum() {
		return pageSum;
	}

	public void setPageSum(long pageSum) {
		this.pageSum = pageSum;
	}

	public long getStart() {
		long start = (index - 1) * perCount + 1;
		if (start > pageSum)
			start = 1;
		return start;
	}

	public long getEnd() {
		long end = index * perCount;
		if (end > pageSum)
			end = pageSum;
		return end;
	}

}
