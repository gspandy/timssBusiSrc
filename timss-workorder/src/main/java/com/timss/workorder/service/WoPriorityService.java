package com.timss.workorder.service;

import java.util.HashMap;
import java.util.Map;

import com.timss.workorder.bean.WoPriority;
import com.yudean.itc.dto.Page;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
/**
 * @title: {title}
 * @description: {desc}
 * @company: gdyd
 * @className: WoPriorityService.java
 * @author: 王中华
 * @createDate: 2014-12-9
 * @updateUser: 王中华
 * @version: 1.0
 */
public interface WoPriorityService {

	/**
	 * @description:修改技能
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param addWoSkillDataMap:
	 */
	void updateWoPriority(Map<String, String> addWoPriorityDataMap);

	/**
	 * @description:添加技能
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param addWoSkillDataMap:
	 */
	void insertWoPriority(Map<String, String> addWoPriorityDataMap);
	
	/**
	 * 根据ID 查询技能
	 * @param id
	 * @return
	 */
	Map<String, Object> queryWoPriorityById(int id,String siteid);
	
	/**
	 * @description:查询技能列表
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param page
	 * @return:
	 */
	Page<WoPriority> queryWoPriorityList(Page<WoPriority> page);

	/**
	 * @description: 根据影响度和紧急度，查询对应的服务级别ID
	 * @author: 王中华
	 * @createDate: 2014-12-9
	 * @param urgentVal
	 * @param influenceVal
	 * @return:
	 */
	Map<String, String> getPriIdValByUrgentInfluence(String urgentVal,
			String influenceVal);

	Map<String, Object> deleteWoPriority(int woPriorityId, String siteid);
	
	
}
