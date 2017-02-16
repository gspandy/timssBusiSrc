package com.timss.purchase.flow.abstr;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.utils.WorkflowPurUtil;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: DefOrderInitProcess工作流控制类
 * @description: DefOrderInitProcess工作流控制类
 * @company: gdyd
 * @className: DefOrderInitProcess.java
 * @author: yuanzh
 * @createDate: 2016-2-2
 * @updateUser: yuanzh
 * @version: 1.0
 */
public abstract class DefOrderProcess extends TaskHandlerBase {

  @Autowired
  public WorkflowPurUtil wpu;

  private static Logger LOG = Logger.getLogger(DefOrderProcess.class);

  @Override
  public void init(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefOrderProcess 环节 Handler 初始化方法...");
    String useTime = wpu.update2Transactor(taskInfo, "PurOrder");
    LOG.info(">>>>>>>>>>>>>>>>>>> DefOrderProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 DefOrderProcess 环节 Handler 初始化方法...");
    String useTime = wpu.poProcessOnComplete(taskInfo);
    LOG.info(">>>>>>>>>>>>>>>>>>> DefOrderProcess 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }

}
