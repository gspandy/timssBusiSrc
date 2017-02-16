package com.timss.pms.flow.itc.receipt.v004;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.timss.pms.bean.Pay;
import com.timss.pms.service.PayService;
import com.timss.pms.util.GetBeanFromBrowerUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;


/**
 * @ClassName:     Kjscpz
 * @company: gdyd
 * @Description:项目立项 会计审查凭证节点类
 * @author: gucw    
 * @date:   2015-8-25 下午15:33:00
 */
public class Kjscpz extends TaskHandlerBase{
	@Autowired
	ItcMvcService itcMvcService;
	@Autowired
	WorkflowService workflowService;
	@Autowired
	PayService payService;
	private static final Logger LOGGER=Logger.getLogger(Kjscpz.class);
	
	public void onComplete(TaskInfo taskInfo) {
		Map<String,Object> map=GetBeanFromBrowerUtil.getMapFromBrower("插入的收款信息为", "businessData", itcMvcService);
        if(map!=null){
    		Pay pay=new Pay();
    		pay=GetBeanFromBrowerUtil.getBeanFromBrower("插入的付款信息为", "businessData", Pay.class, itcMvcService);
    		payService.updatePayApproved(pay);
        }else{
        	String workflowId=taskInfo.getProcessInstanceId();
        	int businessId=(Integer) workflowService.getVariable(workflowId,"businessId");
		payService.updatePayApproved(businessId);
        }

		LOGGER.info("完成项目收款工作流节点"+taskInfo.getTaskDefKey()+"对收款信息的更新");
	}
}
