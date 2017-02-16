package com.timss.ptw.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 隔离证和工作票的隔离方法表
 * @description: {desc}
 * @company: gdyd
 * @className: PtwIsolationMethodBean.java
 * @author: fengzt
 * @createDate: 2014年10月31日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwIsolationMethodBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -8891005417084826029L;
    
    /**
     * id
     */
    private int id;
    
    /**
     * 工作票_id
     */
    private Integer wtId;
    
    /**
     * 隔离证_id
     */
    private Integer islId;
    
    /**
     * 隔离点方法Id
     */
    private int pointMethodId;
    
    /**
     * 接地线编号
     */
    private String elecFloorNo;
    
    /**
     * 类型
     */
    private int safeType;
    
    /**
     * 顺序
     */
    private int safeOrder;
    
    /**
     * 执行人NO
     */
    private String executerNo;
    
    /**
     * 执行人
     */
    private String executer;
    
    /**
     * 解除人
     */
    private String remover;
    
    /**
     * 解除人NO
     */
    private String removerNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public Integer getWtId() {
        return wtId;
    }

    public void setWtId(Integer wtId) {
        this.wtId = wtId;
    }

    public Integer getIslId() {
        return islId;
    }

    public void setIslId(Integer islId) {
        this.islId = islId;
    }

    public int getPointMethodId() {
        return pointMethodId;
    }

    public void setPointMethodId(int pointMethodId) {
        this.pointMethodId = pointMethodId;
    }

    public String getElecFloorNo() {
        return elecFloorNo;
    }

    public void setElecFloorNo(String elecFloorNo) {
        this.elecFloorNo = elecFloorNo;
    }

    public int getSafeType() {
        return safeType;
    }

    public void setSafeType(int safeType) {
        this.safeType = safeType;
    }

    public int getSafeOrder() {
        return safeOrder;
    }

    public void setSafeOrder(int safeOrder) {
        this.safeOrder = safeOrder;
    }

    public String getExecuterNo() {
        return executerNo;
    }

    public void setExecuterNo(String executerNo) {
        this.executerNo = executerNo;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public String getRemover() {
        return remover;
    }

    public void setRemover(String remover) {
        this.remover = remover;
    }

    public String getRemoverNo() {
        return removerNo;
    }

    public void setRemoverNo(String removerNo) {
        this.removerNo = removerNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((elecFloorNo == null) ? 0 : elecFloorNo.hashCode());
        result = prime * result + ((executer == null) ? 0 : executer.hashCode());
        result = prime * result + ((executerNo == null) ? 0 : executerNo.hashCode());
        result = prime * result + id;
        result = prime * result + ((islId == null) ? 0 : islId.hashCode());
        result = prime * result + pointMethodId;
        result = prime * result + ((remover == null) ? 0 : remover.hashCode());
        result = prime * result + ((removerNo == null) ? 0 : removerNo.hashCode());
        result = prime * result + safeOrder;
        result = prime * result + safeType;
        result = prime * result + ((wtId == null) ? 0 : wtId.hashCode());
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
        PtwIsolationMethodBean other = (PtwIsolationMethodBean) obj;
        if ( elecFloorNo == null ) {
            if ( other.elecFloorNo != null )
                return false;
        } else if ( !elecFloorNo.equals( other.elecFloorNo ) )
            return false;
        if ( executer == null ) {
            if ( other.executer != null )
                return false;
        } else if ( !executer.equals( other.executer ) )
            return false;
        if ( executerNo == null ) {
            if ( other.executerNo != null )
                return false;
        } else if ( !executerNo.equals( other.executerNo ) )
            return false;
        if ( id != other.id )
            return false;
        if ( islId == null ) {
            if ( other.islId != null )
                return false;
        } else if ( !islId.equals( other.islId ) )
            return false;
        if ( pointMethodId != other.pointMethodId )
            return false;
        if ( remover == null ) {
            if ( other.remover != null )
                return false;
        } else if ( !remover.equals( other.remover ) )
            return false;
        if ( removerNo == null ) {
            if ( other.removerNo != null )
                return false;
        } else if ( !removerNo.equals( other.removerNo ) )
            return false;
        if ( safeOrder != other.safeOrder )
            return false;
        if ( safeType != other.safeType )
            return false;
        if ( wtId == null ) {
            if ( other.wtId != null )
                return false;
        } else if ( !wtId.equals( other.wtId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PtwIsolationMethodBean [id=" + id + ", wtId=" + wtId + ", islId=" + islId + ", pointMethodId="
                + pointMethodId + ", elecFloorNo=" + elecFloorNo + ", safeType=" + safeType + ", safeOrder="
                + safeOrder + ", executerNo=" + executerNo + ", executer=" + executer + ", remover=" + remover
                + ", removerNo=" + removerNo + "]";
    }
    
    
}
