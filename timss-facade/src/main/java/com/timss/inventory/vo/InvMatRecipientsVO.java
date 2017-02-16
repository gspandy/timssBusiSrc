package com.timss.inventory.vo;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatRecipientsVO.java
 * @author: 890166
 * @createDate: 2014-11-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatRecipientsVO {

    private String imrid; // 发料单id
    private String sheetname; // 发料单名称（和领料单相同）
    private String sheetno; // 发料单编号
    private String applySheetNo; //领料单号（关联查）
    private String applyType; // 领料类型（和领料单相同）
    private String applyDept; // 领料部门（关联查）
    private String createuser; // 创建人
    private Date createdate; // 创建时间
    private String modifyuser; // 修改人
    private Date modifydate; // 修改时间
    private String siteId; // 站点ID
    private String instanceid; // 流程实例ID
    private String imaid; // 领料申请ID
    private String remark;// 备注
    private String applyUse;// 领料用途
    private String outterNo;
    private String status;//发料状态
    private Date deliveryDate; //发料时间

    /**
     * @return the imrid
     */
    public String getImrid() {
        return imrid;
    }

    /**
     * @param imrid the imrid to set
     */
    public void setImrid(String imrid) {
        this.imrid = imrid;
    }

    /**
     * @return the sheetname
     */
    public String getSheetname() {
        return sheetname;
    }

    /**
     * @param sheetname the sheetname to set
     */
    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    /**
     * @return the sheetno
     */
    public String getSheetno() {
        return sheetno;
    }

    /**
     * @param sheetno the sheetno to set
     */
    public void setSheetno(String sheetno) {
        this.sheetno = sheetno;
    }

    /**
     * @return the applyType
     */
    public String getApplyType() {
        return applyType;
    }

    /**
     * @param applyType the applyType to set
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
    }

    /**
     * @return the createdate
     */
    public Date getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(Date createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the modifyuser
     */
    public String getModifyuser() {
        return modifyuser;
    }

    /**
     * @param modifyuser the modifyuser to set
     */
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
    }

    /**
     * @return the modifydate
     */
    public Date getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(Date modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    /**
     * @return the instanceid
     */
    public String getInstanceid() {
        return instanceid;
    }

    /**
     * @param instanceid the instanceid to set
     */
    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    /**
     * @return the imaid
     */
    public String getImaid() {
        return imaid;
    }

    /**
     * @param imaid the imaid to set
     */
    public void setImaid(String imaid) {
        this.imaid = imaid;
    }

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * @return the outterNo
     */
    public String getOutterNo() {
        return outterNo;
    }

    /**
     * @param outterNo the outterNo to set
     */
    public void setOutterNo(String outterNo) {
        this.outterNo = outterNo;
    }

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the deliveryDate
	 */
	public Date getDeliveryDate() {
		return deliveryDate;
	}

	/**
	 * @param deliveryDate the deliveryDate to set
	 */
	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	/**
	 * @return the applySheetNo
	 */
	public String getApplySheetNo() {
		return applySheetNo;
	}

	/**
	 * @param applySheetNo the applySheetNo to set
	 */
	public void setApplySheetNo(String applySheetNo) {
		this.applySheetNo = applySheetNo;
	}

	/**
	 * @return the applyDept
	 */
	public String getApplyDept() {
		return applyDept;
	}

	/**
	 * @param applyDept the applyDept to set
	 */
	public void setApplyDept(String applyDept) {
		this.applyDept = applyDept;
	}

	/**
	 * @return the applyUse
	 */
	public String getApplyUse() {
		return applyUse;
	}

	/**
	 * @param applyUse the applyUse to set
	 */
	public void setApplyUse(String applyUse) {
		this.applyUse = applyUse;
	}
	
}
