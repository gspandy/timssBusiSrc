
/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	closePtw();
};
function rollback(){
	closePtw();    
};
function stop(){
   closePtw();
}

function processPtw(){
	var workFlow = new WorkFlow();
	var bizData = {ptwId:ptwId};
	workFlow.showAudit(taskId,JSON.stringify(bizData),agree,rollback,stop,"");
}

function showAuditInfo(processId){
	var workFlow = new WorkFlow();
	workFlow.showAuditInfo(processId,JSON.stringify({}));
}