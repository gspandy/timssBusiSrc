package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timss.pms.bean.Project;

/**
 * 
 * @ClassName:     ProjectDtlVo
 * @company: gdyd
 * @Description: 返回给项目详细信息页面的vo，可能要包含年度计划的基本信息
 * @author:    黄晓岚
 * @date:   2014-6-25 下午2:33:40
 */
public class ProjectDtlVo extends ProjectVo{
	private String planName;
	
	private  ArrayList<HashMap<String,Object>> attachMap;
	
	
	private List<BidVo> bids;
	
	private List<ContractVo> contracts;
	
	private List<BidResultVo> bidResultVos;
	
	private List<CheckoutVo> checkoutVos;
	
	private List<PayVo> payVos;
	
	private List<WorkloadVo> workloadVos;
	
	private List<OutsourcingVo> outsourcingVos;
	
	private List<MilestoneVo> milestoneVos;
	
	private List<MilestoneHistoryVo> milestoneHistoryVos;

	public String getPlanName() {
		return planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	/**
	 * 
	 * @Title: getAttachMap
	 * @Description: 获得前端需要的附件信息
	 * @return
	 */
	public  ArrayList<HashMap<String,Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap( ArrayList<HashMap<String,Object>> attachMap) {
		this.attachMap = attachMap;
	}

	/**
	 * 项目所属的招标列表
	 * @Title: getBids
	 * @Description: 项目所属的招标列表
	 * @return
	 */
	public List<BidVo> getBids() {
		return bids;
	}

	public void setBids(List<BidVo> bids) {
		this.bids = bids;
	}

	/**
	 * 项目所属的合同列表
	 * @Title: getContracts
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public List<ContractVo> getContracts() {
		return contracts;
	}

	public void setContracts(List<ContractVo> contracts) {
		this.contracts = contracts;
	}

	public List<BidResultVo> getBidResultVos() {
		return bidResultVos;
	}

	public void setBidResultVos(List<BidResultVo> bidResultVos) {
		this.bidResultVos = bidResultVos;
	}

	public List<CheckoutVo> getCheckoutVos() {
		return checkoutVos;
	}

	public void setCheckoutVos(List<CheckoutVo> checkoutVos) {
		this.checkoutVos = checkoutVos;
	}

	public List<PayVo> getPayVos() {
		return payVos;
	}

	public void setPayVos(List<PayVo> payVos) {
		this.payVos = payVos;
	}

	public List<WorkloadVo> getWorkloadVos() {
		return workloadVos;
	}

	public void setWorkloadVos(List<WorkloadVo> workloadVos) {
		this.workloadVos = workloadVos;
	}

	public List<OutsourcingVo> getOutsourcingVos() {
		return outsourcingVos;
	}

	public void setOutsourcingVos(List<OutsourcingVo> outsourcingVos) {
		this.outsourcingVos = outsourcingVos;
	}

	public List<MilestoneVo> getMilestoneVos() {
		return milestoneVos;
	}

	public void setMilestoneVos(List<MilestoneVo> milestoneVos) {
		this.milestoneVos = milestoneVos;
	}

	public List<MilestoneHistoryVo> getMilestoneHistoryVos() {
		return milestoneHistoryVos;
	}

	public void setMilestoneHistoryVos(List<MilestoneHistoryVo> milestoneHistoryVos) {
		this.milestoneHistoryVos = milestoneHistoryVos;
	}
	
	
}
