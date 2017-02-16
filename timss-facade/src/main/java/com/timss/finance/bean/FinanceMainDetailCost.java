package com.timss.finance.bean;

import java.io.Serializable;
import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.itc.annotation.AutoGen;

/**
 * 
 * @title: 财务报销明细类型表 
 * @description: 
 * @company: gdyd
 * @className: FinanceMainDetailCost.java
 * @author: liangzl
 * @createDate: 2014年8月19日
 * @updateUser: liangzl
 * @version: 1.0
 */

public class FinanceMainDetailCost extends ItcMvcBean{
	private static final long serialVersionUID = 2056939201836485068L;

	/**
	 * 明细id
	 * */
	private String id;
	
	/**
	 * 费用ID
	 * */
	private String cost_id;
	
	/**
	 * 费用名称
	 * */
	private String cost_type;
	
	/**
	 * 金额
	 * */
	private double amount;
	
	private String siteid;
	private String deptid;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCost_id() {
		return cost_id;
	}

	public void setCost_id(String cost_id) {
		this.cost_id = cost_id;
	}

	public String getCost_type() {
		return cost_type;
	}

	public void setCost_type(String cost_type) {
		this.cost_type = cost_type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getSiteid() {
		return siteid;
	}

	public void setSiteid(String siteid) {
		this.siteid = siteid;
	}

	public String getDeptid() {
		return deptid;
	}

	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	
	
}
