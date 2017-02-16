package com.timss.pms.bean;

import com.yudean.mvc.bean.ItcMvcBean;
import com.yudean.itc.annotation.AutoGen;

/**
 * WorkflowBusiness entity. @author MyEclipse Persistence Tools
 * 流程id与业务id关系bean
 */

public class WorkflowBusiness extends ItcMvcBean {

	// Fields
	@AutoGen("PMS_PROJECT_ADD")
	private String id;
	private String instanceId;
	private String businessId;

	// Constructors

	/** default constructor */
	public WorkflowBusiness() {
	}

	/** minimal constructor */
	public WorkflowBusiness(String id) {
		this.id = id;
	}

	/** full constructor */
	public WorkflowBusiness(String id, String instanceId,
			String businessId) {
		this.id = id;
		this.instanceId = instanceId;
		this.businessId = businessId;
	}

	// Property accessors

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstanceId() {
		return this.instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getBusinessId() {
		return this.businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

}