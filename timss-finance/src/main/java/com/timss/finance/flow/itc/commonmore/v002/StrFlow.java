package com.timss.finance.flow.itc.commonmore.v002;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceManagementApplyService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class StrFlow extends TaskHandlerBase {
    @Autowired
    WorkflowService wfs; // by type

    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    IAuthorizationManager im;

    @Autowired
    private FinanceMainService financeMainService;
    @Autowired
    FinanceManagementApplyService financeManagementApplyService;
    
    public void init(TaskInfo taskInfo) {

        String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
        financeMainService.updateFinanceMainStatusByFid( "finance_flow_str", fid );

    }

    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = (UserInfoScope) itcMvcService.getUserInfoScopeDatas();
        String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), WorkFlowConstants.BUSINESS_ID );
        financeMainService.updateFinanceMainStatusByFid( "wait_user_confirm", fid );

        String userId = userInfoScope.getUserId();

        Map<String, Object> map = financeMainService.queryFinanceMainByFid( fid );
        FinanceMain fm = (FinanceMain) map.get( "financeMain" );
        // 获取对应的申请单Id
        String applyId = fm.getApplyId();
        if(applyId != null){
         // 查找对应的申请单bean
            FinanceManagementApply financeManagementApply = financeManagementApplyService
                    .queryFinanceManagementApplyById( applyId );

            // 找到对应的申请单流程的processInStanceId
            String applyProcessInstanceId = financeManagementApply.getProInstId();
            List<Task> taskInfoList = wfs.getAllActiveTasks( applyProcessInstanceId );
            if ( !taskInfoList.isEmpty() ) {
                // 获取对应的申请单的taskId
                String taskId = taskInfoList.get( 0 ).getId();// 任务id
                String assignee = userId; // 执行人 ， 可以为null，表示取第一位候选人为执行人
                String owner = userId; // owner , 可以为null，表示与执行人一样
                Map<String, List<String>> userIds = null; // 下一环节候选人，可以为null
                String message = "同意"; // 意见，可以为null
                boolean checkAssignee = true;// 是否执行“执行人合法性检查”
                wfs.complete( taskId, assignee, owner, userIds, message, checkAssignee );
            }  
        }
        

    }

}
