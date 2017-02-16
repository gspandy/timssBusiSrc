package com.timss.workorder.bean;

import java.io.Serializable;

public class WoDelayConfig implements Serializable {
    
    private static final long serialVersionUID = 4656071561427920823L;
    
    private String siteId; // 站点
    private String delayType; // 延时类型
    private String priority; // 优先级
    private float maxLength; // 最大延时时长（小时）
    
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public String getDelayType() {
        return delayType;
    }
    public void setDelayType(String delayType) {
        this.delayType = delayType;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }
    public float getMaxLength() {
        return maxLength;
    }
    public void setMaxLength(float maxLength) {
        this.maxLength = maxLength;
    }
    @Override
    public String toString() {
        return "WoDelayConfig [siteId=" + siteId + ", delayType=" + delayType + ", priority=" + priority
                + ", maxLength=" + maxLength + "]";
    }
   
   
   

}
