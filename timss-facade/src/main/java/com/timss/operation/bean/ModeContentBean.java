package com.timss.operation.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 运行方式（存储值）
 * @description: {desc}
 * @company: gdyd
 * @className: ModeContentBean.java
 * @author: fengzt
 * @createDate: 2015年10月29日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ModeContentBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 4059115609565808733L;

    /**
     * 主键
     */
    private String contentId;
    
    /**
     * 设备树ID
     */
    private String assetId;
    
    /**
     * 选项值
     */
    private String content;
    
    /**
     * 值别ID
     */
    private int dutyId;
    
    /**
     * 工种ID
     */
    private int jobsId;
    
    /**
     * 交接班ID
     */
    private int handoverId;
    
    /**
     * 是否删除
     */
    private String isDelete;
    
    /**
     * 创建人名称
     */
    private String createUserName;
    
    /**
     * 最近更新人名称
     */
    private String modifyUserName;

    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getAssetId() {
        return assetId;
    }

    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDutyId() {
        return dutyId;
    }

    public void setDutyId(int dutyId) {
        this.dutyId = dutyId;
    }

    public int getJobsId() {
        return jobsId;
    }

    public void setJobsId(int jobsId) {
        this.jobsId = jobsId;
    }

    public int getHandoverId() {
        return handoverId;
    }

    public void setHandoverId(int handoverId) {
        this.handoverId = handoverId;
    }

    public String getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(String isDelete) {
        this.isDelete = isDelete;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }
    
    
    
}
