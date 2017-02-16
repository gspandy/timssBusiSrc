//显示流程图
function showFlowDialog( defKey ){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}

//流程实例  流程实例id,业务表单数据
function showAuditInfo(processInstId, formData ){
	var workFlow = new WorkFlow();
	workFlow.showAuditInfo(processInstId,formData);
}