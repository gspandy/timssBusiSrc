package com.timss.inventory.bean;

import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资详细信息主体信息
 * @description: 物资详细信息主体信息
 * @company: gdyd
 * @className: InvItemBaseField.java
 * @author: yuanzh
 * @createDate: 2016-4-27
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class InvItemBaseField extends ItcMvcBean {

    private static final long serialVersionUID = 1L;

    private String	    itemid;	       // 物资ID
    private String	    itemcode;	     // 物资编码
    private String	    itemname;	     // 物资名称
    private String	    cusmodel;	     // 物资参数
    private String	    warehouseid;	  // 仓库ID
    private String	    invcateid;	    // 物资类别

    /**
     * @return the itemid
     */
    public String getItemid () {
	return itemid;
    }

    /**
     * @param itemid the itemid to set
     */
    public void setItemid ( String itemid ) {
	this.itemid = itemid;
    }

    /**
     * @return the itemcode
     */
    public String getItemcode () {
	return itemcode;
    }

    /**
     * @param itemcode the itemcode to set
     */
    public void setItemcode ( String itemcode ) {
	this.itemcode = itemcode;
    }

    /**
     * @return the itemname
     */
    public String getItemname () {
	return itemname;
    }

    /**
     * @param itemname the itemname to set
     */
    public void setItemname ( String itemname ) {
	this.itemname = itemname;
    }

    /**
     * @return the cusmodel
     */
    public String getCusmodel () {
	return cusmodel;
    }

    /**
     * @param cusmodel the cusmodel to set
     */
    public void setCusmodel ( String cusmodel ) {
	this.cusmodel = cusmodel;
    }

    /**
     * @return the warehouseid
     */
    public String getWarehouseid () {
	return warehouseid;
    }

    /**
     * @param warehouseid the warehouseid to set
     */
    public void setWarehouseid ( String warehouseid ) {
	this.warehouseid = warehouseid;
    }

    /**
     * @return the invcateid
     */
    public String getInvcateid () {
	return invcateid;
    }

    /**
     * @param invcateid the invcateid to set
     */
    public void setInvcateid ( String invcateid ) {
	this.invcateid = invcateid;
    }

}
