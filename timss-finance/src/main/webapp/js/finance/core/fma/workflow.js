/**
 * 打开流程图
 */
function viewWorkFlow(id,defKey){
	var workflow=new WorkFlow();
	var processInstId=getWorkflowProcessInstId(id);
	if(processInstId){
		var businessData={};
		var fields = [{
	        title : "创建时间", 
            id : "createtime",
            type : "label"
	    }];
	    var data={'createtime':FW.long2time(pmsPager.createTime)};
	    businessData['fields'] = fields;
	    businessData['data'] = data;
	    //是否是审批状态，流程信息对话下面显示审批按钮，否则不显示
	    
	    if(isApproving(pmsPager.opt.flowStatus)){
	    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData),1,shenpi,null);
	    }else{
	    	workflow.showAuditInfo(processInstId,JSON.stringify(businessData));
	    }
		
	}else{
		//显示流程图
		workflow.showDiagram(defKey);
	}
	
}

//发起审批事件
function shenpiTemplate(data,stopWorkflow,id){
	var taskId=getWorkflowTaskId(id);
	var processInstId=getWorkflowProcessInstId(id);
	//先更新流程变量
	$.post(basePath+'pms/wf/setWFVariable.do',{taskId:taskId,processInstId:processInstId,data:FW.stringify(data)},
		function(result){
		if(result && result.flag=='success'){
			openNewUserDialog(data,stopWorkflow,id);
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

/**
 * 打开选择下一位处理人对话框
 * data 提交的表单信息
 */
function openNewUserDialog(data,stopWorkflow,id){
	var workFlow = new WorkFlow();
	var taskId=getWorkflowTaskId(id);
	forbidTipAfterCloseTab();
	//暂时屏蔽流程终止功能
    //workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,stopWorkflow);
    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,0);
}

//提交时，触发的对话框弹出
function openNewUserDialogFirstInTab(result,options,id){
	var taskId=getWorkflowTaskId(id) || (result && result.data && result.data.taskId);
	var data=(options && options.data )|| {};
	var workFlow = new WorkFlow();
	forbidTipAfterCloseTab();
	workFlow.submitApply(taskId,FW.stringify(data),closeTab,null,0,closeTab);
	
}



//获取流程id
function getWorkflowProcessInstId(id){
	if(!id){
		if(pmsPager && pmsPager.opt && pmsPager.opt.workflow && pmsPager.opt.workflow.processInstId){
			return pmsPager.opt.workflow.processInstId;
		}
	}else{
		//获取某个流程下的流程实例id
		var workflowData=getWorkflowDataFromPri(pmsPager.opt,id);
		if(workflowData && workflowData.processInstId){
			return workflowData.processInstId;
		}
	}
	
	return null;
}

//获取节点id
function getWorkflowTaskId(id){
	if(!id){
		//获取只有一个关联的流程时的流程id
		if(pmsPager && pmsPager.opt && pmsPager.opt.workflow && pmsPager.opt.workflow.taskId){
			return pmsPager.opt.workflow.taskId;
		}
	}else{
		//获取某个流程下的状态
		var workflowData=getWorkflowDataFromPri(pmsPager.opt,id);
		if(workflowData && workflowData.taskId){
			return workflowData.taskId;
		}
	}
	
	return null;
}

function setWorkflowTaskId(id){
}
//是否可以审批
function isApproving(id){
	if(!id){
		if(pmsPager && pmsPager.opt && pmsPager.opt.workflow && pmsPager.opt.workflow.approve){
			return pmsPager.opt.workflow.approve;
		}
	}else{
		var workflowData=getWorkflowDataFromPri(pmsPager.opt,id);
		return isCandidate(workflowData);
	}
	
	return null;
}

function getWorkflowData(data,id){
	if(id){
		return data &&  data.pri && data.pri.workflow && data.pri.workflow[id];
	}else{
		return data && data.pri && data.pri.workflow;
	}
}
function getWorkflowDataFromPri(pri,id){
	return pri && pri.workflow && pri.workflow[id];
}

function isCandidate(workflowData){
	return workflowData && workflowData.isCandidate;
}
//作废流程
function voidFlow(options){
	var destUrl=options.destUrl;
	var data={};
	data["businessId"]=options.businessId;
	data["taskId"]=options.taskId;
	data["processInstId"]=options.processInstId;
	var tipMessage=options.tipMessage || "确认作废流程，作废后不可恢复";
	FW.confirm(tipMessage,function(){
		$.ajax({
        	url:destUrl,
        	type:"GET",
        	data:data,
        	success:function(){
        		FW.success("流程作废成功");
        		forbidTipAfterCloseTab();
        		closeTab();
        	},
        	error:function(){
        		FW.error("流程作废失败");
        	}
        });
	});
	
}
