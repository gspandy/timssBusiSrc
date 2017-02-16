package com.timss.pms.flow.sfc.bidresult.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.service.FlowStatusService;
import com.timss.pms.util.FlowStatusUpdateUtil;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 议标小组审批类
 * @ClassName:     Ybgzxzsp
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */
public class Ybgzxzsp extends TaskHandlerBase{
	@Autowired
	@Qualifier("bidResultFlowStatusServiceImpl")
	FlowStatusService flowStatusService;
	@Autowired
	WorkflowService workflowService;
	
	private final static Logger LOGGER=Logger.getLogger(Ybgzxzsp.class);
	/**
	 * 用户审批时选择同意时，工作流触发的事件
	 * <p>Title: onComplete</p>
	 * <p>Description: </p>
	 * @param taskInfo
	 * @see com.yudean.workflow.task.MutiInstanceTaskHandleBase#onComplete(com.yudean.workflow.task.TaskInfo)
	 */
	public void onComplete(TaskInfo taskInfo) {
		LOGGER.info("完成招标结果审批工作流节点"+taskInfo.getTaskDefKey()+"，流程id为"+taskInfo.getProcessInstanceId()+"对招标结果的更新");
		
	}
	
	/**
	 * 用户审批时选择不同意时，工作流触发的事件
	 * <p>Title: onDisagree</p>
	 * <p>Description: </p>
	 * @param taskInfo
	 * @see com.yudean.workflow.task.MutiInstanceTaskHandleBase#onDisagree(com.yudean.workflow.task.TaskInfo)
	 */
	public void onDisagree(TaskInfo taskInfo){
		
		
	}
	public void init(TaskInfo taskinfo){
		
		FlowStatusUpdateUtil.updateFlowStatus(taskinfo, flowStatusService, workflowService);
		
    }

}
