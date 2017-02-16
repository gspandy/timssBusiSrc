package com.timss.pms.vo;

import com.timss.pms.bean.Checkout;

/**
 * 项目验收vo类，用于列表
 * @ClassName:     CheckoutVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-22 上午10:06:35
 */
public class CheckoutVo extends Checkout{
	private String contractName;
	private String contractCode;
	private String contractType;
	
        private String checkTypeName;
        private String payTypeName;
        private String statusName;
    
        private String flowid;
        private String projectId;
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getContractCode() {
		return contractCode;
	}
	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getCheckTypeName() {
		return checkTypeName;
	}
	public void setCheckTypeName(String checkTypeName) {
		this.checkTypeName = checkTypeName;
	}
	public String getPayTypeName() {
		return payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}
	public String getStatusName() {
		return statusName;
	}
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getFlowid() {
		return flowid;
	}
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}
        public String getProjectId() {
            return projectId;
        }
        public void setProjectId(String projectId) {
            this.projectId = projectId;
        }
}
