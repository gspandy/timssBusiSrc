package com.timss.ptw.vo;

import java.io.Serializable;

/**
 * 
 * @title: 隔离证-添加弹出框VO
 * @description: {desc}
 * @company: gdyd
 * @className: IsMethodPointVo.java
 * @author: fengzt
 * @createDate: 2014年10月27日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class IsMethodPointVo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 2226195742718924767L;

    /**
     * 隔离点与隔离方法关联表 id
     */
    private int id;
    
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((methodName == null) ? 0 : methodName.hashCode());
        result = prime * result + ((pointName == null) ? 0 : pointName.hashCode());
        result = prime * result + ((pointNo == null) ? 0 : pointNo.hashCode());
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
        IsMethodPointVo other = (IsMethodPointVo) obj;
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
        if ( stdMethodId == null ) {
            if ( other.stdMethodId != null )
                return false;
        } else if ( !stdMethodId.equals( other.stdMethodId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "IsMethodPointVo [id=" + id + ", stdMethodId=" + stdMethodId + ", methodName=" + methodName
                + ", pointNo=" + pointNo + ", pointName=" + pointName + "]";
    }
    
    
}
