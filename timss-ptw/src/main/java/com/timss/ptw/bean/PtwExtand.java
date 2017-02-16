package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 工作票延期信息
 * @description: {desc}
 * @company: gdyd
 * @className: PtwExtand.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwExtand implements Serializable {
    
    private static final long serialVersionUID = 3212092625571666767L;
    
    private int id;
    private int wtId;
    private Date extApprTime;
    private String extWpic;
    private String extWpicNo;
    private String extWl;
    private String extWlNo;
    private Date extSignTime;
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
     * @return 延期_批准延期到
     */
    public Date getExtApprTime() {
        return extApprTime;
    }
    /**
     * @param 延期_批准延期到
     */
    public void setExtApprTime(Date extApprTime) {
        this.extApprTime = extApprTime;
    }
    /**
     * @return 延期_工作负责人
     */
    public String getExtWpic() {
        return extWpic;
    }
    /**
     * @param 延期_工作负责人
     */
    public void setExtWpic(String extWpic) {
        this.extWpic = extWpic;
    }
    /**
     * @return 延期_工作许可人
     */
    public String getExtWl() {
        return extWl;
    }
    /**
     * @param 延期_工作许可人
     */
    public void setExtWl(String extWl) {
        this.extWl = extWl;
    }
    /**
     * @return 延期_批准时间
     */
    public Date getExtSignTime() {
        return extSignTime;
    }
    /**
     * @param 延期_批准时间
     */
    public void setExtSignTime(Date extSignTime) {
        this.extSignTime = extSignTime;
    }
    public String getExtWpicNo() {
        return extWpicNo;
    }
    public void setExtWpicNo(String extWpicNo) {
        this.extWpicNo = extWpicNo;
    }
    public String getExtWlNo() {
        return extWlNo;
    }
    public void setExtWlNo(String extWlNo) {
        this.extWlNo = extWlNo;
    }
    public PtwExtand() {
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
        PtwExtand other = (PtwExtand) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    
    
}
