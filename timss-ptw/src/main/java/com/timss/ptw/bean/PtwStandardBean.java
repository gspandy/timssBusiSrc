package com.timss.ptw.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 标准工作票主表 对应 表PTW_STD_INFO
 * @description: {desc}
 * @company: gdyd
 * @className: PtwStandardBean.java
 * @author: fengzt
 * @createDate: 2015年7月17日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class PtwStandardBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -3274452330342745213L;
    
    /**
     * id
     */
    private String id;
    
    @AutoGen("PTW_STD_NO_SEQ")
    private String sheetNo;
    
    /**
     * 工作票类型_id
     */
    private String wtTypeId;
    
    /**
     * 工作票编号
     */
    private String wtNo;
    /**
     * 设备编码
     */
    private String eqNo;
    
    /**
     * 设备名称
     */
    private String eqName;
    
    /**
     * 工作内容
     */
    private String workContent;
    
    /**
     * 工作地点
     */
    private String workPlace;
    
    /**
     * 历史操作票ID
     */
//    private String parentWtId;
    
    /**
     * 版本
     */
    private int version;
    
    /**
     * 是否年审
     */
    private int isCheck;
    
    /**
     * 年审日期
     */
    private Date checkDate;
    
    /**
     * 生效时间
     */
    private Date beginTime;
    
    /**
     * 失效时间
     */
    private Date endTime;
    
    /**
     * 流程Id
     */
    private String instantId;
    
    /**
     * 流程状态
     */
    private String flowStatus;
    
    /**
     * 是否过期，1为可用，0为过期
     */
    private int isExpire;
    
    /**
     * 是否作废，1为可用，0为作废
     */
    private int isCancel;
    
    /**
     * 是否删除，1为可用，0为删除
     */
    private int inUse;
    
    /**
     * 创建人名称
     */
    private String createUserName;
    
    /**
     * 最近更新人名称
     */
    private String modifyUserName;
    
    /**
     * 批准人名称
     */
    private String approveUserName;
    
    /**
     * 批准人
     */
    private String approveUser;
    
    /**
     * 批准时间
     */
    private Date approveDate;
    
    /**
     * 作废_时间
     */
    private Date cancelDate;
    
    /**
     * 作废_作废人 工号
     */
    private String cancelUser;
    
    /**
     * 作废_作废人
     */
    private String cancelUserName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSheetNo() {
        return sheetNo;
    }

    public void setSheetNo(String sheetNo) {
        this.sheetNo = sheetNo;
    }

    public String getWtTypeId() {
        return wtTypeId;
    }

    public void setWtTypeId(String wtTypeId) {
        this.wtTypeId = wtTypeId;
    }

    public String getWtNo() {
        return wtNo;
    }

    public void setWtNo(String wtNo) {
        this.wtNo = wtNo;
    }

    public String getEqNo() {
        return eqNo;
    }

    public void setEqNo(String eqNo) {
        this.eqNo = eqNo;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEqName() {
        return eqName;
    }

    public void setEqName(String eqName) {
        this.eqName = eqName;
    }

    public String getWorkContent() {
        return workContent;
    }

    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }

//    public String getParentWtId() {
//        return parentWtId;
//    }
//
//    public void setParentWtId(String parentWtId) {
//        this.parentWtId = parentWtId;
//    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public Date getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

   

    public String getInstantId() {
        return instantId;
    }

    public void setInstantId(String instantId) {
        this.instantId = instantId;
    }

    public String getFlowStatus() {
        return flowStatus;
    }

    public void setFlowStatus(String flowStatus) {
        this.flowStatus = flowStatus;
    }

    public int getIsExpire() {
        return isExpire;
    }

    public void setIsExpire(int isExpire) {
        this.isExpire = isExpire;
    }

    public int getIsCancel() {
        return isCancel;
    }

    public void setIsCancel(int isCancel) {
        this.isCancel = isCancel;
    }

    public int getInUse() {
        return inUse;
    }

    public void setInUse(int inUse) {
        this.inUse = inUse;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getModifyUserName() {
        return modifyUserName;
    }

    public void setModifyUserName(String modifyUserName) {
        this.modifyUserName = modifyUserName;
    }

    public String getApproveUserName() {
        return approveUserName;
    }

    public void setApproveUserName(String approveUserName) {
        this.approveUserName = approveUserName;
    }

    public String getApproveUser() {
        return approveUser;
    }

    public void setApproveUser(String approveUser) {
        this.approveUser = approveUser;
    }

    public Date getApproveDate() {
        return approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCancelUser() {
        return cancelUser;
    }

    public void setCancelUser(String cancelUser) {
        this.cancelUser = cancelUser;
    }

    public String getCancelUserName() {
        return cancelUserName;
    }

    public void setCancelUserName(String cancelUserName) {
        this.cancelUserName = cancelUserName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((approveDate == null) ? 0 : approveDate.hashCode());
        result = prime * result + ((approveUser == null) ? 0 : approveUser.hashCode());
        result = prime * result + ((approveUserName == null) ? 0 : approveUserName.hashCode());
        result = prime * result + ((cancelDate == null) ? 0 : cancelDate.hashCode());
        result = prime * result + ((cancelUser == null) ? 0 : cancelUser.hashCode());
        result = prime * result + ((cancelUserName == null) ? 0 : cancelUserName.hashCode());
        result = prime * result + ((checkDate == null) ? 0 : checkDate.hashCode());
        result = prime * result + ((createUserName == null) ? 0 : createUserName.hashCode());
        result = prime * result + ((beginTime == null) ? 0 : beginTime.hashCode());
        result = prime * result + ((eqName == null) ? 0 : eqName.hashCode());
        result = prime * result + ((eqNo == null) ? 0 : eqNo.hashCode());
        result = prime * result + ((flowStatus == null) ? 0 : flowStatus.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + inUse;
        result = prime * result + ((instantId == null) ? 0 : instantId.hashCode());
        result = prime * result + isCancel;
        result = prime * result + isCheck;
        result = prime * result + isExpire;
        result = prime * result + ((modifyUserName == null) ? 0 : modifyUserName.hashCode());
        result = prime * result + version;
        result = prime * result + ((workContent == null) ? 0 : workContent.hashCode());
        result = prime * result + ((workPlace == null) ? 0 : workPlace.hashCode());
        result = prime * result + ((wtNo == null) ? 0 : wtNo.hashCode());
        result = prime * result + ((wtTypeId == null) ? 0 : wtTypeId.hashCode());
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
        PtwStandardBean other = (PtwStandardBean) obj;
        if ( approveDate == null ) {
            if ( other.approveDate != null )
                return false;
        } else if ( !approveDate.equals( other.approveDate ) )
            return false;
        if ( approveUser == null ) {
            if ( other.approveUser != null )
                return false;
        } else if ( !approveUser.equals( other.approveUser ) )
            return false;
        if ( approveUserName == null ) {
            if ( other.approveUserName != null )
                return false;
        } else if ( !approveUserName.equals( other.approveUserName ) )
            return false;
        if ( cancelDate == null ) {
            if ( other.cancelDate != null )
                return false;
        } else if ( !cancelDate.equals( other.cancelDate ) )
            return false;
        if ( cancelUser == null ) {
            if ( other.cancelUser != null )
                return false;
        } else if ( !cancelUser.equals( other.cancelUser ) )
            return false;
        if ( cancelUserName == null ) {
            if ( other.cancelUserName != null )
                return false;
        } else if ( !cancelUserName.equals( other.cancelUserName ) )
            return false;
        if ( checkDate == null ) {
            if ( other.checkDate != null )
                return false;
        } else if ( !checkDate.equals( other.checkDate ) )
            return false;
        if ( createUserName == null ) {
            if ( other.createUserName != null )
                return false;
        } else if ( !createUserName.equals( other.createUserName ) )
            return false;
        if ( beginTime == null ) {
            if ( other.beginTime != null )
                return false;
        } else if ( !beginTime.equals( other.beginTime ) )
            return false;
        if ( endTime == null ) {
            if ( other.endTime != null )
                return false;
        } else if ( !endTime.equals( other.endTime ) )
            return false;
        if ( eqName == null ) {
            if ( other.eqName != null )
                return false;
        } else if ( !eqName.equals( other.eqName ) )
            return false;
        if ( eqNo == null ) {
            if ( other.eqNo != null )
                return false;
        } else if ( !eqNo.equals( other.eqNo ) )
            return false;
        if ( flowStatus == null ) {
            if ( other.flowStatus != null )
                return false;
        } else if ( !flowStatus.equals( other.flowStatus ) )
            return false;
        if ( id == null ) {
            if ( other.id != null )
                return false;
        } else if ( !id.equals( other.id ) )
            return false;
        if ( inUse != other.inUse )
            return false;
        if ( instantId == null ) {
            if ( other.instantId != null )
                return false;
        } else if ( !instantId.equals( other.instantId ) )
            return false;
        if ( isCancel != other.isCancel )
            return false;
        if ( isCheck != other.isCheck )
            return false;
        if ( isExpire != other.isExpire )
            return false;
        if ( modifyUserName == null ) {
            if ( other.modifyUserName != null )
                return false;
        } else if ( !modifyUserName.equals( other.modifyUserName ) )
            return false;
        if ( version != other.version )
            return false;
        if ( workContent == null ) {
            if ( other.workContent != null )
                return false;
        } else if ( !workContent.equals( other.workContent ) )
            return false;
        if ( workPlace == null ) {
            if ( other.workPlace != null )
                return false;
        } else if ( !workPlace.equals( other.workPlace ) )
            return false;
        if ( wtNo == null ) {
            if ( other.wtNo != null )
                return false;
        } else if ( !wtNo.equals( other.wtNo ) )
            return false;
        if ( wtTypeId == null ) {
            if ( other.wtTypeId != null )
                return false;
        } else if ( !wtTypeId.equals( other.wtTypeId ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PtwStandardBean [id=" + id + ", wtTypeId=" + wtTypeId + ", wtNo=" + wtNo + ", eqNo=" + eqNo
                + ", eqName=" + eqName + ", workContent=" + workContent + ", workPlace=" + workPlace 
                + ", version=" + version + ", isCheck=" + isCheck + ", checkDate=" + checkDate
                 + ", instantId=" + instantId + ", flowStatus=" + flowStatus
                + ", isExpire=" + isExpire + ", isCancel=" + isCancel + ", inUse=" + inUse + ", createUserName="
                + createUserName + ", modifyUserName=" + modifyUserName + ", approveUserName=" + approveUserName
                + ", approveUser=" + approveUser + ", approveDate=" + approveDate + ", cancelDate=" + cancelDate
                + ", cancelUser=" + cancelUser + ", cancelUserName=" + cancelUserName + "]";
    }

    
}
