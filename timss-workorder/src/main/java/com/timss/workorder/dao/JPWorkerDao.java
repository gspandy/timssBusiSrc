package com.timss.workorder.dao;


import java.util.List;

import com.timss.workorder.bean.JPWorker;

/**
 * 作业方案 中 人员信息 DAO操作
 * @author 王中华
 * 2014-6-11
 */
public interface JPWorkerDao {
	/**
	 * 添加
	 * @param jpWorker
	 */
	void insertJPWorker(JPWorker jpWorker);
	/**
	 * 更新
	 * @param jpWorker
	 */
	void updateJPWorker(JPWorker jpWorker);
	/***
	 * 更加作业方案ID查询
	 * @param jpId
	 * @return
	 */
	List<JPWorker> queryJPWorkerByJPId(int jpId);
	
	/**
	 * @description: 删除某个作业方案对应的人员信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param jpId
	 * @return:
	 */
	int deleteJPWorkerByJPId(int jpId);
	/**
	 * 更加ID 查询
	 * @param id
	 * @return
	 */
	JPWorker queryJPWorkerById(int id);
	
	/**
	 * @description: 获取下一个插入人员记录的ID
	 * @author: 王中华
	 * @createDate: 2014-6-26
	 * @return:
	 */
	int getNextJPWorkerId();
}
