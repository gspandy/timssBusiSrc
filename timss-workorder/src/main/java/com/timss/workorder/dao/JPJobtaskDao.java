package com.timss.workorder.dao;

import java.util.List;

import com.timss.workorder.bean.JPJobtask;
/**
 *  作业方案中 “工作内容”的DAO操作
 * @author 王中华
 *
 */
public interface JPJobtaskDao {
	/**
	 *  添加“工作内容”
	 * @param jpJobtask
	 */
	void insertJPJobtask(JPJobtask jpJobtask);
	/**
	 * 更新“工作内容”
	 * @param jpJobtask
	 */
	void updateJPJobtask(JPJobtask jpJobtask);
	/**
	 * 根据作业方案ID 查询其所有的“工作内容”
	 * @param jpId
	 * @return
	 */
	List<JPJobtask> queryJPJobtaskByJPId(int jpId);
	
	/**
	 * @description: 删除某个作业方案对应的工作内容信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param jpId
	 * @return:
	 */
	int deleteJPJobtaskByJPId(int jpId);
	/**
	 * 根据工作内容的ID，查询其详情
	 * @param id
	 * @return
	 */
	JPJobtask queryJPJobtaskById(int id);
	
	/**
	 * @description: 获取下一次插入的工作内容的ID
	 * @author: 王中华
	 * @createDate: 2014-6-26
	 * @return:
	 */
	int getNextJPJobtaskId();
}
