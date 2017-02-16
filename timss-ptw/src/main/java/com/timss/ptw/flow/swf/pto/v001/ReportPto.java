package com.timss.ptw.flow.swf.pto.v001;


import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.ptw.bean.PtoOperItem;
import com.timss.ptw.dao.PtoInfoDao;
import com.timss.ptw.service.PtoInfoService;
import com.timss.ptw.service.PtwUtilService;
import com.timss.ptw.vo.PtoInfoVo;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class ReportPto extends TaskHandlerBase {
    private static final Logger log = Logger.getLogger( ReportPto.class );
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private PtwUtilService ptwUtilService;
    @Autowired
    private PtoInfoDao ptoInfoDao;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PtoInfoService ptoInfoService;
    
    
    
    @Override
    public void init(TaskInfo taskInfo) {
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfoScope.getUserId();
        String instanceId = taskInfo.getProcessInstanceId();
        String ptoId = workflowService.getVariable( instanceId, "businessId" ).toString();
        
        ptoInfoDao.updatePtoStatusById( ptoId, "report", userId, new Date() );  //PTW_PTO_STATUS
        
    }
   
    @Override
    public void onComplete(TaskInfo taskInfo) {
        log.debug( "-------------进入‘设备审批’的onComplete(),开始处理业务逻辑-----------------" );

        UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
        String userId = userInfo.getUserId();
        String siteid = userInfo.getSiteId();
        String instanceId = taskInfo.getProcessInstanceId();
        String ptoId = workflowService.getVariable( instanceId, "businessId" ).toString();
        
        // update的时候拿出传入的参数
        String ptoDataStr = null;
        try {
            ptoDataStr = userInfo.getParam( "businessData" );
        } catch (Exception e) {
            log.error( e.getMessage(), e );
            throw new RuntimeException( e );
        }
        log.info( "businessData=" + ptoDataStr );
        
        if ( StringUtils.isNotBlank( ptoDataStr ) ) {
            JSONObject jsonObject = JSONObject.fromObject( ptoDataStr );
            String ptoFormData = jsonObject.getString( "ptoFormData" );
            String ptoItemData = jsonObject.getString( "ptoItemData" );
            String uploadIds = jsonObject.getString( "uploadIds" );
            
            PtoInfoVo ptoInfoVo = JsonHelper.fromJsonStringToBean( ptoFormData, PtoInfoVo.class );
            ptwUtilService.setRelatePersonName(ptoInfoVo,siteid);
            
            List<PtoOperItem> operItems = JsonHelper.toList( ptoItemData, PtoOperItem.class );
            ptoInfoVo.setPtoOperItemList( operItems );
            ptoInfoVo.setAttach( uploadIds );
            
            try {
                String[] params = new String[] { "beginOperTime","endOperTime",
                        "operator", "operatorName", "ondutyPrincipal", "ondutyPrincipalName", "ondutyMonitor", "ondutyMonitorName","guardian", "guardianName", "commander", "commanderName"};
                ptoInfoService.updatePtoInfoNoWithWorkflow(ptoInfoVo, params, "report");
            } catch (Exception e) {
                log.error( e.getMessage(), e );
                throw new RuntimeException( e );
            }
        }
       
        ptoInfoDao.updatePtoStatusById( ptoId, "end", userId, new Date() );  //PTW_PTO_STATUS
        Map<String, String> params = new HashMap<String, String>();
        params.put( "currHandlerUser", "" );
        params.put( "currHandUserName", "" );
        params.put( "ptoId", ptoId );
        ptoInfoDao.updateCurrHandUserById(params);  //清空当前处理人
    }
    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String ptoId = workflowService.getVariable( taskInfo.getProcessInstanceId(), "businessId" ).toString();
        
        ptwUtilService.updatePtoCurrHandlerUser( ptoId, userInfoScope, "rollback" );
        
        String userId = userInfoScope.getUserId();
        ptoInfoDao.updatePtoStatusById( ptoId, "new", userId, new Date() );
    }
}
