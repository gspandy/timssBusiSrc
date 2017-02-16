package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 工作票间断与转移
 * @description: {desc}
 * @company: gdyd
 * @className: PtwWaitRestore.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwWaitRestore implements Serializable{

    private static final long serialVersionUID = -139715333696335474L;
    private int id;
    private int wtId;
    private Date witTime;
    private String witWpic;
    private String witWpicNo;
    private String witWl;
    private String witWlNo;
    private Date resTime;
    private String resWpic;
    private String resWpicNo;
    private String resWl;
    private String resWlNo;
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
     * @return 工作票收回_时间
     */
    public Date getWitTime() {
        return witTime;
    }
    /**
     * @param 工作票收回_时间
     */
    public void setWitTime(Date witTime) {
        this.witTime = witTime;
    }
    /**
     * @return 工作票收回_工作负责人
     */
    public String getWitWpic() {
        return witWpic;
    }
    /**
     * @param 工作票收回_工作负责人
     */
    public void setWitWpic(String witWpic) {
        this.witWpic = witWpic;
    }
    /**
     * @return 工作票收回_工作许可人
     */
    public String getWitWl() {
        return witWl;
    }
    /**
     * @param 工作票收回_工作许可人
     */
    public void setWitWl(String witWl) {
        this.witWl = witWl;
    }
    /**
     * @return 重新开工_开工日期
     */
    public Date getResTime() {
        return resTime;
    }
    /**
     * @param 重新开工_开工日期
     */
    public void setResTime(Date resTime) {
        this.resTime = resTime;
    }
    /**
     * @return 重新开工_工作负责人
     */
    public String getResWpic() {
        return resWpic;
    }
    /**
     * @param 重新开工_工作负责人
     */
    public void setResWpic(String resWpic) {
        this.resWpic = resWpic;
    }
    /**
     * @return 重新开工_工作许可人
     */
    public String getResWl() {
        return resWl;
    }
    /**
     * @param 重新开工_工作许可人
     */
    public void setResWl(String resWl) {
        this.resWl = resWl;
    }
    
    
    public String getWitWpicNo() {
        return witWpicNo;
    }
    public void setWitWpicNo(String witWpicNo) {
        this.witWpicNo = witWpicNo;
    }
    public String getWitWlNo() {
        return witWlNo;
    }
    public void setWitWlNo(String witWlNo) {
        this.witWlNo = witWlNo;
    }
    public String getResWpicNo() {
        return resWpicNo;
    }
    public void setResWpicNo(String resWpicNo) {
        this.resWpicNo = resWpicNo;
    }
    public String getResWlNo() {
        return resWlNo;
    }
    public void setResWlNo(String resWlNo) {
        this.resWlNo = resWlNo;
    }
    public PtwWaitRestore() {
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
        PtwWaitRestore other = (PtwWaitRestore) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    
    
    

}
