package com.timss.pms.vo;

import java.util.ArrayList;
import java.util.HashMap;

import com.timss.pms.bean.BidResult;

public class BidResultDtlVo extends BidResult{

	private  ArrayList<HashMap<String,Object>> attachMap;
	
	private String projectName;
	
	private String projectProperty;
	

	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectProperty() {
		return projectProperty;
	}

	public void setProjectProperty(String projectProperty) {
		this.projectProperty = projectProperty;
	}
	
	
	
	
}
