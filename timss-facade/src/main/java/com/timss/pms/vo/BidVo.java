package com.timss.pms.vo;

import com.timss.pms.bean.Bid;

/**
 * 
 * @ClassName:     BidVo
 * @company: gdyd
 * @Description: 招标列表的vo类
 * @author:    黄晓岚
 * @date:   2014-7-3 上午11:01:58
 */
public class BidVo extends Bid{
	private String typeName;
	
	private String statusValue;

	/**
	 * 
	 * @Title: getTypeName
	 * @Description: 招标类型名称
	 * @return
	 */
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}
	
	
}
