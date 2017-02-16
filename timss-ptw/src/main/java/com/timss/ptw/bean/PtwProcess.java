package com.timss.ptw.bean;

import java.io.Serializable;

/**
 * 
 * @title: 工作票-流程信息对应关系表
 * @description: {desc}
 * @company: gdyd
 * @className: PtwTask.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwProcess implements Serializable{

    private static final long serialVersionUID = -9129038244781209832L;
    
    private int id;
    private int wtId;
    private String processId;
    private int wtStatus;
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
     * @return 工作票Id
     */
    public int getWtId() {
        return wtId;
    }
    /**
     * @param 工作票Id
     */
    public void setWtId(int wtId) {
        this.wtId = wtId;
    }
    
    
    public String getProcessId() {
        return processId;
    }
    public void setProcessId(String processId) {
        this.processId = processId;
    }
    public int getWtStatus() {
        return wtStatus;
    }
    public void setWtStatus(int wtStatus) {
        this.wtStatus = wtStatus;
    }
    public PtwProcess() {
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
        PtwProcess other = (PtwProcess) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    
}
