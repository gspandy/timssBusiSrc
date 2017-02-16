package com.timss.inventory.flow.zjw.accept.v001;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.timss.inventory.bean.InvMatAccept;
import com.timss.inventory.bean.InvMatAcceptDetail;
import com.timss.inventory.service.core.InvMatAcceptDetailService;
import com.timss.inventory.service.core.InvMatAcceptService;
import com.timss.inventory.utils.AcceptStatusEnum;
import com.timss.inventory.utils.GetAcceptDataInWorkflowUtil;
import com.yudean.homepage.service.HomepageService;
import com.yudean.mvc.service.ItcMvcService;
import com.yudean.workflow.service.WorkflowService;
import com.yudean.workflow.task.TaskHandlerBase;
import com.yudean.workflow.task.TaskInfo;

/**
 * @title: {title}
 * @description:提出申请
 * @company: gdyd
 * @className: AidDistribution.java
 * @author: 890166
 * @createDate: 2014-7-29
 * @updateUser: 890166
 * @version: 1.0
 */
public class Cgytcyssq extends TaskHandlerBase {

    @Autowired
    private HomepageService homepageService;
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
    	//更新状态
        String inacId=(String) workflowService.getVariable(taskInfo.getProcessInstanceId(), "inacId");
        InvMatAccept invMatAccept=new InvMatAccept();
        invMatAccept.setInacId(inacId);
        invMatAccept.setStatus(AcceptStatusEnum.WAITING.toString());
        invMatAcceptService.updateInvMatAccept(invMatAccept);
    }
    @Override
    @Transactional
    public void onComplete(TaskInfo taskInfo) {
    	Object isDeparted= workflowService.getVariable(taskInfo.getProcessInstanceId(), "isDeparted");
    	
    	InvMatAccept invMatAccept=getAcceptDataInWorkflowUtil.getAccept();
    	List<InvMatAcceptDetail> details=getAcceptDataInWorkflowUtil.getDetails();
    	//如果是进行拆分的，直接流转
    	if(isDeparted!=null && "Y".equals(isDeparted)){
    		workflowService.setVariable(taskInfo.getProcessInstanceId(), "isDeparted","N");
    	}else{
    		//更新
        	invMatAcceptService.updateInvMatAccept(invMatAccept);
    		invMatAcceptDetailService.updateInvMatAcceptDetail(invMatAccept, details);
    		//进行流程拆分
    		invMatAcceptService.departInvMat(invMatAccept, details);
    	}
    	
    	//更新状态
        String inacId=(String) workflowService.getVariable(taskInfo.getProcessInstanceId(), "inacId");
        invMatAccept=new InvMatAccept();
        invMatAccept.setInacId(inacId);
        invMatAccept.setStatus(AcceptStatusEnum.WAITING.toString());
        invMatAcceptService.updateInvMatAccept(invMatAccept);
        
        
    }
}
