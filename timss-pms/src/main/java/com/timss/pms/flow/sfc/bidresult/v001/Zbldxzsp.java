package com.timss.pms.flow.sfc.bidresult.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.pms.dao.WorkflowBusinessDao;
import com.timss.pms.service.BidResultService;
import com.timss.pms.service.ContractService;
import com.timss.pms.service.FlowStatusService;
import com.timss.pms.service.PayplanService;
import com.timss.pms.service.PayplanTmpService;
import com.timss.pms.util.FlowStatusUpdateUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.bean.MutiInstanceVoteResult;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.service.mutiInstance.MutiInstanceVoteManager;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * 项目立项 提出申请节点类
 * @ClassName:     Apply
 * @company: gdyd
 * @author:    黄晓岚
 * @date:   2014-7-9 上午11:42:11
 */
public class Zbldxzsp extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	@Qualifier("contractServiceImpl")
	ContractService contractService;
	@Autowired
	PayplanTmpService payplanTmpService;
	@Autowired
	PayplanService payplanService;
	@Autowired
	WorkflowBusinessDao workflowBusinessDao;
	@Autowired
	@Qualifier("bidResultFlowStatusServiceImpl")
	FlowStatusService flowStatusService;
	
	@Autowired
	@Qualifier("defaultMutiInstanceVoteManager")
	MutiInstanceVoteManager mutiInstanceVoteManager;

	
	@Autowired
	@Qualifier("bidResultServiceImpl")
	BidResultService bidResultService;
	private static final Logger LOGGER=Logger.getLogger(Zbldxzsp.class);
	
	public void onComplete(TaskInfo taskInfo) {
		int businessId=(Integer) workflowService.getVariable(taskInfo.getProcessInstanceId(),"businessId");
		MutiInstanceVoteResult voteResul=mutiInstanceVoteManager.getMutiInstanceVoteResult(taskInfo.getProcessInstanceId(), taskInfo.getTaskDefKey(), taskInfo.getTaskInstId());
		if(mutiInstanceVoteManager.isApproval(voteResul)){
			bidResultService.updateBidResultApproved(businessId);
		}
		
		
		LOGGER.info("完成招标结果审批工作流节点"+taskInfo.getTaskDefKey()+"，流程id为"+taskInfo.getProcessInstanceId()+"对招标结果的更新");
	}
	
	@Override
	public void onDisagree(TaskInfo taskInfo) {
		
	}
	
	public void init(TaskInfo taskinfo){
		
		FlowStatusUpdateUtil.updateFlowStatus(taskinfo, flowStatusService, workflowService);
    }

}
