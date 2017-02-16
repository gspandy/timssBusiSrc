package com.timss.ptw.vo;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 两票审批人员用户信息bean
 * @description: 两票用户信息
 * @company: gdyd
 * @className: PtwPtoSelectUserInfoVo.java
 * @author: gucw
 * @createDate: 2015年8月4日
 * @updateUser:
 * @version: 1.0
 */
public class PtwPtoSelectUserInfoVo extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 6434751743160947801L;
    private String id;
    private String type;
    private String step;
    private String category;
    private String delFlag;
    private Date beginDate;
    private Date endDate;
    private Date curDate;
    private String userId;
    private String userName;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStep() {
        return step;
    }
    public void setStep(String step) {
        this.step = step;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    public Date getBeginDate() {
        return beginDate;
    }
    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getCurDate() {
        return curDate;
    }
    public void setCurDate(Date curDate) {
        this.curDate = curDate;
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
        return "PtwPtoSelectUserInfoVo [id=" + id + ", type=" + type + ", step=" + step + ", category=" + category
                + ", delFlag=" + delFlag + ", beginDate=" + beginDate + ", endDate=" + endDate + ", curDate=" + curDate
                + ", userId=" + userId + ", userName=" + userName + "]";
    }
    
}
