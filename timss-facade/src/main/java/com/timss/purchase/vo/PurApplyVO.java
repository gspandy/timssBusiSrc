package com.timss.purchase.vo;


/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplyVO.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurApplyVO {

    private String sheetId;
    private String sheetno; // 编号
    private String dhdate; // 要求到货日期
    private String totalcost; // 总价(元)
    private String excutionId;
    private String sheetname; // 名称
    private String status; // 状态
    private String createaccount;
    private String createname; // 申请人
    private String orgname; // 申请部门
    private String createdate; // 申请日期
    private String createdatesec;
    private String assignee;
    private String assigneeName; // 办理人
    private String siteid;
    private String sheetclassid; // 采购类型
    private String sheetclassname; // 采购类型
    private String purchstatus; // 采购申请状态
    private String purchtype; // 是否采购申请
    private String remark;

    private String modifydate;
    private String modifyaccount;

    private String curHandler; // 当前处理人
    private String curLink; // 当前环节
    private String source;
    private String isauth; // 是否授权项目
    private String dept; // 部门
    private String usage; // 用途
    private String transactor; // 待办人
    private String isToBusiness; // 是否发送到商务网
    private String itemclassid;// 物资分类
    private String major;// 专业
    private String isUrgentPurchase;// 是否紧急采购
    private String projectCode;// 项目编号
    private String projectAscription;// 项目名称

    private String _projectAscription;// 项目名称(模糊查询用)
    private String projectclassid;// 项目分类

    private String assetNature;// 资产性质
    
    private String itemCode; //物资编号
    private String  itemName;//物资名称
    private String stopStatus;//终止状态
    private String stopProcInstId;//终止流程实例id
    /**
     * @return the assetNature
     */
    public String getAssetNature() {
        return assetNature;
    }

    /**
     * @param assetNature the assetNature to set
     */
    public void setAssetNature(String assetNature) {
        this.assetNature = assetNature;
    }

    /**
     * @return the _projectAscription
     */
    public String get_projectAscription() {
        return _projectAscription;
    }

    /**
     * @param _projectAscription the _projectAscription to set
     */
    public void set_projectAscription(String _projectAscription) {
        this._projectAscription = _projectAscription;
    }

    /**
     * @return the projectAscription
     */
    public String getProjectAscription() {
        return projectAscription;
    }

    /**
     * @param projectAscription the projectAscription to set
     */
    public void setProjectAscription(String projectAscription) {
        this.projectAscription = projectAscription;
    }

    /**
     * @return the sheetclassname
     */
    public String getSheetclassname() {
        return sheetclassname;
    }

    /**
     * @param sheetclassname the sheetclassname to set
     */
    public void setSheetclassname(String sheetclassname) {
        this.sheetclassname = sheetclassname;
    }

    /**
     * @return the isauth
     */
    public String getIsauth() {
        return isauth;
    }

    /**
     * @param isauth the isauth to set
     */
    public void setIsauth(String isauth) {
        this.isauth = isauth;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the purchstatus
     */
    public String getPurchstatus() {
        return purchstatus;
    }

    /**
     * @param purchstatus the purchstatus to set
     */
    public void setPurchstatus(String purchstatus) {
        this.purchstatus = purchstatus;
    }

    /**
     * @return the curHandler
     */
    public String getCurHandler() {
        return curHandler;
    }

    /**
     * @param curHandler the curHandler to set
     */
    public void setCurHandler(String curHandler) {
        this.curHandler = curHandler;
    }

    /**
     * @return the curLink
     */
    public String getCurLink() {
        return curLink;
    }

    /**
     * @param curLink the curLink to set
     */
    public void setCurLink(String curLink) {
        this.curLink = curLink;
    }

    /**
     * @return the modifydate
     */
    public String getModifydate() {
        return modifydate;
    }

    /**
     * @param modifydate the modifydate to set
     */
    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    /**
     * @return the modifyaccount
     */
    public String getModifyaccount() {
        return modifyaccount;
    }

    /**
     * @param modifyaccount the modifyaccount to set
     */
    public void setModifyaccount(String modifyaccount) {
        this.modifyaccount = modifyaccount;
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
     * @return the purchtype
     */
    public String getPurchtype() {
        return purchtype;
    }

    /**
     * @param purchtype the purchtype to set
     */
    public void setPurchtype(String purchtype) {
        this.purchtype = purchtype;
    }

    /**
     * @return the sheetclassid
     */
    public String getSheetclassid() {
        return sheetclassid;
    }

    /**
     * @param sheetclassid the sheetclassid to set
     */
    public void setSheetclassid(String sheetclassid) {
        this.sheetclassid = sheetclassid;
    }

    /**
     * @return the sheetId
     */
    public String getSheetId() {
        return sheetId;
    }

    /**
     * @param sheetId the sheetId to set
     */
    public void setSheetId(String sheetId) {
        this.sheetId = sheetId;
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
     * @return the dhdate
     */
    public String getDhdate() {
        return dhdate;
    }

    /**
     * @param dhdate the dhdate to set
     */
    public void setDhdate(String dhdate) {
        this.dhdate = dhdate;
    }

    /**
     * @return the totalcost
     */
    public String getTotalcost() {
        return totalcost;
    }

    /**
     * @param totalcost the totalcost to set
     */
    public void setTotalcost(String totalcost) {
        this.totalcost = totalcost;
    }

    /**
     * @return the excutionId
     */
    public String getExcutionId() {
        return excutionId;
    }

    /**
     * @param excutionId the excutionId to set
     */
    public void setExcutionId(String excutionId) {
        this.excutionId = excutionId;
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
     * @return the createaccount
     */
    public String getCreateaccount() {
        return createaccount;
    }

    /**
     * @param createaccount the createaccount to set
     */
    public void setCreateaccount(String createaccount) {
        this.createaccount = createaccount;
    }

    /**
     * @return the createname
     */
    public String getCreatename() {
        return createname;
    }

    /**
     * @param createname the createname to set
     */
    public void setCreatename(String createname) {
        this.createname = createname;
    }

    /**
     * @return the orgname
     */
    public String getOrgname() {
        return orgname;
    }

    /**
     * @param orgname the orgname to set
     */
    public void setOrgname(String orgname) {
        this.orgname = orgname;
    }

    /**
     * @return the createdate
     */
    public String getCreatedate() {
        return createdate;
    }

    /**
     * @param createdate the createdate to set
     */
    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    /**
     * @return the createdatesec
     */
    public String getCreatedatesec() {
        return createdatesec;
    }

    /**
     * @param createdatesec the createdatesec to set
     */
    public void setCreatedatesec(String createdatesec) {
        this.createdatesec = createdatesec;
    }

    /**
     * @return the assignee
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * @param assignee the assignee to set
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    /**
     * @return the assigneeName
     */
    public String getAssigneeName() {
        return assigneeName;
    }

    /**
     * @param assigneeName the assigneeName to set
     */
    public void setAssigneeName(String assigneeName) {
        this.assigneeName = assigneeName;
    }

    /**
     * @return the siteid
     */
    public String getSiteid() {
        return siteid;
    }

    /**
     * @param siteid the siteid to set
     */
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getIsToBusiness() {
        return isToBusiness;
    }

    public void setIsToBusiness(String isToBusiness) {
        this.isToBusiness = isToBusiness;
    }

    public String getItemclassid() {
        return itemclassid;
    }

    public void setItemclassid(String itemclassid) {
        this.itemclassid = itemclassid;
    }

    public String getProjectclassid() {
        return projectclassid;
    }

    public void setProjectclassid(String projectclassid) {
        this.projectclassid = projectclassid;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getIsUrgentPurchase() {
        return isUrgentPurchase;
    }

    public void setIsUrgentPurchase(String isUrgentPurchase) {
        this.isUrgentPurchase = isUrgentPurchase;
    }

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

    public String getStopStatus() {
        return stopStatus;
    }

    public void setStopStatus(String stopStatus) {
        this.stopStatus = stopStatus;
    }

    public String getStopProcInstId() {
        return stopProcInstId;
    }

    public void setStopProcInstId(String stopProcInstId) {
        this.stopProcInstId = stopProcInstId;
    }
    
}
