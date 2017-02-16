package com.timss.workorder.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.timss.workorder.bean.WoPriority;
import com.yudean.itc.dto.Page;
/**
 * 技能
 * @author 王中华
 * 2014-6-11
 */
public interface WoPriorityDao {

	/**
	 * @description:插入优先级
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int insertWoPriority(WoPriority priority);
	
	/**
	 * @description:修改优先级
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int updateWoPriority(WoPriority priority);
	
	/**
	 * @description:删除优先级
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	int deleteWoPriority(@Param("id") int priId,@Param("siteid") String siteid);
	
	/**
	 * @description:更加id查询
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	WoPriority queryWoPriorityById(int id);
	
	/**
	 * @description:获取下一个插入的ID
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @return:
	 */
	int getNextParamsConfId();
	/**
	 * @description:列表页面查询 
	 * @author: 王中华
	 * @createDate: 2014-8-27
	 * @param page
	 * @return:
	 */
	List<WoPriority> queryWoPriorityList(Page<WoPriority> page);

	/**
	 * @description: 根据站点ID查找优先级
	 * @author: 王中华
	 * @createDate: 2014-11-25
	 * @param siteid
	 * @return:
	 */
	List<WoPriority> queryWoPriorityListBySiteId(@Param("siteid") String siteid);

	
}
