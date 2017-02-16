package com.timss.workorder.dao;

 
import java.util.List;

import com.timss.workorder.bean.JPItems;
/**
 * 工具
 * @author 王中华
 *
 */
public interface JPItemsDao {
	/**
	 * 
	 * @param jpItems
	 */
	void insertJPItems(JPItems jpItems) throws Exception;
	
	/**
	 * 
	 * @param jpItems
	 */
	void updateJPItems(JPItems jpItems);
	
	/**
	 * 查询某个作业方案的 所有 工具信息
	 * @param param
	 * @return
	 */
	List<JPItems> queryJPItemsByJPId(int jpId); 
	/**
	 * 
	 * @param id
	 * @return
	 */
	JPItems queryJPItemsById(int id);
	
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
