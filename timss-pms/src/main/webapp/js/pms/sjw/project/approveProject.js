
/**
 * 审批流程信息
 */
function shenpi(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	var businessLeader = $('#form1').iForm('getVal','businessLeader');
	$.post(basePath+'pms/project/setWFVariable.do',{businessLeader:businessLeader,taskId:taskId,processInstId:processInstId,project:FW.stringify(data)},
		function(result){
		if(result && result.flag=='success'){
			var workFlow = new WorkFlow();
		    workFlow.showAudit(taskId,FW.stringify(data));
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

function showWorkflow(){
	var workFlow = new WorkFlow();
    workFlow.showAuditInfo(processInstId);
}

function initOther(opt){
	var data=opt.data;
	var form=opt.form;
	var elementMap=data.data.elementMap;
	if(elementMap ){
		modifiable=elementMap.modifiable;
	}
	//initRemoteField(form);
	form.iForm('setVal',data.data);
	initFormByModifiable(modifiable,form);
	form.iForm('endEdit','planName');
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly);
}