package com.timss.ptw.vo;

import java.io.Serializable;
import java.util.Date;

/**'
 * 
 * @title: 标准树vo
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStdTree.java
 * @author: fengzt
 * @createDate: 2014年12月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwStdTreeVo  implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -7762933655843648015L;

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
    private int parentId;
    
    /**
     * 父节点名字
     */
    private String parentName;
    

    /**
     * 设备树ID
     */
    private String equipmentId;
    
    /**
     * 设备树名字
     */
    private String equipmentName;
    
    /**
     * 设备树名字
     */
    private String equipmentCode;
    
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
    
    /**
     * 创建用户ID
     */
    private String createuser;
    
    /**
     * 树的图标
     */
    private String iconCls;
    
    private Date createdate;
    
    private String modifyuser;
    
    private Date modifydate;
    
    
    public String getIconCls() {
        return iconCls;
    }

    public void setIconCls(String iconCls) {
        this.iconCls = iconCls;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
    
    public String getCreateuser() {
        return createuser;
    }

    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    public Date getCreatedate() {
        return createdate;
    }

    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    public String getModifyuser() {
        return modifyuser;
    }

    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    public Date getModifydate() {
        return modifydate;
    }

    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

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


    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
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
    
    
    public String getEquipmentCode() {
		return equipmentCode;
	}

	public void setEquipmentCode(String equipmentCode) {
		this.equipmentCode = equipmentCode;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((createdate == null) ? 0 : createdate.hashCode());
        result = prime * result + ((createuser == null) ? 0 : createuser.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((equipmentId == null) ? 0 : equipmentId.hashCode());
        result = prime * result + ((equipmentName == null) ? 0 : equipmentName.hashCode());
        result = prime * result + ((iconCls == null) ? 0 : iconCls.hashCode());
        result = prime * result + id;
        result = prime * result + ((modifydate == null) ? 0 : modifydate.hashCode());
        result = prime * result + ((modifyuser == null) ? 0 : modifyuser.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + parentId;
        result = prime * result + ((parentName == null) ? 0 : parentName.hashCode());
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
        PtwStdTreeVo other = (PtwStdTreeVo) obj;
        if ( code == null ) {
            if ( other.code != null )
                return false;
        } else if ( !code.equals( other.code ) )
            return false;
        if ( createdate == null ) {
            if ( other.createdate != null )
                return false;
        } else if ( !createdate.equals( other.createdate ) )
            return false;
        if ( createuser == null ) {
            if ( other.createuser != null )
                return false;
        } else if ( !createuser.equals( other.createuser ) )
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
        if ( equipmentName == null ) {
            if ( other.equipmentName != null )
                return false;
        } else if ( !equipmentName.equals( other.equipmentName ) )
            return false;
        if ( iconCls == null ) {
            if ( other.iconCls != null )
                return false;
        } else if ( !iconCls.equals( other.iconCls ) )
            return false;
        if ( id != other.id )
            return false;
        if ( modifydate == null ) {
            if ( other.modifydate != null )
                return false;
        } else if ( !modifydate.equals( other.modifydate ) )
            return false;
        if ( modifyuser == null ) {
            if ( other.modifyuser != null )
                return false;
        } else if ( !modifyuser.equals( other.modifyuser ) )
            return false;
        if ( name == null ) {
            if ( other.name != null )
                return false;
        } else if ( !name.equals( other.name ) )
            return false;
        if ( parentId != other.parentId )
            return false;
        if ( parentName == null ) {
            if ( other.parentName != null )
                return false;
        } else if ( !parentName.equals( other.parentName ) )
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
        return "PtwStdTreeVo [id=" + id + ", name=" + name + ", code=" + code + ", parentId=" + parentId
                + ", parentName=" + parentName + ", equipmentId=" + equipmentId + ", equipmentName=" + equipmentName
                + ", remark=" + remark + ", siteId=" + siteId + ", deptId=" + deptId + ", yxbz=" + yxbz
                + ", createuser=" + createuser + ", iconCls=" + iconCls + ", createdate=" + createdate
                + ", modifyuser=" + modifyuser + ", modifydate=" + modifydate + "]";
    }
    
    
}
