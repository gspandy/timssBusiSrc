function submit(_this){
	if(!valid()){
		return ;
	}
	buttonLoading(_this);
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/insertContract.do",{toComplete:toComplete,taskId:taskId,contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		data.id=result && result.data && result.data.id;
		data.payplans=payplanData;
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage,
				{successFunction:openNewUserDialogFirstInThisTab,tabOpen:true,data:data,resetId:_this});
	});
}

function tmpSave(){
	if(!valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/tmpInsertContract.do",{toComplete:toComplete,taskId:taskId,contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:closeThisTab});
	});
}

/**
 * 关闭当前tab页
 */
function closeThisTab(){
	var curTabId = FW.getCurrentTabId();
	if(null!=closeTabId&&''!=closeTabId&&"true"==toComplete){
		FW.deleteTabById(closeTabId);
	}
	FW.deleteTabById(curTabId);
}
//提交时，触发的对话框弹出
function openNewUserDialogFirstInThisTab(result,options,id,opt){
	var taskId=getWorkflowTaskId(id) || (result && result.data && result.data.taskId);
	var data=(options && options.data )|| {};
	var workFlow = new WorkFlow();
	//关闭页面提示信息
	forbidTipAfterCloseTab();
	workFlow.submitApply(taskId,FW.stringify(data),closeThisTab,null,0,closeThisTab);
}

function initFormWithProjectData(projectId){
	
	function initOtherWithData(opt){
		var form=opt.form;
		var value=opt.otherData;
		form.iForm('endEdit',['projectName']);
		form.iForm('setVal',value);
		addContractCodeEvent();
		initRemoteFirstPartyData(form);
		initRemoteSecondPartyData(form);
		initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
		initPayplanWrapper();
		initPayplansDataGrid();
		initContractCode();
		form.iForm("setVal",{"qaTime":90});
		hideColumn();
	}
	$.post(basePath+'pms/project/queryProjectById.do',{id:projectId},function(result){
		result=result.data;

		var value={
			projectId:result['id'],
			projectName:result['projectName'],
			name:result['projectName'],
			type:result["property"],
			command:result["command"]
		};
		var opt={
			form:$('#form1'),
			otherData:value,
			formFields:contractFormFields,
			initOther:initOtherWithData
		};
		pmsPager.init(opt);

		
	});
}


//初始化表单时没有包含项目和招标信息
function initFormWithEmptyData(){
	var opt={
		form:$("#form1"),
		formFields:contractFormFields,
		initOther:initOtherWithEmptyData
	};
	pmsPager.init(opt);
}

function initOtherWithEmptyData(opt){
	var form=opt.form;
	initRemoteData(form);
	initRemoteFirstPartyData(form);
	initRemoteSecondPartyData(form);
	addContractCodeEvent();
	initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
	initPayplanWrapper();
	initPayplansDataGrid();
	initContractCode();
	hideColumn();
	form.iForm("setVal",{"qaTime":90});
}
//初始化合同编码
function initContractCode(){
	$.ajax({
		url : basePath + "pms/contract/generateNewContractCode.do",
		type:"POST",
		success:function(data){
			$('#form1').iForm('setVal',{"contractCode":data.newCode});
		},
		error:function(){
			FW.error("无法生成 合同编号");
		}
	});	
}

//初始化项目信息，使其能够获取远程项目数据
function initRemoteData(form){
	var $projectInput = $('#f_projectName');
	var projectInit = {
		datasource : basePath + "pms/project/queryProjectByKeyWord.do",
		clickEvent : function(id, name) {
			$projectInput.val(name);
			$.post(basePath+'pms/project/queryProjectById.do',{id:id},function(result){
				result=result.data;
				var value={
					projectId:result['id'],
					projectName:result['projectName'],
					name:result['projectName'],
					//默认选定第一个类型
					type:null!=result["property"]?result["property"]:"cost",
					command:result["command"]
				};
				form.iForm("setVal", value);
			});
		}
	};
	
	$projectInput.iHint('init', projectInit);
}

function createContractCode(){
	$.post(basePath+'pms/sequence/getContractSequence.do',{},function(result){
		var data=result.data;
		pmsPager.opt.form.iForm('setVal',{contractCode:data});
	});
}

function showWorkflow(){
	
	var workFlow = new WorkFlow();
    workFlow.showDiagram(defKey);
}