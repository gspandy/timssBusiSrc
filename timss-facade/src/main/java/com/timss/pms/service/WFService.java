package com.timss.pms.service;

import java.util.Map;

import com.timss.pms.vo.BidResultDtlVo;
import com.timss.pms.vo.BiddingDtlVo;
import com.timss.pms.vo.ContractDtlVo;
import com.timss.pms.vo.PayDtlVo;

/**
 * 工作流相关service类，只是用于pms模块
 * @ClassName:     WFService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-28 下午4:08:59
 */
public interface WFService {
	public  Map<String,Object> createProjectMap(String businessId,String processInstId,String taskId);
	
	public Map<String,Object> createCheckoutMap(String businessInd,String processInstId,String taskId);
	
	public Map<String,Object> createPayMap(PayDtlVo payDtlVo,String processInstId,String taskId);
	
	public Map<String,Object> createPayUndoMap(PayDtlVo payDtlVo,String processInstId,String taskId);
	
	public Map<String,Object> createContractMap(ContractDtlVo contractDtlVo,String processInstId,String taskId);
	
	public Map<String,Object> createContractAppMap(ContractDtlVo contractDtlVo,String processInstId,String taskId);
	
	public Map<String, Object> createContractAppNullifyMap(ContractDtlVo contractDtlVo);
	
	public Map<String,Object> createBidResultMap(BidResultDtlVo bidResultDtlVo);
	
	String queryBusinessIdByFlowId(String proInstId);
	
	public Map<String,Object> createBiddingMap(BiddingDtlVo biddingDtlVo);
}
