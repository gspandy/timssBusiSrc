package com.timss.itsm.service;

import java.util.Map;

import com.timss.itsm.bean.ItsmJobPlan;
import com.yudean.itc.dto.Page;
/**
 *  作业方案 Service接口
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmJobPlanService {

	/**
	 * @description:
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param jobPlanData
	 * @return 返回插入的作业方案的ID值
	 * @throws Exception:
	 */
	int insertJobPlan(Map<String,String> jobPlanData) throws Exception;
	/**
	 * 更新作业方案
	 * @param id
	 * @return 
	 * @throws Exception 
	 */
	int updateJobPlan(Map<String,String> jpDataMap) throws Exception;
	/**
	 * @description:禁用作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-23
	 * @param jobPlanId:
	 */
	void updateJPToUnvailable(int jobPlanId);
	/***
	 * 
	 * @description:查询标识作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param page
	 * @return
	 * @throws Exception:
	 */
	Page<ItsmJobPlan> queryStandardJP(Page<ItsmJobPlan> page)  throws Exception;
	
	/**
	 * @description:根据ID 查询作业方案
	 * @author: 王中华
	 * @createDate: 2014-6-18
	 * @param id
	 * @return:
	 */
	Map<String,Object> queryJPById(Integer id);
	/**
	 * @description:根据工单ID查询工单策划时的策划信息
	 * @author: 王中华
	 * @createDate: 2014-7-12
	 * @param valueOf
	 * @return:
	 */
	ItsmJobPlan queryPlanJPByWOId(String woId);
	
	/**
	 * @description:根据工单号，删除对应的汇报作业方案
	 * @author: 王中华
	 * @createDate: 2014-7-12
	 * @param woId:
	 */
	void deleteWOReportByWOId(String woId);
	/**
	 * @description:根据作业方案ID删除作业方案的信息 
	 * @author: 王中华
	 * @createDate: 2014-7-12
	 * @param id:
	 */
	void deleteJobPlanById(int id);
	
	
	
	
}
