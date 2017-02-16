package com.timss.finance.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.timss.finance.bean.FinanceManagementApply;

public class FinanceManagementApplyDtlVo extends FinanceManagementApply{
	private ArrayList<HashMap<String, Object>> attachMap;
	
	List<FinanceMainDetailVo> financeMainDetailVos;

	public ArrayList<HashMap<String, Object>> getAttachMap() {
		return attachMap;
	}

	public void setAttachMap(ArrayList<HashMap<String, Object>> attachMap) {
		this.attachMap = attachMap;
	}

	public List<FinanceMainDetailVo> getFinanceMainDetailVos() {
		return financeMainDetailVos;
	}

	public void setFinanceMainDetailVos(
			List<FinanceMainDetailVo> financeMainDetailVos) {
		this.financeMainDetailVos = financeMainDetailVos;
	}
	
	
}
