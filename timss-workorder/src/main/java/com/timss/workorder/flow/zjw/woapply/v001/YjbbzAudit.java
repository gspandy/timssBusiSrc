package com.timss.workorder.flow.zjw.woapply.v001;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.bean.WorkOrder;
import com.timss.workorder.dao.WoWorkapplyDao;
import com.timss.workorder.dao.WorkOrderDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.util.WoProcessStatusUtilZJW;
import com.timss.workorder.util.WoapplyStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class YjbbzAudit extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoWorkapplyDao woWorkapplyDao;
    @Autowired
    private WoUtilService woUtilService;
    
    private static Logger logger = Logger.getLogger(YjbbzAudit.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woapplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "MonitorAudit init --- instantId = " + instantId + "-- businessId = " + woapplyId );
        
        //更新开工申请状态
        woUtilService.updateWoaplyStatus(woapplyId, WoapplyStatusUtil.RUN_LEADER_AUDIT);
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woapplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        //修改当前处理人
        Map<String, String> parmas = null;
        try {
            parmas = woUtilService.getWoapplyCurrHanderInfo( userInfoScope );
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        
        if(parmas != null){
            parmas.put( "woapplyId", woapplyId );
            woWorkapplyDao.updateCurrHander( parmas );
        }
        super.onComplete( taskInfo ); 
    }
    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String woapplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
       
        woUtilService.rollbackUpdateWoapplyCurrHander(userInfoScope,woapplyId);
        
	 super.beforeRollback(taskInfo, destTaskKey);		
    }	

}
