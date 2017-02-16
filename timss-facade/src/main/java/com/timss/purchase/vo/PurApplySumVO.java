package com.timss.purchase.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurApplySumVO.java
 * @author: 890166
 * @createDate: 2014-6-20
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurApplySumVO {

    private String sheetId;
    private String sheetno; // 申请单编号
    private String dhdate; // 要求到货日期
    private String totalcost; // 总价(元)
    private String excutionId;
    private String sheetname; // 申请单名称
    private String status; // 状态
    private String createaccount;
    private String createname; // 申请人
    private String orgname; // 申请部门
    private String createdate; // 申请日期
    private String workflowcode;
    private String siteid;
    private String deptid;

    private String assignee;
    private String assigneeName; // 办理人

    private String curHandler; // 当前处理人
    private String curLink; // 当前环节

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
     * @return the workflowcode
     */
    public String getWorkflowcode() {
        return workflowcode;
    }

    /**
     * @param workflowcode the workflowcode to set
     */
    public void setWorkflowcode(String workflowcode) {
        this.workflowcode = workflowcode;
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
     * @return the deptid
     */
    public String getDeptid() {
        return deptid;
    }

    /**
     * @param deptid the deptid to set
     */
    public void setDeptid(String deptid) {
        this.deptid = deptid;
    }

}
