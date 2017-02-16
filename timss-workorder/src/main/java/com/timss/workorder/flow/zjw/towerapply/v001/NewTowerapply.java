package com.timss.workorder.flow.zjw.towerapply.v001;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.workorder.bean.TowerApply;
import com.timss.workorder.dao.WoTowerApplyDao;
import com.timss.workorder.flow.zjw.woapply.v001.NewWoapply;
import com.timss.workorder.service.WoTowerApplyService;
import com.timss.workorder.util.TowerApplyStatusUtil;
import com.yudean.itc.util.json.JsonHelper;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

public class NewTowerapply extends TaskHandlerBase {
	@Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private WoTowerApplyDao woTowerApplyDao;
    @Autowired
    private WoTowerApplyService woTowerApplyService;
    
    private static Logger logger = Logger.getLogger(NewWoapply.class);

    @Override
    public void init(TaskInfo taskInfo){
        String instantId = taskInfo.getProcessInstanceId();
        String towerApplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        logger.info( "MonitorAudit init --- instantId = " + instantId + "-- businessId = " + towerApplyId );
        
        //更新登塔申请状态
        woTowerApplyDao.updateTowerApplyStatus(towerApplyId, TowerApplyStatusUtil.FILL_APPLY);
        super.init( taskInfo );
    }

    @Override
    public void onComplete(TaskInfo taskInfo){
        UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
        String towerApplyId = workflowService.getVariable(taskInfo.getProcessInstanceId(), "businessId").toString();
        //修改当前处理人
        Map<String, String> parmas = null;
        try {
            parmas = woTowerApplyService.getTowerApplyCurrHanderInfo( userInfoScope );
        } catch (Exception e) {
        	logger.error(e.getMessage());
        }
        
        if(parmas != null){
            parmas.put( "towerApplyId", towerApplyId );
            woTowerApplyDao.updateCurrHander( parmas );
        }
        
      //修改表单数据
        String businessData = null;
        try {
                businessData = userInfoScope.getParam("businessData");
                TowerApply towerApply = JsonHelper.toObject(businessData, TowerApply.class);
                towerApply.setModifyDate( new Date() );
                towerApply.setModifyuser( userInfoScope.getUserId() );
                towerApply.setModifyuserName(userInfoScope.getUserName());
                woTowerApplyDao.updateTowerApply( towerApply );
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
