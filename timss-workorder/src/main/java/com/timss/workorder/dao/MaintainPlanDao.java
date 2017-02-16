package com.timss.workorder.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.MaintainPlan;
import com.yudean.itc.dto.Page;
 
/**
 * 
 * @title: 维护计划 列表DAO
 * @description: {desc}
 * @company: gdyd
 * @className: MaintainPlanDao.java
 * @author: 王中华
 * @createDate: 2014-6-16
 * @updateUser: 王中华
 * @version: 1.0
 */
/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: MaintainPlanDao.java
 * @author: 王中华
 * @createDate: 2014-7-28
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface MaintainPlanDao {
	/**
	 * 
	 * @description： 插入
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param maintainPlan:
	 */
	void insertMaintainPlan(MaintainPlan maintainPlan);
	/**
	 * 
	 * @description:更新
	 * @author: 890152
	 * @createDate: 2014-6-16
	 * @param maintainPlan:
	 */
	void updateMaintainPlan(MaintainPlan maintainPlan);
	
	/**
	 * 
	 * @description:根据ID查找唯一的结果
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param id
	 * @return:
	 */
	MaintainPlan queryMTPById(int id);
	/**
	 * @description:根据ID查找唯一的结果(IT)
	 * @author: 王中华
	 * @createDate: 2014-9-22
	 * @param id
	 * @return:
	 */
	MaintainPlan queryITMTPById(int id);
	/**
	 *  查询维护计划列表基本上都可以用这个方法，只要你设置传入page的params参数即可
	 * @description:
	 * @author: 王中华
	 * @createDate: 2014-6-16
	 * @param page
	 * @return:
	 */
	List<MaintainPlan> queryAllMTP(Page<MaintainPlan> page);
	/**
	 * @description:查询维护计划列表基本上都可以用这个方法，只要你设置传入page的params参数即可(IT)
	 * @author: 王中华
	 * @createDate: 2014-9-22
	 * @param page
	 * @return:
	 */
	List<MaintainPlan> queryAllITMTP(Page<MaintainPlan> page);
	/**
	 * @description:查询父维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-18
	 * @param page
	 * @return:
	 */
	List<MaintainPlan> queryAllParentMTP(Page<MaintainPlan> page);
	/**
	 * @description: 获取下一个维护计划ID 
	 * @author: 王中华
	 * @createDate: 2014-6-25
	 * @return:
	 */
	int getNextMTPId();
	/**
	 * @description:禁用（删除）维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-23
	 * @param maintainPlanId:
	 */
	void updateMTPToUnvailable(int maintainPlanId);
	
	/**
	 * @description:周期性维护计划
	 * @author: 王中华
	 * @createDate: 2014-7-28
	 * @return: 
	 */
	List<MaintainPlan> queryAllCycMTP();
	/**
	 * @description:周期性维护计划生成待办之后修改“下次生成待办时间”和“当前周期时间”
	 * @author: 王中华
	 * @createDate: 2014-7-29
	 * @param newToDoTime:
	 */
	void updateMTPTodoTime(@Param("currStartTime") Date currStartTime , @Param("newToDoTime") Date newToDoTime,
			@Param("id") int mtpId);
	
	/**
	 * @description:修改维护计划中，是否已经生成提醒待办（0：未生成，1：已生成）
	 * @author: 王中华
	 * @createDate: 2014-10-11
	 * @param mtpId
	 * @param value:
	 */
	void updateMTPhasAlertTodo(@Param("mtpId") int mtpId, @Param("value") int value);
	
	
	
	
	
}
