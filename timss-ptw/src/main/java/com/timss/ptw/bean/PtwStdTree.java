package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**'
 * 
 * @title: 标准树bean
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStdTree.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwStdTree  extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -1707413824708411544L;

    /**
     * id
     */
    private int id;
    
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * 编码
     */
    private String code;
    
    /**
     * 父节点ID
     */
    private String parentId;
    
    /**
     * 设备树ID
     */
    private String equipmentId;
    
    /**
     * 备注
     */
    private String remark;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 有效标识(1--有效    0--无效)
     */
    private int yxbz ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public int getYxbz() {
        return yxbz;
    }

    public void setYxbz(int yxbz) {
        this.yxbz = yxbz;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((equipmentId == null) ? 0 : equipmentId.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + ((remark == null) ? 0 : remark.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + yxbz;
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
        PtwStdTree other = (PtwStdTree) obj;
        if ( code == null ) {
            if ( other.code != null )
                return false;
        } else if ( !code.equals( other.code ) )
            return false;
        if ( deptId == null ) {
            if ( other.deptId != null )
                return false;
        } else if ( !deptId.equals( other.deptId ) )
            return false;
        if ( equipmentId == null ) {
            if ( other.equipmentId != null )
                return false;
        } else if ( !equipmentId.equals( other.equipmentId ) )
            return false;
        if ( id != other.id )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( parentId == null ) {
            if ( other.parentId != null )
                return false;
        } else if ( !parentId.equals( other.parentId ) )
            return false;
        if ( remark == null ) {
            if ( other.remark != null )
                return false;
        } else if ( !remark.equals( other.remark ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( yxbz != other.yxbz )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PtwStdTree [id=" + id + ", name=" + name + ", code=" + code + ", parentId=" + parentId
                + ", equipmentId=" + equipmentId + ", remark=" + remark + ", siteId=" + siteId + ", deptId=" + deptId
                + ", yxbz=" + yxbz + "]";
    }
    
    
}
