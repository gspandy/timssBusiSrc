package com.timss.ptw.vo;

import java.io.Serializable;

/**
 * 
 * @title: 隔离证 新建页面vo
 * @description: {desc}
 * @company: gdyd
 * @className: IsolationVo.java
 * @author: fengzt
 * @createDate: 2014年11月3日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class IsolationVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5001295621410957412L;

    /**
     * 隔离点与隔离方法关联表 id
     */
    private int id;
    
    /**
     * 顺序
     */
    private int safeOrder;
    
    /**
     * 隔离方法Id
     */
    private String stdMethodId ;
    
    /**
     * 隔离方法名字
     */
    private String methodName;
    
    /**
     * 隔离点ID
     */
    private String pointNo;
    
    /**
     * 隔离点名字
     */
    private String pointName;
    
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
    
    /**
     * 接地线编号
     */
    private String elecFloorNo;
    
    /**
     * 类型 （应接地线，safeType = 4）
     */
    private int safeType;

    
    public int getSafeType() {
        return safeType;
    }

    public void setSafeType(int safeType) {
        this.safeType = safeType;
    }

    public String getElecFloorNo() {
        return elecFloorNo;
    }

    public void setElecFloorNo(String elecFloorNo) {
        this.elecFloorNo = elecFloorNo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSafeOrder() {
        return safeOrder;
    }

    public void setSafeOrder(int safeOrder) {
        this.safeOrder = safeOrder;
    }

    public String getStdMethodId() {
        return stdMethodId;
    }

    public void setStdMethodId(String stdMethodId) {
        this.stdMethodId = stdMethodId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getPointNo() {
        return pointNo;
    }

    public void setPointNo(String pointNo) {
        this.pointNo = pointNo;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
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
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((pointName == null) ? 0 : pointName.hashCode());
        result = prime * result + ((pointNo == null) ? 0 : pointNo.hashCode());
        result = prime * result + ((remover == null) ? 0 : remover.hashCode());
        result = prime * result + ((removerNo == null) ? 0 : removerNo.hashCode());
        result = prime * result + safeOrder;
        result = prime * result + safeType;
        result = prime * result + ((stdMethodId == null) ? 0 : stdMethodId.hashCode());
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
        IsolationVo other = (IsolationVo) obj;
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
        if ( methodName == null ) {
            if ( other.methodName != null )
                return false;
        } else if ( !methodName.equals( other.methodName ) )
            return false;
        if ( pointName == null ) {
            if ( other.pointName != null )
                return false;
        } else if ( !pointName.equals( other.pointName ) )
            return false;
        if ( pointNo == null ) {
            if ( other.pointNo != null )
                return false;
        } else if ( !pointNo.equals( other.pointNo ) )
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
        if ( stdMethodId == null ) {
            if ( other.stdMethodId != null )
                return false;
        } else if ( !stdMethodId.equals( other.stdMethodId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "IsolationVo [id=" + id + ", safeOrder=" + safeOrder + ", stdMethodId=" + stdMethodId + ", methodName="
                + methodName + ", pointNo=" + pointNo + ", pointName=" + pointName + ", executerNo=" + executerNo
                + ", executer=" + executer + ", remover=" + remover + ", removerNo=" + removerNo + ", elecFloorNo="
                + elecFloorNo + ", safeType=" + safeType + "]";
    }
    
    
    
}
