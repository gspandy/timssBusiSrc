package com.timss.asset.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 用户配置参数项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbParamsBean.java
 * @author: fengzt
 * @createDate: 2015年8月10日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CmdbParamsBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -9180981310823833701L;
    
    /**
     * id
     */
    private String id;
    
    /**
     * CI类型
     */
    private String ciType;
    
    /**
     * 参数项
     */
    private String paramType;
    
    /**
     * 参数值
     */
    private String paramVal;
    
    /**
     * 排序
     */
    private int orderType;
    
    /**
     * 是否删除
     */
    private String isDelete;
    
    
    /**
     * 创建人
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

    public String getCiType() {
        return ciType;
    }

    public void setCiType(String ciType) {
        this.ciType = ciType;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getParamVal() {
        return paramVal;
    }

    public void setParamVal(String paramVal) {
        this.paramVal = paramVal;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((ciType == null) ? 0 : ciType.hashCode());
        result = prime * result + ((createUserName == null) ? 0 : createUserName.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isDelete == null) ? 0 : isDelete.hashCode());
        result = prime * result + ((modifyUserName == null) ? 0 : modifyUserName.hashCode());
        result = prime * result + orderType;
        result = prime * result + ((paramType == null) ? 0 : paramType.hashCode());
        result = prime * result + ((paramVal == null) ? 0 : paramVal.hashCode());
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
        CmdbParamsBean other = (CmdbParamsBean) obj;
        if ( ciType == null ) {
            if ( other.ciType != null )
                return false;
        } else if ( !ciType.equals( other.ciType ) )
            return false;
        if ( createUserName == null ) {
            if ( other.createUserName != null )
                return false;
        } else if ( !createUserName.equals( other.createUserName ) )
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
        if ( orderType != other.orderType )
            return false;
        if ( paramType == null ) {
            if ( other.paramType != null )
                return false;
        } else if ( !paramType.equals( other.paramType ) )
            return false;
        if ( paramVal == null ) {
            if ( other.paramVal != null )
                return false;
        } else if ( !paramVal.equals( other.paramVal ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CmdbParamsBean [id=" + id + ", ciType=" + ciType + ", paramType=" + paramType + ", paramVal="
                + paramVal + ", orderType=" + orderType + ", isDelete=" + isDelete + ", createUserName="
                + createUserName + ", modifyUserName=" + modifyUserName + "]";
    }
    
}
