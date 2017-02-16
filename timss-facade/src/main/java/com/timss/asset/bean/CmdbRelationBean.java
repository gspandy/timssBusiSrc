package com.timss.asset.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 上下关联关系
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbRelationBean.java
 * @author: fengzt
 * @createDate: 2015年8月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CmdbRelationBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 1474962699472504228L;

    /**
     * ID
     */
    private String id;
    
    /**
     * 上联CI
     */
    private String upCiId;
    
    /**
     * 上联CI类型
     */
    private String upCiType;
    
    /**
     * 上联CI名称
     */
    private String upCiName;
    
    /**
     * 下联CI
     */
    private String downCiId;
    
    /**
     * 下联CI类型
     */
    private String downCiType;
    
    /**
     * 下联CI名称
     */
    private String downCiName;
    
    /**
     * 名称
     */
    private String name;
    
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUpCiId() {
        return upCiId;
    }

    public void setUpCiId(String upCiId) {
        this.upCiId = upCiId;
    }

    public String getUpCiType() {
        return upCiType;
    }

    public void setUpCiType(String upCiType) {
        this.upCiType = upCiType;
    }

    public String getUpCiName() {
        return upCiName;
    }

    public void setUpCiName(String upCiName) {
        this.upCiName = upCiName;
    }

    public String getDownCiId() {
        return downCiId;
    }

    public void setDownCiId(String downCiId) {
        this.downCiId = downCiId;
    }

    public String getDownCiType() {
        return downCiType;
    }

    public void setDownCiType(String downCiType) {
        this.downCiType = downCiType;
    }

    public String getDownCiName() {
        return downCiName;
    }

    public void setDownCiName(String downCiName) {
        this.downCiName = downCiName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "CmdbRelationBean [id=" + id + ", upCiId=" + upCiId + ", upCiType=" + upCiType + ", upCiName="
                + upCiName + ", downCiId=" + downCiId + ", downCiType=" + downCiType + ", downCiName=" + downCiName
                + ", name=" + name + ", isDelete=" + isDelete + ", createUserName=" + createUserName
                + ", modifyUserName=" + modifyUserName + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createUserName == null) ? 0 : createUserName.hashCode());
        result = prime * result + ((downCiId == null) ? 0 : downCiId.hashCode());
        result = prime * result + ((downCiName == null) ? 0 : downCiName.hashCode());
        result = prime * result + ((downCiType == null) ? 0 : downCiType.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isDelete == null) ? 0 : isDelete.hashCode());
        result = prime * result + ((modifyUserName == null) ? 0 : modifyUserName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((upCiId == null) ? 0 : upCiId.hashCode());
        result = prime * result + ((upCiName == null) ? 0 : upCiName.hashCode());
        result = prime * result + ((upCiType == null) ? 0 : upCiType.hashCode());
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
        CmdbRelationBean other = (CmdbRelationBean) obj;
        if ( createUserName == null ) {
            if ( other.createUserName != null )
                return false;
        } else if ( !createUserName.equals( other.createUserName ) )
            return false;
        if ( downCiId == null ) {
            if ( other.downCiId != null )
                return false;
        } else if ( !downCiId.equals( other.downCiId ) )
            return false;
        if ( downCiName == null ) {
            if ( other.downCiName != null )
                return false;
        } else if ( !downCiName.equals( other.downCiName ) )
            return false;
        if ( downCiType == null ) {
            if ( other.downCiType != null )
                return false;
        } else if ( !downCiType.equals( other.downCiType ) )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( isDelete == null ) {
            if ( other.isDelete != null )
                return false;
        } else if ( !isDelete.equals( other.isDelete ) )
            return false;
        if ( modifyUserName == null ) {
            if ( other.modifyUserName != null )
                return false;
        } else if ( !modifyUserName.equals( other.modifyUserName ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( upCiId == null ) {
            if ( other.upCiId != null )
                return false;
        } else if ( !upCiId.equals( other.upCiId ) )
            return false;
        if ( upCiName == null ) {
            if ( other.upCiName != null )
                return false;
        } else if ( !upCiName.equals( other.upCiName ) )
            return false;
        if ( upCiType == null ) {
            if ( other.upCiType != null )
                return false;
        } else if ( !upCiType.equals( other.upCiType ) )
            return false;
        return true;
    }
}
