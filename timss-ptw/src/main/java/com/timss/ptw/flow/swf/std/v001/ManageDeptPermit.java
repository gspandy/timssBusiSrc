package com.timss.ptw.flow.swf.std.v001;

import java.util.Calendar;
import java.util.Date;

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

public class ManageDeptPermit extends TaskHandlerBase {
    private static final Logger log = Logger.getLogger( ManageDeptPermit.class );
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
        log.debug( "-------------进入‘设备审批’的init(),开始处理业务逻辑-----------------" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        String siteId = getUserInfoScope().getSiteId();
        
        PtwStandardBean bean = new PtwStandardBean();
        bean.setFlowStatus( siteId.toLowerCase() + "_flow_std_status_3" );
        bean.setInstantId( instanceId );
        bean.setId( id );
        
        int count = ptwStandardService.updatePtwStandardStatus( bean );
        log.debug( "-------------进入‘设备审批’的init(),处理业务逻辑结束,更新条数：" + count
                + "-----------------" );
        
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey){
        log.debug( "-------------进入‘设备审批’的beforeRollback(),开始处理业务逻辑-----------------" );
        
        log.debug( "-------------进入‘设备审批’的beforeRollback(),处理业务逻辑结束-----------------" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备审批’的onComplete(),开始处理业务逻辑-----------------" );
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        String siteId = getUserInfoScope().getSiteId();
        PtwStandardBean newSptwBean = ptwStandardService.queryPtwStandardById( id );
        
        PtwStandardBean bean = new PtwStandardBean();
        bean.setFlowStatus( siteId.toLowerCase() + "_flow_std_status_4" );
        bean.setInstantId( instanceId );
        bean.setId( id );
        bean.setApproveDate( new Date() );
        bean.setApproveUser( getUserInfoScope().getUserId() );
        bean.setApproveUserName( getUserInfoScope().getUserName() );
        //设置生效时间和实效时间
        Calendar calendar = Calendar.getInstance();
        Date beginTime = calendar.getTime();
        bean.setBeginTime( beginTime );
        calendar.add(Calendar.DAY_OF_YEAR, 365);
        Date endTime = calendar.getTime();
        bean.setEndTime( endTime );
        //设置版本号
        Integer newVersion = 1 + ptwStandardService.getMaxVersionByCode(newSptwBean.getWtNo(),siteId);  //获取最大版本号，没有版本的时候接口返回0
        bean.setVersion( newVersion );
//        String parentWtId = childBean.getParentWtId();
//        if( StringUtils.isNotBlank( parentWtId ) ){
//            PtwStandardBean parentBean = ptwStandardService.queryPtwStandardById( parentWtId );
//            bean.setVersion( parentBean.getVersion() + 1 );
//            //父 设为过期
//            int expireCount = ptwStandardService.deletePtwStandardBaseInfo( parentWtId, "expire" );
//            log.info( "父节点 ---- " + parentWtId + " 设置为过期  条数： " + expireCount );
//        }
        
        int count = ptwStandardService.updatePtwStandardStatus( bean );
        
        log.debug( "-------------进入‘设备审批’的onComplete(),开始处理业务逻辑 " + + count 
                + "-----------------" );
        
        ptwStandardService.invalidateOtherSameCodeSptw(newSptwBean.getId(),newSptwBean.getWtNo(),
                getUserInfoScope().getUserId(),getUserInfoScope().getSiteId());
    }
}
