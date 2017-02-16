package com.timss.asset.bean;

/**
 * @title: 资产台账初始化数据实体
 * @description: 资产台账初始化数据实体
 * @company: gdyd
 * @className: AstOrganizeData.java
 * @author: yuanzh
 * @createDate: 2015-11-9
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class AstOrganizeData {

    private String parentid;
    private String assetid;
    private String assetname;
    private String spec;
    private String description;
    private String modeldesc;
    private String manufacturer;
    private String producedate;
    private String isroot;
    private String cumodel;
    private String siteid;
    private String status;
    private String remark;

    /**
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark the remark to set
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * @return the assetid
     */
    public String getAssetid() {
        return assetid;
    }

    /**
     * @param assetid the assetid to set
     */
    public void setAssetid(String assetid) {
        this.assetid = assetid;
    }

    /**
     * @return the assetname
     */
    public String getAssetname() {
        return assetname;
    }

    /**
     * @param assetname the assetname to set
     */
    public void setAssetname(String assetname) {
        this.assetname = assetname;
    }

    /**
     * @return the spec
     */
    public String getSpec() {
        return spec;
    }

    /**
     * @param spec the spec to set
     */
    public void setSpec(String spec) {
        this.spec = spec;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the modeldesc
     */
    public String getModeldesc() {
        return modeldesc;
    }

    /**
     * @param modeldesc the modeldesc to set
     */
    public void setModeldesc(String modeldesc) {
        this.modeldesc = modeldesc;
    }

    /**
     * @return the manufacturer
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * @param manufacturer the manufacturer to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * @return the producedate
     */
    public String getProducedate() {
        return producedate;
    }

    /**
     * @param producedate the producedate to set
     */
    public void setProducedate(String producedate) {
        this.producedate = producedate;
    }

    /**
     * @return the isroot
     */
    public String getIsroot() {
        return isroot;
    }

    /**
     * @param isroot the isroot to set
     */
    public void setIsroot(String isroot) {
        this.isroot = isroot;
    }

    /**
     * @return the cumodel
     */
    public String getCumodel() {
        return cumodel;
    }

    /**
     * @param cumodel the cumodel to set
     */
    public void setCumodel(String cumodel) {
        this.cumodel = cumodel;
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

}
