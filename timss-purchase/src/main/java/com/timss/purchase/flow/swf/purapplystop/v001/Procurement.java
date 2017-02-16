package com.timss.purchase.flow.swf.purapplystop.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.timss.purchase.service.PurApplyService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Procurement extends TaskHandlerBase {
    private static final Logger LOG = Logger.getLogger(Procurement.class);
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    @Qualifier("PurApplyServiceImpl")
    private PurApplyService purApplyService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Override
    public void onComplete(TaskInfo taskInfo) {
        String procInstId = taskInfo.getProcessInstanceId();
        String sheetId = String.valueOf( workflowService.getVariable( procInstId, "businessId" ) );
        try {
            //第一个环节
            purApplyService.startStopPurApply( sheetId,procInstId );
            //最后一个环节
            purApplyService.stopPurApply( sheetId );
        } catch (Exception e) {
            LOG.error( "purapplystop-Procurement-onComplete:", e );
        }
        
    }
}
