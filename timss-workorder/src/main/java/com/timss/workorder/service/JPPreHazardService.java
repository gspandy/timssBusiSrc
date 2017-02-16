package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.JPPreHazard;
/**
 * 安全事项与预控措施 关联关系表Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface JPPreHazardService {
	/**
	 * 添加   安全事项与预控措施 关联关系
	 * @param jpPreHazard
	 */
	void insertJPPreHazard(JPPreHazard jpPreHazard);
	/**
	 * 根据作业方案ID查询所有  安全事项与预控措施 关联关系
	 * @param jpId
	 * @return
	 */
	List<JPPreHazard> queryJPPreHazardByJPId(int jpId);
	/**
	 *   查询  安全事项与预控措施 关联关系
	 * @param id
	 * @return
	 */
	JPPreHazard queryJPPreHazardById(int id);
}
