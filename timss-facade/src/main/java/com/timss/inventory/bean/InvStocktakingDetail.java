package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;

/**
 * @title: 库存盘点明细流程信息子表
 * @description: 库存盘点明细流程信息子表
 * @company: gdyd
 * @className: InvStocktakingDetail.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvStocktakingDetail extends InvItemBaseField {

    private static final long serialVersionUID = -5370543054327921390L;

    @AutoGen(value = "INV_STOCKTAKINGD_ID", requireType = GenerationType.REQUIRED_NULL)
    private String istdid; // 盘点明细ID
    private String istid; // 盘点审批ID
    private String remark; // 备注
    private String binid; // 货柜ID
    private String siteId; // 站点ID

    private BigDecimal qtyBefore; // 盘点前数量
    private BigDecimal qtyAfter; // 盘点后数量

    private BigDecimal price; // 单价（元）

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the istdid
     */
    public String getIstdid() {
        return istdid;
    }

    /**
     * @param istdid the istdid to set
     */
    public void setIstdid(String istdid) {
        this.istdid = istdid;
    }

    /**
     * @return the istid
     */
    public String getIstid() {
        return istid;
    }

    /**
     * @param istid the istid to set
     */
    public void setIstid(String istid) {
        this.istid = istid;
    }

    /**
     * @return the qtyBefore
     */
    public BigDecimal getQtyBefore() {
        return qtyBefore;
    }

    /**
     * @param qtyBefore the qtyBefore to set
     */
    public void setQtyBefore(BigDecimal qtyBefore) {
        this.qtyBefore = qtyBefore;
    }

    /**
     * @return the qtyAfter
     */
    public BigDecimal getQtyAfter() {
        return qtyAfter;
    }

    /**
     * @param qtyAfter the qtyAfter to set
     */
    public void setQtyAfter(BigDecimal qtyAfter) {
        this.qtyAfter = qtyAfter;
    }

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
     * @return the binid
     */
    public String getBinid() {
        return binid;
    }

    /**
     * @param binid the binid to set
     */
    public void setBinid(String binid) {
        this.binid = binid;
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
