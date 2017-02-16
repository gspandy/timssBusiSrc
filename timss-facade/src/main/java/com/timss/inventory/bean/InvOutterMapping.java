package com.timss.inventory.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvOutterMapping.java
 * @author: 890166
 * @createDate: 2014-8-11
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvOutterMapping extends ItcMvcBean {

    private static final long serialVersionUID = -6418742461828499952L;

    private String invId;
    private String invType;
    private String outterId;
    private String outterType;
    private String outterNo;
    private String siteid;
    private String applyType;

    /**
     * @return the applyType
     */
    public String getApplyType() {
        return applyType;
    }

    /**
     * @param applyType the applyType to set
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * @return the invId
     */
    public String getInvId() {
        return invId;
    }

    /**
     * @param invId the invId to set
     */
    public void setInvId(String invId) {
        this.invId = invId;
    }

    /**
     * @return the invType
     */
    public String getInvType() {
        return invType;
    }

    /**
     * @param invType the invType to set
     */
    public void setInvType(String invType) {
        this.invType = invType;
    }

    /**
     * @return the outterId
     */
    public String getOutterId() {
        return outterId;
    }

    /**
     * @param outterId the outterId to set
     */
    public void setOutterId(String outterId) {
        this.outterId = outterId;
    }

    /**
     * @return the outterType
     */
    public String getOutterType() {
        return outterType;
    }

    /**
     * @param outterType the outterType to set
     */
    public void setOutterType(String outterType) {
        this.outterType = outterType;
    }

    /**
     * @return the outterNo
     */
    public String getOutterNo() {
        return outterNo;
    }

    /**
     * @param outterNo the outterNo to set
     */
    public void setOutterNo(String outterNo) {
        this.outterNo = outterNo;
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

}
