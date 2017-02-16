package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.timss.pms.bean.Bidding;

public class BiddingDtlVo extends Bidding{
	private String projectName ;
	
	private String businessLeader;
	private String projectLeader;
	
	private ArrayList<HashMap<String, Object>> attachMap;
	
	private ArrayList<HashMap<String, Object>> resultAttachMap;
	
	private List<BiddingSupplierVo> biddingSupplierVos;

	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getBusinessLeader() {
		return businessLeader;
	}

	public void setBusinessLeader(String businessLeader) {
		this.businessLeader = businessLeader;
	}

	

	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	public List<BiddingSupplierVo> getBiddingSupplierVos() {
		return biddingSupplierVos;
	}

	public void setBiddingSupplierVos(List<BiddingSupplierVo> biddingSupplierVos) {
		this.biddingSupplierVos = biddingSupplierVos;
	}

	public ArrayList<HashMap<String, Object>> getResultAttachMap() {
		return resultAttachMap;
	}

	public void setResultAttachMap(
			ArrayList<HashMap<String, Object>> resultAttachMap) {
		this.resultAttachMap = resultAttachMap;
	}
	
    
}
