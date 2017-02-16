package com.timss.atd.flow.swf.overtime.v001;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.bean.OvertimeItemBean;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 提交加班申请
 */
public class OvertimeApplyHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    
    final String handlerName="SWF OvertimeApplyHandler";
    Logger log = LoggerFactory.getLogger( OvertimeApplyHandler.class );
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        log.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
        log.info( handlerName+" init" );
        wfUtil.initCommonOvertimeApply(taskInfo);
        super.init( taskInfo );
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	//拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	//保存
        	OvertimeBean bean=wfUtil.updateOvertime(userInfo.getParam( "businessData" ));
        	String instanceId = params.get( "processInstId" ).toString();
        	if(bean!=null&&bean.getItemList()!=null&&bean.getItemList().size()>0){
        		//在这里获取加班申请单里的人员，设置orgId和是否包含部门负责人
	        	String orgId=bean.getItemList().get(0).getDeptId();
	        	workflowService.setVariable( instanceId, "orgId", orgId );
	        	
	        	String bmldIds=privUtil.getGroupUserIdsStr("SWF_BMLD");//部门负责人id
	        	String gotoStr="deptMgr";
	        	for (OvertimeItemBean item : bean.getItemList()) {
					if(bmldIds.contains(item.getUserId()+",")){
						gotoStr="viceMgr";
						break;
					}
				}
	        	workflowService.setVariable( instanceId, "goto", gotoStr );
        	}else{
        		log.error(handlerName+" onShowAudit bean:"+bean.toString());
        	}
        } catch (Exception e) {
        	log.error( e.getMessage(), e );
        }
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.info( handlerName+" onComplete" );
        wfUtil.completeCommonOvertimeApply(taskInfo);
        super.onComplete( taskInfo );
    }

    
}
