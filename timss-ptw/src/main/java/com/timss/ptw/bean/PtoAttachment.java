package com.timss.ptw.bean;

import java.util.Date;

import com.yudean.mvc.bean.ItcMvcBean;


public class PtoAttachment extends ItcMvcBean {

    private static final long serialVersionUID = 5350871534346896229L;
    private String id; //业务单ID
    private String type;  //类型(表示是哪部分功能的附件，工单、维护计划……)
    private String attachId;  //上传文件ID
    private String loadPhase; //上传的环节
    private Date loadTime;  //上传时间
    private String loadUser;   //上传者
    private String deleteUser;  // 删除者
    private String delFlag;//删除标志
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
    public String getAttachId() {
        return attachId;
    }
    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
    public String getLoadPhase() {
        return loadPhase;
    }
    public void setLoadPhase(String loadPhase) {
        this.loadPhase = loadPhase;
    }
    public Date getLoadTime() {
        return loadTime;
    }
    public void setLoadTime(Date loadTime) {
        this.loadTime = loadTime;
    }
    public String getLoadUser() {
        return loadUser;
    }
    public void setLoadUser(String loadUser) {
        this.loadUser = loadUser;
    }
    public String getDeleteUser() {
        return deleteUser;
    }
    public void setDeleteUser(String deleteUser) {
        this.deleteUser = deleteUser;
    }
    
    public String getDelFlag() {
        return delFlag;
    }
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }
    @Override
    public String toString() {
        return "PtoAttachment [id=" + id + ", type=" + type + ", attachId=" + attachId + ", loadPhase=" + loadPhase
                + ", loadTime=" + loadTime + ", loadUser=" + loadUser + ", deleteUser=" + deleteUser + ", delFlag="
                + delFlag + "]";
    }
}
