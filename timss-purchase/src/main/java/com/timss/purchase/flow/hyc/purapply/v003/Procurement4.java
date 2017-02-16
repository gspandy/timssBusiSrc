package com.timss.purchase.flow.hyc.purapply.v003;

import org.apache.log4j.Logger;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: Procurement4工作流控制类
 * @description:
 * @company: gdyd
 * @className: Procurement4.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Procurement4 extends DefApplyProcess {

  private static Logger LOG = Logger.getLogger(Procurement4.class);

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 Procurement4 环节 Handler 完成方法...");
    String useTime = wpu.update2TransactorOver(taskInfo, "PurApply");
    LOG.info(">>>>>>>>>>>>>>>>>>> Procurement4 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }
}
