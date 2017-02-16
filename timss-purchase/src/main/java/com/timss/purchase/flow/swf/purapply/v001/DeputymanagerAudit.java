package com.timss.purchase.flow.swf.purapply.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: DeputymanagerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: DeputymanagerAudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: 890162
 * @version: 1.0
 */
public class DeputymanagerAudit extends DefApplyProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  @Qualifier("PurApplyServiceImpl")
  private PurApplyService purApplyService;

  private static Logger LOG = Logger.getLogger(DeputymanagerAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    String processInst = taskInfo.getProcessInstanceId();
    String tatolcost = workflowService.getVariable(processInst, "tatolcost") == null ? "" : String
        .valueOf(workflowService.getVariable(processInst, "tatolcost"));
    Double totalcost = 0D;
    if (!"".equals(tatolcost)) {
      totalcost = Double.parseDouble(tatolcost);
    }

    if (totalcost < 50000) {
      workflowService.setVariable(processInst, "isLastStep", true);
    }
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    String processInst = taskInfo.getProcessInstanceId();
    String businessId = workflowService.getVariable(processInst, "businessId") == null ? ""
        : String.valueOf(workflowService.getVariable(processInst, "businessId"));

    boolean isLastStep = workflowService.getVariable(processInst, "isLastStep") == null ? false
        : true;

    try {
      if (isLastStep && !"".equals(businessId)) {
        purApplyService.updatePurApplyToPass(businessId, taskInfo.getTaskName());
      }
    } catch (Exception e) {
      LOG.warn(">>>>>>>>>>>>>>>>>>> 更新待办人异常", e);
    }
  }
}
