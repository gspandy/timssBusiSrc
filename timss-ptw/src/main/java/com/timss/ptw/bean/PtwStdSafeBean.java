package com.timss.ptw.bean;

import java.io.Serializable;

/**
 * 
 * @title: 标准工作票隔离方法 表 
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStdSafeBean.java
 * @author: fengzt
 * @createDate: 2015年7月17日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwStdSafeBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9000167144198786565L;
    
    /**
     * id
     */
    private String id;
    
    /**
     * 标准工作票_id
     */
    private String wtId;
    
    /**
     * 措施
     */
    private String safeContent;
    
    /**
     * 类型
     */
    private String safeType;
    
    /**
     * 顺序
     */
    private int safeOrder;
    
    /**
     * 执行人
     */
    private String executer;
    
    /**
     * 执行人NO
     */
    private String executerNo;
    
    /**
     * 解除人
     */
    private String remover;
    
    /**
     * 解除人NO
     */
    private String removerNo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWtId() {
        return wtId;
    }

    public void setWtId(String wtId) {
        this.wtId = wtId;
    }

    public String getSafeContent() {
        return safeContent;
    }

    public void setSafeContent(String safeContent) {
        this.safeContent = safeContent;
    }

    public String getSafeType() {
        return safeType;
    }

    public void setSafeType(String safeType) {
        this.safeType = safeType;
    }

    public int getSafeOrder() {
        return safeOrder;
    }

    public void setSafeOrder(int safeOrder) {
        this.safeOrder = safeOrder;
    }

    public String getExecuter() {
        return executer;
    }

    public void setExecuter(String executer) {
        this.executer = executer;
    }

    public String getExecuterNo() {
        return executerNo;
    }

    public void setExecuterNo(String executerNo) {
        this.executerNo = executerNo;
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
        result = prime * result + ((executer == null) ? 0 : executer.hashCode());
        result = prime * result + ((executerNo == null) ? 0 : executerNo.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((remover == null) ? 0 : remover.hashCode());
        result = prime * result + ((removerNo == null) ? 0 : removerNo.hashCode());
        result = prime * result + ((safeContent == null) ? 0 : safeContent.hashCode());
        result = prime * result + safeOrder;
        result = prime * result + ((safeType == null) ? 0 : safeType.hashCode());
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
        PtwStdSafeBean other = (PtwStdSafeBean) obj;
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
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
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
        if ( safeContent == null ) {
            if ( other.safeContent != null )
                return false;
        } else if ( !safeContent.equals( other.safeContent ) )
            return false;
        if ( safeOrder != other.safeOrder )
            return false;
        if ( safeType == null ) {
            if ( other.safeType != null )
                return false;
        } else if ( !safeType.equals( other.safeType ) )
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
        return "PtwStdSafeBean [id=" + id + ", wtId=" + wtId + ", safeContent=" + safeContent + ", safeType="
                + safeType + ", safeOrder=" + safeOrder + ", executer=" + executer + ", executerNo=" + executerNo
                + ", remover=" + remover + ", removerNo=" + removerNo + "]";
    }

}
