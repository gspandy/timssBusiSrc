package com.timss.purchase.vo;

import java.util.Date;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: EamItemVO.java
 * @author: 890166
 * @createDate: 2014-10-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class EamItemVO {

    private String claes;// 物资类别
    private String cufigurenum;// 图号
    private String cumaterial;// 材质
    private String cumodel;// 规格型号
    private String description;// 物资名称
    private String orderunit;// 物资计量单位 个、辆、块 等
    private String issueunit;// 物资计量单位 个、辆、块 等，和orderunit一样
    private int itemid;// 中间库编码ID，石碑山由800000000开始
    private String itemnum;// 物编码资
    private String rowstamp;// format 99999999 格式的数字，唯一
    private String status;// 启用标志
    private Date statusdate;// 状态日期
    private String yudeannum;// 粤电编码
    private String cusiteid;// 站點信息
    private String siteid;

    private String warehouseid;// 仓库id
    private String invcateid;//物资分类id

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
     * @return the claes
     */
    public String getClaes() {
        return claes;
    }

    /**
     * @param claes the claes to set
     */
    public void setClaes(String claes) {
        this.claes = claes;
    }

    /**
     * @return the cufigurenum
     */
    public String getCufigurenum() {
        return cufigurenum;
    }

    /**
     * @param cufigurenum the cufigurenum to set
     */
    public void setCufigurenum(String cufigurenum) {
        this.cufigurenum = cufigurenum;
    }

    /**
     * @return the cumaterial
     */
    public String getCumaterial() {
        return cumaterial;
    }

    /**
     * @param cumaterial the cumaterial to set
     */
    public void setCumaterial(String cumaterial) {
        this.cumaterial = cumaterial;
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
     * @return the orderunit
     */
    public String getOrderunit() {
        return orderunit;
    }

    /**
     * @param orderunit the orderunit to set
     */
    public void setOrderunit(String orderunit) {
        this.orderunit = orderunit;
    }

    /**
     * @return the issueunit
     */
    public String getIssueunit() {
        return issueunit;
    }

    /**
     * @param issueunit the issueunit to set
     */
    public void setIssueunit(String issueunit) {
        this.issueunit = issueunit;
    }

    /**
     * @return the itemid
     */
    public int getItemid() {
        return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    /**
     * @return the itemnum
     */
    public String getItemnum() {
        return itemnum;
    }

    /**
     * @param itemnum the itemnum to set
     */
    public void setItemnum(String itemnum) {
        this.itemnum = itemnum;
    }

    /**
     * @return the rowstamp
     */
    public String getRowstamp() {
        return rowstamp;
    }

    /**
     * @param rowstamp the rowstamp to set
     */
    public void setRowstamp(String rowstamp) {
        this.rowstamp = rowstamp;
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
     * @return the statusdate
     */
    public Date getStatusdate() {
        return statusdate;
    }

    /**
     * @param statusdate the statusdate to set
     */
    public void setStatusdate(Date statusdate) {
        this.statusdate = statusdate;
    }

    /**
     * @return the yudeannum
     */
    public String getYudeannum() {
        return yudeannum;
    }

    /**
     * @param yudeannum the yudeannum to set
     */
    public void setYudeannum(String yudeannum) {
        this.yudeannum = yudeannum;
    }

    /**
     * @return the cusiteid
     */
    public String getCusiteid() {
        return cusiteid;
    }

    /**
     * @param cusiteid the cusiteid to set
     */
    public void setCusiteid(String cusiteid) {
        this.cusiteid = cusiteid;
    }

    public String getInvcateid() {
        return invcateid;
    }

    public void setInvcateid(String invcateid) {
        this.invcateid = invcateid;
    }
    
}
