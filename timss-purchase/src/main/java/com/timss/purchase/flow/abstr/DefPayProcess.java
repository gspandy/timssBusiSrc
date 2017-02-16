package com.timss.purchase.flow.abstr;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.utils.WorkflowPurUtil;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: DefPayProcess工作流控制类
 * @description: DefPayProcess工作流控制类
 * @company: gdyd
 * @className: DefPayProcess.java
 * @author: gucw
 * @createDate: 2016-12-21
 * @updateUser: gucw
 * @version: 1.0
 */
public abstract class DefPayProcess extends TaskHandlerBase {

  @Autowired
  public WorkflowPurUtil wpu;

  private static Logger LOG = Logger.getLogger(DefPayProcess.class);

  @Override
  public void init(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefPayProcess 环节 Handler 初始化方法...");
    String useTime = wpu.update2Transactor4PurPay( taskInfo );
    LOG.info(">>>>>>>>>>>>>>>>>>> DefPayProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefPayProcess 环节 Handler 完成方法...");
    String useTime = wpu.paProcessOnComplete(taskInfo);
    LOG.info(">>>>>>>>>>>>>>>>>>> DefPayProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

}
