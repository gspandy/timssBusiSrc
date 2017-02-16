package com.timss.ptw.flow.core.spto.v001;

import java.util.Calendar;
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

public class ManageDeptPermit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private SptoInfoDao sptoInfoDao;
    @Autowired
    private WorkflowService workflowService;
    private static final Logger LOG = Logger.getLogger( ManageDeptPermit.class );
    @Override
    public void init(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘管理批准’的init(),开始处理业务逻辑-----------------" );
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        SptoInfoVo sptoInfo = new SptoInfoVo();
        sptoInfo.setId( id );
        sptoInfo.setStatus( "thirdstep" );
        String[] params = new String[] { "status" };
        sptoInfoDao.updateSptoInfo( sptoInfo, params );
        
        LOG.debug( "-------------进入‘管理批准’的init(),处理业务逻辑结束-----------------" );
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        LOG.debug( "-------------进入‘管理批准’的beforeRollback(),开始处理业务逻辑-----------------" );
       
        LOG.debug( "-------------进入‘管理批准’的beforeRollback(),开始处理业务逻辑-----------------" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        LOG.debug( "-------------进入‘管理批准’的complete(),开始处理业务逻辑-----------------" );
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        SptoInfoVo sptoInfo = new SptoInfoVo();
        sptoInfo.setId( id );
        sptoInfo.setStatus( "passed" );
        //sptoInfo.setBeginDate( new Date() );
        SptoInfoVo newSptoInfoVo = sptoInfoDao.querySptoMainInfoById( id ,userInfoScope.getSiteId());
        Integer newVersion = 1 + sptoInfoDao.getMaxVersionByCode(newSptoInfoVo.getCode(),userInfoScope.getSiteId());  //获取最大版本号，没有版本的时候接口返回0
        sptoInfo.setVersion( newVersion.toString() );
        //设置生效时间和实效时间
        Calendar calendar = Calendar.getInstance();
        Date beginTime = calendar.getTime();
        sptoInfo.setBeginTime( beginTime );
        calendar.add(Calendar.DAY_OF_YEAR, 365);
        Date endTime = calendar.getTime();
        sptoInfo.setEndTime( endTime );
        
//        String[] params = new String[] { "status","beginDate","version" };
        String[] params = new String[] { "status","version","beginTime","endTime" };
        sptoInfoDao.updateSptoInfo( sptoInfo, params );
        
        //更新批准人
        Map<String, String> workFlowInfoMap = workflowService.getElementInfo(taskInfo.getTaskInstId());
        String modifiable = workFlowInfoMap.get("modifiable");
        PtoRelateUserInfo puserInfo = new PtoRelateUserInfo();
        puserInfo.setSiteid( userInfoScope.getSiteId() );
        puserInfo.setUserId( userInfoScope.getUserId() );
        puserInfo.setUserName( userInfoScope.getUserName() );
        puserInfo.setStandardPtoId( id );
        puserInfo.setType( modifiable );
        sptoInfoDao.deleteSptoRelateUser( puserInfo );
        puserInfo.setOprDate( new Date() );
        sptoInfoDao.insertSptoRelateUser( puserInfo );
        LOG.debug( "-------------进入‘管理批准’的complete(),处理业务逻辑结束-----------------" );
        //TODO 修改同编号的标票，将正在生效的标票失效时间改为当前时间
        sptoInfoDao.invalidateOtherSameCodeSpto(newSptoInfoVo.getId(),newSptoInfoVo.getCode(),
                userInfoScope.getUserId(),userInfoScope.getSiteId(),new Date());
        
    }
}
