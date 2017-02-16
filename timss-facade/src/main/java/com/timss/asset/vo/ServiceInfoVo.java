package com.timss.asset.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 服务信息VO
 * @description: {desc}
 * @company: gdyd
 * @className: ServiceInfoVo.java
 * @author: fengzt
 * @createDate: 2014年11月25日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class ServiceInfoVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 7642095029963709332L;

    /** 投入使用时间
     * 
     * */
    private Date toUseTime;
    /** 过保时间
     * 
     * */
    private Date elapsedTime;
    /** 维护
     * 
     * */
    private String maintain;
    /** 供应商
     * 
     * */
    private String supplier;
    /** 维修记录
     * 
     * */
    private String repairRecard;
    
    /** 资产编号
     * 
     * */
    private String assetCode;
    
    /** 
     * 所属单位
     * */
    private String ownOrg;
    /** 
     * 使用状态
     * */
    private String status;
    
    /**
     * 运维负责人ID
     */
    private String principal;
    
    /**
     * 运维负责人名称
     */
    private String principalName;
    
    
    
    public String getPrincipal() {
        return principal;
    }
    public void setPrincipal(String principal) {
        this.principal = principal;
    }
    public String getPrincipalName() {
        return principalName;
    }
    public void setPrincipalName(String principalName) {
        this.principalName = principalName;
    }
    public String getOwnOrg() {
        return ownOrg;
    }
    public void setOwnOrg(String ownOrg) {
        this.ownOrg = ownOrg;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }
    
    
    public Date getToUseTime() {
        return toUseTime;
    }
    public void setToUseTime(Date toUseTime) {
        this.toUseTime = toUseTime;
    }
    public Date getElapsedTime() {
        return elapsedTime;
    }
    public void setElapsedTime(Date elapsedTime) {
        this.elapsedTime = elapsedTime;
    }
    public String getMaintain() {
        return maintain;
    }
    public void setMaintain(String maintain) {
        this.maintain = maintain;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getRepairRecard() {
        return repairRecard;
    }
    public void setRepairRecard(String repairRecard) {
        this.repairRecard = repairRecard;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((assetCode == null) ? 0 : assetCode.hashCode());
        result = prime * result + ((elapsedTime == null) ? 0 : elapsedTime.hashCode());
        result = prime * result + ((maintain == null) ? 0 : maintain.hashCode());
        result = prime * result + ((ownOrg == null) ? 0 : ownOrg.hashCode());
        result = prime * result + ((principal == null) ? 0 : principal.hashCode());
        result = prime * result + ((principalName == null) ? 0 : principalName.hashCode());
        result = prime * result + ((repairRecard == null) ? 0 : repairRecard.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((supplier == null) ? 0 : supplier.hashCode());
        result = prime * result + ((toUseTime == null) ? 0 : toUseTime.hashCode());
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
        ServiceInfoVo other = (ServiceInfoVo) obj;
        if ( assetCode == null ) {
            if ( other.assetCode != null )
                return false;
        } else if ( !assetCode.equals( other.assetCode ) )
            return false;
        if ( elapsedTime == null ) {
            if ( other.elapsedTime != null )
                return false;
        } else if ( !elapsedTime.equals( other.elapsedTime ) )
            return false;
        if ( maintain == null ) {
            if ( other.maintain != null )
                return false;
        } else if ( !maintain.equals( other.maintain ) )
            return false;
        if ( ownOrg == null ) {
            if ( other.ownOrg != null )
                return false;
        } else if ( !ownOrg.equals( other.ownOrg ) )
            return false;
        if ( principal == null ) {
            if ( other.principal != null )
                return false;
        } else if ( !principal.equals( other.principal ) )
            return false;
        if ( principalName == null ) {
            if ( other.principalName != null )
                return false;
        } else if ( !principalName.equals( other.principalName ) )
            return false;
        if ( repairRecard == null ) {
            if ( other.repairRecard != null )
                return false;
        } else if ( !repairRecard.equals( other.repairRecard ) )
            return false;
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
            return false;
        if ( supplier == null ) {
            if ( other.supplier != null )
                return false;
        } else if ( !supplier.equals( other.supplier ) )
            return false;
        if ( toUseTime == null ) {
            if ( other.toUseTime != null )
                return false;
        } else if ( !toUseTime.equals( other.toUseTime ) )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "ServiceInfoVo [toUseTime=" + toUseTime + ", elapsedTime=" + elapsedTime + ", maintain=" + maintain
                + ", supplier=" + supplier + ", repairRecard=" + repairRecard + ", assetCode=" + assetCode
                + ", ownOrg=" + ownOrg + ", status=" + status + ", principal=" + principal + ", principalName="
                + principalName + "]";
    }
    
    
    
}
