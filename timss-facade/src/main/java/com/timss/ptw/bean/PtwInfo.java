package com.timss.ptw.bean;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * @title: 工作票基本信息表
 * @description: {desc}
 * @company: gdyd
 * @className: PtwInfo.java
 * @author: 周保康
 * @createDate: 2014-6-25
 * @updateUser: 周保康
 * @version: 1.0
 */
public class PtwInfo implements Serializable {

    private static final long serialVersionUID = 6815564060525687208L;
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
    private int workOrderId;
    private String workOrderNo;
    private String woWorkTask;
    private String keyBoxNo;
    private Integer keyBoxId;
    private String relateKeyBoxId;
    private String eqId;
    private String eqNo;
    private String eqName;
    private String workContent;
    private String workPlace;
    private String workCondition;
    private Date preWorkStartTime;
    private Date preWorkEndTime;
    private String approver;
    private String approverNo;
    private Date approveTime;
    private String issuer;
    private String issuerNo;
    private Date issuedTime;
    private String eleParts;
    private String addFile1;
    private String addFile2;
    private String addFile3;
    private String addFile4;
    private String addFile5;
    private String addFileOtherNo;
    private String addFileOtherName;
    private int lic1;
    private int lic2;
    private int lic3;
    private int lic4;
    private int lic5;
    private int lic6;
    private int lic7;
    private int lic8;
    private int lic9;
    private int lic10;
    private String licOther;
    private String licWorkClass;
    private String licWorkClassPeople;
    private int licWorkClassNum ;
    private Date licWpicTime ;
    private String licWpic;
    private String licWpicNo;
    private String licPl;
    private String licPlNo;
    private Date licStartTime;
    private Date licEndTime;
    private String licDpl;
    private String licDplNo;
    private String licWl;
    private String licWlNo;
    private Date licWlTime ;
    private Date licTime;
    private Date finTime;
    private Date finWpicTime;
    private String finWpic;
    private String finWpicNo;
    private Date finWlTime;
    private String finWl;
    private String finWlNo;
    private int endJdxNum;
    private String endJdxNo;
    private String endWl;
    private String endWlNo;
    private Date endTime;
    private String canceler;
    private String cancelerNo;
    private Date cancelTime;
    private String remark;
    private int consignStatus ;
    private int inUse ;
    private String woWindStation;
    private int isOutSourcing;
    
    private String outIssuer;
    private String outIssuerNo;
    private Date outIssuedTime;
    
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
    public String getCreateUser() {
        return createUser;
    }
    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }
    public Date getCreateDate() {
        return createDate;
    }
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
    public String getModifyUser() {
        return modifyUser;
    }
    public void setModifyUser(String modifyUser) {
        this.modifyUser = modifyUser;
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
    /**工作任务（从工单带过来）*/
    public String getWoWorkTask() {
		return woWorkTask;
	}
    /**工作任务（从工单带过来）*/
	public void setWoWorkTask(String woWorkTask) {
		this.woWorkTask = woWorkTask;
	}
	/**钥匙箱号*/
    public String getKeyBoxNo() {
        return keyBoxNo;
    }
    /**钥匙箱号*/
    public void setKeyBoxNo(String keyBoxNo) {
        this.keyBoxNo = keyBoxNo;
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
    /**工作条件*/
    public String getWorkCondition() {
        return workCondition;
    }
    /**工作条件*/
    public void setWorkCondition(String workCondition) {
        this.workCondition = workCondition;
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
    /**批准人*/
    public String getApprover() {
        return approver;
    }
    /**批准人*/
    public void setApprover(String approver) {
        this.approver = approver;
    }
    /**批准时间*/
    public Date getApproveTime() {
        return approveTime;
    }
    /**批准时间*/
    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
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
    /**带电部分*/
    public String getEleParts() {
        return eleParts;
    }
    /**带电部分*/
    public void setEleParts(String eleParts) {
        this.eleParts = eleParts;
    }
    /**附加文件_安全隔离示意图*/
    public String getAddFile1() {
        return addFile1;
    }
    /**附加文件_安全隔离示意图*/
    public void setAddFile1(String addFile1) {
        this.addFile1 = addFile1;
    }
    /**附加文件_动火工作票*/
    public String getAddFile2() {
        return addFile2;
    }
    /**附加文件_动火工作票*/
    public void setAddFile2(String addFile2) {
        this.addFile2 = addFile2;
    }
    /**附加文件_可燃气体、粉尘检测*/
    public String getAddFile3() {
        return addFile3;
    }
    /**附加文件_可燃气体、粉尘检测*/
    public void setAddFile3(String addFile3) {
        this.addFile3 = addFile3;
    }
    /**附加文件_工作风险分析单*/
    public String getAddFile4() {
        return addFile4;
    }
    /**附加文件_工作风险分析单*/
    public void setAddFile4(String addFile4) {
        this.addFile4 = addFile4;
    }
    /**附加文件_安全措施附页*/
    public String getAddFile5() {
        return addFile5;
    }
    /**附加文件_安全措施附页*/
    public void setAddFile5(String addFile5) {
        this.addFile5 = addFile5;
    }
    /**附加文件_其他_编号*/
    public String getAddFileOtherNo() {
        return addFileOtherNo;
    }
    /**附加文件_其他_编号*/
    public void setAddFileOtherNo(String addFileOtherNo) {
        this.addFileOtherNo = addFileOtherNo;
    }
    /**附加文件_其他_名称*/
    public String getAddFileOtherName() {
        return addFileOtherName;
    }
    /**附加文件_其他_名称*/
    public void setAddFileOtherName(String addFileOtherName) {
        this.addFileOtherName = addFileOtherName;
    }
    /**许可_高温*/
    public int getLic1() {
        return lic1;
    }
    /**许可_高温*/
    public void setLic1(int lic1) {
        this.lic1 = lic1;
    }
    /**许可_高压*/
    public int getLic2() {
        return lic2;
    }
    /**许可_高压*/
    public void setLic2(int lic2) {
        this.lic2 = lic2;
    }
    /**许可_触电*/
    public int getLic3() {
        return lic3;
    }
    /**许可_触电*/
    public void setLic3(int lic3) {
        this.lic3 = lic3;
    }
    /**许可_转动*/
    public int getLic4() {
        return lic4;
    }
    /**许可_转动*/
    public void setLic4(int lic4) {
        this.lic4 = lic4;
    }
    /**许可_高空坠落*/
    public int getLic5() {
        return lic5;
    }
    /**许可_高空坠落*/
    public void setLic5(int lic5) {
        this.lic5 = lic5;
    }
    /**许可_窒息*/
    public int getLic6() {
        return lic6;
    }
    /**许可_窒息*/
    public void setLic6(int lic6) {
        this.lic6 = lic6;
    }
    /**许可_中毒*/
    public int getLic7() {
        return lic7;
    }
    /**许可_中毒*/
    public void setLic7(int lic7) {
        this.lic7 = lic7;
    }
    /**许可_辐射*/
    public int getLic8() {
        return lic8;
    }
    /**许可_辐射*/
    public void setLic8(int lic8) {
        this.lic8 = lic8;
    }
    /**许可_着火*/
    public int getLic9() {
        return lic9;
    }
    /**许可_着火*/
    public void setLic9(int lic9) {
        this.lic9 = lic9;
    }
    /**许可_爆炸*/
    public int getLic10() {
        return lic10;
    }
    /**许可_爆炸*/
    public void setLic10(int lic10) {
        this.lic10 = lic10;
    }
    /**许可_其他*/
    public String getLicOther() {
        return licOther;
    }
    /**许可_其他*/
    public void setLicOther(String licOther) {
        this.licOther = licOther;
    }
    /**许可_工作班（公司/部门/班组）*/
    public String getLicWorkClass() {
        return licWorkClass;
    }
    /**许可_工作班（公司/部门/班组）*/
    public void setLicWorkClass(String licWorkClass) {
        this.licWorkClass = licWorkClass;
    }
    /**许可_工作班成员*/
    public String getLicWorkClassPeople() {
        return licWorkClassPeople;
    }
    /**许可_工作班成员*/
    public void setLicWorkClassPeople(String licWorkClassPeople) {
        this.licWorkClassPeople = licWorkClassPeople;
    }
    /**许可_工作班总人数*/
    public int getLicWorkClassNum() {
        return licWorkClassNum;
    }
    /**许可_工作班总人数*/
    public void setLicWorkClassNum(int licWorkClassNum) {
        this.licWorkClassNum = licWorkClassNum;
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
    /**许可_项目负责人*/
    public String getLicPl() {
        return licPl;
    }
    /**许可_项目负责人*/
    public void setLicPl(String licPl) {
        this.licPl = licPl;
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
    /**许可_机组值班负责人*/
    public String getLicDpl() {
        return licDpl;
    }
    /**许可_机组值班负责人*/
    public void setLicDpl(String licDpl) {
        this.licDpl = licDpl;
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
    /**工作结束_工作负责人确认时间*/
    public Date getFinWpicTime() {
        return finWpicTime;
    }
    /**工作结束_工作负责人确认时间*/
    public void setFinWpicTime(Date finWpicTime) {
        this.finWpicTime = finWpicTime;
    }
    /**工作结束_工作负责人*/
    public String getFinWpic() {
        return finWpic;
    }
    /**工作结束_工作负责人*/
    public void setFinWpic(String finWpic) {
        this.finWpic = finWpic;
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
    /**工作终结_接地线（刀）组数*/
    public int getEndJdxNum() {
        return endJdxNum;
    }
    /**工作终结_接地线（刀）组数*/
    public void setEndJdxNum(int endJdxNum) {
        this.endJdxNum = endJdxNum;
    }
    /**工作终结_接地线（刀）编号*/
    public String getEndJdxNo() {
        return endJdxNo;
    }
    /**工作终结_接地线（刀）编号*/
    public void setEndJdxNo(String endJdxNo) {
        this.endJdxNo = endJdxNo;
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
    /**作废_作废人*/
    public String getCanceler() {
        return canceler;
    }
    /**作废_作废人*/
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }
    /**作废_时间*/
    public Date getCancelTime() {
        return cancelTime;
    }
    /**作废_时间*/
    public void setCancelTime(Date cancelTime) {
        this.cancelTime = cancelTime;
    }
    /**备注*/
    public String getRemark() {
        return remark;
    }
    /**备注*/
    public void setRemark(String remark) {
        this.remark = remark;
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
    
    /**风电场*/
    public String getWoWindStation() {
		return woWindStation;
	}
    /**风电场*/
	public void setWoWindStation(String woWindStation) {
		this.woWindStation = woWindStation;
	}
	/**是否委外,1为是，0为否*/
	public int getIsOutSourcing() {
		return isOutSourcing;
	}
	/**是否委外,1为是，0为否*/
	public void setIsOutSourcing(int isOutSourcing) {
		this.isOutSourcing = isOutSourcing;
	}
	
	 /**外委签发人*/
	public String getOutIssuer() {
		return outIssuer;
	}
	/**外委签发人*/
	public void setOutIssuer(String outIssuer) {
		this.outIssuer = outIssuer;
	}
	/**外委签发人*/
	public String getOutIssuerNo() {
		return outIssuerNo;
	}
	/**外委签发人*/
	public void setOutIssuerNo(String outIssuerNo) {
		this.outIssuerNo = outIssuerNo;
	}
	/**外委签发时间*/
	public Date getOutIssuedTime() {
		return outIssuedTime;
	}
	/**外委签发时间*/
	public void setOutIssuedTime(Date outIssuedTime) {
		this.outIssuedTime = outIssuedTime;
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
    public String getApproverNo() {
        return approverNo;
    }
    public void setApproverNo(String approverNo) {
        this.approverNo = approverNo;
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
    public String getLicPlNo() {
        return licPlNo;
    }
    public void setLicPlNo(String licPlNo) {
        this.licPlNo = licPlNo;
    }
    public String getLicDplNo() {
        return licDplNo;
    }
    public void setLicDplNo(String licDplNo) {
        this.licDplNo = licDplNo;
    }
    public String getLicWlNo() {
        return licWlNo;
    }
    public void setLicWlNo(String licWlNo) {
        this.licWlNo = licWlNo;
    }
    public String getFinWpicNo() {
        return finWpicNo;
    }
    public void setFinWpicNo(String finWpicNo) {
        this.finWpicNo = finWpicNo;
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
    public String getCancelerNo() {
        return cancelerNo;
    }
    public void setCancelerNo(String cancelerNo) {
        this.cancelerNo = cancelerNo;
    }
    
    
    public int getWorkOrderId() {
        return workOrderId;
    }
    public void setWorkOrderId(int workOrderId) {
        this.workOrderId = workOrderId;
    }
    public String getEqId() {
        return eqId;
    }
    public void setEqId(String eqId) {
        this.eqId = eqId;
    }
    
    
    public Integer getKeyBoxId() {
		return keyBoxId;
	}
	public void setKeyBoxId(Integer keyBoxId) {
		this.keyBoxId = keyBoxId;
	}
	public String getRelateKeyBoxId() {
		return relateKeyBoxId;
	}
	public void setRelateKeyBoxId(String relateKeyBoxId) {
		this.relateKeyBoxId = relateKeyBoxId;
	}
	public PtwInfo() {
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
        PtwInfo other = (PtwInfo) obj;
        if ( id != other.id )
            return false;
        return true;
    }
	@Override
	public String toString() {
		return "PtwInfo [id=" + id + ", wtTypeId=" + wtTypeId + ", wtNo="
				+ wtNo + ", wtStatus=" + wtStatus + ", isStdWt=" + isStdWt
				+ ", createUser=" + createUser + ", createUserName="
				+ createUserName + ", createDate=" + createDate
				+ ", modifyUser=" + modifyUser + ", modifyUserName="
				+ modifyUserName + ", modifyDate=" + modifyDate + ", deptId="
				+ deptId + ", siteId=" + siteId + ", workOrderId="
				+ workOrderId + ", workOrderNo=" + workOrderNo + ", keyBoxNo="
				+ keyBoxNo + ", keyBoxId=" + keyBoxId + ", relateKeyBoxId="
				+ relateKeyBoxId + ", eqId=" + eqId + ", eqNo=" + eqNo
				+ ", eqName=" + eqName + ", workContent=" + workContent
				+ ", workPlace=" + workPlace + ", workCondition="
				+ workCondition + ", preWorkStartTime=" + preWorkStartTime
				+ ", preWorkEndTime=" + preWorkEndTime + ", approver="
				+ approver + ", approverNo=" + approverNo + ", approveTime="
				+ approveTime + ", issuer=" + issuer + ", issuerNo=" + issuerNo
				+ ", issuedTime=" + issuedTime + ", eleParts=" + eleParts
				+ ", addFile1=" + addFile1 + ", addFile2=" + addFile2
				+ ", addFile3=" + addFile3 + ", addFile4=" + addFile4
				+ ", addFile5=" + addFile5 + ", addFileOtherNo="
				+ addFileOtherNo + ", addFileOtherName=" + addFileOtherName
				+ ", lic1=" + lic1 + ", lic2=" + lic2 + ", lic3=" + lic3
				+ ", lic4=" + lic4 + ", lic5=" + lic5 + ", lic6=" + lic6
				+ ", lic7=" + lic7 + ", lic8=" + lic8 + ", lic9=" + lic9
				+ ", lic10=" + lic10 + ", licOther=" + licOther
				+ ", licWorkClass=" + licWorkClass + ", licWorkClassPeople="
				+ licWorkClassPeople + ", licWorkClassNum=" + licWorkClassNum
				+ ", licWpicTime=" + licWpicTime + ", licWpic=" + licWpic
				+ ", licWpicNo=" + licWpicNo + ", licPl=" + licPl
				+ ", licPlNo=" + licPlNo + ", licStartTime=" + licStartTime
				+ ", licEndTime=" + licEndTime + ", licDpl=" + licDpl
				+ ", licDplNo=" + licDplNo + ", licWl=" + licWl + ", licWlNo="
				+ licWlNo + ", licWlTime=" + licWlTime + ", licTime=" + licTime
				+ ", finTime=" + finTime + ", finWpicTime=" + finWpicTime
				+ ", finWpic=" + finWpic + ", finWpicNo=" + finWpicNo
				+ ", finWlTime=" + finWlTime + ", finWl=" + finWl
				+ ", finWlNo=" + finWlNo + ", endJdxNum=" + endJdxNum
				+ ", endJdxNo=" + endJdxNo + ", endWl=" + endWl + ", endWlNo="
				+ endWlNo + ", endTime=" + endTime + ", canceler=" + canceler
				+ ", cancelerNo=" + cancelerNo + ", cancelTime=" + cancelTime
				+ ", remark=" + remark + ", consignStatus=" + consignStatus
				+ ", inUse=" + inUse + "]";
	}
    
     
    
}
