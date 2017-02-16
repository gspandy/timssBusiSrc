package com.timss.ptw.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 操作票审批用户信息bean
 * @description: 操作票审批用户信息
 * @company: gdyd
 * @className: PtoRelateUserInfo.java
 * @author: gucw
 * @createDate: 2015年7月13日
 * @updateUser:
 * @version: 1.0
 */
public class PtoRelateUserInfo extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 3464317683543655908L;
    private String standardPtoId;
    private String type;
    private Date oprDate;
    private String userId;
    private String userName;

    public String getStandardPtoId() {
        return standardPtoId;
    }

    public void setStandardPtoId(String standardPtoId) {
        this.standardPtoId = standardPtoId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getOprDate() {
        return oprDate;
    }

    public void setOprDate(Date oprDate) {
        this.oprDate = oprDate;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "PtoRelateUserInfo [standardPtoId=" + standardPtoId + ", oprDate=" + oprDate + ", userId="
                + userId + ", userName=" + userName + "]";
    }

}
