package com.timss.attendance.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 考勤异常表
 * @description: {desc}
 * @company: gdyd
 * @className: Abnormity.java
 * @author: fengzt
 * @createDate: 2014年8月26日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class AbnormityBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 4194151668305535416L;

    /**
     * id
     */
    private int id;
    
    /**
     * 编号
     */
    @AutoGen(value="ATD_AB_NUM_SEQ")
    private String num;
    
    /**
     * 姓名
     */
    private String userName;
    
    /**
     * 部门ID
     */
    private String deptId;
    
    /**
     * 申请类别
     */
    private String category;
    
    /**
     * 申请理由
     */
    private String reason;
    
    /**
     * 站点id
     */
    private String siteId;
    
    /**
     * 部门名称
     */
    private String deptName;
    
    /**
     * 开始时间
     */
    private Date startDate;
    
    /**
     * 结束时间
     */
    private Date endDate;
    
    /**
     * 流程ID
     */
    private String instantId;
    
    /**
     * 流程状态
     */
    private String status;
    
    /**
     * 流程当前处理人
     */
    private String currentDealUser;
    
    /**
     * 创建人
     */
    private String createBy;
    
    /**
     * 创建时间
     */
    private Date createDate;
    
    /**
     * 更新人
     */
    private String updateBy;
    
    /**
     * 更新时间
     */
    private Date updateDate;

    private Date createDay;//提交申请时间
    
    /**
     * 加班详情列表
     */
    private List<AbnormityItemBean>itemList;
    private List<AbnormityItemBean>addItemList;
    private List<AbnormityItemBean>delItemList;
    private List<AbnormityItemBean>updateItemList;
    /**
     * 附件id，用于插入附件
     */
    private String[]fileIds; 
    
    public Date getCreateDay() {
		return createDay;
	}

	public void setCreateDay(Date createDay) {
		this.createDay = createDay;
	}

	public String[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(String[] fileIds) {
		this.fileIds = fileIds;
	}

	public List<AbnormityItemBean> getAddItemList() {
		return addItemList;
	}

	public void setAddItemList(List<AbnormityItemBean> addItemList) {
		this.addItemList = addItemList;
	}

	public List<AbnormityItemBean> getDelItemList() {
		return delItemList;
	}

	public void setDelItemList(List<AbnormityItemBean> delItemList) {
		this.delItemList = delItemList;
	}

	public List<AbnormityItemBean> getUpdateItemList() {
		return updateItemList;
	}

	public void setUpdateItemList(List<AbnormityItemBean> updateItemList) {
		this.updateItemList = updateItemList;
	}
	
	public List<AbnormityItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<AbnormityItemBean> itemList) {
		this.itemList = itemList;
	}

	public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getSiteId() {
        return siteId==null?super.getSiteid():siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
        super.setSiteid(siteId);
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getInstantId() {
        return instantId;
    }

    public void setInstantId(String instantId) {
        this.instantId = instantId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentDealUser() {
        return currentDealUser;
    }

    public void setCurrentDealUser(String currentDealUser) {
        this.currentDealUser = currentDealUser;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((category == null) ? 0 : category.hashCode());
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((currentDealUser == null) ? 0 : currentDealUser.hashCode());
        result = prime * result + ((deptId == null) ? 0 : deptId.hashCode());
        result = prime * result + ((deptName == null) ? 0 : deptName.hashCode());
        result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
        result = prime * result + id;
        result = prime * result + ((instantId == null) ? 0 : instantId.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
        result = prime * result + ((userName == null) ? 0 : userName.hashCode());
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
        AbnormityBean other = (AbnormityBean) obj;
        if ( category == null ) {
            if ( other.category != null )
                return false;
        } else if ( !category.equals( other.category ) )
            return false;
        if ( createBy == null ) {
            if ( other.createBy != null )
                return false;
        } else if ( !createBy.equals( other.createBy ) )
            return false;
        if ( createDate == null ) {
            if ( other.createDate != null )
                return false;
        } else if ( !createDate.equals( other.createDate ) )
            return false;
        if ( currentDealUser == null ) {
            if ( other.currentDealUser != null )
                return false;
        } else if ( !currentDealUser.equals( other.currentDealUser ) )
            return false;
        if ( deptId == null ) {
            if ( other.deptId != null )
                return false;
        } else if ( !deptId.equals( other.deptId ) )
            return false;
        if ( deptName == null ) {
            if ( other.deptName != null )
                return false;
        } else if ( !deptName.equals( other.deptName ) )
            return false;
        if ( endDate == null ) {
            if ( other.endDate != null )
                return false;
        } else if ( !endDate.equals( other.endDate ) )
            return false;
        if ( id != other.id )
            return false;
        if ( instantId == null ) {
            if ( other.instantId != null )
                return false;
        } else if ( !instantId.equals( other.instantId ) )
            return false;
        if ( num == null ) {
            if ( other.num != null )
                return false;
        } else if ( !num.equals( other.num ) )
            return false;
        if ( reason == null ) {
            if ( other.reason != null )
                return false;
        } else if ( !reason.equals( other.reason ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
            return false;
        if ( startDate == null ) {
            if ( other.startDate != null )
                return false;
        } else if ( !startDate.equals( other.startDate ) )
            return false;
        if ( status == null ) {
            if ( other.status != null )
                return false;
        } else if ( !status.equals( other.status ) )
            return false;
        if ( updateBy == null ) {
            if ( other.updateBy != null )
                return false;
        } else if ( !updateBy.equals( other.updateBy ) )
            return false;
        if ( updateDate == null ) {
            if ( other.updateDate != null )
                return false;
        } else if ( !updateDate.equals( other.updateDate ) )
            return false;
        if ( userName == null ) {
            if ( other.userName != null )
                return false;
        } else if ( !userName.equals( other.userName ) )
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AbnormityBean [id=" + id + ", num=" + num + ", userName=" + userName + ", deptId=" + deptId
                + ", category=" + category + ", reason=" + reason + ", siteId=" + siteId + ", deptName=" + deptName
                + ", startDate=" + startDate + ", endDate=" + endDate + ", instantId=" + instantId + ", status="
                + status + ", currentDealUser=" + currentDealUser + ", createBy=" + createBy + ", createDate="
                + createDate + ", updateBy=" + updateBy + ", updateDate=" + updateDate + "]";
    }

    
    
}
