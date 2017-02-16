package com.timss.workorder.service;

import com.timss.workorder.bean.Hazard;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface HazardService {
	/**
	 * 添加 安全事项
	 * @param hazard
	 * @return
	 */
	void insertHazard(Hazard hazard);
	/**
	 * 更新 安全事项
	 * @param harzard
	 */
	void updateHazard(Hazard harzard);
	/**
	 * 根据ID 查询安全事项
	 * @param id
	 * @return
	 */
	Hazard queryHazardById(int id);
}
