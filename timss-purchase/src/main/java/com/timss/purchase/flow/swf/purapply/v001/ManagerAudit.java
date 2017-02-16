package com.timss.purchase.flow.swf.purapply.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: ManagerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: ManagerAudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class ManagerAudit extends DefApplyProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  @Qualifier("PurApplyServiceImpl")
  private PurApplyService purApplyService;

  private static Logger LOG = Logger.getLogger(ManagerAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", true);
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    Object businessid = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
    String businessId = businessid == null ? "" : String.valueOf(businessid);
    try {
      // 更新状态和待办人
      if (!"".equals(businessId)) {
        purApplyService.updatePurApplyToPass(businessId, taskInfo.getTaskName());
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 更新待办人异常", e);
    }
  }
}
