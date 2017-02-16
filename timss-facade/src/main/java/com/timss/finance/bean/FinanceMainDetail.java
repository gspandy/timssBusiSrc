package com.timss.finance.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 财务报销Detail表
 * @description:
 * @company: gdyd
 * @className: FinanceMainDetail.java
 * @author: wus
 * @createDate: 2014年6月20日
 * @updateUser: wus
 * @version: 1.0
 */
public class FinanceMainDetail extends ItcMvcBean {
    private static final long serialVersionUID = 2056939201836485068L;

    /* 明细id,使用 FINDTL+自增 */
    @AutoGen(value = "FIN_DID_SEQ")
    private String id; // 报销单明细编号
    private String fid; // 报销单编号
    private double amount; // 报销金额
    private String description; // 报销事由
    private String beneficiary; // 报销者名称
    private String beneficiaryid; // 报销者编号
    private String department; // 部门名称
    private String departmentid; // 部门编号
    private String address; // 起止地址
    private int join_nbr; // 参与者的数量
    private String join_boss; // 参与领导
    private String join_bossid; // 参与领导编号
    private String status; // 状态
    private int doc_nbr; // 单据张数
    private int allowance_days; // 补贴天数
    private String allowanceType; // 补贴类型，商务出差、一般出差、培训出差

    private int other_days; // 其他天数
    private int stay_days; // 住宿天数
    private Date strdate; // 开始日期
    private Date enddate; // 结束日期
    private String remark; // 备注
    private String spremark;// 特殊说明
    private String siteid;
    private String deptid;

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

    public int getOther_days() {
        return other_days;
    }

    public void setOther_days(int other_days) {
        this.other_days = other_days;
    }

    public int getStay_days() {
        return stay_days;
    }

    public void setStay_days(int stay_days) {
        this.stay_days = stay_days;
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

    public String getSiteid() {
        return siteid;
    }

    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }

    public String getDeptid() {
        return deptid;
    }

    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

}
