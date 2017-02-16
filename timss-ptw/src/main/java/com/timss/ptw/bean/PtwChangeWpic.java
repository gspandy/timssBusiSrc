package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;
/**
 * 
 * @title: 工作票负责人变更信息
 * @description: {desc}
 * @company: gdyd
 * @className: PtwChangeWpic.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwChangeWpic implements Serializable{

    private static final long serialVersionUID = -6719402789874867956L;
    private int id;
    private int wtId;
    private String chaOldWpic;
    private String chaOldWpicNo;
    private String chaWl;
    private String chaWlNo;
    private String chaNewWpic;
    private String chaNewWpicNo;
    private Date chaSignTime;
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
     * @return 工作票ID
     */
    public int getWtId() {
        return wtId;
    }
    /**
     * @param 工作票ID
     */
    public void setWtId(int wtId) {
        this.wtId = wtId;
    }
    /**
     * @return 变更_原工作负责人
     */
    public String getChaOldWpic() {
        return chaOldWpic;
    }
    /**
     * @param 变更_原工作负责人
     */
    public void setChaOldWpic(String chaOldWpic) {
        this.chaOldWpic = chaOldWpic;
    }
    /**
     * @return 变更_工作许可人
     */
    public String getChaWl() {
        return chaWl;
    }
    /**
     * @param 变更_工作许可人
     */
    public void setChaWl(String chaWl) {
        this.chaWl = chaWl;
    }
    /**
     * @return 变更_变更后工作负责人
     */
    public String getChaNewWpic() {
        return chaNewWpic;
    }
    /**
     * @param 变更_变更后工作负责人
     */
    public void setChaNewWpic(String chaNewWpic) {
        this.chaNewWpic = chaNewWpic;
    }
    /**
     * @return 变更_批准时间
     */
    public Date getChaSignTime() {
        return chaSignTime;
    }
    /**
     * @param 变更_批准时间
     */
    public void setChaSignTime(Date chaSignTime) {
        this.chaSignTime = chaSignTime;
    }
    
    public String getChaOldWpicNo() {
        return chaOldWpicNo;
    }
    public void setChaOldWpicNo(String chaOldWpicNo) {
        this.chaOldWpicNo = chaOldWpicNo;
    }
    public String getChaWlNo() {
        return chaWlNo;
    }
    public void setChaWlNo(String chaWlNo) {
        this.chaWlNo = chaWlNo;
    }
    public String getChaNewWpicNo() {
        return chaNewWpicNo;
    }
    public void setChaNewWpicNo(String chaNewWpicNo) {
        this.chaNewWpicNo = chaNewWpicNo;
    }
    public PtwChangeWpic() {
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
        PtwChangeWpic other = (PtwChangeWpic) obj;
        if ( id != other.id )
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "PtwChangeWpic [id=" + id + ", wtId=" + wtId + ", chaOldWpic=" + chaOldWpic + ", chaWl=" + chaWl
                + ", chaNewWpic=" + chaNewWpic + ", chaSignTime=" + chaSignTime + "]";
    }
    

}
