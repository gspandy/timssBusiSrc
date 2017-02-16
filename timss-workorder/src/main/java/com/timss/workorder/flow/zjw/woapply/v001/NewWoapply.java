package com.timss.workorder.flow.zjw.woapply.v001;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.bean.WoapplyRisk;
import com.timss.workorder.bean.WoapplySafeInform;
import com.timss.workorder.bean.WoapplyWorker;
import com.timss.workorder.bean.Workapply;
import com.timss.workorder.dao.WoWorkapplyDao;
import com.timss.workorder.service.WoUtilService;
import com.timss.workorder.service.WoapplyRiskService;
import com.timss.workorder.service.WoapplySafeInformService;
import com.timss.workorder.service.WoapplyWorkerService;
import com.timss.workorder.util.WoapplyStatusUtil;
import com.timss.workorder.util.WoapplyUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class NewWoapply extends TaskHandlerBase {
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoWorkapplyDao woWorkapplyDao;
    @Autowired
    private WoapplyWorkerService woapplyWorkerService;
    @Autowired
    private WoapplyRiskService woapplyRiskService;
    @Autowired
    private WoapplySafeInformService woapplySafeInformService;
    @Autowired
    private WoUtilService woUtilService;
    
    private static Logger logger = Logger.getLogger(NewWoapply.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String woapplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "MonitorAudit init --- instantId = " + instantId + "-- businessId = " + woapplyId );
        
        //更新开工申请状态
        woUtilService.updateWoaplyStatus(woapplyId, WoapplyStatusUtil.FILL_APPLY);
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
            throw new RuntimeException(e);
        }
        
        if(parmas != null){
            parmas.put( "woapplyId", woapplyId );
            woWorkapplyDao.updateCurrHander( parmas );
        }
        
        //修改表单数据
        String businessData = null;
        try {
                businessData = userInfoScope.getParam("businessData");
                businessData = businessData.replace( "\r\n", "\\r\\n" );//换行符转换
                JSONObject businessDataobj = JSONObject.fromObject( businessData );
                String woapplyFormDataStr = businessDataobj.getString( "woapplyFormData" );
                String safeInformFormDataStr = businessDataobj.getString( "safeInformFormData" );
                JSONObject safeInformFormDataObj = JSONObject.fromObject( safeInformFormDataStr );
                String safeinformDataStr = businessDataobj.getString( "safeinformData" );
                String workerDataStr = businessDataobj.getString( "workerData" );
                String riskAssessmentDataStr = businessDataobj.getString( "riskAssessmentData" );
                String uploadIds = businessDataobj.getString( "uploadIds" );
                
                Workapply workapply = JsonHelper.toObject(woapplyFormDataStr, Workapply.class);
                workapply.setSafeInformUser( safeInformFormDataObj.getString( "safeInformUser" ) );
                
                //安全交底内容
                List<WoapplySafeInform> woapplySafeInformList = WoapplyUtil.converToWoapplySafeInform(safeinformDataStr);
                //外来队伍施工人员
                ArrayList<WoapplyWorker> woapplyWorkerList = WoapplyUtil.convertToWoapplyWorkerList(workerDataStr);
                //风险评估
                ArrayList<WoapplyRisk> riskAssessmentList = WoapplyUtil.convertToRiskAssessmentList(riskAssessmentDataStr);
                
              //更新主表信息
                workapply.setModifydate( new Date() );
                workapply.setModifyuser( userInfoScope.getUserId() );
                woWorkapplyDao.updateWorkapply( workapply );
             
                //删除安全交底、交底内容、外来施工人员、风险评估内容
                woapplySafeInformService.deleteSafeInformByWoapplyId( woapplyId );
                woapplyWorkerService.deleteWoapplyWorkerByWoapplyId( woapplyId );
                woapplyRiskService.deleteRiskListByWoapplyId( woapplyId );
                
                WoapplyUtil.setWoapplyIdInList(woapplySafeInformList,woapplyWorkerList,riskAssessmentList,woapplyId);
              //添加安全交底内容
                woapplySafeInformService.insertWoapplySafeInformList( woapplySafeInformList );
                //添加施工人员信息
                woapplyWorkerService.insertWoapplyWorkerList( woapplyWorkerList );
                //添加风险评估信息
                woapplyRiskService.insertWorkapplyList( riskAssessmentList );
                //添加删除附件信息
                woUtilService.insertAttachMatch(woapplyId, uploadIds, "WORKAPPLY", "txkgsq");
        } catch (Exception e) {
                logger.error(e.getMessage());
                throw new RuntimeException(e);
        }
        
        super.onComplete( taskInfo ); 
    }
    
    @Override
    public void beforeRollback(TaskInfo taskInfo, String destTaskKey){
	 super.beforeRollback(taskInfo, destTaskKey);		
    }	

}
