package com.timss.purchase.flow.swf.purapply.v001;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: ItDeptManagerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: ItDeptManagerAudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class ItDeptManagerAudit extends DefApplyProcess {

  @Autowired
  private WorkflowService workflowService;

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", false);

  }
}
