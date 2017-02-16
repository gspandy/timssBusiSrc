package com.timss.asset.bean;

/**
 * @title: ast_attach_match表的bean
 * @description: {desc}
 * @company: gdyd
 * @className: AssetAttachBean.java
 * @author: 890165
 * @createDate: 2014-7-3
 * @updateUser: 890165
 * @version: 1.0
 */
public class AssetAttachBean {
    private String assetId;
    private String attachId;
    private String site;
    /**
     * @return the assetId
     */
    public String getAssetId() {
        return assetId;
    }
    /**
     * @param assetId the assetId to set
     */
    public void setAssetId(String assetId) {
        this.assetId = assetId;
    }
    /**
     * @return the attachId
     */
    public String getAttachId() {
        return attachId;
    }
    /**
     * @param attachId the attachId to set
     */
    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
    /**
     * @return the site
     */
    public String getSite() {
        return site;
    }
    /**
     * @param site the site to set
     */
    public void setSite(String site) {
        this.site = site;
    }
}
