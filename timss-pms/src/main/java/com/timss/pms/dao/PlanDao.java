package com.timss.pms.dao;

import java.util.List;

import com.timss.pms.bean.Plan;
import com.timss.pms.vo.PlanDtlVo;
import com.timss.pms.vo.PlanVo;
import com.yudean.itc.dto.Page;
/**
 * 
 * @ClassName:     PlanDao
 * @company: gdyd
 * @Description: 年度计划模块基本dao接口类
 * @author:    黄晓岚
 * @data:   2014-6-18 下午3:20:23
 */
public interface PlanDao {
	/**
	 * 
	 * @Title: retrivePlanList
	 * @Description: 查询年度计划信息
	 * @param: @param page 查询的条件
	 * @return: List<Plan>  查询结果
	 * @throws
	 */
	List<PlanVo> queryPlanList(Page<PlanVo> page);
	
	/**
	 * 
	 * @Title: insertPlan
	 * @Description: 新建年度计划，id自动生成
	 * @param plan  插入年度计划信息，
	 * @return: void   
	 * @throws
	 */
	int insertPlan(Plan plan);
	
	/**
	 * 
	 * @Title: updatePlan
	 * @Description: 修改年度计划信息
	 * @param plan 年度计划信息
	 * @return
	 */
    int updatePlan(Plan plan);
    
    /**
     * 
     * @Title: queryPlanById
     * @Description: 根据id查询年度计划
     * @param id
     * @return
     */
    PlanDtlVo queryPlanById(int id);
    
    /**
     * 
     * @Title: deletePlan
     * @Description: 根据id删除年度计划,物理删除
     * @param id
     * @return
     */
    int deletePlan(int id);
    
    /**
     * 计算年度计划总数
     * @Title: countPlan
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @return
     */
    Long countPlan();
    
    Double queryActualCost(String planId);
    
    Double queryActualIncome(String planId);
    
    /**
     * 更新年度计划的历史信息
     * @Title: updateHistInfo
     * @param plan
     * @return
     */
    int updateHistInfo(Plan plan);
    
    /**
     * 年度计划的结转次数加一
     * @Title: increaseCarryOverTimes
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @param id
     * @return
     */
    int increaseCarryOverTimes(int id);
	
}
