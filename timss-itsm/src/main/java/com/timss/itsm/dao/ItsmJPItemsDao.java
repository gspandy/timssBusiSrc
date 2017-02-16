package com.timss.itsm.dao;

 
import java.util.List;

import com.timss.itsm.bean.ItsmJPItems;
/**
 * 工具
 * @author 王中华
 *
 */
public interface ItsmJPItemsDao {
	/**
	 * 
	 * @param jpItems
	 */
	void insertJPItems(ItsmJPItems jpItems);
	
	/**
	 * 
	 * @param jpItems
	 */
	void updateJPItems(ItsmJPItems jpItems);
	
	/**
	 * 查询某个作业方案的 所有 工具信息
	 * @param param
	 * @return
	 */
	List<ItsmJPItems> queryJPItemsByJPId(int jpId); 
	/**
	 * 
	 * @param id
	 * @return
	 */
	ItsmJPItems queryJPItemsById(int id);
	
	/**
	 * @description: 删除某个作业方案对应的工具信息
	 * @author: 王中华
	 * @createDate: 2014-7-1
	 * @param jpId
	 * @return:
	 */
	int deleteJPItemsByJPId(int jpId);
	
	int getNextJPItemsId();
}
