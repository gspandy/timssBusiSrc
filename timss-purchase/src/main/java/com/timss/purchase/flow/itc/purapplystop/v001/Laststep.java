package com.timss.purchase.flow.itc.purapplystop.v001;

import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.service.PurApplyService;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class Laststep extends TaskHandlerBase {
    private static final Logger LOG = Logger.getLogger(Laststep.class);
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PurApplyService purApplyService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private HomepageService homepageService;
    @Override
    public void onComplete(TaskInfo taskInfo) {
        String procInstId = taskInfo.getProcessInstanceId();
        String sheetId = String.valueOf( workflowService.getVariable( procInstId, "businessId" ) );
        String orgProcessInstId = String.valueOf( workflowService.getVariable( procInstId, "orgProcessInstId" ) );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        //最后一个环节
        try {
            purApplyService.stopPurApply( sheetId );
        } catch (Exception e) {
            LOG.error( "--purapplystop-Laststep-onComplete:", e );
        }
        //如果原有的采购申请审批流程没有完结，需要触发原有采购流程的归档。
        if ( StringUtils.isNotEmpty( orgProcessInstId ) ) {
            List<Task> orgTaskList = workflowService.getActiveTasks( orgProcessInstId );
            Task orgTask = orgTaskList.get( 0 );
            homepageService.complete( orgTask.getProcessInstanceId(), userInfo );
        }
    }
}
