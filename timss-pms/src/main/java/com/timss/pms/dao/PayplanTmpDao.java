
package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.PayplanTmp;
import com.timss.pms.vo.PayplanTmpVo;

/**
 * 结算计划dao
 * @ClassName:     PayplanDao
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 下午2:47:19
 */
public interface PayplanTmpDao {
	/**
	 * 插入结算计划列表, payplans 不能为空，否则报错
	 * @Title: insertPayplanList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplans
	 * @return
	 */
	int insertPayplanTmpList(List<PayplanTmp> payplans);
	
	/**
	 * 根据合同id，查询合同结算计划。
	 * @Title: queryPayplanListByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<PayplanTmpVo> queryPayplanTmpListByContractId(int contractId);
	
	PayplanTmpVo queryPayplanById(int id);
	
	int deletePayplanTmpByContractId(int contractId);
	
	int deletePayplanTmpByFlowId(String flowId);
	
	List<PayplanTmpVo> queryPayplanTmpListByFlowId(String flowId);
	
	
}
