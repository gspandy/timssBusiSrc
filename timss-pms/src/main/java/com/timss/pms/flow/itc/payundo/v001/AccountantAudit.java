package com.timss.pms.flow.itc.payundo.v001;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Pay;
import com.timss.pms.service.PayService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.timss.pms.util.InitUserAndSiteIdUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * 退票申请 会计审批节点类
 * 
 * @ClassName: AccountantAudit
 * @company: gdyd
 * @Description:TODO
 * @author: 谷传伟
 * @date: 2015-08-28 
 */
public class AccountantAudit extends TaskHandlerBase {
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    PayService payService;
    private static final Logger LOGGER = Logger.getLogger( AccountantAudit.class );

    @Override
    public void init(TaskInfo taskInfo) {
        Pay pay = GetBeanFromBrowerUtil.getBeanFromBrower( "退票信息为", "businessData", Pay.class, itcMvcService );
        if ( null == pay ){
            String processInstId = taskInfo.getProcessInstanceId();
            String payId = payService.queryBusinessIdByUndoFlowId( processInstId );
            pay = payService.queryPayById( payId ); 
        }
        if ( pay != null ) {
            pay.setStatus( "undoing" );
            pay.setUndoStatus( "accountantAudit" );
            InitUserAndSiteIdUtil.initUpdate( pay, itcMvcService );
            payService.updateStatus( pay );
        }
        LOGGER.info( "初始化退票会计审批节点" + taskInfo.getTaskDefKey() + "对结算信息信息的更新" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        LOGGER.info( "完成退票会计审批节点" + taskInfo.getTaskDefKey() + "对结算信息信息的更新" );
    }
}
