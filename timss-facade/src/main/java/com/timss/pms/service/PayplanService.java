package com.timss.pms.service;

import java.util.List;

import com.timss.pms.bean.Contract;
import com.timss.pms.bean.Payplan;
import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.vo.PayplanVo;

/**
 * 结算计划service接口
 * @ClassName:     PayplanService
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-24 上午10:43:36
 */
public interface PayplanService {
	/**
	 * 插入结算计划
	 * @Title: insertPayplan
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplans
	 */
	void insertPayplan(List<Payplan> payplans);
	
	/**
	 * 根据合同id，查询结算计划
	 * @Title: queryPayplanListByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<PayplanVo> queryPayplanListByContractId(int id);
	/**
	 * 更新结算计划验收状态为通过
	 * @Title: updateCheckStatusApproval
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean updateCheckStatusApproval(int id);
	/**
	 * 更新结算计划验收状态为验收中
	 * @Title: updateCheckStatusApproving
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean updateCheckStatusApproving(int id);
	/**
	 * 更新结算计划验收状态为未验收
	 * @Title: resetCheckStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean resetCheckStatus(int id);
	
	/**
	 * 更新结算计划的结算状态为已结算
	 * @Title: updatePayStatusApproval
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean updatePayStatusApproval(int id);
	
	/**
	 * 更新结算状态为结算中
	 * @Title: updatePayStatusApproving
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean updatePayStatusApproving(int id);
	
	/**
	 * 重置结算状态为未结算
	 * @Title: resetPayStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	boolean resetPayStatus(int id);
	
	/**
	 * 根据合同id，获取可以验收的验收阶段
	 * @Title: getCheckableByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<PayplanVo> getCheckableByContractId(int id);
	
	/**
	 * 根据合同id，获取可以结算的结算计划
	 * @Title: getPayableByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<PayplanVo> getPayableByContractId(int id);
	
	PayplanVo queryPayplanById(int id);
	
	/**
	 * 更新结算计划信息，先删除后增加
	 * @Title: updatePayplan
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplans
	 * @param contract
	 * @return
	 */
	int updatePayplan(List<Payplan> payplans,Contract contract);
	
	/**
	 * 当结算变更审批通过后，用临时保存的结算计划信息更新结算计划
	 * @Title: updatePayplanByPayplanTmp
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplanTmps
	 * @param contract
	 * @return
	 */
	int updatePayplanByPayplanTmp(List<PayplanTmp> payplanTmps,Contract contract);
	
	
}
