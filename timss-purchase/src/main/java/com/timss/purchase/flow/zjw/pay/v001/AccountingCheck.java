package com.timss.purchase.flow.zjw.pay.v001;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.flow.abstr.DefPayProcess;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.vo.PurPayDtlVO;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.bean.userinfo.UserInfoScope;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

public class AccountingCheck extends DefPayProcess {
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
    private static Logger LOG = Logger.getLogger(AccountingCheck.class);
    
    @Override
    public void init(TaskInfo taskInfo) {
      super.init( taskInfo );
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
      String procInstId = taskInfo.getProcessInstanceId();
      String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
      try {
          purPayService.updatePurPayStatus( businessId, "processed" );
          purPayService.updatePurPayInfoTransactor( businessId, "" );
          purPayService.updatePurPayAuditDate( businessId );
          UserInfoScope userInfoScope = itcMvcService.getUserInfoScopeDatas();
          PurPayVO purPayVO = purPayService.queryPurPayVoByPayId( userInfoScope, businessId );
          if ( "arrivepay".equals( purPayVO.getPayType() )||"settlepay".equals( purPayVO.getPayType() ) ) {
              List<PurPayDtlVO> purPayDtlVOs =  purPayService.queryPurPayDtlVoListByCondition( userInfoScope, businessId, null, null );
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
          }
          
      } catch (Exception e) {
          LOG.warn(">>>>>>>>>>>>>>>>>>> 更新状态异常", e);
      }
    }
}
