package com.timss.purchase.flow.swf.purorder.v004;

import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurOrder;
import com.timss.purchase.flow.abstr.DefOrderProcess;
import com.timss.purchase.service.PurOrderService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: BusinessDeptAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: BusinessDeptAudit.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class BusinessDeptAudit extends DefOrderProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  private PurOrderService purOrderService;

  private static Logger LOG = Logger.getLogger(BusinessDeptAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    try {
      String processInst = taskInfo.getProcessInstanceId();
      String businessId = null;
      Object business = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId");
      if (null != business) {
        businessId = business.toString();
      }

      workflowService.setVariable(processInst, "isLastStep", false);
      // 由于放在第一个环节的init设置isLess流程变量，从采购申请生成采购合同时会因无法获取businessId而导致异常，放在第二个环节，businessId定不为空
      if (StringUtils.isNotEmpty(businessId)) {
        PurOrder purOrder = purOrderService.queryPurOrderBySheetId(businessId);
        if (purOrder.getTotalPrice().compareTo(BigDecimal.valueOf(200000D)) == -1) {
          workflowService.setVariable(processInst, "isLess", "true");
        } else {
          workflowService.setVariable(processInst, "isLess", "false");
        }
      } else {
        workflowService.setVariable(processInst, "isLess", "true");
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 流程变量加入失败 ", e);
    }
  }
}
