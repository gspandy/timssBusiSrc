package com.timss.atd.flow.itc.overtime.v001;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.service.StatService;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.ProcessStatusUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 加班申请-行政部门登记备案
 */
public class OvertimeXzbbeianHandler extends TaskHandlerBase {
    Logger logger = LoggerFactory.getLogger( OvertimeXzbbeianHandler.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private StatService statService;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="ITC OvertimeXzbbeianHandler";
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info(  handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        logger.info(  handlerName+" init" );
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.XZBDJBA);
        super.init( taskInfo );
    }

    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        logger.info(  handlerName+" onComplete" );
        wfUtil.updateOvertimeAuditStatusByTask(taskInfo, ProcessStatusUtil.CLOSED);
        String instanceId = taskInfo.getProcessInstanceId();
        String id = workflowService.getVariable( instanceId, "businessId").toString();
        //更新统计信息
        //statService.updateCurrentYearStat();
        
        //核定转补休时长
        wfUtil.updateOvertimeTransferCompensate(Integer.parseInt(id),null);
        
        try {
			wfUtil.checkWorkStatusAndStatByOvertimeId(Integer.parseInt(id));
		} catch (Exception e) {
			logger.error("error checkWorkStatusAndStatByOvertimeId("+id+")",e);
		}
        super.onComplete( taskInfo );
    }
}
