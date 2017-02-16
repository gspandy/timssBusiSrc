package com.timss.inventory.bean;

import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;

/**
 * 计算公式记录表
 * 
 * @author yuanzh
 */
public class InvFormulaDef {

    @UUIDGen ( requireType = GenerationType.REQUIRED_NULL )
    private String ifdId;	 // 定义id
    private String fieldCode;     // 字段编码
    private String formulaContent; // 公式内容
    private String siteid;	// 站点id

    /**
     * @return the ifdId
     */
    public String getIfdId () {
	return ifdId;
    }

    /**
     * @param ifdId the ifdId to set
     */
    public void setIfdId ( String ifdId ) {
	this.ifdId = ifdId;
    }

    /**
     * @return the fieldCode
     */
    public String getFieldCode () {
	return fieldCode;
    }

    /**
     * @param fieldCode the fieldCode to set
     */
    public void setFieldCode ( String fieldCode ) {
	this.fieldCode = fieldCode;
    }

    /**
     * @return the formulaContent
     */
    public String getFormulaContent () {
	return formulaContent;
    }

    /**
     * @param formulaContent the formulaContent to set
     */
    public void setFormulaContent ( String formulaContent ) {
	this.formulaContent = formulaContent;
    }

    /**
     * @return the siteid
     */
    public String getSiteid () {
	return siteid;
    }

    /**
     * @param siteid the siteid to set
     */
    public void setSiteid ( String siteid ) {
	this.siteid = siteid;
    }

}
