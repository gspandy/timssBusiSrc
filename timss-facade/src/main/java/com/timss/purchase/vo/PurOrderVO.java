package com.timss.purchase.vo;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrderVO.java
 * @author: 890166
 * @createDate: 2014-6-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurOrderVO {

    private String sheetId; // sheet_id
    private String sheetno; // 编号
    private String sheetIType; // sheet_i_type
    private String companyNo; // company_no
    private String companyName;// company_name
    private String dhdate; // 要求到货日期
    private String totalPrice;// total_price 总价（元）
    private String status;
    private String statusName;// status_name 状态
    private String username;
    private String department; // 申请部门
    private String createdate; // 申请日期
    private String createdatesec;
    private String sheetname; // 名称
    private String siteid;

    private String remark;
    private String modifydate;

    private String applysheetno; // 申请单号

    private String curHandler; // 当前办理人

    private BigDecimal taxRate; // 税率

    private String purOrderIsGm; // 是否提交总经理审批标识

    private String businessno; // 商务网订单号
    private String transactor; // 待办人
    // 买家信息
    private String purchaserName;
    // 创建人
    private String createaccount;
    //供应商名字
    private String supComName;
    //质保天数
    private String qualityOkLen; 
    //物资编号
    private String itemCode;
    //物资名称
    private String  itemName;
    //招标方式
    private String bidType;
    //自定义编号
    private String spNo;
	/**
     * @return the supComName
     */
    public String getSupComName() {
        return supComName;
    }

    /**
     * @param supComName the supComName to set
     */
    public void setSupComName(String supComName) {
        this.supComName = supComName;
    }

    /**
     * @return the purOrderIsGm
     */
    public String getPurOrderIsGm() {
        return purOrderIsGm;
    }

    /**
     * @param purOrderIsGm the purOrderIsGm to set
     */
    public void setPurOrderIsGm(String purOrderIsGm) {
        this.purOrderIsGm = purOrderIsGm;
    }

    /**
     * @return the taxRate
     */
    public BigDecimal getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate the taxRate to set
     */
    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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
     * @return the applysheetno
     */
    public String getApplysheetno() {
        return applysheetno;
    }

    /**
     * @param applysheetno the applysheetno to set
     */
    public void setApplysheetno(String applysheetno) {
        this.applysheetno = applysheetno;
    }

    /**
     * @return the companyNo
     */
    public String getCompanyNo() {
        return companyNo;
    }

    /**
     * @param companyNo the companyNo to set
     */
    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
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
     * @return the sheetIType
     */
    public String getSheetIType() {
        return sheetIType;
    }

    /**
     * @param sheetIType the sheetIType to set
     */
    public void setSheetIType(String sheetIType) {
        this.sheetIType = sheetIType;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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
     * @return the totalPrice
     */
    public String getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return StringUtils.trimToEmpty( status );
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the statusName
     */
    public String getStatusName() {
        return statusName;
    }

    /**
     * @param statusName the statusName to set
     */
    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
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

    public String getBusinessno() {
        return businessno;
    }

    public void setBusinessno(String businessno) {
        this.businessno = businessno;
    }

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getPurchaserName() {
        return purchaserName;
    }

    public void setPurchaserName(String purchaserName) {
        this.purchaserName = purchaserName;
    }

    public String getCreateaccount() {
        return createaccount;
    }

    public void setCreateaccount(String createaccount) {
        this.createaccount = createaccount;
    }
    
    public String getQualityOkLen() {
        return qualityOkLen;
    }

    public void setQualityOkLen(String qualityOkLen) {
        this.qualityOkLen = qualityOkLen;
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

    public String getBidType() {
        return bidType;
    }

    public void setBidType(String bidType) {
        this.bidType = bidType;
    }

    public String getSpNo() {
        return spNo;
    }

    public void setSpNo(String spNo) {
        this.spNo = spNo;
    }
     
}
