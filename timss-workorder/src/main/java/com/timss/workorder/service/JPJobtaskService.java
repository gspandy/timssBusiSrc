package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.JPJobtask;
/**
 * 工作内容 Service 操作
 * @author 王中华
 * 2014-6-11
 */
public interface JPJobtaskService {
	/**
	 * 添加 工作内容
	 * @param jpJobtask
	 */
	void insertJPJobtask(JPJobtask jpJobtask);
	/**
	 * 更新 工作内容
	 * @param jpJobtask
	 */
	void updateJPJobtask(JPJobtask jpJobtask);
	/***
	 * 更加作业方案ID 查询其所有工作内容
	 * @param jpId
	 * @return
	 */
	List<JPJobtask> queryJPJobtaskByJPId(int jpId);
	/***
	 * 根据ID 查询工作内容
	 * @param id
	 * @return
	 */
	JPJobtask queryJPJobtaskById(int id);
}
