package com.timss.pms.util;

import java.util.List;

import com.timss.pms.service.FlowStatusService;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;


/**
 * 流程节点更新流程状态辅助类
 * @ClassName:     FlowStatusUpdateUtil
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2015-9-22 下午3:41:46
 */
public class FlowStatusUpdateUtil {

	/**
	 * 根据流程节点名称更新流程状态
	 * @Title: updateFlowStatus
	 * @param taskInfo
	 * @param businessId
	 * @param flowStatusService
	 * @param workflowService
	 * @return
	 */
	public static boolean updateFlowStatus(TaskInfo taskInfo,FlowStatusService flowStatusService,WorkflowService workflowService){
	
		Object businessId=workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
		flowStatusService.updateFlowStatus(String.valueOf(businessId),taskInfo.getTaskName());
		
		return true;
	}
	
	/**
	 * 根据枚举变量PMS_STATUS值更新更新流程状态
	 * @Title: updateFlowStatusWithEnum
	 * @param itcMvcService
	 * @param flowStatusService
	 * @param businessId
	 * @param enumCode
	 * @return
	 */
	public static boolean updateFlowStatusWithEnum(ItcMvcService itcMvcService,FlowStatusService flowStatusService,String businessId,String enumCode){
		String enumValue=getEnumValue(itcMvcService, enumCode);
		flowStatusService.updateFlowStatus(businessId, enumValue);
		return true;
	}
	
	/**
	 * 获取枚举变量PMS_STATUS的code值对应的value
	 * @Title: updateFlowStatusWithEnum
	 * @param itcMvcService
	 * @param flowStatusService
	 * @param businessId
	 * @param enumCode
	 * @return
	 */
	public static String getEnumValue(ItcMvcService itcMvcService,String enumCode){
		List<AppEnum> appEnums=itcMvcService.getEnum("PMS_STATUS");
		return InitVoEnumUtil.getEnumVal(enumCode, appEnums);
	}
}
