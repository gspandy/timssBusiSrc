package com.timss.purchase.flow.swf.purapply.v002;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.bean.PurApplyItem;
import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: ManagerAudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: ManagerAudit.java
 * @author: 890162
 * @createDate: 2015-9-22
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class ManagerAudit extends DefApplyProcess {

  @Autowired
  private WorkflowService workflowService;

  @Autowired
  @Qualifier("PurApplyServiceImpl")
  private PurApplyService purApplyService;

  private static Logger LOG = Logger.getLogger(ManagerAudit.class);

  @Override
  public void init(TaskInfo taskInfo) {
    super.init(taskInfo);
    workflowService.setVariable(taskInfo.getProcessInstanceId(), "isLastStep", true);
  }

  @Override
  public void onComplete(TaskInfo taskInfo) {
    String processInst = taskInfo.getProcessInstanceId();
    Object businessid = workflowService.getVariable(processInst, "businessId");
    String taskName = taskInfo.getTaskName();
    String businessId = businessid == null ? "" : String.valueOf(businessid);
    List<String> itemids = new ArrayList<String>(0);  
    List<PurApplyItem> purItemList = purApplyService.queryPurApplyItemListBySheetId( businessId );
    for ( PurApplyItem purApplyItem : purItemList ) {
        itemids.add( purApplyItem.getItemid()+"_"+purApplyItem.getInvcateid() );
    }
    PurApply purApply =StringUtils.isNotEmpty( businessId )?purApplyService.queryPurApplyBySheetId( businessId ):null;
    String isToBusiness = (null!=purApply?purApply.getIsToBusiness():"N");
    LOG.info( "--ManagerAudit-onComplete:businessId:"+businessId+" isToBusiness:"+isToBusiness);
    try {
      // 更新状态和待办人
      if (!"".equals(businessId)) {
        if ( "Y".equals( isToBusiness ) ) {
            purApplyService.autoSendDataToBusiness( businessId, taskName,JsonHelper.fromBeanToJsonString( itemids ));
        }else {
            purApplyService.updatePurApplyToPass(businessId, taskName);
        }
      }
    } catch (Exception e) {
      LOG.info(">>>>>>>>>>>>>>>>>>> 更新待办人异常", e);
    }
  }
}
