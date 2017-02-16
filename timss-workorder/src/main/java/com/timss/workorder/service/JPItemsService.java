package com.timss.workorder.service;

import java.util.List;

import com.timss.workorder.bean.JPItems;
/**
 * 作业方案中 的 工具 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface JPItemsService {
	/**
	 * 添加工具
	 * @param jpItems
	 */
	void insertJPItems(JPItems jpItems);
	/**
	 * 更新工具信息
	 * @param id
	 */
	void updateJPItems(JPItems jpItems);
	/**
	 * 查询某个作业方案的工具列表
	 * @param jpId
	 * @return
	 */
	List<JPItems> queryJPItemsByJPId(int jpId);
	/**
	 * 根据ID查询工具信息
	 * @param id
	 * @return
	 */
	JPItems queryJPItemsById(int id); 
}
