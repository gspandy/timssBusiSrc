package com.timss.workorder.dao;

import com.timss.workorder.bean.Precaution;

/***
 * 预控措施 DAO操作接口
 * @author 王中华
 * 2014-6-11
 */
public interface PrecautionDao {
	/***
	 * 添加
	 * @param precaution
	 * @return
	 */
	void insertPrecaution(Precaution precaution);
	/***
	 * 更新
	 * @param precaution
	 */
	void updatePrecaution(Precaution precaution);
	/***
	 * 查询
	 * @param id
	 * @return
	 */
	Precaution queryPrecautionById(int id);
	
	/**
	 * @description: 删除某个预控措施信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param id
	 * @return:
	 */
	int deletePrecautionById(int id);
	/**
	 * @description: 获取下一个插入记录的id
	 * @author: 王中华
	 * @createDate: 2014-6-26
	 * @return:
	 */
	int getNextPrecautionId();
}
