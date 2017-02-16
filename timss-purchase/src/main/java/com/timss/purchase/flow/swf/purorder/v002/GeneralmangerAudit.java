package com.timss.purchase.flow.swf.purorder.v002;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.timss.purchase.service.PurOrderService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: GeneralmangerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: GeneralmangerAudit.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class GeneralmangerAudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private PurOrderService purOrderService;

  private static Logger LOG = Logger.getLogger(GeneralmangerAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", true);
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 GeneralmangerAudit 环节 Handler 初始化方法...");

    String businessId = String.valueOf(workflowService.getVariable(taskInfo.getProcessInstanceId(),
        "businessId"));
    try {
      PurOrder purOrder = new PurOrder();
      purOrder.setSheetId(businessId);
      purOrder.setStatus("1");
      purOrderService.updatePurOrderInfo(purOrder);
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 更新采购合同状态失败 ", e);
    }

    String useTime = wpu.update2TransactorOver(taskInfo, "PurOrder");
    LOG.info(">>>>>>>>>>>>>>>>>>> GeneralmangerAudit 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }
}
