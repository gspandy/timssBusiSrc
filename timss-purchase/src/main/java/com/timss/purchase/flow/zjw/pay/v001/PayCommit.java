package com.timss.purchase.flow.zjw.pay.v001;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.flow.abstr.DefPayProcess;
import com.timss.purchase.service.PurPayService;
import com.timss.purchase.vo.PurPayVO;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

public class PayCommit extends DefPayProcess {
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
    private static Logger LOG = Logger.getLogger(PayCommit.class);
    
    @Override
    public void init(TaskInfo taskInfo) {
      super.init( taskInfo );
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
      String procInstId = taskInfo.getProcessInstanceId();
      String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
      /**
       * 第一个环节结束时需要更新一下流程变量
       */
      List<PurPayVO> payVOs = purPayDao.queryPurPayByCondition( null, businessId );
      Double pay = 0D; 
      if ( 0<payVOs.size() ) {
          pay = payVOs.get( 0 ).getPay();
      }      
      workflowService.setVariable( procInstId, "isLess", pay>=50000?"N":"Y");
      try {
          purPayService.updatePurPayStatus( businessId, "processing" );
      } catch (Exception e) {
          LOG.warn(">>>>>>>>>>>>>>>>>>> 更新状态异常", e);
      }
    }
}
