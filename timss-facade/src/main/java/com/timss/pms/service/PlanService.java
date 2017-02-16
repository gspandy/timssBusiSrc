package com.timss.pms.service;

import java.util.Map;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.itc.dto.Page;
import java.util.*;
import com.timss.pms.bean.*;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.PlanVo;

/**
 * 
 * @ClassName:     PlanService
 * @company: gdyd
 * @Description:年度计划service接口类
 * @author:    黄晓岚
 * @date:   2014-6-18 下午3:35:10
 */
public interface PlanService {
	/**
	 * 
	 * @Title: queryPlanList
	 * @Description: 查询年度计划信息
	 * @param: @param page 查询条件，包括分页信息
	 * @param: @param userInfo 用户信息
	 * @return: Page<Plan>  包含的分页信息的年度计划信息
	 * @throws
	 */
	public Page<PlanVo> queryPlanList(Page<PlanVo> page,UserInfoScope userInfo);
	
	/**
	 * 
	 * @Title: queryPlanListByKeyWord
	 * @Description: 根据关键字，即项目名称模块查询最多10条记录
	 * @param kw
	 * @return
	 */
	public List<Map> queryPlanListByKeyWord(String kw);
	
	/**
	 * 
	 * @Title: insertPlan
	 * @Description: 插入一个年度计划
	 * @param: @param plan  年度计划bean类
	 * @return: void   
	 * @throws
	 */
	public void insertPlan(Plan plan);
	
	/**
	 * 
	 * @Title: tmpInsertPlan
	 * @Description: 插入一个暂存状态的年度计划
	 * @param: @param plan  年度计划bean类
	 * @return: void   
	 * @throws
	 */
	public void tmpInsertPlan(Plan plan);
	
	/**
	 * 
	 * @Title: updatePlan
	 * @Description: 修改年度计划信息,并提交
	 * @param plan
	 */
	public void updatePlan(Plan plan);
	
	/**
	 * 
	 * @Title: updatePlan
	 * @Description: 修改年度计划信息,之后年度计划还是草稿状态
	 * @param plan
	 */
	public void tmpUpdatePlan(Plan plan);
	
	/**
	 * 
	 * @Title: queryPlanById
	 * @Description: 根据id查询年度计划
	 * @param id
	 * @return
	 */
	public PlanDtlVo queryPlanById(String id);
	
	/**
	 * 
	 * @Title: deletePlan
	 * @Description: 根据id删除年度计划
	 * @param id
	 * @return
	 */
	public int deletePlan(String id);
	
	public int changePlan(Plan plan);
	
	public int changePlan(Plan plan,String processInstId);
	
	/**
	 * 查询实际成本
	 * @Title: queryActualCost
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param planId
	 * @return
	 */
	public double getActualCost(String planId);
	
	/**
	 * 查询实际收入
	 * @Title: queryActualIncome
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param income
	 * @return
	 */
	public double getActualIncome(String planId);
}
