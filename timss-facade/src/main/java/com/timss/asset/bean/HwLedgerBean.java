package com.timss.asset.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.EntityID;
import com.yudean.mvc.bean.ItcMvcBean;

public class HwLedgerBean extends ItcMvcBean {
    private static final long serialVersionUID = -5648816713305159843L;
    /**
     * 硬件台账的id
     */
    @AutoGen("AST_HW_SEQ")
    @EntityID
    private String hwId;
    /**
     * 硬件台账的名称
     */
    private String hwName;
    /**
     * 硬件台账树的父节点
     */
    private String parentId;
    /**
     * 硬件台账的类型，从枚举中选择物理地点、机房、机柜、服务器、虚机
     */
    private String hwType;

    private String isRoot;

    /**
     * 是否有孩子节点
     */
    private boolean withChild;

    @Override
    public String toString() {
        return "HwLedgerBean [hwId=" + hwId + ", hwName=" + hwName + ", parentId=" + parentId + ", hwType=" + hwType
                + ", isRoot=" + isRoot + ", withChild=" + withChild + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hwId == null) ? 0 : hwId.hashCode());
        result = prime * result + ((hwName == null) ? 0 : hwName.hashCode());
        result = prime * result + ((hwType == null) ? 0 : hwType.hashCode());
        result = prime * result + ((isRoot == null) ? 0 : isRoot.hashCode());
        result = prime * result + ((parentId == null) ? 0 : parentId.hashCode());
        result = prime * result + (withChild ? 1231 : 1237);
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
        HwLedgerBean other = (HwLedgerBean) obj;
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
        if ( isRoot == null ) {
            if ( other.isRoot != null )
                return false;
        } else if ( !isRoot.equals( other.isRoot ) )
            return false;
        if ( parentId == null ) {
            if ( other.parentId != null )
                return false;
        } else if ( !parentId.equals( other.parentId ) )
            return false;
        if ( withChild != other.withChild )
            return false;
        return true;
    }

    public boolean isWithChild() {
        return withChild;
    }

    public void setWithChild(boolean withChild) {
        this.withChild = withChild;
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

    public String getHwType() {
        return hwType;
    }

    public void setHwType(String hwType) {
        this.hwType = hwType;
    }

    public String getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(String isRoot) {
        this.isRoot = isRoot;
    }

}
