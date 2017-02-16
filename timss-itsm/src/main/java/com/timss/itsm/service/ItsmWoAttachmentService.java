package com.timss.itsm.service;

import java.util.List;

import com.timss.itsm.bean.ItsmWoAttachment;
/**
 * 安全事项 Service操作
 * @author 王中华
 * 2014-6-11
 */
public interface ItsmWoAttachmentService {
	/**
	 * 添加 安全事项
	 * @param hazard
	 * @return
	 */
	void insertWoAttachment(ItsmWoAttachment woAttachment);
	
	/**
	 * @description:查询某个业务单的所有附件见
	 * @author: 王中华
	 * @createDate: 2014-11-14
	 * @param id  业务单ID
	 * @param type :属于哪个功能的附件（工单、维护计划、作业方案）
	 * @return:
	 */
	List<ItsmWoAttachment> queryWoAttachmentById(String id,String type);

	void deleteWoAttachment(String businessId, String string, String type,
			String userId);

	
}
