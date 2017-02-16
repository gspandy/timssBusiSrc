package com.timss.purchase.flow.swf.purorder.v004;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.timss.purchase.service.PurOrderService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: NegotiationApplydeptAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: NegotiationApplydeptAudit.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class NegotiationApplydeptAudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private PurOrderService purOrderService;

  private static Logger LOG = Logger.getLogger(NegotiationApplydeptAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", false);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "nosubmit", "N");
  }

  /**
   * 在首环节结束后将状态从草稿改成执行中
   */
  @Override
  public void onComplete(TaskInfo taskInfo) {
    String sheetId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(),
        "businessId"));
    try {
      PurOrder po = new PurOrder();
      po.setSheetId(sheetId);
      po.setStatus("0");
      purOrderService.updatePurOrderInfo(po);
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 状态更新异常", e);
    }
  }
}
