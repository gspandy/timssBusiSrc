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

public class DevDeptWrite extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private SptoInfoDao sptoInfoDao;
    @Autowired
    private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger( DevDeptWrite.class );
    @Override
    public void init(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘设备编写’的init(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        SptoInfoVo sptoInfo = new SptoInfoVo();
        sptoInfo.setId( id );
        sptoInfo.setStatus( "firststep" );
        String[] params = new String[] { "status" };
        sptoInfoDao.updateSptoInfo( sptoInfo, params );
        LOG.debug( "-------------进入‘设备编写’的init(),处理业务逻辑结束-----------------" );
        
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey){
        LOG.debug( "-------------进入‘设备编写’的beforeRollback(),开始处理业务逻辑-----------------" );
        
        LOG.debug( "-------------进入‘设备编写’的beforeRollback(),处理业务逻辑结束-----------------" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘设备编写’的onComplete(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        //更新是否为草稿的状态
        SptoInfoVo sptoInfo = new SptoInfoVo();
        sptoInfo.setId( id );
        sptoInfo.setIsDraft( "N" );
        String[] params = new String[] { "isDraft" };
        sptoInfoDao.updateSptoInfo( sptoInfo, params );
        //更新编写人
        Map<String, String> workFlowInfoMap = workflowService.getElementInfo(taskInfo.getTaskInstId());
        String modifiable = workFlowInfoMap.get("modifiable");
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        PtoRelateUserInfo puserInfo = new PtoRelateUserInfo();
        puserInfo.setSiteid( userInfoScope.getSiteId() );
        puserInfo.setUserId( userInfoScope.getUserId() );
        puserInfo.setUserName( userInfoScope.getUserName() );
        puserInfo.setType( modifiable );
        puserInfo.setStandardPtoId( id );
        sptoInfoDao.deleteSptoRelateUser( puserInfo );
        puserInfo.setOprDate( new Date() );
        sptoInfoDao.insertSptoRelateUser( puserInfo );
        LOG.debug( "-------------退出‘设备编写’的onComplete(),开始处理业务逻辑-----------------" );
    }
}
