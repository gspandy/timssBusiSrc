package com.timss.purchase.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurVendor.java
 * @author: 890166
 * @createDate: 2014-6-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurVendor extends ItcMvcBean {

    private static final long serialVersionUID = -8183400167697951152L;

    @AutoGen(value = "SU_COMPANY_NO", requireType = GenerationType.REQUIRED_NULL)
    private String companyNo; // 供应商编码
    private String name; // 公司名称
    private String type; // 公司类型
    private String address; // 地址
    private String bankName;// 银行名称
    private String bankAccount;// 银行账号
    private String contact; // 联系人
    private String tel;// 电话号码
    private String fax;// 传真
    private String parentCompanycode;// 父供应商编码
    private String siteid;// 站点id
    private String deptid;// 部门id
    private String createuser;
    private String modifyuser;
    private String postCode;
    private String city;
    private String province;
    private String activeflag;
    private String source;

    private Date createdate = new Date();
    private Date modifydate = new Date();

    /**
     * @return the companyNo
     */
    public String getCompanyNo() {
        return companyNo;
    }

    /**
     * @param companyNo the companyNo to set
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the bankName
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * @param bankName the bankName to set
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * @return the bankAccount
     */
    public String getBankAccount() {
        return bankAccount;
    }

    /**
     * @param bankAccount the bankAccount to set
     */
    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * @return the fax
     */
    public String getFax() {
        return fax;
    }

    /**
     * @param fax the fax to set
     */
    public void setFax(String fax) {
        this.fax = fax;
    }

    /**
     * @return the parentCompanycode
     */
    public String getParentCompanycode() {
        return parentCompanycode;
    }

    /**
     * @param parentCompanycode the parentCompanycode to set
     */
    public void setParentCompanycode(String parentCompanycode) {
        this.parentCompanycode = parentCompanycode;
    }

    /**
     * @return the siteid
     */
    public String getSiteid() {
        return siteid;
    }

    /**
     * @param siteid the siteid to set
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    /**
     * @return the deptid
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * @param deptid the deptid to set
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

    /**
     * @return the createdate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    /**
     * @return the modifydate
     */
    public Date getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the modifyuser
     */
    public String getModifyuser() {
        return modifyuser;
    }

    /**
     * @param modifyuser the modifyuser to set
     */
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    /**
     * @return the postCode
     */
    public String getPostCode() {
        return postCode;
    }

    /**
     * @param postCode the postCode to set
     */
    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * @return the activeflag
     */
    public String getActiveflag() {
        return activeflag;
    }

    /**
     * @param activeflag the activeflag to set
     */
    public void setActiveflag(String activeflag) {
        this.activeflag = activeflag;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

}
