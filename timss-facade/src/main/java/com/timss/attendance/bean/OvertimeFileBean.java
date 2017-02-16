package com.timss.attendance.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 加班申请附件
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeFileBean.java
 * @author: fengzt
 * @createDate: 2014年9月10日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class OvertimeFileBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 6717146110518043995L;

    /**
     * 文件ID
     */
    private String fileId;
    
    /**
     * 加班申请ID
     */
    private int overtimeId;

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public int getOvertimeId() {
        return overtimeId;
    }

    public void setOvertimeId(int overtimeId) {
        this.overtimeId = overtimeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((fileId == null) ? 0 : fileId.hashCode());
        result = prime * result + overtimeId;
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
        OvertimeFileBean other = (OvertimeFileBean) obj;
        if ( fileId == null ) {
            if ( other.fileId != null )
                return false;
        } else if ( !fileId.equals( other.fileId ) )
            return false;
        if ( overtimeId != other.overtimeId )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "OvertimeFileBean [fileId=" + fileId + ", overtimeId=" + overtimeId + "]";
    }
    
    
}
