package com.timss.purchase.flow.swf.purorder.v004;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.service.HistoryInfoService.HistoricTask;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: BusinessDeptmanagerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: BusinessDeptmanagerAudit.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class BusinessDeptmanagerAudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", false);
    List<HistoricTask> tasks = workflowService.getPreviousTasks( taskInfo.getProcessInstanceId() );
    for ( HistoricTask historicTask : tasks ) {
        if ( "negotiation_applydept_audit".equals( historicTask.getTaskDefinitionKey() ) ) {
            workflowService.setVariable(taskInfo.getProcessInstanceId(), "apply", historicTask.getAssignee());
            break;
        }
    }
  }
}
