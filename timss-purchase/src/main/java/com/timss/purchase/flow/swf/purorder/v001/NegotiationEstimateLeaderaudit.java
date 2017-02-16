package com.timss.purchase.flow.swf.purorder.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.timss.purchase.service.PurOrderService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: NegotiationEstimateLeaderaudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: NegotiationEstimateLeaderaudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class NegotiationEstimateLeaderaudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  public PurOrderService purOrderService;

  private static Logger LOG = Logger.getLogger(NegotiationEstimateLeaderaudit.class);

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 NegotiationEstimateLeaderaudit 环节 Handler 初始化方法...");

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
    LOG.info(">>>>>>>>>>>>>>>>>>> NegotiationEstimateLeaderaudit 环节 Handler 方法执行结束，用时：" + useTime
        + " s");
  }
}
