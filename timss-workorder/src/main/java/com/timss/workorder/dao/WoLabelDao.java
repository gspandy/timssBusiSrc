package com.timss.workorder.dao;
import java.util.List;

import com.timss.workorder.bean.WoLabel;
import com.yudean.itc.dto.Page;
/**
 * 工单标识
 * @author 王中华
 * 2014-6-11
 */
public interface WoLabelDao {

	/**
	 * @description:插入工单标识
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int insertWoLabel(WoLabel woLabel);
	
	/**
	 * @description:修改工单标识
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param skill
	 * @return:
	 */
	int updateWoLabel(WoLabel woLabel);
	
	/**
	 * @description:删除工单标识
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	int deleteWoLabel(int id);
	
	/**
	 * @description:更加id查询
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @param id
	 * @return:
	 */
	WoLabel queryWoLabelById(int id);
	
	/**
	 * @description:获取下一个插入的ID 
	 * @author: 王中华
	 * @createDate: 2014-8-26
	 * @return:
	 */
	int getNextParamsConfId();

	/**
	 * @description:查询列表
	 * @author: 王中华
	 * @createDate: 2014-8-28
	 * @param page
	 * @return:
	 */
	List<WoLabel> queryWoLabelList(Page<WoLabel> page);
}
