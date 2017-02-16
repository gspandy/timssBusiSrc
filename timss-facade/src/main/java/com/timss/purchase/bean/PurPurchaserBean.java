package com.timss.purchase.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurPurchaserBean
 * @description: 买方合同信息bean
 * @company: gdyd
 * @className: PurPurchaserBean.java
 * @author: 890191
 * @createDate: 2015-9-23
 * @updateUser: 890191
 * @version: 1.0
 */
public class PurPurchaserBean extends ItcMvcBean {

    private static final long serialVersionUID = 6289118899588547861L;
    
    /*
     * 买方id
     */
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String purchaserId;
    /*
     * 买方名称
     */
    private String purchaserName;
    /*
     * 地址
     */
    private String address;
    /*
     * 电话
     */
    private String phone;
    /*
     * 传真
     */
    private String fax;
    /*
     * 开户行
     */
    private String bank; 
    /*
     * 账号
     */
    private String account;
    /*
     * 排序
     */
    private Integer sort;
    /*
     * 邮编
     */
    private String zip;
    /*
     * 纳税识别号
     */
    private String taxNo;
    
    public String getPurchaserId() {
		return purchaserId;
	}
	public void setPurchaserId(String purchaserId) {
		this.purchaserId = purchaserId;
	}
	public String getPurchaserName() {
		return purchaserName;
	}
	public void setPurchaserName(String purchaserName) {
		this.purchaserName = purchaserName;
	}
	public String getAddress() {
        return address;
    }
    public void setAddresss(String address) {
        this.address = address;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getFax() {
        return fax;
    }
    public void setFax(String fax) {
        this.fax = fax;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Integer getSort() {
        return sort;
    }
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getTaxNo() {
        return taxNo;
    }
    public void setTaxNo(String taxNo) {
        this.taxNo = taxNo;
    }

    
}
