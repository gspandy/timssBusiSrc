package com.timss.purchase.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: PurApplyPurchaserBean
 * @description: 合同买方副本信息bean
 * @company: gdyd
 * @className: PurPurchaserBean.java
 * @author: gucw
 * @createDate: 2015-10-09
 * @updateUser: gucw
 * @version: 1.0
 */
public class PurOrderPurchaserBean extends ItcMvcBean {

    private static final long serialVersionUID = -2471345952513795642L;
    
    /*
     * 采购申请id
     */
    private String sheetId;
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
    public String getSheetId() {
        return sheetId;
    }
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
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
    public void setAddress(String address) {
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
