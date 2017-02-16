package com.timss.inventory.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: OrganizeDataAttrVO.java
 * @author: 890166
 * @createDate: 2015-5-28
 * @updateUser: 890166
 * @version: 1.0
 */
public class OrganizeDataAttrVO {

    public String name; // 字段名称
    public String type; // 字段类型
    public String isNull; // 是否为null
    public String maxLength; // 最大长度
    public String sortOrder; // 排序
    public String cName;// 中文名

    /**
     * @return the cName
     */
    public String getcName() {
        return cName;
    }

    /**
     * @param cName the cName to set
     */
    public void setcName(String cName) {
        this.cName = cName;
    }

    /**
     * @return the sortOrder
     */
    public String getSortOrder() {
        return sortOrder;
    }

    /**
     * @param sortOrder the sortOrder to set
     */
    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the isNull
     */
    public String getIsNull() {
        return isNull;
    }

    /**
     * @param isNull the isNull to set
     */
    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    /**
     * @return the maxLength
     */
    public String getMaxLength() {
        return maxLength;
    }

    /**
     * @param maxLength the maxLength to set
     */
    public void setMaxLength(String maxLength) {
        this.maxLength = maxLength;
    }

}
