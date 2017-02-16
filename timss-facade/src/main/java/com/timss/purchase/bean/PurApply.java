package com.timss.purchase.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApply.java
 * @author: 890166
 * @createDate: 2014-6-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurApply extends ItcMvcBean {

    private static final long serialVersionUID = 6689551540888173608L;

    @AutoGen(value = "PA_SHEET_NO", requireType = GenerationType.REQUIRED_NULL)
    private String sheetno; // 审批单号
    @AutoGen(value = "PA_SHEET_ID", requireType = GenerationType.REQUIRED_NULL)
    private String sheetId; // 审批id
    private long securitystoreid; // 安全库id

    private String sheetclassid; // 采购类型
    private String purchstatus; // 采购申请状态
    private String purchtype; // 采购申请类型
    private String sumflag; // 汇总状态
    private String orderaccount; // 采购人账号
    private String createaccount; // 创建人账号
    private String sheetname; // 审批单名称
    private String remark; // 备注
    private String modifyaccount; // 修改人账号
    private String siteid; // 站点id

    private Date createdate = new Date(); // 创建时间
    private Date modifydate = new Date(); // 修改时间
    private Date dhdate ; // 到货日期

    private long itemnum = 0L; // 采购数量合计
    private BigDecimal tatolcost; // 总价

    private String source; // 数据来源
    private String isauth; // 是否授权项目
    // 2015-09-18 新增字段
    private String itemclassid; // 物资分类
    private String projectclassid; // 项目归类
    private String projectCode; // 项目编号
    private String isUrgentPurchase; // 是否紧急采购
    private String major; // 专业
    private String dept; // 部门
    private String usage; // 用途
    private String transactor; // 待时办人
    private String isToBusiness; // 发送到商务网

    private String projectAscription;// 项目名称

    private String assetNature;// 资产性质
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
     * @return the itemnum
     */
    public long getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(long itemnum) {
        this.itemnum = itemnum;
    }

    /**
     * @return the tatolcost
     */
    public BigDecimal getTatolcost() {
        return tatolcost;
    }

    /**
     * @param tatolcost the tatolcost to set
     */
    public void setTatolcost(BigDecimal tatolcost) {
        this.tatolcost = tatolcost;
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
     * @return the sumflag
     */
    public String getSumflag() {
        return sumflag;
    }

    /**
     * @param sumflag the sumflag to set
     */
    public void setSumflag(String sumflag) {
        this.sumflag = sumflag;
    }

    /**
     * @return the orderaccount
     */
    public String getOrderaccount() {
        return orderaccount;
    }

    /**
     * @param orderaccount the orderaccount to set
     */
    public void setOrderaccount(String orderaccount) {
        this.orderaccount = orderaccount;
    }

    /**
     * @return the securitystoreid
     */
    public long getSecuritystoreid() {
        return securitystoreid;
    }

    /**
     * @param securitystoreid the securitystoreid to set
     */
    public void setSecuritystoreid(long securitystoreid) {
        this.securitystoreid = securitystoreid;
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

    public String getProjectCode() {
        return projectCode;
    }

    public void setProjectCode(String projectCode) {
        this.projectCode = projectCode;
    }

    public String getIsUrgentPurchase() {
        return isUrgentPurchase;
    }

    public void setIsUrgentPurchase(String isUrgentPurchase) {
        this.isUrgentPurchase = isUrgentPurchase;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
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
