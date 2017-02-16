package com.timss.ptw.vo;

import java.util.Date;
import java.util.List;

import com.timss.ptw.bean.SptoInfo;
import com.timss.ptw.bean.PtoOperItem;

/**
 * @title: 标准操作票VO
 * @description: {desc}
 * @company: gdyd
 * @className: IsMethodPointVo.java
 * @author: gucw
 * @createDate: 2015年7月7日
 * @updateUser: gucw
 * @version: 1.0
 */
public class SptoInfoVo extends SptoInfo {

    /**
     * 
     */
    private static final long serialVersionUID = 1656401501335366863L;

    private List<PtoOperItem> ptoOperItemList;
    private String attach;
    private String preAuditUser;
    private String preAuditUserName;
    private Date preAuditDate;
    private String auditUser;
    private String auditUserName;
    private Date auditDate;
    private String permitUser;
    private String permitUserName;
    private Date permitDate;
    private String createUserName;
    private Date createDate;
    private String equipmentName;
    private String commitStyle;

   
    public List<PtoOperItem> getPtoOperItemList() {
        return ptoOperItemList;
    }

    public void setPtoOperItemList(List<PtoOperItem> ptoOperItemList) {
        this.ptoOperItemList = ptoOperItemList;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getPreAuditUser() {
        return preAuditUser;
    }

    public void setPreAuditUser(String preAuditUser) {
        this.preAuditUser = preAuditUser;
    }

    public String getPreAuditUserName() {
        return preAuditUserName;
    }

    public Date getPreAuditDate() {
        return preAuditDate;
    }

    public void setPreAuditDate(Date preAuditDate) {
        this.preAuditDate = preAuditDate;
    }

    public void setPreAuditUserName(String preAuditUserName) {
        this.preAuditUserName = preAuditUserName;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditUserName() {
        return auditUserName;
    }

    public void setAuditUserName(String auditUserName) {
        this.auditUserName = auditUserName;
    }

    public String getPermitUser() {
        return permitUser;
    }

    public void setPermitUser(String permitUser) {
        this.permitUser = permitUser;
    }

    public String getPermitUserName() {
        return permitUserName;
    }

    public void setPermitUserName(String permitUserName) {
        this.permitUserName = permitUserName;
    }

    public String getCreateUserName() {
        return createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Date auditDate) {
        this.auditDate = auditDate;
    }

    public Date getPermitDate() {
        return permitDate;
    }

    public void setPermitDate(Date permitDate) {
        this.permitDate = permitDate;
    }
    
    public String getCommitStyle() {
        return commitStyle;
    }

    public void setCommitStyle(String commitStyle) {
        this.commitStyle = commitStyle;
    }

    @Override
    public String toString() {
        return "SptoInfoVo [sptoItemList=" + ptoOperItemList + ", attach=" + attach + ", auditUser=" + auditUser
                + ", auditUserName=" + auditUserName + ", permitUser=" + permitUser + ", permitUserName="
                + permitUserName + ", createUserName=" + createUserName + ", equipmentName=" + equipmentName
                + ", createDate=" + createDate + ", auditDate=" + auditDate + ", permitDate=" + permitDate
                + ", commitStyle=" + commitStyle + "]";
    }

}
