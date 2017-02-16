package com.timss.pms.vo;

import com.timss.pms.bean.Payplan;

/**
 * 结算计划vo类用于列表中
 * @ClassName:     PayplanVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-24 上午11:25:55
 */
public class PayplanVo extends Payplan{
	public String typeName;

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
