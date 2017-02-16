package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.AutoGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatReturns.java
 * @author: 890166
 * @createDate: 2015-3-12
 * @updateUser: 890166
 * @version: 1.0
 */
public class InvMatReturns extends ItcMvcBean {

    private static final long serialVersionUID = 28006077659685336L;

    @AutoGen(value = "INV_MATRETURNS_ID", requireType = GenerationType.REQUIRED_NULL)
    private String imrsid;  //退货单ID

    @AutoGen(value = "INV_MATRETURNS_NO", requireType = GenerationType.REQUIRED_NULL)
    private String imrsno;   //退货单编号
    private String imtid; // 物资接收单ID 或 物资申请单ID
    
    private Date returnDate; //退出日期
    private String operType; //操作类型 operType, returns：退货  refund：退库
    private String returnReason;   // 退货原因 或 退货原因
   
    /**
     * @return 退货单ID
     */
    public String getImrsid() {
        return imrsid;
    }
    /**
     * @param imrsid 退货单ID
     */
    public void setImrsid(String imrsid) {
        this.imrsid = imrsid;
    }
    /**
     * @return 退货单编号
     */
    public String getImrsno() {
        return imrsno;
    }
    /**
     * @param imrsno退货单编号
     */
    public void setImrsno(String imrsno) {
        this.imrsno = imrsno;
    }
    /**
     * @return 物资接收单ID
     */
    public String getImtid() {
        return imtid;
    }
    /**
     * @param imtId 物资接收单ID
     */
    public void setImtid(String imtid) {
        this.imtid = imtid;
    }
    /**
     * @return 退出日期
     */
    public Date getReturnDate() {
        return returnDate;
    }
    /**
     * @param returnDate 退出日期
     */
    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
    /**
     * @return 操作类型 operType,默认returns
     */
    public String getOperType() {
        return operType;
    }
    /**
     * @param operType 操作类型 operType,默认returns
     */
    public void setOperType(String operType) {
        this.operType = operType;
    }
    /**
     * @return 退货原因
     */
    public String getReturnReason() {
        return returnReason;
    }
    /**
     * @param refundReason 退货原因
     */
    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }
    
   

    
}
