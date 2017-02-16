package com.timss.purchase.flow.zjw.purapply.v006;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.bean.PurApply;
import com.timss.purchase.flow.abstr.DefApplyProcess;
import com.timss.purchase.service.PurApplyService;
import com.yudean.itc.dto.support.AppEnum;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.mvc.service.ItcSysConfService;
import com.yudean.workflow.service.WorkflowService;

/**
 * @title: Draft工作流控制类
 * @description:
 * @company: gdyd
 * @className: Draft.java
 * @author: 890162
 * @createDate: 2015-10-19
 * @updateUser: yuanzh
 * @version: 1.0
 */
public class Draft extends DefApplyProcess {
    private static final Logger LOG = Logger.getLogger(Draft.class);
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
        String businessId = String.valueOf( workflowService.getVariable(processInstId, "businessId") );
        PurApply purApply = purApplyService.queryPurApplyBySheetId( businessId );
        String sheetClassid = purApply.getSheetclassid();
        List<AppEnum> emList = itcMvcService.getEnum( "ITEMAPPLY_TYPE" );
        for ( AppEnum appEnum : emList ) {
            if ( sheetClassid.equals( appEnum.getCode() ) ) {
                sheetClassid = appEnum.getLabel();
                break;
            }
        }
        String bgypysy = "";
        String scwhysy = "";
        String jjwzysy = "";
        try {
            bgypysy = itcSysConfService.queryBSysById( "zjw_bgypysy", "ZJW" ).getVal();
            scwhysy = itcSysConfService.queryBSysById( "zjw_scwhysy", "ZJW" ).getVal();
            jjwzysy = itcSysConfService.queryBSysById( "zjw_jjwzysy", "ZJW" ).getVal();
        } catch (Throwable e) {
            LOG.error( "--Draft onShowAudit发生异常--",e );
        }
        List<String> bgypysyList = Arrays.asList( bgypysy.split( "," ) );
        List<String> scwhysyList = Arrays.asList( scwhysy.split( "," ) );
        List<String> jjwzysyList = Arrays.asList( jjwzysy.split( "," ) );
        if ( bgypysyList.contains( sheetClassid ) ) {
            workflowService.setVariable( processInstId, "deptPreauditGroup", "ZJW_BGYPYSY" );
        }else if ( scwhysyList.contains( sheetClassid ) ) {
            workflowService.setVariable( processInstId, "deptPreauditGroup", "ZJW_SCWHYSY" );
        }else if ( jjwzysyList.contains( sheetClassid ) ) {
            workflowService.setVariable( processInstId, "deptPreauditGroup", "ZJW_JJWZYSY" );
        }
    }
}
