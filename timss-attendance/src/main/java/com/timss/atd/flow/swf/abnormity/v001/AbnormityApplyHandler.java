package com.timss.atd.flow.swf.abnormity.v001;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.AbnormityBean;
import com.timss.attendance.bean.AbnormityItemBean;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
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
    
    final String handlerName="SWF AbnormityApplyHandler";
    
    Logger log = LoggerFactory.getLogger( AbnormityApplyHandler.class );
    
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
    	wfUtil.initCommonAbnormityApply(taskInfo);
        super.init( taskInfo );
    }

    @Override
    public void onShowAudit(String taskId){
    	log.info( handlerName+" onShowAudit" );
    	UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
            //拿出传入的参数
        	Map<String, Object>params=VOUtil.fromJsonToHashMap(userInfo.getParam( "businessData" ));
        	//保存修改
        	AbnormityBean bean=wfUtil.updateAbnormity(userInfo.getParam( "businessData" ));
            String instanceId = params.get( "processInstId" ).toString();
            if(bean!=null&&bean.getItemList()!=null&&bean.getItemList().size()>0){
            	//是否仅备案
            	workflowService.setVariable( instanceId, "isBeian", "Y".equals(params.get( "isBeian" ).toString())?"Y":"N" );
            	
            	//取第一个详情项来决定流程走向
            	AbnormityItemBean firstItem=bean.getItemList().get(0);
            	//考勤异常类别
            	workflowService.setVariable( instanceId, "type", firstItem.getCategory() );
            	workflowService.setVariable( instanceId, "orgId", firstItem.getDeptId() );
            	
            	String bmldIds=privUtil.getGroupUserIdsStr("SWF_BMLD");//部门负责人
            	String isBMLD="N";
            	String isRLDQ="N";
	        	for (AbnormityItemBean item : bean.getItemList()) {
					if(bmldIds.contains(item.getUserId()+",")){//是否含有部门负责人
						isBMLD="Y";
					}
					if("1232403".equals(item.getDeptId())){//是否含有人力党群部人员
						isRLDQ="Y";
					}
				}
            	workflowService.setVariable( instanceId, "isBMLD", isBMLD );
            	workflowService.setVariable( instanceId, "isRLDQ", isRLDQ );
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
    	wfUtil.completeCommonAbnormityApply(taskInfo);
        super.onComplete( taskInfo );
    }
}
