package com.timss.workorder.service;

import java.util.Map;

import com.timss.workorder.bean.WoLabel;
import com.yudean.itc.dto.Page;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface WoLabelService {

	/**
	 * @description: 添加标识
	 * @author: 王中华
	 * @createDate: 2014-8-28
	 * @param addWoLabelDataMap:
	 */
	void insertWoLabel(Map<String, String> addWoLabelDataMap);

	/**
	 * @description: 修改标识
	 * @author: 王中华
	 * @createDate: 2014-8-28
	 * @param addWoLabelDataMap:
	 */
	void updateWoLabel(Map<String, String> addWoLabelDataMap);

	/**
	 * 根据ID 查询标识
	 * @param id
	 * @return
	 */
	WoLabel queryWoLabelById(int id);
	
	/**
	 * @description:删除标识
	 * @author: 王中华
	 * @createDate: 2014-8-28
	 * @param id:
	 */
	void deleteWoLabelById(int id);

	/**
	 * @description:查询标识列表
	 * @author: 王中华
	 * @createDate: 2014-8-28
	 * @param page
	 * @return:
	 */
	Page<WoLabel> queryWoLabelList(Page<WoLabel> page);
	
}
