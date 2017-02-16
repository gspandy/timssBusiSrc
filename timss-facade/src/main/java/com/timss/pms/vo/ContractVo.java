package com.timss.pms.vo;

import com.timss.pms.bean.Contract;

/**
 * 合同vo，用于列表展示
 * @ClassName:     ContractVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 上午10:52:52
 */
public class ContractVo extends Contract{

    private static final long serialVersionUID = -6541688989437640361L;

    private String statusValue;
	
	private String typeName;
	
	private String projectName;
	
	private String flowid;
	
	private String statusChangeValue;
	
	private String belongToName;
	
	private String contractCategoryName;
	
	private String companyId;
	
	private String percent;//结算完成比例(已付款比例)
	public String getFlowid() {
		return flowid;
	}
	public void setFlowid(String flowid) {
		this.flowid = flowid;
	}

	public String getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(String statusValue) {
		this.statusValue = statusValue;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getStatusChangeValue() {
		return statusChangeValue;
	}
	public void setStatusChangeValue(String statusChangeValue) {
		this.statusChangeValue = statusChangeValue;
	}
	public String getBelongToName() {
		return belongToName;
	}
	public void setBelongToName(String belongToName) {
		this.belongToName = belongToName;
	}
	public String getContractCategoryName() {
		return contractCategoryName;
	}
	public void setContractCategoryName(String contractCategoryName) {
		this.contractCategoryName = contractCategoryName;
	}
        public String getCompanyId() {
            return companyId;
        }
        public void setCompanyId(String companyId) {
            this.companyId = companyId;
        }
        public String getPercent() {
            return percent;
        }
        public void setPercent(String percent) {
            this.percent = percent;
        }
}
