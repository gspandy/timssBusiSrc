package com.timss.workorder.dao;


import java.util.Map;

import com.timss.workorder.bean.Hazard;
/**
 * 安全事项
 * @author 王中华
 * 2014-6-11
 */
public interface HazardDao {

	int insertHazard(Hazard hazard);
	
	int updateHazard(Hazard hazard);
	
	Hazard queryHazardById(int id);
	/**
	 * @description: 删除某个安全事项
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param id
	 * @return:
	 */
	int deleteHazardById(int id);
	/**
	 * @description: 获取下一个插入记录的id
	 * @author: 王中华
	 * @createDate: 2014-6-26
	 * @return:
	 */
	int getNextHazardId();
	
	int insertPreHazard(Map<String, Object> preHazard);
	
	
}
