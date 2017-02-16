package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 工作票备注中增加的工作任务信息
 * @description: {desc}
 * @company: gdyd
 * @className: PtwRemarkTask.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwRemarkTask implements Serializable{
    
    private static final long serialVersionUID = 5394489187339362335L;
    
    private int id;
    private int wtId;
    private String remarkWorkContent;
    private String remarkWpic;
    private String remarkWpicNo;
    private String remarkWl;
    private String remarkWlNo;
    private Date remarkSignTime;
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
     * @return 备注_工作内容
     */
    public String getRemarkWorkContent() {
        return remarkWorkContent;
    }
    /**
     * @param 备注_工作内容
     */
    public void setRemarkWorkContent(String remarkWorkContent) {
        this.remarkWorkContent = remarkWorkContent;
    }
    /**
     * @return 备注_工作负责人
     */
    public String getRemarkWpic() {
        return remarkWpic;
    }
    /**
     * @param 备注_工作负责人
     */
    public void setRemarkWpic(String remarkWpic) {
        this.remarkWpic = remarkWpic;
    }
    /**
     * @return 备注_工作许可人
     */
    public String getRemarkWl() {
        return remarkWl;
    }
    /**
     * @param 备注_工作许可人
     */
    public void setRemarkWl(String remarkWl) {
        this.remarkWl = remarkWl;
    }
    /**
     * @return 备注_许可时间
     */
    public Date getRemarkSignTime() {
        return remarkSignTime;
    }
    /**
     * @param 备注_许可时间
     */
    public void setRemarkSignTime(Date remarkSignTime) {
        this.remarkSignTime = remarkSignTime;
    }
    
    
    public String getRemarkWpicNo() {
        return remarkWpicNo;
    }
    public void setRemarkWpicNo(String remarkWpicNo) {
        this.remarkWpicNo = remarkWpicNo;
    }
    public String getRemarkWlNo() {
        return remarkWlNo;
    }
    public void setRemarkWlNo(String remarkWlNo) {
        this.remarkWlNo = remarkWlNo;
    }
    public PtwRemarkTask() {
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
        PtwRemarkTask other = (PtwRemarkTask) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    
    
}
