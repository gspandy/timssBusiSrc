package com.timss.ptw.vo;

import java.util.Date;
import java.util.List;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 两票用户信息bean
 * @description: 两票用户信息
 * @company: gdyd
 * @className: PtwPtoUserInfoVo.java
 * @author: gucw
 * @createDate: 2015年7月28日
 * @updateUser:
 * @version: 1.0
 */
public class PtwPtoUserInfoVo extends ItcMvcBean {

    
    /**
     * 
     */
    private static final long serialVersionUID = 5251962138081852557L;
    private List<String> userList;
    private String id;
    private String type;
    private String step;
    private String category;
    private String delFlag;
    private Date beginDate;
    private Date endDate;
    private Date curDate;
    public List<String> getUserList() {
        return userList;
    }
    public void setUserList(List<String> userList) {
        this.userList = userList;
    }
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
    @Override
    public String toString() {
        return "PtwPtoUserInfoVo [userList=" + userList + ", id=" + id + ", type=" + type + ", step=" + step
                + ", category=" + category + ", delFlag=" + delFlag + ", beginDate=" + beginDate + ", endDate=" + endDate
                + ", curDate=" + curDate + "]";
    }
    
    
}
