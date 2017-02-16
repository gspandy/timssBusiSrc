package com.timss.inventory.vo;

import java.math.BigDecimal;

import com.timss.inventory.bean.InvItemBaseField;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatApplyToWorkOrder.java
 * @author: 890166
 * @createDate: 2014-9-30
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatApplyToWorkOrder extends InvItemBaseField {

    private static final long serialVersionUID = 1L;

    private BigDecimal qtyStock; // 库存数量
    private BigDecimal qtyApply; // 申请数量
    private BigDecimal outQty; // 实际出库数量
    private BigDecimal refundQty; // 实际退库数量

    private String unit1; // 单位
    private String sheetno; // 领料申请单号
    private String woSheetno; // 工单申请单号
    private String applyType; // 领料类型

    private String warehousename;// 仓库名称
    private String invcatename;// 物资分类名称

    /**
     * @return the warehousename
     */
    public String getWarehousename() {
        return warehousename;
    }

    /**
     * @param warehousename the warehousename to set
     */
    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
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
     * @return the applyType
     */
    public String getApplyType() {
        return applyType;
    }

    /**
     * @param applyType the applyType to set
     */
    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    /**
     * @return the refundQty
     */
    public BigDecimal getRefundQty() {
        return refundQty;
    }

    /**
     * @param refundQty the refundQty to set
     */
    public void setRefundQty(BigDecimal refundQty) {
        this.refundQty = refundQty;
    }

    /**
     * @return the outQty
     */
    public BigDecimal getOutQty() {
        return outQty;
    }

    /**
     * @param outQty the outQty to set
     */
    public void setOutQty(BigDecimal outQty) {
        this.outQty = outQty;
    }

    /**
     * @return the qtyStock
     */
    public BigDecimal getQtyStock() {
        return qtyStock;
    }

    /**
     * @param qtyStock the qtyStock to set
     */
    public void setQtyStock(BigDecimal qtyStock) {
        this.qtyStock = qtyStock;
    }

    /**
     * @return the woSheetno
     */
    public String getWoSheetno() {
        return woSheetno;
    }

    /**
     * @param woSheetno the woSheetno to set
     */
    public void setWoSheetno(String woSheetno) {
        this.woSheetno = woSheetno;
    }

    /**
     * @return the qtyApply
     */
    public BigDecimal getQtyApply() {
        return qtyApply;
    }

    /**
     * @param qtyApply the qtyApply to set
     */
    public void setQtyApply(BigDecimal qtyApply) {
        this.qtyApply = qtyApply;
    }

    /**
     * @return the unit1
     */
    public String getUnit1() {
        return unit1;
    }

    /**
     * @param unit1 the unit1 to set
     */
    public void setUnit1(String unit1) {
        this.unit1 = unit1;
    }

    /**
     * @return the sheetno
     */
    public String getSheetno() {
        return sheetno;
    }

    /**
     * @param sheetno the sheetno to set
     */
    public void setSheetno(String sheetno) {
        this.sheetno = sheetno;
    }

}
