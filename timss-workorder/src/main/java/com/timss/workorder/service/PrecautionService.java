package com.timss.workorder.service;

import com.timss.workorder.bean.Precaution;
/***
 * 预控措施 Service 操作
 * @author 王中华
 * 2014-6-11
 */
public interface PrecautionService {
	/***
	 * 添加  预控措施
	 * @param precaution
	 * @return
	 */
	void insertPrecaution(Precaution precaution);
	/**
	 * 更新 预控措施
	 * @param precaution
	 */
	void updatePrecaution(Precaution precaution);
	/**
	 * 根据ID 查询预控措施
	 * @param id
	 * @return
	 */
	Precaution queryPrecautionById(int id);
}
