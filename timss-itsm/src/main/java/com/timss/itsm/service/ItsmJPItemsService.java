package com.timss.itsm.service;

import java.util.List;

import com.timss.itsm.bean.ItsmJPItems;
/**
 * 作业方案中 的 工具 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmJPItemsService {
	/**
	 * 添加工具
	 * @param jpItems
	 */
	void insertJPItems(ItsmJPItems jpItems);
	/**
	 * 更新工具信息
	 * @param id
	 */
	void updateJPItems(ItsmJPItems jpItems);
	/**
	 * 查询某个作业方案的工具列表
	 * @param jpId
	 * @return
	 */
	List<ItsmJPItems> queryJPItemsByJPId(int jpId);
	/**
	 * 根据ID查询工具信息
	 * @param id
	 * @return
	 */
	ItsmJPItems queryJPItemsById(int id); 
}
