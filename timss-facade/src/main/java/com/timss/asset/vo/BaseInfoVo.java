package com.timss.asset.vo;

import java.io.Serializable;

/**
 * 
 * @title: 服务器基本信息VO
 * @description: {desc}
 * @company: gdyd
 * @className: BaseInfoVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class BaseInfoVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -2916299847958470847L;

    /**
     * 硬件台账的id
     */
    private String hwId;
    
    /** 
    * 硬件台账的名称
    */
    private String hwName;
    
    /** 
     * 硬件台账的类型，从枚举中选择物理地点、机房、机柜、服务器、虚机
     */
     private String hwType;
    
    /** 
     * 硬件台账树的父节点
     */
     private String parentId;
     
    
    /** 放在机柜上的位置
     * 
     * */
    private String location;
   
    /** 
     * 备注
     * */
    private String remarks;
    
    /** 
     * 外联业务
     * */
    private String relatedBusiness;
    
    /**
     * 父节点名称
     */
    private String parentName;
    
    
    public String getRelatedBusiness() {
        return relatedBusiness;
    }
    public void setRelatedBusiness(String relatedBusiness) {
        this.relatedBusiness = relatedBusiness;
    }
    public String getParentName() {
        return parentName;
    }
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    public String getHwId() {
        return hwId;
    }
    public void setHwId(String hwId) {
        this.hwId = hwId;
    }
    public String getHwName() {
        return hwName;
    }
    public void setHwName(String hwName) {
        this.hwName = hwName;
    }
    public String getParentId() {
        return parentId;
    }
    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
  
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public String getHwType() {
        return hwType;
    }
    public void setHwType(String hwType) {
        this.hwType = hwType;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hwId == null) ? 0 : hwId.hashCode());
        result = prime * result + ((hwName == null) ? 0 : hwName.hashCode());
        result = prime * result + ((hwType == null) ? 0 : hwType.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((parentName == null) ? 0 : parentName.hashCode());
        result = prime * result + ((relatedBusiness == null) ? 0 : relatedBusiness.hashCode());
        result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        BaseInfoVo other = (BaseInfoVo) obj;
        if ( hwId == null ) {
            if ( other.hwId != null )
                return false;
        } else if ( !hwId.equals( other.hwId ) )
            return false;
        if ( hwName == null ) {
            if ( other.hwName != null )
                return false;
        } else if ( !hwName.equals( other.hwName ) )
            return false;
        if ( hwType == null ) {
            if ( other.hwType != null )
                return false;
        } else if ( !hwType.equals( other.hwType ) )
            return false;
        if ( location == null ) {
            if ( other.location != null )
                return false;
        } else if ( !location.equals( other.location ) )
            return false;
        if ( parentId == null ) {
            if ( other.parentId != null )
                return false;
        } else if ( !parentId.equals( other.parentId ) )
            return false;
        if ( parentName == null ) {
            if ( other.parentName != null )
                return false;
        } else if ( !parentName.equals( other.parentName ) )
            return false;
        if ( relatedBusiness == null ) {
            if ( other.relatedBusiness != null )
                return false;
        } else if ( !relatedBusiness.equals( other.relatedBusiness ) )
            return false;
        if ( remarks == null ) {
            if ( other.remarks != null )
                return false;
        } else if ( !remarks.equals( other.remarks ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "BaseInfoVo [hwId=" + hwId + ", hwName=" + hwName + ", hwType=" + hwType + ", parentId=" + parentId
                + ", location=" + location + ", remarks=" + remarks + ", relatedBusiness=" + relatedBusiness
                + ", parentName=" + parentName + "]";
    }
    
    
    
}
