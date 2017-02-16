package com.timss.attendance.bean;

import java.util.Date;
import java.util.List;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * 
 * @title: 加班申请表
 * @description: {desc}
 * @company: gdyd
 * @className: OvertimeBean.java
 * @author: fengzt
 * @createDate: 2014年9月10日
 * @updateUser: fengzt
 * @version: 1.0
 */
public class OvertimeBean extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = -9060268081099912179L;
    
    /**
     * id
     */
    private int id;
    
    /**
     * 编号
     */
    @AutoGen("ATD_OV_NUM_SEQ")
    private String num;
    
    /**
     * 申请时间
     */
    private Date createDay;
    
    /**
     * 站点
     */
    private String siteId;
    
    /**
     * 加班事由
     */
    private String overTimeReason;
    
    /**
     * 创建人
     */
    private String createBy ;
    private String userName ;//申请人
    private String deptId ;//申请人部门
    private String deptName ;
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
     * 加班详情列表
     */
    private List<OvertimeItemBean>itemList;
    private List<OvertimeItemBean>addItemList;
    private List<OvertimeItemBean>delItemList;
    private List<OvertimeItemBean>updateItemList;
    
    public List<OvertimeItemBean> getAddItemList() {
		return addItemList;
	}

	public void setAddItemList(List<OvertimeItemBean> addItemList) {
		this.addItemList = addItemList;
	}

	public List<OvertimeItemBean> getDelItemList() {
		return delItemList;
	}

	public void setDelItemList(List<OvertimeItemBean> delItemList) {
		this.delItemList = delItemList;
	}

	public List<OvertimeItemBean> getUpdateItemList() {
		return updateItemList;
	}

	public void setUpdateItemList(List<OvertimeItemBean> updateItemList) {
		this.updateItemList = updateItemList;
	}

	/**
     * 附件id，用于插入附件
     */
    private String[]fileIds; 

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

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String[] getFileIds() {
		return fileIds;
	}

	public void setFileIds(String[] fileIds) {
		this.fileIds = fileIds;
	}

	public List<OvertimeItemBean> getItemList() {
		return itemList;
	}

	public void setItemList(List<OvertimeItemBean> itemList) {
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

    public Date getCreateDay() {
        return createDay;
    }

    public void setCreateDay(Date createDay) {
        this.createDay = createDay;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getOverTimeReason() {
        return overTimeReason;
    }

    public void setOverTimeReason(String overTimeReason) {
        this.overTimeReason = overTimeReason;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((createBy == null) ? 0 : createBy.hashCode());
        result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
        result = prime * result + ((createDay == null) ? 0 : createDay.hashCode());
        result = prime * result + ((currentDealUser == null) ? 0 : currentDealUser.hashCode());
        result = prime * result + id;
        result = prime * result + ((instantId == null) ? 0 : instantId.hashCode());
        result = prime * result + ((num == null) ? 0 : num.hashCode());
        result = prime * result + ((overTimeReason == null) ? 0 : overTimeReason.hashCode());
        result = prime * result + ((siteId == null) ? 0 : siteId.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((updateBy == null) ? 0 : updateBy.hashCode());
        result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
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
        OvertimeBean other = (OvertimeBean) obj;
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
        if ( createDay == null ) {
            if ( other.createDay != null )
                return false;
        } else if ( !createDay.equals( other.createDay ) )
            return false;
        if ( currentDealUser == null ) {
            if ( other.currentDealUser != null )
                return false;
        } else if ( !currentDealUser.equals( other.currentDealUser ) )
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
        if ( overTimeReason == null ) {
            if ( other.overTimeReason != null )
                return false;
        } else if ( !overTimeReason.equals( other.overTimeReason ) )
            return false;
        if ( siteId == null ) {
            if ( other.siteId != null )
                return false;
        } else if ( !siteId.equals( other.siteId ) )
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
        return true;
    }

    @Override
    public String toString() {
        return "OvertimeBean [id=" + id + ", num=" + num + ", createDay=" + createDay + ", siteId=" + siteId
                + ", overTimeReason=" + overTimeReason + ", createBy=" + createBy + ", createDate=" + createDate
                + ", updateBy=" + updateBy + ", updateDate=" + updateDate + ", instantId=" + instantId + ", status="
                + status + ", currentDealUser=" + currentDealUser + "]";
    }
    
    

}
