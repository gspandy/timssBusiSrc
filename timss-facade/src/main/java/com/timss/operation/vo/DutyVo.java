package com.timss.operation.vo;

import java.io.Serializable;

/**
 * 
* @ClassName: DutyVo 
* @Description: 值别表vo
* @author: fengzt 
* @date: 2014年5月28日
*
 */
public class DutyVo implements Serializable {

    
    
    /**
     * 
     */
    private static final long serialVersionUID = -6540775678062545675L;

    /**
     * 值别编码
     */
    private String num;
    
    /**
     * 值别名称
     */
    private String name;
    
    /**
     * 排序
     */
    private int sortType;
    
    /**
     * 部门 (如果部门表有了，替换部门实体)
     */
    private int deptId;
    
    /**
     * 站点（如果有站点表，则替换站点实体）
     */
    private int siteId;


    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSortType() {
        return sortType;
    }

    public void setSortType(int sortType) {
        this.sortType = sortType;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getSiteId() {
        return siteId;
    }

    public void setSiteId(int siteId) {
        this.siteId = siteId;
    }

    @Override
    public String toString() {
        return "DutyVo [num=" + num + ", name=" + name + ", sortType="
                + sortType + ", deptId=" + deptId + ", siteId=" + siteId + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + deptId;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + siteId;
        result = prime * result + sortType;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DutyVo other = (DutyVo) obj;
        if (deptId != other.deptId)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (num == null) {
            if (other.num != null)
                return false;
        } else if (!num.equals(other.num))
            return false;
        if (siteId != other.siteId)
            return false;
        if (sortType != other.sortType)
            return false;
        return true;
    }

    
}
