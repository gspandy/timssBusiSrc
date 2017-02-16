package com.timss.purchase.flow.zjw.purapply.v003;

import org.apache.log4j.Logger;

import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: Procurement工作流控制类
 * @description:
 * @company: gdyd
 * @className: Procurement.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Procurement extends DefApplyProcess {

  private static Logger LOG = Logger.getLogger(Procurement.class);

  @Override
  public void onComplete(TaskInfo taskInfo) {
    LOG.info(">>>>>>>>>>>>>>>>>>> 开始执行 Procurement 环节 Handler 完成方法...");
    String useTime = wpu.update2TransactorOver(taskInfo, "PurApply");
    LOG.info(">>>>>>>>>>>>>>>>>>> Procurement 环节 Handler 方法执行结束，用时：" + useTime + " s");
  }
}
