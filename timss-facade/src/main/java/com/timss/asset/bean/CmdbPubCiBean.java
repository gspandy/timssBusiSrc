package com.timss.asset.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 公共配置项
 * @description: {desc}
 * @company: gdyd
 * @className: CmdbPubCiBean.java
 * @author: fengzt
 * @createDate: 2015年8月21日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class CmdbPubCiBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 6813449748690161005L;

    /**
     * id
     */
    private String id;
    
    /**
     * 名称
     */
    private String name;
    
    /**
     * CI类型
     */
    private String ciType;
    
    /**
     * CI子类型
     */
    private String subType;
    
    /**
     * CI状态
     */
    private String status;
    
    /**
     * CI责任人ID
     */
    private String responUserId;
    
    /**
     * CI责任人
     */
    private String responUserName;
    
    /**
     * 资产编号
     */
    private String eqId;
    
    /**
     * 描述
     */
    private String description;
    
    /**
     * 所属单位Id
     */
    private String unitId;
    
    /**
     * 所属单位名称
     */
    private String unitName;
    
    /**
     *  供应商id
     */
    private String supplierId;
    
    /**
     * A端Id
     */
    private String aportId;
    
    /**
     * A端名称
     */
    private String aportName;
    
    /**
     * b端Id
     */
    private String bportId;
    
    /**
     * b端名称
     */
    private String bportName;
    
    /**
     * 机房ID
     */
    private String engineRoomId;
    
    /**
     * 机房名称
     */
    private String engineRoomName;
    
    /**
     * 机柜ID
     */
    private String cabinetId;

	/**
     * 机柜名称
     */
    private String cabinetName;
    
    public String getCabinetId() {
		return cabinetId;
	}

	public void setCabinetId(String cabinetId) {
		this.cabinetId = cabinetId;
	}

	public String getCabinetName() {
		return cabinetName;
	}

	public void setCabinetName(String cabinetName) {
		this.cabinetName = cabinetName;
	}

    public String getEngineRoomId() {
        return engineRoomId;
    }

    public void setEngineRoomId(String engineRoomId) {
        this.engineRoomId = engineRoomId;
    }

    public String getEngineRoomName() {
        return engineRoomName;
    }

    public void setEngineRoomName(String engineRoomName) {
        this.engineRoomName = engineRoomName;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    
    public String getAportId() {
        return aportId;
    }

    public void setAportId(String aportId) {
        this.aportId = aportId;
    }

    public String getAportName() {
        return aportName;
    }

    public void setAportName(String aportName) {
        this.aportName = aportName;
    }

    public String getBportId() {
        return bportId;
    }

    public void setBportId(String bportId) {
        this.bportId = bportId;
    }

    public String getBportName() {
        return bportName;
    }

    public void setBportName(String bportName) {
        this.bportName = bportName;
    }


    /**
     *  供应商
     */
    private String supplier;
    
    /**
     * 供应商联系人
     */
    private String connectUser;
    
    /**
     * 供应商电话
     */
    private String connectPhone;
    
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCiType() {
        return ciType;
    }

    public void setCiType(String ciType) {
        this.ciType = ciType;
    }

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponUserId() {
        return responUserId;
    }

    public void setResponUserId(String responUserId) {
        this.responUserId = responUserId;
    }

    public String getResponUserName() {
        return responUserName;
    }

    public void setResponUserName(String responUserName) {
        this.responUserName = responUserName;
    }

    public String getEqId() {
        return eqId;
    }

    public void setEqId(String eqId) {
        this.eqId = eqId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getConnectUser() {
        return connectUser;
    }

    public void setConnectUser(String connectUser) {
        this.connectUser = connectUser;
    }

    public String getConnectPhone() {
        return connectPhone;
    }

    public void setConnectPhone(String connectPhone) {
        this.connectPhone = connectPhone;
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
        result = prime * result + ((aportId == null) ? 0 : aportId.hashCode());
        result = prime * result + ((aportName == null) ? 0 : aportName.hashCode());
        result = prime * result + ((bportId == null) ? 0 : bportId.hashCode());
        result = prime * result + ((bportName == null) ? 0 : bportName.hashCode());
        result = prime * result + ((ciType == null) ? 0 : ciType.hashCode());
        result = prime * result + ((connectPhone == null) ? 0 : connectPhone.hashCode());
        result = prime * result + ((connectUser == null) ? 0 : connectUser.hashCode());
        result = prime * result + ((createUserName == null) ? 0 : createUserName.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((engineRoomId == null) ? 0 : engineRoomId.hashCode());
        result = prime * result + ((engineRoomName == null) ? 0 : engineRoomName.hashCode());
        result = prime * result + ((eqId == null) ? 0 : eqId.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((isDelete == null) ? 0 : isDelete.hashCode());
        result = prime * result + ((modifyUserName == null) ? 0 : modifyUserName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((responUserId == null) ? 0 : responUserId.hashCode());
        result = prime * result + ((responUserName == null) ? 0 : responUserName.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((subType == null) ? 0 : subType.hashCode());
        result = prime * result + ((supplier == null) ? 0 : supplier.hashCode());
        result = prime * result + ((supplierId == null) ? 0 : supplierId.hashCode());
        result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
        result = prime * result + ((unitName == null) ? 0 : unitName.hashCode());
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
        CmdbPubCiBean other = (CmdbPubCiBean) obj;
        if ( aportId == null ) {
            if ( other.aportId != null )
                return false;
        } else if ( !aportId.equals( other.aportId ) )
            return false;
        if ( aportName == null ) {
            if ( other.aportName != null )
                return false;
        } else if ( !aportName.equals( other.aportName ) )
            return false;
        if ( bportId == null ) {
            if ( other.bportId != null )
                return false;
        } else if ( !bportId.equals( other.bportId ) )
            return false;
        if ( bportName == null ) {
            if ( other.bportName != null )
                return false;
        } else if ( !bportName.equals( other.bportName ) )
            return false;
        if ( ciType == null ) {
            if ( other.ciType != null )
                return false;
        } else if ( !ciType.equals( other.ciType ) )
            return false;
        if ( connectPhone == null ) {
            if ( other.connectPhone != null )
                return false;
        } else if ( !connectPhone.equals( other.connectPhone ) )
            return false;
        if ( connectUser == null ) {
            if ( other.connectUser != null )
                return false;
        } else if ( !connectUser.equals( other.connectUser ) )
            return false;
        if ( createUserName == null ) {
            if ( other.createUserName != null )
                return false;
        } else if ( !createUserName.equals( other.createUserName ) )
            return false;
        if ( description == null ) {
            if ( other.description != null )
                return false;
        } else if ( !description.equals( other.description ) )
            return false;
        if ( engineRoomId == null ) {
            if ( other.engineRoomId != null )
                return false;
        } else if ( !engineRoomId.equals( other.engineRoomId ) )
            return false;
        if ( engineRoomName == null ) {
            if ( other.engineRoomName != null )
                return false;
        } else if ( !engineRoomName.equals( other.engineRoomName ) )
            return false;
        if ( eqId == null ) {
            if ( other.eqId != null )
                return false;
        } else if ( !eqId.equals( other.eqId ) )
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
        if ( responUserId == null ) {
            if ( other.responUserId != null )
                return false;
        } else if ( !responUserId.equals( other.responUserId ) )
            return false;
        if ( responUserName == null ) {
            if ( other.responUserName != null )
                return false;
        } else if ( !responUserName.equals( other.responUserName ) )
            return false;
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
            return false;
        if ( subType == null ) {
            if ( other.subType != null )
                return false;
        } else if ( !subType.equals( other.subType ) )
            return false;
        if ( supplier == null ) {
            if ( other.supplier != null )
                return false;
        } else if ( !supplier.equals( other.supplier ) )
            return false;
        if ( supplierId == null ) {
            if ( other.supplierId != null )
                return false;
        } else if ( !supplierId.equals( other.supplierId ) )
            return false;
        if ( unitId == null ) {
            if ( other.unitId != null )
                return false;
        } else if ( !unitId.equals( other.unitId ) )
            return false;
        if ( unitName == null ) {
            if ( other.unitName != null )
                return false;
        } else if ( !unitName.equals( other.unitName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "CmdbPubCiBean [id=" + id + ", name=" + name + ", ciType=" + ciType + ", subType=" + subType
                + ", status=" + status + ", responUserId=" + responUserId + ", responUserName=" + responUserName
                + ", eqId=" + eqId + ", description=" + description + ", unitId=" + unitId + ", unitName=" + unitName
                + ", supplierId=" + supplierId + ", aportId=" + aportId + ", aportName=" + aportName + ", bportId="
                + bportId + ", bportName=" + bportName + ", engineRoomId=" + engineRoomId + ", engineRoomName="
                + engineRoomName + ", supplier=" + supplier + ", connectUser=" + connectUser + ", connectPhone="
                + connectPhone + ", isDelete=" + isDelete + ", createUserName=" + createUserName + ", modifyUserName="
                + modifyUserName + "]";
    }
    
    
}
