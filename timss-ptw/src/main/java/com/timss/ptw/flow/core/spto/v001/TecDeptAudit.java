package com.timss.ptw.flow.core.spto.v001;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.bean.PtoRelateUserInfo;
import com.timss.ptw.dao.SptoInfoDao;
import com.timss.ptw.vo.SptoInfoVo;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class TecDeptAudit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private SptoInfoDao sptoInfoDao;
    @Autowired
    private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger( TecDeptAudit.class );
    @Override
    public void init(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘技术审核’的init(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        SptoInfoVo sptoInfo = new SptoInfoVo();
        sptoInfo.setId( id );
        sptoInfo.setStatus( "secondstep" );
        String[] params = new String[] { "status" };
        sptoInfoDao.updateSptoInfo( sptoInfo, params );
        LOG.debug( "-------------进入‘技术审核’的init(),处理业务逻辑结束-----------------" );
        
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        LOG.debug( "-------------进入‘技术审核’的beforeRollback(),开始处理业务逻辑-----------------" );
        
        LOG.debug( "-------------进入‘技术审核’的beforeRollback(),开始处理业务逻辑-----------------" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘技术审核’的onComplete(),开始处理业务逻辑-----------------" );
        //更新审批人
        Map<String, String> workFlowInfoMap = workflowService.getElementInfo(taskInfo.getTaskInstId());
        String modifiable = workFlowInfoMap.get("modifiable");
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        PtoRelateUserInfo puserInfo = new PtoRelateUserInfo();
        puserInfo.setSiteid( userInfoScope.getSiteId() );
        puserInfo.setUserId( userInfoScope.getUserId() );
        puserInfo.setUserName( userInfoScope.getUserName() );
        puserInfo.setStandardPtoId( id );
        puserInfo.setType( modifiable );
        sptoInfoDao.deleteSptoRelateUser( puserInfo );
        puserInfo.setOprDate( new Date() );
        sptoInfoDao.insertSptoRelateUser( puserInfo );
        LOG.debug( "-------------进入‘技术审核’的onComplete(),处理业务逻辑结束-----------------" );
    }
}
