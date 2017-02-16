package com.timss.atd.flow.hyg.overtime.v001;

import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.attendance.bean.OvertimeBean;
import com.timss.attendance.util.AtdUserPrivUtil;
import com.timss.attendance.util.AtdWorkFlowUtil;
import com.timss.attendance.util.VOUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 提交加班申请
 */
public class ApplyOvertime extends TaskHandlerBase {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private AtdUserPrivUtil privUtil;
    @Autowired
    private AtdWorkFlowUtil wfUtil;
    @Autowired
    private HomepageService homepageService;
    
    final String handlerName="HYG ApplyOvertime";
    Logger log = LoggerFactory.getLogger( ApplyOvertime.class );
    
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
        		//在这里获取加班申请单里的人员，设置orgId，取第一个人
        		String orgId=bean.getItemList().get(0).getDeptId();
	        	workflowService.setVariable( instanceId, "orgId", orgId );
	        	String deptName = bean.getDeptName();
	        	String isSpecialDept;
	        	if("财务部".equals(deptName) || "综合部".equals(deptName)){
	        		isSpecialDept ="Y";
	        	}else{
	        		isSpecialDept ="N";
	        	}
	        	workflowService.setVariable( instanceId, "isSpecialDept", isSpecialDept);
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
        UserInfoScope userInfo = privUtil.getUserInfoScope();
        try {
        	String overtimeData = userInfo.getParam( "businessData" );
        	JSONObject jsonObject = JSONObject.fromObject( overtimeData );;
        	OvertimeBean bean=VOUtil.fromJsonToVoUtil( jsonObject.getString( "formData" ), OvertimeBean.class );
        	String flowCode = bean.getNum();
            String reason = bean.getOverTimeReason();
            if( reason.length() > 50 ){
                reason = reason.substring( 0, 47 ) + "...";
            }
            homepageService.modify(null, flowCode, null, reason, null, null, null, null);
        } catch (Exception e) {
        	log.error( e.getMessage(), e );
        }
        wfUtil.completeCommonOvertimeApply(taskInfo);
        super.onComplete( taskInfo );
    }

    
}
