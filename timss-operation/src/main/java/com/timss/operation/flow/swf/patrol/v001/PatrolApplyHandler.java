package com.timss.operation.flow.swf.patrol.v001;

import java.util.Date;
import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.operation.bean.Patrol;
import com.timss.operation.dao.PatrolDao;
import com.timss.operation.util.ProcessStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 
 * @title: 提交点检日志申请
 * @description: {desc}
 * @company: gdyd
 * @className: PatrolApplyHandler.java
 * @author: fengtw
 * @createDate: 2015年11月3日
 * @updateUser: fengtw
 * @version: 1.0
 */
public class PatrolApplyHandler extends TaskHandlerBase {

    @Autowired
    private WorkflowService workflowService;
    
    @Autowired
    private ItcMvcService itcMvcService;
    
    @Autowired
    private PatrolDao patrolDao;
   
    
    Logger logger = LoggerFactory.getLogger( PatrolApplyHandler.class );

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        
        String instantId = taskInfo.getProcessInstanceId();
        String patrolId = workflowService.getVariable( instantId, "businessId").toString();
        logger.info( "PatrolApplyHandler init --- instantId = " + instantId + "-- patrolId = " + patrolId );
                        
        HashMap<String, Object> parmas = new HashMap<String, Object>();
        parmas.put( "instantId", instantId);
        parmas.put( "status", ProcessStatusUtil.APPLY_STR );//提交点检日志
        parmas.put( "patrolId", patrolId );
        patrolDao.updatePatrolStatus(parmas);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        logger.info( "PatrolApplyHandler onComplete" );
        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        try {
            //update的时候拿出传入的参数
            String formData = userInfo.getParam( "businessData" );
            if( StringUtils.isNotBlank( formData ) ){
                Patrol patrolBean = JsonHelper.fromJsonStringToBean( formData, Patrol.class );
                patrolBean.setModifyuser( userInfo.getUserId() );
                patrolBean.setModifyUserName( userInfo.getUserName() );
                patrolBean.setModifydate( new Date() );
                int count = patrolDao.updatePatrol( patrolBean );
                logger.info( "PatrolApplyHandler update patrol count=" + count );
            }
        } catch (Exception e) {
            logger.error( e.getMessage(), e );
        }
        super.onComplete( taskInfo );
    }

    
}
