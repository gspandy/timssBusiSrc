package com.timss.itsm.service;

import java.util.List;
import com.timss.itsm.bean.ItsmAttachment;
import com.timss.itsm.bean.ItsmWoAttachment;
/**
 * 安全事项 Service操作
 * @author yangkun
 * 2016-8-23
 */
public interface ItsmAttachmentService {
	/**
	 * @description: 插入附件
	 * @author: 杨坤
	 * @createDate: 2016-8-23
	 */
	void insertAttachment(ItsmAttachment attachment);
	
	/**
	 * @description: 删除附件
	 * @author: yangkun
	 * @createDate: 2016-8-23
	 * @param businessId 业务Id
	 */
	void deleteAttachment(String businessId, String string, String type,
			String userId);
	/**
	 * @description:查询某个业务单的所有附件
	 * @author: yangukn
	 * @createDate: 2016-8-23
	 * @param id  业务单ID
	 * @param type :属于哪个功能的附件（工单、维护计划、作业方案）
	 * @return:
	 */
	List<ItsmAttachment> queryAttachmentById(String id,String type);

}
