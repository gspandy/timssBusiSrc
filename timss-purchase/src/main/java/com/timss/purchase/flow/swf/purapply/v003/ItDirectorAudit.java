package com.timss.purchase.flow.swf.purapply.v003;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: ItDirectorAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: ItDirectorAudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class ItDirectorAudit extends DefApplyProcess {

  @Autowired
  private WorkflowService workflowService;

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", false);
  }
}
