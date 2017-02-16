package com.timss.inventory.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshotVO.java
 * @author: 890166
 * @createDate: 2014-11-24
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatSnapshotVO {

    private String imsid;
    private String opertype;
    private String opertypeName;
    private String remark;

    private String createuserName;
    private String createdate;
    private String modifyuserName;
    private String modifydate;

    /**
     * @return the createdate
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the modifydate
     */
    public String getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the imsid
     */
    public String getImsid() {
        return imsid;
    }

    /**
     * @param imsid the imsid to set
     */
    public void setImsid(String imsid) {
        this.imsid = imsid;
    }

    /**
     * @return the opertype
     */
    public String getOpertype() {
        return opertype;
    }

    /**
     * @param opertype the opertype to set
     */
    public void setOpertype(String opertype) {
        this.opertype = opertype;
    }

    /**
     * @return the opertypeName
     */
    public String getOpertypeName() {
        return opertypeName;
    }

    /**
     * @param opertypeName the opertypeName to set
     */
    public void setOpertypeName(String opertypeName) {
        this.opertypeName = opertypeName;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the createuserName
     */
    public String getCreateuserName() {
        return createuserName;
    }

    /**
     * @param createuserName the createuserName to set
     */
    public void setCreateuserName(String createuserName) {
        this.createuserName = createuserName;
    }

    /**
     * @return the modifyuserName
     */
    public String getModifyuserName() {
        return modifyuserName;
    }

    /**
     * @param modifyuserName the modifyuserName to set
     */
    public void setModifyuserName(String modifyuserName) {
        this.modifyuserName = modifyuserName;
    }

}
