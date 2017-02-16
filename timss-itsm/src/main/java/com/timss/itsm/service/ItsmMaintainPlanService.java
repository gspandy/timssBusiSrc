package com.timss.itsm.service;

import java.util.Map;

import com.timss.itsm.bean.ItsmMaintainPlan;
import com.yudean.itc.dto.Page;

/**
 * 维护计划 Service接口
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmMaintainPlanService {
	/**
	 * 
	 * @description:插入
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param maintainPlan
	 * @return:
	 */
	 ItsmMaintainPlan insertMaintainPlan(Map<String,String> maintainPlanData ) throws Exception;
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
	 Page<ItsmMaintainPlan> queryAllMTP(Page<ItsmMaintainPlan> page) throws Exception;
	/**
	 * @description: 查询父维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-18
	 * @param page
	 * @return:
	 */
	Page<ItsmMaintainPlan> queryAllParentMTP(Page<ItsmMaintainPlan> page);
	 /**
	 * @description: 查询维护计划基本信息
	 * @author: 王中华
	 * @createDate: 2014-7-2
	 * @param id 维护计划Id
	 * @return:
	 */
	ItsmMaintainPlan queryMTPById(int id);
	 
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
	/**
	 * @description:查询维护计划
	 * @author: 王中华
	 * @createDate: 2015-2-6
	 * @param valueOf
	 * @return:
	 */
	ItsmMaintainPlan queryITMTPById(Integer id);

	
}
