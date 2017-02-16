package com.timss.inventory.flow.swf.accept.v001;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.core.InvMatAcceptDetailService;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.utils.AcceptStatusEnum;
import com.timss.inventory.utils.GetAcceptDataInWorkflowUtil;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description: 入库
 * @company: gdyd
 * @className: AdminManager.java
 * @author: 890166
 * @createDate: 2014-9-17
 * @updateUser: 890166
 * @version: 1.0
 */
public class Wzrk extends TaskHandlerBase {

	@Autowired
    InvMatAcceptService invMatAcceptService;
    @Autowired
    WorkflowService workflowService;
    @Autowired
    ItcMvcService itcMvcService;
    @Autowired
    GetAcceptDataInWorkflowUtil getAcceptDataInWorkflowUtil;
    @Autowired
    InvMatAcceptDetailService invMatAcceptDetailService;
    @Override
    public void init(TaskInfo taskInfo) {
    	
    }
    @Override
    public void onComplete(TaskInfo taskInfo) {
    	//更新
    	InvMatAccept invMatAccept=getAcceptDataInWorkflowUtil.getAccept();
    	List<InvMatAcceptDetail> details=getAcceptDataInWorkflowUtil.getDetails();
    	invMatAcceptService.updateInvMatAccept(invMatAccept);
		invMatAcceptDetailService.updateInvMatAcceptDetail(invMatAccept, details);
    	//更新状态
        String inacId=(String) workflowService.getVariable(taskInfo.getProcessInstanceId(), "inacId");
        invMatAccept=new InvMatAccept();
        invMatAccept.setInacId(inacId);
        invMatAccept.setStatus(AcceptStatusEnum.INVIN.toString());
        invMatAcceptService.updateInvMatAccept(invMatAccept);
    }
}
