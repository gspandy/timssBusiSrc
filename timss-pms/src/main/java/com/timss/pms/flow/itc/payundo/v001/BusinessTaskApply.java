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
 * 退票申请 商务专责申请退票节点类
 * 
 * @ClassName: BusinessTaskApply
 * @company: gdyd
 * @Description:商务专责申请退票节点类
 * @author: 谷传伟
 * @date: 2015-08-28 
 */
public class BusinessTaskApply extends TaskHandlerBase {
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    PayService payService;
    private static final Logger LOGGER = Logger.getLogger( BusinessTaskApply.class );

    @Override
    public void init(TaskInfo taskInfo) {
        Pay pay = GetBeanFromBrowerUtil.getBeanFromBrower( "退票信息为", "businessData", Pay.class, itcMvcService );
        if ( pay != null ) {
            pay.setStatus( "undoing" );
            pay.setUndoStatus( "businessTaskApply" );
            InitUserAndSiteIdUtil.initUpdate( pay, itcMvcService );
            payService.updateStatus( pay );
        }
        LOGGER.info( "初始化退票商务专责申请节点" + taskInfo.getTaskDefKey() + "对结算信息信息的更新" );
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
        Pay pay = GetBeanFromBrowerUtil.getBeanFromBrower( "退票信息为", "businessData", Pay.class, itcMvcService );
        //更新退票说明
        payService.updateUndoInfo( pay );
        LOGGER.info( "完成退票商务专责申请节点" + taskInfo.getTaskDefKey() + "对结算信息信息的更新" );
    }
    @Override
    public void beforeRollback(TaskInfo sourceTaskInfo, String destTaskKey) {
        
    }

}
