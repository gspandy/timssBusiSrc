package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Payplan;
import com.timss.pms.vo.PayplanVo;

/**
 * 结算计划dao
 * @ClassName:     PayplanDao
 * @company: gdyd
 * @Description:TODO
 * @author:    黄晓岚
 * @date:   2014-7-14 下午2:47:19
 */
public interface PayplanDao {
	/**
	 * 插入结算计划列表, payplans 不能为空，否则报错
	 * @Title: insertPayplanList
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplans
	 * @return
	 */
	int insertPayplanList(List<Payplan> payplans);
	
	/**
	 * 根据合同id，查询合同结算计划。
	 * @Title: queryPayplanListByContractId
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param id
	 * @return
	 */
	List<PayplanVo> queryPayplanListByContractId(int id);
	
	/**
	 * 更新结算计划的验收状态
	 * @Title: updatePayplanCheckStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplan
	 * @return
	 */
	int updatePayplanCheckStatus(Payplan payplan);
	
	/**
	 * 更新结算计划的结算状态
	 * @Title: updatePayplanPayStatus
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param payplan
	 * @return
	 */
	int updatePayplanPayStatus(Payplan payplan);
	
	PayplanVo queryPayplanById(int id);
	
	int deletePayplanByContractId(int contractId);
	
	int deletePayplanById(int id);
	
	int updatePayplan(Payplan payplan);
}
