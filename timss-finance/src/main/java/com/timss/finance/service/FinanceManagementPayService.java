package com.timss.finance.service;

import java.util.List;
import java.util.Map;

import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.vo.FinanceManagementPayDtlVo;
import com.timss.pms.bean.FlowVoidParamBean;

/**
 * 对管理费用报销进行增删改查service
 * @ClassName:     FinanceManagementPayService
 * @company: gdyd
 * @author:    谷传伟
 * @date:   2015-4-22 上午11:24:00
 */
public interface FinanceManagementPayService {
	/**
	 * @Title: queryFMPtById
	 * @Description: 根据id，查询行政报销
	 * @param id
	 * @return
	 */
	public FinanceManagementPayDtlVo queryFMPtById(String id);
	/**
	 * @Title: queryFMPtByMainId
	 * @Description: 根据Mainid，查询行政报销
	 * @param id
	 * @return
	 */
	FinanceManagementPayDtlVo queryFMPtByMainId(String id);
	/**
	 * @Title: deleteFMP
	 * @Description: 根据id，删除行政报销
	 * @param id
	 * @return
	 */
	void deleteFMP(String id);
	/**
	 * @Title: saveOrUpdateFMP
	 * @Description: 提交行政报销信息(由于可能有多次提交，新增或更新，以及是否启动流程需要依据条件区分处理)
	 * @param fmp
	 * @param startWorkFlow
	 */
	Map<String,Object> saveOrUpdateFMP(FinanceManagementPayDtlVo fmp,List<FinanceMainDetail> fmd,boolean startWorkFlow) throws Exception;
	/**
	 * 
	 * @Title: voidFlow
	 * @Description: 合同作废
	 * @param params
	 * @return
	 */
	public boolean voidFlow(FlowVoidParamBean params);
	
	
	/**
	 * 
	 * @Title: updateFMPApproving
	 * @Description: 修改审批状态 为审批中
	 * @param contract suffix
	 * @return
	 */
	int updateFMPApproving(FinanceManagementPayDtlVo fmp);
	/**
	 * @Title: updateFMPApproving
	 * @Description: 修改状态 为审批中
	 * @param id suffix
	 * @return
	 */
	int updateFMPApproving(String id);
	/**
	 * 
	 * @Title: updateFMPApproved
	 * @Description: 修改状态 为审批中通过
	 * @param contract suffix
	 * @return
	 */
	int updateFMPApproved(FinanceManagementPayDtlVo fmp);
	/**
	 * @Title: updateFMPApproved
	 * @Description: 修改状态 为审批中通过
	 * @param id suffix
	 * @return
	 */
	int updateFMPApproved(String id);
	//获取远程数据的方法
	public List<Map<String, Object>> queryFuzzyByName(String name,String type);
	
}
