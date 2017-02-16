package com.timss.pms.service;

/**
 * 流程状态控制
 * @ClassName:     FlowStatusService
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-22 下午3:11:30
 */
public interface FlowStatusService {
	/**
	 * 更新业务的状态.
	 * @Title: updateFlowStatus
	 * @param id
	 * @param status
	 * @return
	 */
	public boolean updateFlowStatus(String id,String status);
}
