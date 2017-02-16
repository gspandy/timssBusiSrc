package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;

/**
 * 合同详细页面对应vo
 * @ClassName:     ContractDtlVo
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 上午11:25:00
 */
public class ContractDtlVo extends Contract{

	private  ArrayList<HashMap<String,Object>> attachMap;
	
	private List<PayplanVo> payplans;
	
	private ProjectDtlVo projectDtlVo;
	
	private String projectName;
	
	private String bidName;

	/**
	 * 前台显示附件需要的信息
	 * @Title: getAttachMap
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	/**
	 * 合同所属的结算计划信息
	 * @Title: getPayplans
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @return
	 */
	public List<PayplanVo> getPayplans() {
		return payplans;
	}

	public void setPayplans(List<PayplanVo> payplans) {
		this.payplans = payplans;
	}

	public ProjectDtlVo getProjectDtlVo() {
		return projectDtlVo;
	}

	public void setProjectDtlVo(ProjectDtlVo projectDtlVo) {
		this.projectDtlVo = projectDtlVo;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBidName() {
		return bidName;
	}

	public void setBidName(String bidName) {
		this.bidName = bidName;
	}
	
	
	
}
