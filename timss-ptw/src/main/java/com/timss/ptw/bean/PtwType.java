package com.timss.ptw.bean;

import java.io.Serializable;


/**
 * 
 * @title: 工作票类型表
 * @description: {desc}
 * @company: gdyd
 * @className: PtwType.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwType implements Serializable {

    private static final long serialVersionUID = -9103387290546888365L;
    
    private int id;
    private int wtTypeDefineId;
    private String typeName;
    private String siteId;
    private String typeCode;
    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }
    /**
     * @return 工作票类型定义_id
     */
    public int getWtTypeDefineId() {
        return wtTypeDefineId;
    }
    /**
     * @param 工作票类型定义_id
     */
    public void setWtTypeDefineId(int wtTypeDefineId) {
        this.wtTypeDefineId = wtTypeDefineId;
    }
    /**
     * @return 自定义类型名称
     */
    public String getTypeName() {
        return typeName;
    }
    /**
     * @param 自定义类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
    /**
     * @return 单位编号
     */
    public String getSiteId() {
        return siteId;
    }
    /**
     * @param 单位编号
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    /**
     * @return 工作票类型编码
     */
    public String getTypeCode() {
        return typeCode;
    }
    /**
     * @param 工作票类型编码
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }
    public PtwType() {
        super();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        PtwType other = (PtwType) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "PtwType [id=" + id + ", wtTypeDefineId=" + wtTypeDefineId + ", typeName=" + typeName + ", siteId="
                + siteId + ", typeCode=" + typeCode + "]";
    }
    
}
