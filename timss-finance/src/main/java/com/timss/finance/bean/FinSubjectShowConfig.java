package com.timss.finance.bean;

import com.yudean.mvc.bean.ItcMvcBean;



/**
 * @title: {title}
 * @description: {desc}报销凭证中需要显示的datagrid的配置bean
 * @company: gdyd
 * @className: FinSubjectShowConfig.java
 * @author: 王中华
 * @createDate: 2015-9-6
 * @updateUser: 王中华
 * @version: 1.0
 */
public class FinSubjectShowConfig extends ItcMvcBean{
	
    private static final long serialVersionUID = 538725228772793682L;
        String siteid; //站点
        String deptid; //部门
        String roleid; //角色
        String finLevel; //报销层面
	String flow; //流程类型(差旅费报销、……)
	String reimburseType; //报销类型（自己、他人、多人）
	String showSubjectNo; // 科目显示配置
	
    public String getSiteid() {
        return siteid;
    }
    public void setSiteid(String siteid) {
        this.siteid = siteid;
    }
    public String getFinLevel() {
        return finLevel;
    }
    public void setFinLevel(String finLevel) {
        this.finLevel = finLevel;
    }
    public String getDeptid() {
        return deptid;
    }
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }
    public String getRoleid() {
        return roleid;
    }
    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
    public String getFlow() {
        return flow;
    }
    public void setFlow(String flow) {
        this.flow = flow;
    }
    public String getReimburseType() {
        return reimburseType;
    }
    public void setReimburseType(String reimburseType) {
        this.reimburseType = reimburseType;
    }
    public String getShowSubjectNo() {
        return showSubjectNo;
    }
    public void setShowSubjectNo(String showSubjectNo) {
        this.showSubjectNo = showSubjectNo;
    }
    @Override
    public String toString() {
        return "FinSubjectShowConfig [siteid=" + siteid + ", deptid=" + deptid + ", roleid=" + roleid + ", flow="
                + flow + ", reimburseType=" + reimburseType + ", showSubjectNo=" + showSubjectNo + "]";
    }
	
	
	
}
