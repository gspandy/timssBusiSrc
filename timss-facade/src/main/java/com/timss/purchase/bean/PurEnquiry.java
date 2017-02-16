package com.timss.purchase.bean;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurEnquiry.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurEnquiry {

    private String enquiryid;
    private String enquiryname;
    private String contactname;
    private String contactphone;
    private String discription;
    private String deliverplace;
    private String createuser;
    private String purchaseorderno;
    private String status;
    private String modifyuser;
    private String deptid;
    private String siteid;

    private Date expirydate = new Date() ;
    private Date createdate = new Date() ;
    private Date modifydate = new Date() ;

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status == null ? "" : status.trim();
    }

    public String getEnquiryid() {
        return enquiryid;
    }

    public void setEnquiryid(String enquiryid) {
        this.enquiryid = enquiryid == null ? "" : enquiryid.trim();
    }

    public String getEnquiryname() {
        return enquiryname;
    }

    public void setEnquiryname(String enquiryname) {
        this.enquiryname = enquiryname == null ? "" : enquiryname.trim();
    }

    public String getContactname() {
        return contactname;
    }

    public void setContactname(String contactname) {
        this.contactname = contactname == null ? "" : contactname.trim();
    }

    public String getContactphone() {
        return contactphone;
    }

    public void setContactphone(String contactphone) {
        this.contactphone = contactphone == null ? "" : contactphone.trim();
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription == null ? "" : discription.trim();
    }

    public String getDeliverplace() {
        return deliverplace;
    }

    public void setDeliverplace(String deliverplace) {
        this.deliverplace = deliverplace == null ? "" : deliverplace.trim();
    }

    public Date getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(Date expirydate) {
        this.expirydate = expirydate;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getPurchaseorderno() {
        return purchaseorderno;
    }

    public void setPurchaseorderno(String purchaseorderno) {
        this.purchaseorderno = purchaseorderno == null ? "" : purchaseorderno.trim();
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
        this.createuser = createuser == null ? "" : createuser.trim();
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
        this.modifyuser = modifyuser == null ? "" : modifyuser.trim();
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
        this.deptid = deptid == null ? "" : deptid.trim();
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
        this.siteid = siteid == null ? "" : siteid.trim();
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }
}