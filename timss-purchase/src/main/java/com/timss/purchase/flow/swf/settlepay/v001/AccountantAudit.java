package com.timss.purchase.flow.swf.settlepay.v001;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.flow.abstr.DefOrderPayProcess;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.service.swf.SendPayEmailService;
import com.timss.purchase.vo.PurPayDtlVO;
import com.yudean.itc.dto.sec.SecureUser;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMsgService;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;
import com.yudean.workflow.utils.WorkFlowConstants;

public class AccountantAudit extends DefOrderPayProcess {
    

	@Autowired
    private WorkflowService workflowService;
    @Autowired
    private PurPayService purPayService;
    @Autowired
    private ItcMvcService itcMvcService;
    @Autowired
    public IAuthorizationManager authManager;
    @Autowired
    ItcMsgService itcMsgService;
    @Autowired
    SendPayEmailService sendPayEmailService;
    
    private static Logger LOG = Logger.getLogger(AccountantAudit.class);
    
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
      try {
          UserInfoScope userInfo = itcMvcService.getUserInfoScopeDatas();
          purPayService.updatePurPayStatus( businessId, "processed" );
          purPayService.updatePurPayInfoTransactor( businessId, "" );
          purPayService.updatePurPayAuditDate( businessId );
          List<PurPayDtlVO> purPayDtlVOs =  purPayService.queryPurPayDtlVoListByCondition( userInfo, businessId, null, null );
          for ( PurPayDtlVO purPayDtlVO : purPayDtlVOs ) {
              String imtdid = purPayDtlVO.getImtdId();
              //如果本次报账的数目为0或空的时候 不会更新价格
              if ( null!=purPayDtlVO.getSendAccount()&&0!=purPayDtlVO.getSendAccount() ) {
                  Double price   = (purPayDtlVO.getNotaxTotal()+purPayDtlVO.getTaxTotal())/purPayDtlVO.getSendAccount();
                  Double tax   = purPayDtlVO.getTaxTotal()/purPayDtlVO.getSendAccount();
                  purPayService.updateImtdPrice( imtdid, price );  
                  //更新库存改造后流水表价格，并触发重新计算价格的
                  purPayService.updateImtrPrice( imtdid, price, tax );
              }
          }
          sendPayEmailService.sendPayEmail(businessId);  
      } catch (Exception e) {
          LOG.warn(">>>>>>>>>>>>>>>>>>> 更新状态异常", e);
      }
    }
}
