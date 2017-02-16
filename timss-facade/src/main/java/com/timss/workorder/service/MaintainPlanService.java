package com.timss.workorder.service;

import java.util.Map;

import com.timss.workorder.bean.MaintainPlan;
import com.yudean.itc.dto.Page;

/**
 * 维护计划 Service接口
 * @author 王中华
 * 2014-6-11
 */
public interface MaintainPlanService {
	/**
	 * 
	 * @description:插入
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param maintainPlan
	 * @return:
	 */
	 MaintainPlan insertMaintainPlan(Map<String,String> maintainPlanData ) throws Exception;
	/**
	 *
	 * @description: 查询
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param data
	 * @param userinfoScope
	 * @return
	 * @throws Exception:
	 */
	 Page<MaintainPlan> queryAllMTP(Page<MaintainPlan> page) throws Exception;
	/**
	 * @description: 查询父维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-18
	 * @param page
	 * @return:
	 */
	Page<MaintainPlan> queryAllParentMTP(Page<MaintainPlan> page);
	 /**
	 * @description: 查询维护计划基本信息
	 * @author: 王中华
	 * @createDate: 2014-7-2
	 * @param id 维护计划Id
	 * @return:
	 */
	MaintainPlan queryMTPById(int id);
	 
	 /**
	 * @description: 获取下一个插入记录的ID
	 * @author: 王中华
	 * @createDate: 2014-6-25
	 * @return:
	 */
	int getNextMTPId();
	
	/**
	 * @description: 更新维护计划数据信息
	 * @author: 王中华
	 * @createDate: 2014-7-2
	 * @param maintainPlanData:
	 * @throws Exception 
	 */
	void updateMaintainPlan(Map<String, String> maintainPlanData) throws Exception;
	/**
	 * @description:禁用维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-23
	 * @param maintainPlanId:
	 */
	void updateMTPToUnvailable(int maintainPlanId);

	
}
