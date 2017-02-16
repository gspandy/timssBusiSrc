package com.timss.purchase.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPaSyncHhc.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurPaSyncHhc extends ItcMvcBean {

    private static final long serialVersionUID = 1L;

    @AutoGen(value = "HHC_PANO", requireType = GenerationType.REQUIRED_NULL)
    private String hhcPano; // 商务网审批单号

    private String timPano; // timss审批单号
    private String status;

    private Date createdate;
    private String createuser;
    private Date modifydate;
    private String modifyuser;
    private String siteid;

    /**
     * @return the hhcPano
     */
    public String getHhcPano() {
        return hhcPano;
    }

    /**
     * @param hhcPano the hhcPano to set
     */
    public void setHhcPano(String hhcPano) {
        this.hhcPano = hhcPano;
    }

    /**
     * @return the timPano
     */
    public String getTimPano() {
        return timPano;
    }

    /**
     * @param timPano the timPano to set
     */
    public void setTimPano(String timPano) {
        this.timPano = timPano;
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

}
