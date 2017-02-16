package com.timss.purchase.flow.itc.purapplystop.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.service.PurApplyService;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Procurement extends TaskHandlerBase {
    private static final Logger LOG = Logger.getLogger(Procurement.class);
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PurApplyService purApplyService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Override
    public void onComplete(TaskInfo taskInfo) {
        String procInstId = taskInfo.getProcessInstanceId();
        String sheetId = String.valueOf( workflowService.getVariable( procInstId, "businessId" ) );
        //第一个环节
        try {
            purApplyService.startStopPurApply( sheetId,procInstId );
        } catch (Exception e) {
            LOG.error( "purapplystop-Procurement-onComplete:", e );
        }
    }
}
