package com.timss.inventory.bean;

import java.util.Date;

import com.yudean.itc.annotation.AutoGen;
import com.yudean.itc.annotation.UUIDGen;
import com.yudean.itc.annotation.UUIDGen.GenerationType;
import com.yudean.mvc.bean.ItcMvcBean;

/**
 * @title: 物资验收表单类
 * @description: 物资验收表单类
 * @company: gdyd
 * @className: InvMatAccept.java
 * @author: yuanzh
 * @createDate: 2015-10-30
 * @updateUser: yuanzh
 * @version: 1.0
 */
@SuppressWarnings("serial")
public class InvMatAccept extends ItcMvcBean {

    @UUIDGen(requireType = GenerationType.REQUIRED_NULL)
    private String inacId;
    @AutoGen(requireType = com.yudean.itc.annotation.AutoGen.GenerationType.REQUIRED_NULL, value="INV_MATACCEPT_CODE")
    private String inacNo;
    private String poId;
    private String poSheetno;
    private String deliveryName;
    private Date deliveryDate;
    private String deliveryMan;
    private String specialMaterials;
    private String acptCnlus;
    private String procRult;
    private String acptType;
    private String instanceid;
    private String delFlag;
    private String status;
    private String problems;
    
    private String createusername;
    private String poName;
    
    private String wareHouseUser;//仓管员ID
    private String imtId;//相关联的物资接收单ID
    private String autoDelivery; //是否即收即发
    /**
     * @return the inacId
     */
    public String getInacId() {
    	
        return inacId;
    }

    /**
     * @param inacId the inacId to set
     */
    public void setInacId(String inacId) {
        this.inacId = inacId;
    }

    /**
     * @return the inacNo
     */
    public String getInacNo() {
        return inacNo;
    }

    /**
     * @param inacNo the inacNo to set
     */
    public void setInacNo(String inacNo) {
        this.inacNo = inacNo;
    }

    /**
     * @return the poId
     */
    public String getPoId() {
        return poId;
    }

    /**
     * @param poId the poId to set
     */
    public void setPoId(String poId) {
        this.poId = poId;
    }

    /**
     * @return the poSheetno
     */
    public String getPoSheetno() {
        return poSheetno;
    }

    /**
     * @param poSheetno the poSheetno to set
     */
    public void setPoSheetno(String poSheetno) {
        this.poSheetno = poSheetno;
    }

    /**
     * @return the deliveryName
     */
    public String getDeliveryName() {
        return deliveryName;
    }

    /**
     * @param deliveryName the deliveryName to set
     */
    public void setDeliveryName(String deliveryName) {
        this.deliveryName = deliveryName;
    }

    /**
     * @return the deliveryDate
     */
    public Date getDeliveryDate() {
        return deliveryDate;
    }

    /**
     * @param deliveryDate the deliveryDate to set
     */
    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    /**
     * @return the deliveryMan
     */
    public String getDeliveryMan() {
        return deliveryMan;
    }

    /**
     * @param deliveryMan the deliveryMan to set
     */
    public void setDeliveryMan(String deliveryMan) {
        this.deliveryMan = deliveryMan;
    }

    /**
     * @return the specialMaterials
     */
    public String getSpecialMaterials() {
        return specialMaterials;
    }

    /**
     * @param specialMaterials the specialMaterials to set
     */
    public void setSpecialMaterials(String specialMaterials) {
        this.specialMaterials = specialMaterials;
    }

    /**
     * @return the acptCnlus
     */
    public String getAcptCnlus() {
        return acptCnlus;
    }

    /**
     * @param acptCnlus the acptCnlus to set
     */
    public void setAcptCnlus(String acptCnlus) {
        this.acptCnlus = acptCnlus;
    }

    /**
     * @return the procRult
     */
    public String getProcRult() {
        return procRult;
    }

    /**
     * @param procRult the procRult to set
     */
    public void setProcRult(String procRult) {
        this.procRult = procRult;
    }

    /**
     * @return the instanceid
     */
    public String getInstanceid() {
        return instanceid;
    }

    /**
     * @param instanceid the instanceid to set
     */
    public void setInstanceid(String instanceid) {
        this.instanceid = instanceid;
    }

    /**
     * @return the delFlag
     */
    public String getDelFlag() {
        return delFlag;
    }

    /**
     * @param delFlag the delFlag to set
     */
    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the problems
     */
    public String getProblems() {
        return problems;
    }

    /**
     * @param problems the problems to set
     */
    public void setProblems(String problems) {
        this.problems = problems;
    }

	public String getCreateusername() {
		return createusername;
	}

	public void setCreateusername(String createusername) {
		this.createusername = createusername;
	}

	public String getPoName() {
		return poName;
	}

	public void setPoName(String poName) {
		this.poName = poName;
	}

	public String getAcptType() {
		return acptType;
	}

	public void setAcptType(String acptType) {
		this.acptType = acptType;
	}

	/**
	 * @return the wareHouseUser
	 */
	public String getWareHouseUser() {
		return wareHouseUser;
	}

	/**
	 * @param wareHouseUser the wareHouseUser to set
	 */
	public void setWareHouseUser(String wareHouseUser) {
		this.wareHouseUser = wareHouseUser;
	}

	/**
	 * @return the imtId
	 */
	public String getImtId() {
		return imtId;
	}

	/**
	 * @param imtId the imtId to set
	 */
	public void setImtId(String imtId) {
		this.imtId = imtId;
	}

	/**
	 * @return the autoDelivery
	 */
	public String getAutoDelivery() {
		return autoDelivery;
	}

	/**
	 * @param autoDelivery the autoDelivery to set
	 */
	public void setAutoDelivery(String autoDelivery) {
		this.autoDelivery = autoDelivery;
	}

}
