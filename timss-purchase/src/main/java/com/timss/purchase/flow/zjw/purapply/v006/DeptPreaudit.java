package com.timss.purchase.flow.zjw.purapply.v006;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.itc.dto.sec.Organization;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSysConfService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: DeptPreaudit工作流控制类
 * @description:
 * @company: gdyd
 * @className: DeptPreaudit.java
 * @author: 890162
 * @createDate: 2017-01-04
 * @updateUser: gucw
 * @version: 1.0
 */
public class DeptPreaudit extends DefApplyProcess {
    private static final Logger LOG = Logger.getLogger(DeptPreaudit.class);
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    IAuthorizationManager authManager;
    @Autowired
    ItcSysConfService itcSysConfService;
    @Autowired
    PurApplyService purApplyService;
    @Override
    public void onShowAudit(String taskId){
        Task task = workflowService.getTaskByTaskId( taskId );
        String processInstId = task.getProcessInstanceId();
        boolean isBz = false;
        String windfarmdeptString = "";
        try {
            windfarmdeptString = itcSysConfService.queryBSysById( "zjw_windfarmdepts", "ZJW" ).getVal();
        } catch (Throwable e) {
            LOG.error( "--DeptPreaudit onShowAudit发生异常--",e );
        }
        List<String> windfarmDeptList = Arrays.asList( windfarmdeptString.split( "," ) );
        String businessId = String.valueOf( workflowService.getVariable(processInstId, "businessId") );
        PurApply purApply = purApplyService.queryPurApplyBySheetId( businessId );
        List<Organization> orgs = itcMvcService.getUserInfo( purApply.getCreateuser(), "ZJW" ).getOrgs();
        //判断采购申请申请人所属的部门是否属于系统配置中的部门
        for ( Organization org : orgs ) {
            if ( windfarmDeptList.contains( org.getCode() ) ) {
                isBz = true;
                break;
            }
        }
        String orgCode = "";
        if ( 1<orgs.size() ) {
            for ( Organization org : orgs ) {
                String orgName =org.getName(); 
                if ( orgName.contains( "风电场" )||orgName.contains( "准备小组" )||orgName.contains( "洋前" )||orgName.contains( "勇士" )||orgName.contains( "红心楼" )||orgName.contains( "曲界" ) ) {
                    orgCode =  org.getCode();
                }
            }
        }
        if (0< orgs.size() &&StringUtils.isEmpty( orgCode )) {
            orgCode= orgs.get( 0 ).getCode();
        }
        workflowService.setVariable( processInstId, "orgId", orgCode );
        workflowService.setVariable( processInstId, "isBz", isBz?"Y":"N" );
    }
}
