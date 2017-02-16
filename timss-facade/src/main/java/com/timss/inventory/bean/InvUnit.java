package com.timss.inventory.bean;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 计量单位表
 * @description: 计量单位表
 * @company: gdyd
 * @className: InvUnit.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890147
 * @version: 1.0
 */
public class InvUnit extends ItcMvcBean {

    private static final long serialVersionUID = -2676812556970789017L;

    @AutoGen(value = "INV_UNIT_ID", requireType = GenerationType.REQUIRED_NULL)
    private String unitid; // 单位ID
    private String unitname; // 单位名称
    private String descriptions; // 描述
    private String active; // 使用状态 ACTIVE启用 NO停用
    private String siteId; // 站点ID
    private String unitcode;

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    /**
     * @return the unitid
     */
    public String getUnitid() {
        return unitid;
    }

    /**
     * @param unitid the unitid to set
     */
    public void setUnitid(String unitid) {
        this.unitid = unitid;
    }

    /**
     * @return the unitname
     */
    public String getUnitname() {
        return unitname;
    }

    /**
     * @param unitname the unitname to set
     */
    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    /**
     * @return the descriptions
     */
    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
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
