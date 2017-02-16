package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatBudget.java
 * @author: 890166
 * @createDate: 2014-9-18
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatBudget extends ItcMvcBean {

    private static final long serialVersionUID = 1L;

    @AutoGen(value = "INV_IMB_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imbid; // 预算ID

    private BigDecimal budget;
    private String deptid;
    private Date setdate;
    private String createuser;
    private Date createdate;
    private String modifyuser;
    private Date modifydate;
    private String siteId;

    /**
     * @return the imbid
     */
    public String getImbid() {
        return imbid;
    }

    /**
     * @param imbid the imbid to set
     */
    public void setImbid(String imbid) {
        this.imbid = imbid;
    }

    /**
     * @return the budget
     */
    public BigDecimal getBudget() {
        return budget;
    }

    /**
     * @param budget the budget to set
     */
    public void setBudget(BigDecimal budget) {
        this.budget = budget;
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

    /**
     * @return the setdate
     */
    public Date getSetdate() {
        return setdate;
    }

    /**
     * @param setdate the setdate to set
     */
    public void setSetdate(Date setdate) {
        this.setdate = setdate;
    }

    /**
     * @return the createuser
     */
    public String getCreateuser() {
        return createuser;
    }

    /**
     * @param createuser the createuser to set
     */
    public void setCreateuser(String createuser) {
        this.createuser = createuser;
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
     * @return the modifyuser
     */
    public String getModifyuser() {
        return modifyuser;
    }

    /**
     * @param modifyuser the modifyuser to set
     */
    public void setModifyuser(String modifyuser) {
        this.modifyuser = modifyuser;
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
     * @return the siteId
     */
    public String getSiteId() {
        return siteId;
    }

    /**
     * @param siteId the siteId to set
     */
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
