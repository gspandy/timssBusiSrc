package com.timss.attendance.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 文件附件
 * @description: {desc}
 * @company: gdyd
 * @className: LeaveFileBean.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class LeaveFileBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -7408690457824519675L;

    
    /**
     * 文件Id
     */
    private String fileId;
    
    /**
     * 请假申请表ID
     */
    private int leaveId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(int leaveId) {
        this.leaveId = leaveId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + leaveId;
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
        LeaveFileBean other = (LeaveFileBean) obj;
        if ( fileId == null ) {
            if ( other.fileId != null )
                return false;
        } else if ( !fileId.equals( other.fileId ) )
            return false;
        if ( leaveId != other.leaveId )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LeaveFileBean [fileId=" + fileId + ", leaveId=" + leaveId + "]";
    }
    
    
}
