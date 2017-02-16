package com.timss.finance.flow.itc.commonone.v002;

import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.facade.util.InitUserAndSiteIdNewUtil;
import com.timss.finance.bean.FinanceMain;
import com.timss.finance.bean.FinanceMainDetail;
import com.timss.finance.bean.FinanceManagementApply;
import com.timss.finance.service.FinanceMainService;
import com.timss.finance.service.FinanceManagementApplyService;
import com.timss.finance.util.ChangeStatusUtil;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class StrFlow extends TaskHandlerBase {

    @Autowired
    private ItcMvcService itcMvcService;

    @Autowired
    WorkflowService wfs; // by type

    @Autowired
    IAuthorizationManager im;

    @Autowired
    private FinanceMainService financeMainService;
    @Autowired
    FinanceManagementApplyService financeManagementApplyService;
    
    public void init(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String siteid = userInfoScope.getSiteId();
        String pid = taskInfo.getProcessInstanceId();
        List<FinanceMainDetail> mainDtlList = (List<FinanceMainDetail>) wfs.getVariable( pid, "mainDtlList" );
        FinanceMainDetail fmd = mainDtlList.get( 0 );

        String fid = fmd.getFid();
        Map<String, Object> map = financeMainService.queryFinanceMainByFid( fid );
        FinanceMain fm = (FinanceMain) map.get( "financeMain" );

        boolean checkIsLeader = false;
        List<SecureUser> suList = im.retriveUsersWithSpecificGroup( siteid + "_INV_LEADER", null, true, true ); // groupid,组织机构,是否包含下属机构，是否返回活动用户
        for ( SecureUser su : suList ) {
            if ( su.getId().contains( fmd.getBeneficiaryid() ) && su.getName().contains( fmd.getBeneficiary() ) ) {
                checkIsLeader = true;
            }
        }

        wfs.setVariable( pid, "amount", fm.getTotal_amount() ); // 获取金额以判断会计分支还是总经理分支
        wfs.setVariable( pid, "userId", fmd.getBeneficiaryid() ); // 对于他人报销走用户确认时要用到
        wfs.setVariable( pid, "isLeader", checkIsLeader ); // 是否领导
        wfs.setVariable( pid, "beneficiaryid", fmd.getBeneficiaryid() ); // 部门经理审批时要用到,为实际报销人

        financeMainService.updateFinanceMainStatusByFid( "finance_flow_str", fid );

    }

    public void onComplete(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = (UserInfoScope) itcMvcService.getUserInfoScopeDatas();
        String fid = (String) wfs.getVariable( taskInfo.getProcessInstanceId(), "fid" );
        String userId = userInfoScope.getUserId();

        Map<String, Object> map = financeMainService.queryFinanceMainByFid( fid );
        FinanceMain fm = (FinanceMain) map.get( "financeMain" );
        //获取对应的申请单Id
        String applyId = fm.getApplyId();
        if(applyId != null){
            //查找对应的申请单bean
            FinanceManagementApply financeManagementApply = financeManagementApplyService.queryFinanceManagementApplyById( applyId );
         
            //找到对应的申请单流程的processInStanceId
            String applyProcessInstanceId = financeManagementApply.getProInstId();
            List<Task> taskInfoList = wfs.getAllActiveTasks( applyProcessInstanceId );       
           if( !taskInfoList.isEmpty()){
             //获取对应的申请单的taskId        
               String taskId = taskInfoList.get( 0 ).getId();//任务id
               String assignee = userId; //执行人 ， 可以为null，表示取第一位候选人为执行人
               String owner = userId;  //owner , 可以为null，表示与执行人一样
               Map<String, List<String>> userIds = null;  // 下一环节候选人，可以为null
               String message ="同意"; //意见，可以为null
               boolean checkAssignee = true;//是否执行“执行人合法性检查”
               wfs.complete( taskId, assignee, owner, userIds, message, checkAssignee  );
           } 
        }
        
    }

}
