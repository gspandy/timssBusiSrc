package com.timss.atd.flow.dpp.abnormity.v001;

import java.util.Map;

import net.sf.json.JSONObject;

import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.bean.LeaveBean;
import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.dao.AbnormityDao;
import com.timss.attendance.service.AbnormityService;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 提交考勤异常申请
 */
public class AbnormityApplyHandler extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private AbnormityService abService;
    @Autowired
    private HomepageService homepageService;
    @Autowired
    private AbnormityService abnormityService;
    
    
    Logger logger = LoggerFactory.getLogger( AbnormityApplyHandler.class );
    final String handlerName="DPP AbnormityApplyHandler";
    
    /**
     * 回滚前
     */
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        logger.info( handlerName+" beforeRollback" );
        super.beforeRollback( sourceTaskInfo, destTaskKey );
    }

    /**
     * 初始化
     */
    @Override
    public void init(TaskInfo taskInfo) {
    	logger.info( handlerName+" init" );
    	wfUtil.initCommonAbnormityApply(taskInfo);
        super.init( taskInfo );
    }

    @Override
    public void onShowAudit(String taskId){
    	logger.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
    	Task task = workflowService.getTaskByTaskId(taskId);
        try {
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	AbnormityBean bean = wfUtil.updateAbnormity(userInfo.getParam( "businessData" ));
        	String instanceId = task.getProcessInstanceId();
        	if(bean!=null&&bean.getItemList()!=null&&bean.getItemList().size()>0){
        		//在这里获取加班申请单里的人员，设置orgId，取第一个人
	        	String orgId=bean.getItemList().get(0).getDeptId();
	        	String userId=bean.getItemList().get(0).getUserId();
	        	workflowService.setVariable(instanceId, "userId", userId);
	        	String bzcyIds=privUtil.getGroupUserIdsStr("DPP_BZCY");//班组成员id
	        	String bzzfzIds=privUtil.getGroupUserIdsStr("DPP_BZZFZ");//班组正副职id
	        	//是班组成员
	        	if (bzcyIds.contains(userId)) {
					//也是班组正副职
	        		if (bzzfzIds.contains(userId)) {
						workflowService.setVariable( instanceId, "type", "N" );
					}else{
						workflowService.setVariable( instanceId, "type", "Y" );
					}
				}else{
					workflowService.setVariable( instanceId, "type", "N" );
				}
	        	workflowService.setVariable( instanceId, "orgId", orgId );
	       }else{
        		logger.error(handlerName+" onShowAudit bean:"+bean.toString());
        	}
        	 
        } catch (Exception e) {
        	logger.error( e.getMessage(), e );
        }
    }
    
    /**
     * 完成
     */
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	logger.info( handlerName+" onComplete" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	 String id = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId").toString();
        	AbnormityBean  bean = abnormityService.queryAbnormityBeanById(Integer.parseInt(id));
        	String flowCode = bean.getNum();
            String reason = bean.getReason();
            if( reason.length() > 50 ){
                reason = reason.substring( 0, 47 ) + "...";
            }
            homepageService.modify(null, flowCode, null, reason, null, null, null, null);
        } catch (Exception e) {
        	logger.error( e.getMessage(), e );
        }
    	wfUtil.completeCommonAbnormityApply(taskInfo);
        super.onComplete( taskInfo );
    }

    

}


