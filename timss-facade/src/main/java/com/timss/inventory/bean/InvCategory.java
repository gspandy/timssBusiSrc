package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资类型表
 * @description: 物资类型表
 * @company: gdyd
 * @className: InvCategory.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvCategory extends ItcMvcBean {

    private static final long serialVersionUID = -5792657131593308295L;

    @AutoGen(value = "INV_CATEGORY_ID", requireType = GenerationType.REQUIRED_NULL)
    private String invcateid; // 物资类型ID
    private String invcatename; // 物资类型名称
    private String descriptions; // 描述
    private String status; // 启用状态 ACTIVE启用 NO停用
    private String parentid; // 父ID
    private String warehouseid; // 仓库ID
    private String siteId; // 站点ID
    private String createuser; // 创建人
    private String modifyuser; // 修改人
    private Date createdate; // 创建时间
    private Date modifydate; // 修改时间

    /**
     * @return the invcateid
     */
    public String getInvcateid() {
        return invcateid;
    }

    /**
     * @param invcateid the invcateid to set
     */
    public void setInvcateid(String invcateid) {
        this.invcateid = invcateid;
    }

    /**
     * @return the invcatename
     */
    public String getInvcatename() {
        return invcatename;
    }

    /**
     * @param invcatename the invcatename to set
     */
    public void setInvcatename(String invcatename) {
        this.invcatename = invcatename;
    }

    /**
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    /**
     * @param descriptions the descriptions to set
     */
    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
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
     * @return the parentid
     */
    public String getParentid() {
        return parentid;
    }

    /**
     * @param parentid the parentid to set
     */
    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    /**
     * @return the warehouseid
     */
    public String getWarehouseid() {
        return warehouseid;
    }

    /**
     * @param warehouseid the warehouseid to set
     */
    public void setWarehouseid(String warehouseid) {
        this.warehouseid = warehouseid;
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

}
