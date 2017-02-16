package com.timss.ptw.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * '
 * 
 * @title: 标准操作票操作项bean
 * @description: {desc}
 * @company: gdyd
 * @className: SptoInfo.java
 * @author: gucw
 * @createDate: 2015年7月9日
 * @updateUser:
 * @version: 1.0
 */
public class PtoOperItem extends ItcMvcBean {

    /**
     * 
     */
    private static final long serialVersionUID = 8771815817638353987L;
    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String id;
    private String ptoId;
    private Integer showOrder;
    private String content;
    private String hasOper;
    private String operTime;
    private String remarks;
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtoId() {
        return ptoId;
    }

    public void setPtoId(String ptoId) {
        this.ptoId = ptoId;
    }

    public String getHasOper() {
        return hasOper;
    }

    public void setHasOper(String hasOper) {
        this.hasOper = hasOper;
    }

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
        this.operTime = operTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getShowOrder() {
        return showOrder;
    }

    public void setShowOrder(Integer showOrder) {
        this.showOrder = showOrder;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PtoOperItem [id=" + id + ", ptoId=" + ptoId + ", showOrder=" + showOrder + ", content=" + content
                + ", hasOper=" + hasOper + ", operTime=" + operTime + ", remarks=" + remarks + "]";
    }

   

    

}
