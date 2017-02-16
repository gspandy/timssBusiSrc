package com.timss.itsm.bean;

import com.yudean.mvc.bean.ItcMvcBean;

public class ItsmWoPriConfig extends ItcMvcBean{

	private static final long serialVersionUID = 465341753563442092L;
	private int priId; // ID
	private String urgentDegree;//紧急程度
	private String influenceScope;//影响范围
	
	 
	public int getPriId() {
		return priId;
	}
	public void setPriId(int priId) {
		this.priId = priId;
	}
	 
	public String getUrgentDegree() {
		return urgentDegree;
	}
	public void setUrgentDegree(String urgentDegree) {
		this.urgentDegree = urgentDegree;
	}
	public String getInfluenceScope() {
		return influenceScope;
	}
	public void setInfluenceScope(String influenceScope) {
		this.influenceScope = influenceScope;
	}
	@Override
	public String toString() {
		return "WoPriConfig [priId=" + priId + ", urgentDegree=" + urgentDegree
				+ ", influenceScope=" + influenceScope + "]";
	}


	
	
	




}
