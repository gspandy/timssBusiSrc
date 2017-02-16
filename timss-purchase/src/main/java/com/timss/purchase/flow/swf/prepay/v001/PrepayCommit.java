package com.timss.purchase.flow.swf.prepay.v001;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.flow.abstr.DefOrderPayProcess;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class PrepayCommit extends DefOrderPayProcess {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private PurPayService purPayService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    public PurPayDao purPayDao;
    private static Logger LOG = Logger.getLogger(PrepayCommit.class);
    
    @SuppressWarnings("unchecked")
    @Override
    public void init(TaskInfo taskInfo) {
      UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
      String procInstId = taskInfo.getProcessInstanceId();
      String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
      String userId = "";
      String userName = "";
      String siteId = userInfoScope.getSiteId();
      List<String> uIdList = (List<String>) workflowService.getVariable(procInstId,WorkFlowConstants.CURRENT_TASK_CANDIDATE_USER);
      try {
          PurPayVO purPayVO = purPayService.queryPurPayVoByPayId( userInfoScope, businessId );
          userName = authManager.retriveUserById(purPayVO.getCreateuser(), siteId).getName();
          if (null == uIdList || uIdList.isEmpty()) {
              uIdList = Collections.emptyList();
          } else {
              StringBuffer sbuffer = new StringBuffer("");
              for (String userIdStr : uIdList) {
                 SecureUser user = authManager.retriveUserById(userIdStr, siteId);
                 sbuffer.append(user.getName()).append(",");
              }
              userName = sbuffer.substring(0, sbuffer.length() - 1);
          }
          if (StringUtils.isNotEmpty(userInfoScope.getParam("userId"))) {
              // 退回
              userId = userInfoScope.getParam("userId");
              userName = authManager.retriveUserById(userId, siteId).getName();
          }
          purPayService.updatePurPayInfoTransactor( businessId, userName );
      } catch (Exception e) {
          LOG.info( ">>>>>>>>>>>>>>>>>>> 更新状态和待办人异常", e );
      }
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
      String procInstId = taskInfo.getProcessInstanceId();
      String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
      /**
       * 第一个环节结束时需要更新一下流程变量
       */
      List<PurPayVO> payVOs = purPayDao.queryPurPayByCondition( null, businessId );
      String itemClassId = "";
      String sheetClassId = "";
      Double pay = 0D; 
      if ( 0<payVOs.size() ) {
          pay = payVOs.get( 0 ).getPay();
          String sheetId = payVOs.get( 0 ).getSheetId(); 
          List<String> itemClassIds = purPayDao.queryPurPayItemClassId( sheetId );
          List<String> sheetClassIds = purPayDao.queryPurPaySheetClassId( sheetId );
          if ( 0<itemClassIds.size() ){
              itemClassId = itemClassIds.get( 0 );
          }
          if ( 0<sheetClassIds.size() ) {
              sheetClassId = sheetClassIds.get( 0 );
          }
      }
      String itemType = "";//给GKHandler用的变量
      if(StringUtils.isNotEmpty( sheetClassId )){
          if(sheetClassId.contains( "计算机" )){
              itemType="IT";
          }else if(sheetClassId.contains( "办公" )){
              itemType="OFFICE";
          }else if ( sheetClassId.contains( "消防" ) ) {
              itemType="FIRE";
          }else if ( sheetClassId.contains( "劳保" ) ) {
              itemType="LABOUR";
          }else if(StringUtils.isNotEmpty( itemClassId )){
              if ( itemClassId.contains( "易耗" ) ) {
                  itemType="EASYCONSUMED";
              }
          }
      }
      workflowService.setVariable( procInstId, "itemType", itemType);
      workflowService.setVariable( procInstId, "needGKAudit", sheetClassId.contains( "计算机" )&&itemClassId.contains( "易耗" )||sheetClassId.contains( "劳保" )||sheetClassId.contains( "消防" )||sheetClassId.contains( "办公" )?"Y":"N");
      workflowService.setVariable( procInstId, "totalPayment", pay>500000?"HIGH":"LOW");
      try {
          purPayService.updatePurPayStatus( businessId, "processing" );
      } catch (Exception e) {
          LOG.warn(">>>>>>>>>>>>>>>>>>> 更新状态异常", e);
      }
    }
}
