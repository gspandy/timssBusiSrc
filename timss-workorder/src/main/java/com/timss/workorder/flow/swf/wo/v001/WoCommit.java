package com.timss.workorder.flow.swf.wo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.WoProcessStatusUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class WoCommit  extends TaskHandlerBase{
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoUtilService woUtilService;
    private static Logger logger = Logger.getLogger(WoCommit.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WoCommit init --- instantId = " + instantId + "-- businessId = " + woId );
        
        //更新工单状态
        woUtilService.updateWoStatus(woId, WoProcessStatusUtil.WO_COMMIT_STR);
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "WoCommit onComplete --- instantId = " + instantId + "-- businessId = " + woId );

        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        Object flag  = itcMvcService.getLocalAttribute("isFromScheduler");  //判断是否从定时任务来
        String flagString = null;
        if(flag != null){
            flagString = flag.toString();
        }
        else{
        	flagString = "no";
        }
        //更新下一步处理人
        if("no".equals(flagString)){  //当是定时任务引起的新建工单时，什么都不做，直接返回
            woUtilService.updateWoCurrHandlerUser(woId, userInfoScope,"normal");
        }
        super.onComplete( taskInfo );
    }
}
