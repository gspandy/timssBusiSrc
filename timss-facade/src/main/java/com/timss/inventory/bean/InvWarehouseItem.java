package com.timss.inventory.bean;

import java.math.BigDecimal;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;

/**
 * @title: 库存中变动信息
 * @description: 库存中变动信息
 * @company: gdyd
 * @className: InvWarehouseItem.java
 * @author: 890166
 * @createDate: 2014-7-10
 * @updateUser: 890166
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class InvWarehouseItem extends InvItemBaseField {

    @AutoGen(value = "INV_WAREHOUSEITEM_ID", requireType = GenerationType.REQUIRED_NEW)
    private String iwiid; // 流水号ID
    private String manufacturer; // 生产厂商
    private String siteId; // 站点ID site_id
    private String defBinid; // 默认站点ID def_binid
    private String sparecode;// 备件编号

    private String issafety;// 是否启用安全库存
    private BigDecimal qtyEconomic;// 经济订购量
    private BigDecimal qtyLowInv;// 最低库存量
    
    private String active;// 物资绑定分类是否禁用 "Y"启用 "N"禁用

    /**
     * @return the issafety
     */
    public String getIssafety() {
        return issafety;
    }

    /**
     * @param issafety the issafety to set
     */
    public void setIssafety(String issafety) {
        this.issafety = issafety;
    }

    /**
     * @return the qtyEconomic
     */
    public BigDecimal getQtyEconomic() {
        return qtyEconomic;
    }

    /**
     * @param qtyEconomic the qtyEconomic to set
     */
    public void setQtyEconomic(BigDecimal qtyEconomic) {
        this.qtyEconomic = qtyEconomic;
    }

    /**
     * @return the qtyLowInv
     */
    public BigDecimal getQtyLowInv() {
        return qtyLowInv;
    }

    /**
     * @param qtyLowInv the qtyLowInv to set
     */
    public void setQtyLowInv(BigDecimal qtyLowInv) {
        this.qtyLowInv = qtyLowInv;
    }

    /**
     * @return the sparecode
     */
    public String getSparecode() {
        return sparecode;
    }

    /**
     * @param sparecode the sparecode to set
     */
    public void setSparecode(String sparecode) {
        this.sparecode = sparecode;
    }

    /**
     * @return the iwiid
     */
    public String getIwiid() {
        return iwiid;
    }

    /**
     * @param iwiid the iwiid to set
     */
    public void setIwiid(String iwiid) {
        this.iwiid = iwiid;
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
     * @return the defBinid
     */
    public String getDefBinid() {
        return defBinid;
    }

    /**
     * @param defBinid the defBinid to set
     */
    public void setDefBinid(String defBinid) {
        this.defBinid = defBinid;
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
}
