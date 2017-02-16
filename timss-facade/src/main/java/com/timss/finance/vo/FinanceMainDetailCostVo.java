package com.timss.finance.vo;

import java.io.Serializable;
import java.util.Date;

public class FinanceMainDetailCostVo implements Serializable {
	private static final long serialVersionUID = -5281798745190714421L;
	
	private String id; //报销明细编号
	private String fid; //报销单编号
	private double amount; //费用金额
	private String description; //费用事由
	private String beneficiary; //报销人姓名
	private String beneficiaryid; //报销人ID
	private String department; //部门名称
	private String departmentid; //部门ID
	private String address; //地址
	private int join_nbr; //参与人数
	private String join_boss; //参与领导
	private String join_bossid; //参与领导ID
	private String status; //状态
	private int doc_nbr; //单据数量
	private int allowance_days; //补贴天数
	private int stay_days; //住宿天数
	private int other_days; //其他天数
	private Date strdate; //开始时间
	private Date enddate; //结束时间
	private String remark; //备注
	private String spremark;//特殊说明
	private double ticketcost; //机票费
	private double staycost; //住宿费
	private double citytrafficcost; //市内交通费
	private double fuelcost; //油费
	private double longbuscost; //长途汽车费
	private double bridgecost; //路桥费
	private double huochecost; //火车费
	private double incidentalcost; //杂费
	private double othertrafficcost; //其他交通费
	private double meetingcost; //会议费
	private double businessentertainment; //业务招待费
	private double officecost; //办公费
	private double welfarism; //福利费
	private double carcost; //汽车费
	private double familymedicinecost; //家属医药费
	private double medicalinsurance; //补充医疗保险
	private double traincost; //培训费
	private double pettycash; //备用金
	private double allowancecost; //补贴费
	private String allowanceType; // 补贴类型，商务出差、一般出差、培训出差
	private double trafficcost; //交通费
	private double vehiclecost; //车辆费
	
	public String getAllowanceType() {
        return allowanceType;
    }
    public void setAllowanceType(String allowanceType) {
        this.allowanceType = allowanceType;
    }
    public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFid() {
		return fid;
	}
	public void setFid(String fid) {
		this.fid = fid;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getBeneficiary() {
		return beneficiary;
	}
	public void setBeneficiary(String beneficiary) {
		this.beneficiary = beneficiary;
	}
	public String getBeneficiaryid() {
		return beneficiaryid;
	}
	public void setBeneficiaryid(String beneficiaryid) {
		this.beneficiaryid = beneficiaryid;
	}
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
	public String getDepartmentid() {
		return departmentid;
	}
	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getJoin_nbr() {
		return join_nbr;
	}
	public void setJoin_nbr(int join_nbr) {
		this.join_nbr = join_nbr;
	}
	public String getJoin_boss() {
		return join_boss;
	}
	public void setJoin_boss(String join_boss) {
		this.join_boss = join_boss;
	}
	public String getJoin_bossid() {
		return join_bossid;
	}
	public void setJoin_bossid(String join_bossid) {
		this.join_bossid = join_bossid;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getDoc_nbr() {
		return doc_nbr;
	}
	public void setDoc_nbr(int doc_nbr) {
		this.doc_nbr = doc_nbr;
	}
	public int getAllowance_days() {
		return allowance_days;
	}
	public void setAllowance_days(int allowance_days) {
		this.allowance_days = allowance_days;
	}
	public Date getStrdate() {
		return strdate;
	}
	public void setStrdate(Date strdate) {
		this.strdate = strdate;
	}
	public Date getEnddate() {
		return enddate;
	}
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getSpremark() {
		return spremark;
	}
	public void setSpremark(String spremark) {
		this.spremark = spremark;
	}
	public double getTicketcost() {
		return ticketcost;
	}
	public void setTicketcost(double ticketcost) {
		this.ticketcost = ticketcost;
	}
	public double getStaycost() {
		return staycost;
	}
	public void setStaycost(double staycost) {
		this.staycost = staycost;
	}
	public double getCitytrafficcost() {
		return citytrafficcost;
	}
	public void setCitytrafficcost(double citytrafficcost) {
		this.citytrafficcost = citytrafficcost;
	}
	public double getFuelcost() {
		return fuelcost;
	}
	public void setFuelcost(double fuelcost) {
		this.fuelcost = fuelcost;
	}
	public double getLongbuscost() {
		return longbuscost;
	}
	public void setLongbuscost(double longbuscost) {
		this.longbuscost = longbuscost;
	}
	public double getBridgecost() {
		return bridgecost;
	}
	public void setBridgecost(double bridgecost) {
		this.bridgecost = bridgecost;
	}
	public double getHuochecost() {
		return huochecost;
	}
	public void setHuochecost(double huochecost) {
		this.huochecost = huochecost;
	}
	public double getIncidentalcost() {
		return incidentalcost;
	}
	public void setIncidentalcost(double incidentalcost) {
		this.incidentalcost = incidentalcost;
	}
	public double getOthertrafficcost() {
		return othertrafficcost;
	}
	public void setOthertrafficcost(double othertrafficcost) {
		this.othertrafficcost = othertrafficcost;
	}
	public int getStay_days() {
		return stay_days;
	}
	public void setStay_days(int stay_days) {
		this.stay_days = stay_days;
	}
	public int getOther_days() {
		return other_days;
	}
	public void setOther_days(int other_days) {
		this.other_days = other_days;
	}
	public double getMeetingcost() {
		return meetingcost;
	}
	public void setMeetingcost(double meetingcost) {
		this.meetingcost = meetingcost;
	}
	public double getBusinessentertainment() {
		return businessentertainment;
	}
	public void setBusinessentertainment(double businessentertainment) {
		this.businessentertainment = businessentertainment;
	}
	public double getOfficecost() {
		return officecost;
	}
	public void setOfficecost(double officecost) {
		this.officecost = officecost;
	}
	public double getCarcost() {
		return carcost;
	}
	public void setCarcost(double carcost) {
		this.carcost = carcost;
	}
	public double getFamilymedicinecost() {
		return familymedicinecost;
	}
	public void setFamilymedicinecost(double familymedicinecost) {
		this.familymedicinecost = familymedicinecost;
	}
	public double getMedicalinsurance() {
		return medicalinsurance;
	}
	public void setMedicalinsurance(double medicalinsurance) {
		this.medicalinsurance = medicalinsurance;
	}
	public double getTraincost() {
		return traincost;
	}
	public void setTraincost(double traincost) {
		this.traincost = traincost;
	}
	public double getPettycash() {
		return pettycash;
	}
	public void setPettycash(double pettycash) {
		this.pettycash = pettycash;
	}
	public double getAllowancecost() {
		return allowancecost;
	}
	public void setAllowancecost(double allowancecost) {
		this.allowancecost = allowancecost;
	}
	public double getWelfarism() {
		return welfarism;
	}
	public void setWelfarism(double welfarism) {
		this.welfarism = welfarism;
	}
	public double getTrafficcost() {
		return trafficcost;
	}
	public void setTrafficcost(double trafficcost) {
		this.trafficcost = trafficcost;
	}
	public double getVehiclecost() {
		return vehiclecost;
	}
	public void setVehiclecost(double vehiclecost) {
		this.vehiclecost = vehiclecost;
	}
}
