package com.timss.itsm.dao;

import java.util.List;

import com.timss.itsm.bean.ItsmJobPlan;
import com.yudean.itc.dto.Page;
/**
 * 作业方案
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmJobPlanDao {
	/**
	 *  添加作业方案 ；返回自增主键
	 * @param jobPlan
	 */
	void insertJobPlan(ItsmJobPlan jobPlan);
	/**
	 * 更新作业方案
	 * @param jobPlan
	 */
	void updateJobPlan(ItsmJobPlan jobPlan);
	/**
	 * @description: 禁用作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-23
	 * @param jobPlanId:
	 */
	void updateJPToUnvailable(int jobPlanId);
	/**
	 * 查找标准作业方案
	 * @param page
	 * @return
	 */
	List<ItsmJobPlan> queryStandardJP(Page<ItsmJobPlan> page);
	/**
	 * @description:查找标准作业方案(IT)
	 * @author: 王中华
	 * @createDate: 2014-9-19
	 * @param page
	 * @return:
	 */
	List<ItsmJobPlan> queryITStandardJP(Page<ItsmJobPlan> page);
	/**
	 * 根据ID查询
	 * @param id
	 * @return
	 */
	ItsmJobPlan queryJPById(int id);
	/**
	 * @description:根据ID查询(IT)
	 * @author: 王中华
	 * @createDate: 2014-9-19
	 * @param jpId
	 * @return:
	 */
	ItsmJobPlan queryITJPById(int jpId);
	/**
	 * @description: 删除某个作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @return:
	 */
	int deleteJPById(int jpId);
	
	/**
	 * @description: 获取下一个插入记录的ID
	 * @author: 王中华
	 * @createDate: 2014-6-25
	 * @return:
	 */
	int getNextJPId();
	
	/**
	 * @description:查询某个工单汇报对应的作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-11
	 * @param woId:
	 */
	ItsmJobPlan queryReportJPByWOId(String woId);
	
	/**
	 * @description:查询某个工单策划对应的作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-11  
	 * @param woId:
	 */
	ItsmJobPlan queryPlanJPByWOId(String woId);
	

	
	
	
	
}
