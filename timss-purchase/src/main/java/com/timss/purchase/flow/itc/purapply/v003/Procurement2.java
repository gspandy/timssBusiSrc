package com.timss.purchase.flow.itc.purapply.v003;

import org.apache.log4j.Logger;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: Procurement2工作流控制类
 * @description:
 * @company: gdyd
 * @className: Procurement2.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Procurement2 extends DefApplyProcess {

  private static Logger LOG = Logger.getLogger(Procurement2.class);

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 Procurement2 环节 Handler 完成方法...");
    String useTime = wpu.update2TransactorOver(taskInfo, "PurApply");
    LOG.info(">>>>>>>>>>>>>>>>>>> Procurement2 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }
}
