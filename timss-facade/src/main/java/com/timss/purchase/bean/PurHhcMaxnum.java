package com.timss.purchase.bean;

import java.math.BigDecimal;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: PurHhcMaxnum.java
 * @author: 890166
 * @createDate: 2014-10-31
 * @updateUser: 890166
 * @version: 1.0
 */
public class PurHhcMaxnum extends ItcMvcBean {

    private static final long serialVersionUID = 1L;
    private BigDecimal maxnum; // 审批单号
    private String siteid; // 站点id

    /**
     * @return the maxnum
     */
    public BigDecimal getMaxnum() {
        return maxnum;
    }

    /**
     * @param maxnum the maxnum to set
     */
    public void setMaxnum(BigDecimal maxnum) {
        this.maxnum = maxnum;
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
