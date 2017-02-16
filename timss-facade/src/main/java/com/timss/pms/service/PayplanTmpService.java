package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.vo.PayplanTmpVo;

public interface PayplanTmpService {
	int updatePayplanTmp(List<PayplanTmp> payplanTmps,Contract contract,String flowId);
	
	List<PayplanTmpVo> queryPayplanTmpByFlowId(String flowId);
}
