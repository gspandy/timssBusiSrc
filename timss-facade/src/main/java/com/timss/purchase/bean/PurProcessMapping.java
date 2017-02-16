package com.timss.purchase.bean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurProcessMapping.java
 * @author: 890166
 * @createDate: 2014-7-4
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurProcessMapping {

    private String masterkey;// 主键
    private String processid;// 流程id
    private String modeltype;// 模块类型
    private String siteid;// 站点id
    private String curHandler; // 当前处理人 cur_handler
    private String curLink; // 当前环节 cur_link

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
     * @return the masterkey
     */
    public String getMasterkey() {
        return masterkey;
    }

    /**
     * @param masterkey the masterkey to set
     */
    public void setMasterkey(String masterkey) {
        this.masterkey = masterkey;
    }

    /**
     * @return the processid
     */
    public String getProcessid() {
        return processid;
    }

    /**
     * @param processid the processid to set
     */
    public void setProcessid(String processid) {
        this.processid = processid;
    }

    /**
     * @return the modeltype
     */
    public String getModeltype() {
        return modeltype;
    }

    /**
     * @param modeltype the modeltype to set
     */
    public void setModeltype(String modeltype) {
        this.modeltype = modeltype;
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
