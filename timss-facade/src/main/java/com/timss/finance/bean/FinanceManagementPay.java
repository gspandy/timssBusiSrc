package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;

public class FinanceManagementPay extends ItcMvcBean{
	private static final long serialVersionUID = 6522734577490531563L;
	private String id;//主键
	private String mainId;//报销主表id
	private String attach;//附件
	private String type;//报销类型，自己，他人，对外
	private String name;//名称
	private Double price;//价格
	private String payeeCode;//报销人编码
	private String payeeName;//报销人名称
	private String delFlag;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMainId() {
		return mainId;
	}
	public void setMainId(String mainId) {
		this.mainId = mainId;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getPayeeCode() {
		return payeeCode;
	}
	public void setPayeeCode(String payeeCode) {
		this.payeeCode = payeeCode;
	}
	public String getPayeeName() {
		return payeeName;
	}
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}
	public String getDelFlag() {
		return delFlag;
	}
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}
}
