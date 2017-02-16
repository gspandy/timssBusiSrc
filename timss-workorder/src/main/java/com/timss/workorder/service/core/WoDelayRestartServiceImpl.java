package com.timss.workorder.service.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.service.WoDelayRestartService;
import com.timss.workorder.service.WorkOrderService;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.mvc.bean.userinfo.UserInfo;
import com.yudean.mvc.handler.ThreadLocalHandler;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.SelectUserService;
import com.yudean.workflow.service.WorkflowService;
@Service
public class WoDelayRestartServiceImpl implements WoDelayRestartService{

    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WorkOrderService workOrderService;  
    @Autowired
    private SelectUserService selectUserService;
    @Autowired
    private ItcMvcService itcMvcService;
    private static Logger logger = Logger.getLogger( WoDelayRestartServiceImpl.class );
    
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void woDelayRestart() throws Exception {

        logger.debug( "工单延迟重启定时任务扫描开始时间" + new Date() );
        // 有优先级，工单处于“workPlan”状态
        List<WorkOrder> woList = workOrderService.queryAllDelayWoNoSiteId();

        for ( int i = 0; i < woList.size(); i++ ) {
            WorkOrder workOrder = woList.get( i );
            long dutyConfirmDelayTime = workOrder.getModifydate().getTime();
            long now = new Date().getTime();
            long delayLen =  (int)(workOrder.getWoDelayLen()*60*60*1000);
            
            if(now - dutyConfirmDelayTime > delayLen){ //满足条件(延期超时)，则自动重启工单
                String processInstId = workOrder.getWorkflowId();
                String siteid = workOrder.getSiteid();
                List<Task> activities = workflowService.getActiveTasks( processInstId );
                Task task = activities.get( 0 );
                
                String assignee = siteid + "scheduler";  //执行人
                
              //构造登录人
                UserInfo userinfo=itcMvcService.getUserInfo(assignee,siteid);
                ThreadLocalHandler.createNewVarableOweUserInfo(userinfo);
                
                //如果要重新启动工单，则需要找到启动后给谁
                Map<String, List<String>> userIds = new HashMap<String, List<String>>();
                List<SecureUser> secureUserList = selectUserService.byUserId(workOrder.getEndReportUser());
                List<String> auditUserList = getAuditUserList(secureUserList);
                String nextTaskDefKey = workOrderService.getDelayRestartNextTaskKey();
                userIds.put( nextTaskDefKey, auditUserList ); //下一个节点审批人
                
                workflowService.complete( task.getId(), assignee, null, userIds, "定时任务，自动重启工单", false );
           
                //修改工单的当前处理人
                Map<String, String> parmas = new HashMap<String, String>();
                parmas.put("woId", String.valueOf( workOrder.getId() ));
                parmas.put("currHandlerUser", secureUserList.get( 0 ).getId());
                parmas.put("currHandUserName", secureUserList.get( 0 ).getName());
                workOrderService.updateCurrHandUserById(parmas);
                
            }
            
        }
    
        
    }
	
    
    /**
     * @description:获取下一个节点的审批人
     * @author: 王中华
     * @createDate: 2016-6-22
     * @param workOrder
     * @return:
     */
    private List<String> getAuditUserList(List<SecureUser> secureUserList) {
        List<String>  result = new ArrayList<String>();
        for ( SecureUser secureUser : secureUserList ) {
            result.add( secureUser.getId() );
        }
        return result;
    }
    
}
