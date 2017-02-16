package com.timss.inventory.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatSnapshot.java
 * @author: 890166
 * @createDate: 2014-11-24
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatSnapshot extends ItcMvcBean {

    private static final long serialVersionUID = 95263006784480743L;

    @AutoGen(value = "INV_IMS_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imsid;
    private String opertype;
    private String remark;

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

}
