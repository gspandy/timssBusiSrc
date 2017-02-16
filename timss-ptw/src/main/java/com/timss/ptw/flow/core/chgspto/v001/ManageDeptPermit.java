package com.timss.ptw.flow.core.chgspto.v001;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
        String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        
        //更新批准人
        Map<String, String> workFlowInfoMap = workflowService.getElementInfo(taskInfo.getTaskInstId());
        String modifiable = workFlowInfoMap.get("modifiable");
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
        //旧的设为过期 补充版本号   新的设置版本号以及状态
        Map<String, String> statusCon = new HashMap<String, String>(0);
        statusCon.put( "writeStatus", "write" );
        statusCon.put( "auditStatus", "audit" );
        statusCon.put( "permitStatus", "permit" );
        statusCon.put( "preauditStatus", "preaudit" );
        SptoInfoVo newSptoInfoVo = sptoInfoDao.querySptoInfoById( id,statusCon );
       
        
        newSptoInfoVo.setStatus( "passed" );
        Integer newVersion = 1 + sptoInfoDao.getMaxVersionByCode(newSptoInfoVo.getCode(),userInfoScope.getSiteId());  //获取最大版本号，没有版本的时候接口返回0
        newSptoInfoVo.setVersion( newVersion.toString() );
//        newSptoInfoVo.setBeginDate( new Date() );
//        String[] params2 = new String[] { "status","version","beginDate" };
        //设置生效时间和实效时间
        Calendar calendar = Calendar.getInstance();
        Date beginTime = calendar.getTime();
        newSptoInfoVo.setBeginTime( beginTime );
        calendar.add(Calendar.DAY_OF_YEAR, 365);
        Date endTime = calendar.getTime();
        newSptoInfoVo.setEndTime( endTime );
        String[] params2 = new String[] { "status","version","beginTime","endTime"};
        sptoInfoDao.updateSptoInfo( newSptoInfoVo, params2 );
        
        LOG.debug( "-------------进入‘管理批准’的complete(),处理业务逻辑结束-----------------" );
        //TODO 修改同编号的标票，将正在生效的标票失效时间改为当前时间
        sptoInfoDao.invalidateOtherSameCodeSpto(newSptoInfoVo.getId(),newSptoInfoVo.getCode(),
                userInfoScope.getUserId(),userInfoScope.getSiteId(),new Date());
        
    }
}
