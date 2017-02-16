package com.timss.ptw.flow.zjw.std.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.bean.PtwStandardBean;
import com.timss.ptw.service.PtwStandardService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.bean.userinfo.impl.UserInfoScopeImpl;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class TecDeptAudit extends TaskHandlerBase {
    private static final Logger log = Logger.getLogger( TecDeptAudit.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwStandardService ptwStandardService;
    @Autowired
    private WorkflowService workflowService;
    
    /**
     * 
     * @description:拿到登录用户信息
     * @author: fengzt
     * @createDate: 2014年8月26日
     * @return:String
     */
    private UserInfoScope getUserInfoScope(){
        UserInfoScope userInfoScope = new UserInfoScopeImpl();
        try {
            userInfoScope = itcMvcService.getUserInfoScopeDatas();
        } catch (Exception e) {
            log.error( e.getMessage(), e );
        }
        return userInfoScope;
    }
    
    
    @Override
    public void init(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备批准发布’的init(),开始处理业务逻辑-----------------" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        String siteId = getUserInfoScope().getSiteId();
        
        PtwStandardBean bean = new PtwStandardBean();
        bean.setFlowStatus( siteId.toLowerCase() + "_flow_std_status_2" );
        bean.setInstantId( instanceId );
        bean.setId( id );
        
        int count = ptwStandardService.updatePtwStandardStatus( bean );
        log.debug( "-------------进入‘设备批准发布’的init(),处理业务逻辑结束,更新条数：" + count
                + "-----------------" );
        
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey){
        log.debug( "-------------进入‘设备批准发布’的beforeRollback(),开始处理业务逻辑-----------------" );
        
        log.debug( "-------------进入‘设备批准发布’的beforeRollback(),处理业务逻辑结束-----------------" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备批准发布’的onComplete(),开始处理业务逻辑-----------------" );
        log.debug( "-------------进入‘设备批准发布’的onComplete(),开始处理业务逻辑-----------------" );
    }
}
