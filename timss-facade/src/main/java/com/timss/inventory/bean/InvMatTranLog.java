package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 出入库的来源批次记录表
 * @description: 出入库的来源批次记录表
 * @company: gdyd
 * @className: InvRealTimeData.java
 * @author: 890151
 * @createDate: 2016年5月5日
 * @updateUser: 890151
 * @version: 1.0
 */
public class InvMatTranLog extends ItcMvcBean {

	private static final long serialVersionUID = 908196700772521104L;
	
	private String imtlId;	//出入库的来源批次记录表主键ID
    private String imtdId;	//出入库流水ID
    private String fromImtdId;	//出入库的来源批次流水ID
    private BigDecimal tranQty;	//出入库数量
    private String tranCategory;//出入库类型
	/**
	 * @return the imtlId
	 */
	public String getImtlId() {
		return imtlId;
	}
	/**
	 * @param imtlId the imtlId to set
	 */
	public void setImtlId(String imtlId) {
		this.imtlId = imtlId;
	}
	/**
	 * @return the imtdId
	 */
	public String getImtdId() {
		return imtdId;
	}
	/**
	 * @param imtdId the imtdId to set
	 */
	public void setImtdId(String imtdId) {
		this.imtdId = imtdId;
	}
	/**
	 * @return the fromImtdId
	 */
	public String getFromImtdId() {
		return fromImtdId;
	}
	/**
	 * @param fromImtdId the fromImtdId to set
	 */
	public void setFromImtdId(String fromImtdId) {
		this.fromImtdId = fromImtdId;
	}
	/**
	 * @return the tranQty
	 */
	public BigDecimal getTranQty() {
		return tranQty;
	}
	/**
	 * @param tranQty the tranQty to set
	 */
	public void setTranQty(BigDecimal tranQty) {
		this.tranQty = tranQty;
	}
	/**
	 * @return the tranCategory
	 */
	public String getTranCategory() {
		return tranCategory;
	}
	/**
	 * @param tranCategory the tranCategory to set
	 */
	public void setTranCategory(String tranCategory) {
		this.tranCategory = tranCategory;
	}

}
