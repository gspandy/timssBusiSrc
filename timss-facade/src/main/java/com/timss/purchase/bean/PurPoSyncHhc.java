package com.timss.purchase.bean;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurPoSyncHhc.java
 * @author: 890166
 * @createDate: 2015-3-24
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurPoSyncHhc {

    private String hhcPono;
    private String hhcPano;
    private String timPono;
    private Date createdate;
    private String siteid;

    /**
     * @return the timPono
     */
    public String getTimPono() {
        return timPono;
    }

    /**
     * @param timPono the timPono to set
     */
    public void setTimPono(String timPono) {
        this.timPono = timPono;
    }

    /**
     * @return the hhcPono
     */
    public String getHhcPono() {
        return hhcPono;
    }

    /**
     * @param hhcPono the hhcPono to set
     */
    public void setHhcPono(String hhcPono) {
        this.hhcPono = hhcPono;
    }

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
