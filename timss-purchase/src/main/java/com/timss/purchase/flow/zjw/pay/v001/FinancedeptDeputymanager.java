package com.timss.purchase.flow.zjw.pay.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.purchase.dao.PurPayDao;
import com.timss.purchase.flow.abstr.DefPayProcess;
import com.timss.purchase.service.PurPayService;
import com.yudean.itc.manager.sec.IAuthorizationManager;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskInfo;

public class FinancedeptDeputymanager extends DefPayProcess {
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
    private static Logger LOG = Logger.getLogger(FinancedeptDeputymanager.class);
    
    @Override
    public void init(TaskInfo taskInfo) {
      super.init( taskInfo );
    }
    
    @Override
    public void onComplete(TaskInfo taskInfo) {
      String procInstId = taskInfo.getProcessInstanceId();
      String businessId = workflowService.getVariable(procInstId, "businessId") == null ? "": String.valueOf(workflowService.getVariable(procInstId, "businessId"));
      try {
          purPayService.updatePurPayStatus( businessId, "processing" );
      } catch (Exception e) {
          LOG.warn(">>>>>>>>>>>>>>>>>>> 更新状态异常", e);
      }
    }
}
