package com.timss.purchase.flow.swf.purorder.v003;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: LawDeptAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: LawDeptAudit.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class LawDeptAudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", false);
  }
}
