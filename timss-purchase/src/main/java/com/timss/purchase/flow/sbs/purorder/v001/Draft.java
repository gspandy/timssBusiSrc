package com.timss.purchase.flow.sbs.purorder.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.timss.purchase.service.PurOrderService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: Draft工作流控制类
 * @description:
 * @company: gdyd
 * @className: Draft.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Draft extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;
  @Autowired
  private PurOrderService purOrderService;

  private static Logger LOG = Logger.getLogger(Draft.class);

  /**
   * 在首环节结束后将状态从草稿改成执行中
   */
  @Override
  public void onComplete(TaskInfo taskInfo) {
    String processId = taskInfo.getProcessInstanceId();
    Object sheetIdObj = workflowService.getVariable(processId, "sheetId");
    try {
      if ( null!=sheetIdObj ) {
          PurOrder po = new PurOrder();
          po.setSheetId(sheetIdObj.toString());
          po.setStatus("0");
          purOrderService.updatePurOrderInfo(po);
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 状态更新异常", e);
    }
  }
}
