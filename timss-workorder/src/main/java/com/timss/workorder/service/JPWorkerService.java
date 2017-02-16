package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.JPWorker;
/**
 * 作业方案中 人员 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface JPWorkerService {
	/**
	 * 添加 人员 信息 
	 * @param jpWorker
	 */
	void insertJPWorker(JPWorker jpWorker);
	/**
	 * 更新 人员 信息
	 * @param jpWorker
	 */
	void updateJPWorker(JPWorker jpWorker);
	/***
	 * 更加作业方案ID 查询 人员信息
	 * @param jpId
	 * @return
	 */
	List<JPWorker> queryJPWorkerByJPId(int jpId);
	/***
	 * 根据ID 查询人员信息
	 * @param id
	 * @return
	 */
	JPWorker queryJPWorkerById(int id);
}
