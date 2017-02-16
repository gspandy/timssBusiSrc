package com.timss.ptw.bean;

import java.io.Serializable;

/**
 * 
 * @title: 工作票安全措施
 * @description: {desc}
 * @company: gdyd
 * @className: PtwSafe.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwSafe implements Serializable{

    private static final long serialVersionUID = -8845883580581484913L;
    
    private int id;
    private int wtId;
    private String safeContent;
    private int safeType;
    private int safeOrder;
    private String executer;
    private String executerNo;
    private String remover;
    private String removerNo;
    private String remarks;  //如果终结工作票时安全措施不解除，需要补充的备注
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
     * @return 工作票_id
     */
    public int getWtId() {
        return wtId;
    }
    /**
     * @param 工作票_id
     */
    public void setWtId(int wtId) {
        this.wtId = wtId;
    }
    /**
     * @return 措施
     */
    public String getSafeContent() {
        return safeContent;
    }
    /**
     * @param 措施
     */
    public void setSafeContent(String safeContent) {
        this.safeContent = safeContent;
    }
    /**
     * @return 类型
     */
    public int getSafeType() {
        return safeType;
    }
    /**
     * @param 类型
     */
    public void setSafeType(int safeType) {
        this.safeType = safeType;
    }
    /**
     * @return 顺序
     */
    public int getSafeOrder() {
        return safeOrder;
    }
    /**
     * @param 顺序
     */
    public void setSafeOrder(int safeOrder) {
        this.safeOrder = safeOrder;
    }
    /**
     * @return 执行人
     */
    public String getExecuter() {
        return executer;
    }
    /**
     * @param 执行人
     */
    public void setExecuter(String executer) {
        this.executer = executer;
    }
    /**
     * @return 解除人
     */
    public String getRemover() {
        return remover;
    }
    /**
     * @param 解除人
     */
    public void setRemover(String remover) {
        this.remover = remover;
    }
    public String getExecuterNo() {
        return executerNo;
    }
    public void setExecuterNo(String executerNo) {
        this.executerNo = executerNo;
    }
    public String getRemoverNo() {
        return removerNo;
    }
    public void setRemoverNo(String removerNo) {
        this.removerNo = removerNo;
    }
    
    public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public PtwSafe() {
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
        PtwSafe other = (PtwSafe) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "PtwSafe [id=" + id + ", wtId=" + wtId + ", safeContent=" + safeContent + ", safeType=" + safeType
                + ", safeOrder=" + safeOrder + ", executer=" + executer + ", executerNo=" + executerNo + ", remover="
                + remover + ", removerNo=" + removerNo + "]";
    }
    
        
}
