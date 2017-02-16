package com.timss.inventory.vo;

/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: MTPurOrderVO.java
 * @author: 890166
 * @createDate: 2014-7-23
 * @updateUser: 890166
 * @version: 1.0
 */
public class MTPurOrderVO {

    private String sheetno;
    private String sheetname;
    private String companyname;
    private String dhdate;
    private String applyTypeName; // 采购申请类型
    private String applyuser;
    private String applyuserid;

	public String getApplyuserid() {
		return applyuserid;
	}

	public void setApplyuserid(String applyuserid) {
		this.applyuserid = applyuserid;
	}

	public String getApplyuser() {
		return applyuser;
	}

	public void setApplyuser(String applyuser) {
		this.applyuser = applyuser;
	}

	/**
     * @return the applyTypeName
     */
    public String getApplyTypeName() {
        return applyTypeName;
    }

    /**
     * @param applyTypeName the applyTypeName to set
     */
    public void setApplyTypeName(String applyTypeName) {
        this.applyTypeName = applyTypeName;
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

    /**
     * @return the sheetname
     */
    public String getSheetname() {
        return sheetname;
    }

    /**
     * @param sheetname the sheetname to set
     */
    public void setSheetname(String sheetname) {
        this.sheetname = sheetname;
    }

    /**
     * @return the companyname
     */
    public String getCompanyname() {
        return companyname;
    }

    /**
     * @param companyname the companyname to set
     */
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    /**
     * @return the dhdate
     */
    public String getDhdate() {
        return dhdate;
    }

    /**
     * @param dhdate the dhdate to set
     */
    public void setDhdate(String dhdate) {
        this.dhdate = dhdate;
    }

}
