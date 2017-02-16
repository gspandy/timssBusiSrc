package com.timss.inventory.bean;

import java.math.BigDecimal;
import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturnsDetail.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatReturnsDetail extends ItcMvcBean {

    private static final long serialVersionUID = -7862985109407900955L;

    @AutoGen(value = "INV_MRDETAIL_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imrsdid;  //退货明细ID

    private String imtdid; //物资接收单明细ID
    private String imtno;  //物资接收编号
    private BigDecimal curReturnQty; 
    private BigDecimal returnQty;  //退货数量
    private String imrsid;  //物资退货单id

    /**
     * @return the 退货明细ID
     */
    public String getImrsdid() {
        return imrsdid;
    }

    /**
     * @param imrsdid 退货明细ID
     */
    public void setImrsdid(String imrsdid) {
        this.imrsdid = imrsdid;
    }

    /**
     * @return 物资接收单明细ID
     */
    public String getImtdid() {
        return imtdid;
    }

    /**
     * @param imtdid 物资接收单明细ID
     */
    public void setImtdid(String imtdid) {
        this.imtdid = imtdid;
    }

    /**
     * @return 物资接收编号
     */
    public String getImtno() {
        return imtno;
    }

    /**
     * @param imtno 物资接收编号
     */
    public void setImtno(String imtno) {
        this.imtno = imtno;
    }

    /**
     * @return the curReturnQty
     */
    public BigDecimal getCurReturnQty() {
        return curReturnQty;
    }

    /**
     * @param curReturnQty the curReturnQty to set
     */
    public void setCurReturnQty(BigDecimal curReturnQty) {
        this.curReturnQty = curReturnQty;
    }

    /**
     * @return 退货数量
     */
    public BigDecimal getReturnQty() {
        return returnQty;
    }

    /**
     * @param returnQty 退货数量
     */
    public void setReturnQty(BigDecimal returnQty) {
        this.returnQty = returnQty;
    }

    /**
     * @return 物资退货单id
     */
    public String getImrsid() {
        return imrsid;
    }

    /**
     * @param imrsid 物资退货单id
     */
    public void setImrsid(String imrsid) {
        this.imrsid = imrsid;
    }

}
