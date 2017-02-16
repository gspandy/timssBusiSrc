package com.timss.purchase.bean;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurOrder.java
 * @author: 890166
 * @createDate: 2014-6-27
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurOrder extends ItcMvcBean {

    private static final long serialVersionUID = -9000795434777939413L;
    @AutoGen(value = "PO_SHEET_ID", requireType = GenerationType.REQUIRED_NULL)
    private String sheetId; // sheet_id

    @AutoGen(value = "PO_SHEET_NO", requireType = GenerationType.REQUIRED_NULL)
    private String sheetno;
    private String sheetClass; // sheet_class
    private String companyRemark; // company_remark
    private String remark;
    private String sheetname;
    private String siteid;
    private String createaccount;
    private String modifyaccount;
    private String sheetIType; // sheet_i_type
    private String status;
    private String companyNo; // company_no

    private Date createdate = new Date();
    private Date dhdate ;
    private Date modifydate = new Date();

    private BigDecimal totalPrice; // total_price
    private BigDecimal taxRate; // 税率

    private String businessno; // 商务网订单号
    private String transactor; // 待办人
    
    private String qualityOkLen; //质保天数
    private String bidType;//招标方式
    private String spNo;//自定义编号

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
     * @return the dhdate
     */
    public Date getDhdate() {
        return dhdate;
    }

    /**
     * @param dhdate the dhdate to set
     */
    public void setDhdate(Date dhdate) {
        this.dhdate = dhdate;
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
     * @return the sheetClass
     */
    public String getSheetClass() {
        return sheetClass;
    }

    /**
     * @param sheetClass the sheetClass to set
     */
    public void setSheetClass(String sheetClass) {
        this.sheetClass = sheetClass;
    }

    /**
     * @return the totalPrice
     */
    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    /**
     * @param totalPrice the totalPrice to set
     */
    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    /**
     * @return the companyRemark
     */
    public String getCompanyRemark() {
        return companyRemark;
    }

    /**
     * @param companyRemark the companyRemark to set
     */
    public void setCompanyRemark(String companyRemark) {
        this.companyRemark = companyRemark;
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
        this.remark = remark.trim();
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

    public String getQualityOkLen() {
        return qualityOkLen;
    }

    public void setQualityOkLen(String qualityOkLen) {
        this.qualityOkLen = qualityOkLen;
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
