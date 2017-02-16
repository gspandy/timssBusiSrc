package com.timss.workorder.util;

import org.activiti.engine.task.Task;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.WoUtilService;
import com.yudean.homepage.bean.HomepageWorkTask;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;


public class WorkOrderServiceImplUtil {
    
    /**
     * @description:提交或者暂存工单时，设置首页待办或者草稿中跳转路径
     * @author: 王中华
     * @createDate: 2016-2-16
     * @param siteid
     * @param woId
     * @return:
     */
    public static  String getJumpPathBySiteId(String siteid,String woId) {
        String result = null;
        if("ZJW".equals( siteid )){
            result = "workorder/workorder/openWorkOrderAddPageZJW.do?woId=" + woId;
        }else if("SWF".equals( siteid )){
            result =  "workorder/workorder/openWorkOrderAddPage.do?woId=" + woId;
        }
        return result;
    }
    
    /**
     * @description:暂存工单时，设置工单状态
     * @author: 王中华
     * @createDate: 2016-2-16
     * @param siteid
     * @return:
     */
    public static  String getDraftStatusBySiteid(String siteid) {
        String result = null;
        if("ZJW".equals( siteid )){
            result = WoProcessStatusUtilZJW.DRAFT;
        }else if("SWF".equals( siteid )){
            result = WoProcessStatusUtil.DRAFT_STR;
        }
        return result;
    }
    
    /**
     * @description:暂存工单时，在草稿中的显示状态
     * @author: 王中华
     * @createDate: 2016-2-16
     * @param homeworkTask
     * @param siteid
     * @param itcMvcService:
     */
    public static  void setDraftStatusNameBySiteid(HomepageWorkTask homeworkTask, String siteid,ItcMvcService itcMvcService) {
        if("ZJW".equals( siteid )){
            homeworkTask.setStatusName( WoProcessStatusUtilZJW.getEnumName(itcMvcService, "WO_STATUS", 
                    WoProcessStatusUtilZJW.DRAFT) ); 
        }else if("SWF".equals( siteid )){
            homeworkTask.setStatusName( WoProcessStatusUtil.getEnumName( itcMvcService, "WO_STATUS",
                    WoProcessStatusUtil.DRAFT_STR ) );
        }
        
        
    }
    
    
    /**
     * @description:提交工单时，设置工单状态
     * @author: 王中华
     * @createDate: 2016-2-16
     * @param siteId
     * @return:
     */
    public static String getCommitStatusBySiteid(String siteId) {
        String result = null;
        if("ZJW".equals( siteId )){
            result = WoProcessStatusUtilZJW.WORK_ORDER_COMMIT;
        }else if("SWF".equals( siteId )){
            result = WoProcessStatusUtil.WO_COMMIT_STR;
        }
        return result;
    }

    /**
     * @description:提交工单时，新建待办里面的状态设置
     * @author: 王中华
     * @createDate: 2016-2-16
     * @param homeworkTask
     * @param siteId
     * @param itcMvcService:
     */
    public static void setCommitStatusNameBySiteid(HomepageWorkTask homeworkTask, String siteId,
            ItcMvcService itcMvcService) {
        if("ZJW".equals( siteId )){
            homeworkTask.setStatusName( WoProcessStatusUtilZJW.getEnumName(itcMvcService, "WO_STATUS", 
                    WoProcessStatusUtilZJW.WORK_ORDER_COMMIT) ); // 流程状态名称
        }else if("SWF".equals( siteId )){
            homeworkTask.setStatusName( WoProcessStatusUtil.getEnumName( itcMvcService, "WO_STATUS",
                    WoProcessStatusUtil.DUTY_CONFIRM_DEFECT_STR ) ); // 流程状态名称
        }
        
    }

    
    public static void setWorkflowVariable(WoUtilService woUtilService,WorkflowService workflowService, Task task,String processInstId, 
            WorkOrder workOrder,UserInfoScope userInfoScope) {
        String siteId = userInfoScope.getSiteId();
        String userId = userInfoScope.getUserId();
        
        if("ZJW".equals( siteId )){
          //设置变量
            if(workOrder.getWoCommitHandleStyle().equals("SHIELD_CODE")){
                workflowService.setVariable(processInstId, "commitHandleStyle", "SHIELD_CODE");
            }else if(workOrder.getWoCommitHandleStyle().equals("MAINTAIN")){
                workflowService.setVariable(processInstId, "commitHandleStyle", "MAINTAIN");
            }else if(workOrder.getWoCommitHandleStyle().equals("REMOTE_RESET")){
                workflowService.setVariable(processInstId, "commitHandleStyle", "REMOTE_RESET");
                //状态为已完成
                String woStatus = WoProcessStatusUtilZJW.DONE;
                woUtilService.updateWoStatus(String.valueOf(workOrder.getId()), woStatus);
               //流程结束
                workflowService.complete(task.getId(), userId, userId, null, "远程复位", false);
            }else{
                workflowService.setVariable(processInstId, "commitHandleStyle", "REPAIR_NOW");
            }
        }else if("SWF".equals( siteId )){
            // 设置变量
            if ( workOrder.getWorkOrderTypeCode().equals( "DEFECT" ) ) {
                workflowService.setVariable( processInstId, "woType", "DEFECT" );
            } else {
                workflowService.setVariable( processInstId, "woType", "OTHER" );
                workflowService.setVariable( processInstId, "isDefect", "Y" );// 工作流判断条件用到
                workflowService.setVariable( processInstId, "woSpecCode", workOrder.getWoSpecCode() );// WoSelectExpertBySpec用到
            }
        }
        
    }

    public static String getObsoleteWoStatusNameBySite(ItcMvcService itcMvcService,String siteid) {
        String result = null;
        if("ZJW".equals( siteid )){
            result = WoProcessStatusUtilZJW.getEnumName(itcMvcService, "WO_STATUS", WoProcessStatusUtilZJW.OBSELETE);
        }else if("SWF".equals( siteid )){
            result = WoProcessStatusUtil.getEnumName( itcMvcService, "WO_STATUS",
                    WoProcessStatusUtil.OBSELETE_STR );
        }
        return result;
    }

    public static String getWoObsoleteStatusBySite(String siteid) {
        String status = null;
        if("ZJW".equals( siteid )){
            status = WoProcessStatusUtilZJW.OBSELETE;
        }else if("SWF".equals( siteid )){
            status = WoProcessStatusUtil.OBSELETE_STR;
        }
        return status;
    }
   
}
