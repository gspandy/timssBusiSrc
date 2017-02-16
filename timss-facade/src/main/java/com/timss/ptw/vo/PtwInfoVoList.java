package com.timss.ptw.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @title: 工作票列表展示用的VO
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfoVoList.java
 * @author: 周保康
 * @createDate: 2014-6-26
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwInfoVoList implements Serializable{

    private static final long serialVersionUID = -8039415355208502681L;
    
    private int id;
    private int wtTypeId;
    private String wtNo;
    private int wtStatus;
    private int isStdWt;
    private String createUser;
    private String createUserName;
    private Date createDate;
    private String modifyUser;
    private String modifyUserName;
    private Date modifyDate;
    private String deptId;
    private String siteId;
    private String workOrderNo;
    private String eqId;
    private String eqNo;
    private String eqName;
    private String workContent;
    private String workPlace;
    private Date preWorkStartTime;
    private Date preWorkEndTime;
    private String issuer;
    private String issuerNo;
    private Date issuedTime;
    private Date licWpicTime ;
    private String licWpic;
    private String licWpicNo;
    private Date licStartTime;
    private Date licEndTime;
    private String licWl;
    private String licWlNo;
    private Date licWlTime ;
    private Date licTime;
    private Date finTime;
    private Date finWlTime;
    private String finWl;
    private String endWl;
    private String finWlNo;
    private String endWlNo;
    private Date endTime;
    private int consignStatus ;
    private int inUse ;
    private String wtTypeName;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    /**工作票类型Id*/
    public int getWtTypeId() {
        return wtTypeId;
    }
    /**工作票类型Id*/
    public void setWtTypeId(int wtTypeId) {
        this.wtTypeId = wtTypeId;
    }
    /**工作票类型Name*/
    public String getWtTypeName() {
        return wtTypeName;
    }
    /**工作票类型name*/
    public void setWtTypeName(String wtTypeName) {
        this.wtTypeName = wtTypeName;
    }
    /**工作票编号*/
    public String getWtNo() {
        return wtNo;
    }
    /**工作票编号*/
    public void setWtNo(String wtNo) {
        this.wtNo = wtNo;
    }
    /**工作票状态*/
    public int getWtStatus() {
        return wtStatus;
    }
    /**工作票状态*/
    public void setWtStatus(int wtStatus) {
        this.wtStatus = wtStatus;
    }
    /**是否为标准票*/
    public int getIsStdWt() {
        return isStdWt;
    }
    /**是否为标准票*/
    public void setIsStdWt(int isStdWt) {
        this.isStdWt = isStdWt;
    }
    
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    
    public Date getModifyDate() {
        return modifyDate;
    }
    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
    /**工作票部门Id*/
    public String getDeptId() {
        return deptId;
    }
    /**工作票部门Id*/
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
    /**工作票站点Id*/
    public String getSiteId() {
        return siteId;
    }
    /**工作票站点Id*/
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    /**工单编号*/
    public String getWorkOrderNo() {
        return workOrderNo;
    }
    /**工单编号*/
    public void setWorkOrderNo(String workOrderNo) {
        this.workOrderNo = workOrderNo;
    }
    
    /**设备编号*/
    public String getEqNo() {
        return eqNo;
    }
    /**设备编号*/
    public void setEqNo(String eqNo) {
        this.eqNo = eqNo;
    }
    /**设备名称*/
    public String getEqName() {
        return eqName;
    }
    /**设备名称*/
    public void setEqName(String eqName) {
        this.eqName = eqName;
    }
    /**工作内容*/
    public String getWorkContent() {
        return workContent;
    }
    /**工作内容*/
    public void setWorkContent(String workContent) {
        this.workContent = workContent;
    }
    /**工作地点*/
    public String getWorkPlace() {
        return workPlace;
    }
    /**工作地点*/
    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace;
    }
    
    /**预计开工时间*/
    public Date getPreWorkStartTime() {
        return preWorkStartTime;
    }
    /**预计开工时间*/
    public void setPreWorkStartTime(Date preWorkStartTime) {
        this.preWorkStartTime = preWorkStartTime;
    }
    /**预计完工时间*/
    public Date getPreWorkEndTime() {
        return preWorkEndTime;
    }
    /**预计完工时间*/
    public void setPreWorkEndTime(Date preWorkEndTime) {
        this.preWorkEndTime = preWorkEndTime;
    }
   
    /**签发人*/
    public String getIssuer() {
        return issuer;
    }
    /**签发人*/
    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }
    /**签发时间*/
    public Date getIssuedTime() {
        return issuedTime;
    }
    /**签发时间*/
    public void setIssuedTime(Date issuedTime) {
        this.issuedTime = issuedTime;
    }
    
    /**许可_工作负责人签字时间*/
    public Date getLicWpicTime() {
        return licWpicTime;
    }
    /**许可_工作负责人签字时间*/
    public void setLicWpicTime(Date licWpicTime) {
        this.licWpicTime = licWpicTime;
    }
    /**许可_工作负责人*/
    public String getLicWpic() {
        return licWpic;
    }
    /**许可_工作负责人*/
    public void setLicWpic(String licWpic) {
        this.licWpic = licWpic;
    }
    
    /**许可_开工时间*/
    public Date getLicStartTime() {
        return licStartTime;
    }
    /**许可_开工时间*/
    public void setLicStartTime(Date licStartTime) {
        this.licStartTime = licStartTime;
    }
    /**许可_结束时间*/
    public Date getLicEndTime() {
        return licEndTime;
    }
    /**许可_结束时间*/
    public void setLicEndTime(Date licEndTime) {
        this.licEndTime = licEndTime;
    }
    
    /**许可_工作许可人*/
    public String getLicWl() {
        return licWl;
    }
    /**许可_工作许可人*/
    public void setLicWl(String licWl) {
        this.licWl = licWl;
    }
    /**许可_工作许可人确认时间*/
    public Date getLicWlTime() {
        return licWlTime;
    }
    /**许可_工作许可人确认时间*/
    public void setLicWlTime(Date licWlTime) {
        this.licWlTime = licWlTime;
    }
    /**许可_许可时间*/
    public Date getLicTime() {
        return licTime;
    }
    /**许可_许可时间*/
    public void setLicTime(Date licTime) {
        this.licTime = licTime;
    }
    /**工作结束_时间*/
    public Date getFinTime() {
        return finTime;
    }
    /**工作结束_时间*/
    public void setFinTime(Date finTime) {
        this.finTime = finTime;
    }
   
    /**工作结束_工作许可人确认时间*/
    public Date getFinWlTime() {
        return finWlTime;
    }
    /**工作结束_工作许可人确认时间*/
    public void setFinWlTime(Date finWlTime) {
        this.finWlTime = finWlTime;
    }
    /**工作结束_工作许可人*/
    public String getFinWl() {
        return finWl;
    }
    /**工作结束_工作许可人*/
    public void setFinWl(String finWl) {
        this.finWl = finWl;
    }
    
    /**工作终结_工作许可人*/
    public String getEndWl() {
        return endWl;
    }
    /**工作终结_工作许可人*/
    public void setEndWl(String endWl) {
        this.endWl = endWl;
    }
    /**工作终结_时间*/
    public Date getEndTime() {
        return endTime;
    }
    /**工作终结_时间*/
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    
    /**会签状态*/
    public int getConsignStatus() {
        return consignStatus;
    }
    /**会签状态*/
    public void setConsignStatus(int consignStatus) {
        this.consignStatus = consignStatus;
    }
    /**是否可用，1为可用，0为删除*/
    public int getInUse() {
        return inUse;
    }
    /**是否可用，1为可用，0为删除*/
    public void setInUse(int inUse) {
        this.inUse = inUse;
    }
    
    /**设备Id*/
    public String getEqId() {
        return eqId;
    }
    /**设备Id*/
    public void setEqId(String eqId) {
        this.eqId = eqId;
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
    
    
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public String getModifyUser() {
        return modifyUser;
    }
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
    }
    public String getIssuerNo() {
        return issuerNo;
    }
    public void setIssuerNo(String issuerNo) {
        this.issuerNo = issuerNo;
    }
    public String getLicWpicNo() {
        return licWpicNo;
    }
    public void setLicWpicNo(String licWpicNo) {
        this.licWpicNo = licWpicNo;
    }
    public String getLicWlNo() {
        return licWlNo;
    }
    public void setLicWlNo(String licWlNo) {
        this.licWlNo = licWlNo;
    }
    public String getFinWlNo() {
        return finWlNo;
    }
    public void setFinWlNo(String finWlNo) {
        this.finWlNo = finWlNo;
    }
    public String getEndWlNo() {
        return endWlNo;
    }
    public void setEndWlNo(String endWlNo) {
        this.endWlNo = endWlNo;
    }
    
    public PtwInfoVoList() {
        super();
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
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
        PtwInfoVoList other = (PtwInfoVoList) obj;
        if ( id != other.id )
            return false;
        return true;
    }
    @Override
    public String toString() {
        return "PtwInfoForList [id=" + id + ", wtTypeId=" + wtTypeId + ", wtNo=" + wtNo + ", wtStatus=" + wtStatus
                + ", isStdWt=" + isStdWt + ", createUserName=" + createUserName + ", createDate=" + createDate
                + ", modifyUserName=" + modifyUserName + ", modifyDate=" + modifyDate + ", deptId=" + deptId
                + ", siteId=" + siteId + ", workOrderNo=" + workOrderNo + ", eqNo=" + eqNo + ", eqName=" + eqName
                + ", workContent=" + workContent + ", workPlace=" + workPlace + ", preWorkStartTime="
                + preWorkStartTime + ", preWorkEndTime=" + preWorkEndTime + ", issuer=" + issuer + ", issuedTime="
                + issuedTime + ", licWpicTime=" + licWpicTime + ", licWpic=" + licWpic + ", licStartTime="
                + licStartTime + ", licEndTime=" + licEndTime + ", licWl=" + licWl + ", licWlTime=" + licWlTime
                + ", licTime=" + licTime + ", finTime=" + finTime + ", finWlTime=" + finWlTime + ", finWl=" + finWl
                + ", endWl=" + endWl + ", endTime=" + endTime + ", consignStatus=" + consignStatus + ", inUse=" + inUse
                + "]";
    }
    
    
    
}
