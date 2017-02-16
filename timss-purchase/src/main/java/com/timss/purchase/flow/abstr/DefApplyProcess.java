package com.timss.purchase.flow.abstr;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.utils.WorkflowPurUtil;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: DefApplyInitProcess工作流控制类
 * @description: DefApplyInitProcess工作流控制类
 * @company: gdyd
 * @className: DefApplyInitProcess.java
 * @author: yuanzh
 * @createDate: 2016-2-2
 * @updateUser: yuanzh
 * @version: 1.0
 */
public abstract class DefApplyProcess extends TaskHandlerBase {

  @Autowired
  public WorkflowPurUtil wpu;

  private static Logger LOG = Logger.getLogger(DefApplyProcess.class);

  @Override
  public void init(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefApplyProcess 环节 Handler 初始化方法...");
    String useTime = wpu.update2Transactor(taskInfo, "PurApply");
    LOG.info(">>>>>>>>>>>>>>>>>>> DefApplyProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefApplyProcess 环节 Handler 完成方法...");
    String useTime = wpu.paProcessOnComplete(taskInfo);
    LOG.info(">>>>>>>>>>>>>>>>>>> DefApplyProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

}
