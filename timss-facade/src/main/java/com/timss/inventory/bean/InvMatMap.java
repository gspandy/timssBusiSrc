package com.timss.inventory.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: InvMatMap.java
 * @author: 890151
 * @createDate: 2016-5-27
 * @updateUser: 890151
 * @version: 1.0
 */
public class InvMatMap extends ItcMvcBean {

	private static final long serialVersionUID = 7878797720782850198L;
	private String imtdid;
    private String outterid;
    private String tranType;
    private String itemcode;

    /**
     * @return the itemcode
     */
    public String getItemcode() {
        return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    /**
     * @return the imtdid
     */
    public String getImtdid() {
        return imtdid;
    }

    /**
     * @param imtdid the imtdid to set
     */
    public void setImtdid(String imtdid) {
        this.imtdid = imtdid;
    }

    /**
     * @return the outterid
     */
    public String getOutterid() {
        return outterid;
    }

    /**
     * @param outterid the outterid to set
     */
    public void setOutterid(String outterid) {
        this.outterid = outterid;
    }

    /**
     * @return the tranType
     */
    public String getTranType() {
        return tranType;
    }

    /**
     * @param tranType the tranType to set
     */
    public void setTranType(String tranType) {
        this.tranType = tranType;
    }

}
