function initContractForm(contractId){
	var $form=$('#form1');
	$.post(basePath+'pms/contract/queryContractById.do',{id:contractId},function(result){
		if(result && result.flag=='success'){
			var opt={
				form:$form,
				formFields:contractFormFields,
				data:result
			};
			//新的权限控制
			Priv.map("showWorkFlowPriv()","pms-workflow-show");
			pmsPager.init(opt);
			FW.showProjectInfo("projectName");
			FW.showBidInfo("bidName");
		}else{
			FW.error(result.msg || "服务器出错，请重新打开页面");
		}
	})
}
function hideColumn(){
	hideDataGridColumns(dataGrid,['garbage-colunms']);
}

function initOther(opt){
	var data=opt.data;
	var form=opt.form;
	form.iForm('endEdit',['projectName']);
	partyReadOnly();
	var type = data.data['type'];
	if(type=='income'){
		form.iForm('endEdit',['secondParty']);
	}else{
		form.iForm('endEdit',['firstParty']);
	}
	addContractCodeEvent();
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly);
	showPayList(opt,data);
	//initProjectDetail(data.data.projectDtlVo);
	initChangePayListFlow();
	pmsPager.ctype=data.data.type;
}

//结算计划变更页面需要显示的信息
function initChangePayListFlow(){
	if(changeContractStatus==1){
		$.post(basePath+'pms/contract/getPayplanTmpListByFlowId.do',{flowId:flowId},function(result){
			if(result && result.flag=="success"){
				initPayplanTmpList(result.data);
				$('#payplanListTmpWrapper').iFold();
				hideDataGridColumns(initPayplanTmpList.dataGrid,['payStatus','checkStatus','garbage-colunms']);
				$('#payplanListWrapper').prev().find('.itcui_frm_grp_title_txt').html('变更前结算计划');
			}
		});
	}else{
		$('#payplanTmpListWrp').hide();
	}
}
//初始化变更后的结算计划信息
function initPayplanTmpList(data){
	if(!initPayplanTmpList.dataGrid){
		initPayplanTmpList.dataGrid=$('#payplanTmpList');
		initPayplanTmpList.dataGrid.datagrid({
			fitColumns:true,
			singleSelect:true,
			scrollbarSize:0,
			
			onDblClickRow : function(rowIndex, rowData) {
				if(pmsPager.opt.readOnly==true){
					return;
				}
				//dataGrid.datagrid('beginEdit',rowIndex);
			},
			data:data,
			onClickCell:delelteGarbageColumnWhenClick,
			columns : payplanListColumns
		});
	}
}
function initProjectDetail(data){
	if($('#f_projectName').val){
		$('#f_projectName').val(data.projectName);
	}else{
		$('#form1').iForm('setVal',{projectName:data.projectName})
		
	}
	
}
//使datagrid某一行变成可编辑状态，要先做数据校验
function changeGridColumnToEdit(index,rowData,dataGrid){
	var checkStatus=rowData.checkStatus;
	var payStatus=rowData.payStatus;
	if(checkStatus=='approving' || checkStatus=='approved' ||
			payStatus=='approving' || payStatus=='approved'){
		
	}else{
		dataGrid.datagrid('beginEdit',index);
	}
}
//
function showEditablePayList(data){
	initPayplanWrapper();
	initPayplansDataGrid(data && data.data && data.data.payplans);
	
	dataGrid.datagrid('showColumn',"garbage-colunms");
	dataGrid.datagrid('showColumn',"needchecked");
	$('#b-add-payplan').show();
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		changeGridColumnToEdit(i,rows[i],dataGrid);
		
	}
}

function showReadOnlyPayList(data){
	if(data && data.data && data.data.payplans){
		$("#payplanListWrp").show();
		initPayplanWrapper();
		initPayplansDataGrid(data.data.payplans);
		hideColumn();
		$('#b-add-payplan').hide();
	}else{
		//隐藏结算计划列表，防止出现异常的高度间距
		$("#payplanListWrp").hide();
	}
}
//根据状态决定结算结算的状态显示
function showPayList(opt,data){
	if(opt.readOnly==true){
		if(pmsPager.hasPriInOpt("edit-payplan")){
			showEditablePayList(data);
		}else{
			showReadOnlyPayList(data);
		}
		
	}else{
		
		showEditablePayList(data);
	}
}
//变更结算计划
function changePay(){
	if(!changePay.init){
		showEditablePayList(pmsPager.opt.data);
		$('#pms-b-contract-change-payplan').html('确认变更');
		//流程图按钮显示
		$("#b-contract-change-workflow").show();
		changePay.init=true;
		return ;
	}
	if(!valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+'pms/contract/changePayList.do',{"contract":FW.stringify(data),"payplans":FW.stringify(payplanData)},function(result){
		data.id=result && result.data && result.data.id;
		data.payplans=payplanData;
		//后台不再处理暂存的结算计划信息标志位
		data.notPayplan="true";
		showBasicMessageFromServer(result,"提交成功","提交失败",
				closeTab);
	});
}

function showContractChangeWorkflow(){
	var defKey="pms_itc_contract";
	var workFlow = new WorkFlow();
    workFlow.showDiagram(defKey);
}
function openNewCheckoutTab(){
	//var contractId=$('#f_id').val();
	openTab('pms/checkout/insertCheckoutJsp.do?contractId='+contractId,'验收','pmsNewCheckoutTab',openMutiTabAndBackReflesh);
}

function openViewPmsCheckOutTab(){
	var select=dataGrid.datagrid('getSelected');
	if(select==null){
		FW.error("请选择一条要查看的结算计划");
		return ;
	}
	if(select.needChecked=="false" || select.checkStatus=="reset" || !select.checkStatus){
		FW.error("该结算计划没有验收信息");
	}else{
		openTab('pms/checkout/editCheckoutJsp.do?contractId='+contractId+"&payplanId="+select.id,'验收','pmsViewCheckoutTab'+select.id,openTabBackReflesh);
	}
}
function openNewPpayTab(){
	//var contractId=$('#f_id').val();
	openTab('pms/pay/insertPayJsp.do?contractId='+contractId+"&ctype="+pmsPager.ctype,'结算','pmsNewPayTab',openMutiTabAndBackReflesh);
}

function openNewInvoiceTab(){
	//var contractId=$('#f_id').val();
	openTab('pms/invoice/insertInvoiceJsp.do?contractId='+contractId+"&ctype="+pmsPager.ctype,'发票','pmsNewInvoiceTab',canOpenMutiTab);
}
function openViewPmsPayTab(){
	var select=dataGrid.datagrid('getSelected');
	if(select==null){
		FW.error("请选择一条要查看的结算计划");
		return ;
	}
	if(select.payStatus=="approving" || select.payStatus=="approved" ){
		openTab('pms/pay/editPayJsp.do?contractId='+contractId+"&payplanId="+select.id+"&ctype="+pmsPager.ctype,'结算','pmsViewPayTab'+select.id,openTabBackReflesh);
		
	}else{
		FW.error("该结算计划没有结算信息");
	}
}
function openViewProject(){
	var projectId=$("#form1").iForm('getVal')["projectId"];
	openTab('pms/project/editProjectJsp.do?id='+projectId,'立项','pmsViewProjectTab'+projectId);
}
function shenpi(){
//	if(!valid()){
//		return ;
//	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	data.payplans=payplanData;
	shenpiTemplate(data,stopWorkflow);
}
/**
 * 流程终止函数
 */
function stopWorkflow(reason){
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var processInstId=getWorkflowProcessInstId();
	$.post(basePath+"pms/contract/stopWorkflow.do",{contract:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
		
		showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
	});
}
function submit(_this){
	if(!valid()){
		return ;
	}
	buttonLoading(_this);
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/updateContract.do",{contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		data.payplans=payplanData;
		showBasicMessageFromServer(result,saveSuccessMessage,saveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
}

function tmpSave(){
	if(!valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var payplanData=getPayplanListData(dataGrid);
	$.post(basePath+"pms/contract/tmpUpdateContract.do",{contract:FW.stringify(data),payplans:FW.stringify(payplanData)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,closeTab);
	});
}

function del(){
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/contract/deleteContract.do',{contractId:contractId},function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
	
}

function delWorkflow(){
	FW.confirm("确定删除结算计划变更流程吗？",function(){
		$.post(basePath+'pms/contract/delWorkflow.do',{contractId:contractId},function(result){
			showBasicMessageFromServer(result,"删除成功","删除失败");
		});
	});
}

//重新加载datagrid信息
function pmsReload(){
	$.post(basePath+'pms/contract/queryContractById.do',{id:contractId},function(data){
		
		if(data && data.flag=='success'){
			var opt=data;
				
			showPayList(pmsPager.opt,data);
			if(data && data.data && data.data.payplans){
				dataGrid.datagrid("loadData",data.data.payplans);
			}
			
		}
	});
}

//合同审批流程的审批操作
function approveContract(){
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var taskId=null;
	var processInstId=null;
	var elementKey=null;
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflowSec ){
		taskId = pmsPager.opt.data.pri.workflowSec.taskId;
		processInstId=pmsPager.opt.data.pri.workflowSec.processInstId;
		elementKey=pmsPager.opt.data.pri.workflowSec.elements.__elementKey__;
	}
	var parameters = {taskId:taskId,processInstId:processInstId,data:FW.stringify(data)};
	if(!$('#payplanListWrapper').valid()){
		return false;
	}
	var payplanData=getPayplanListData(dataGrid);
	data.payplans=FW.stringify(payplanData);
	getDatagridData(dataGrid,true);
	if(payplanData.length==0){
		FW.error("请填写结算计划");
		return false;
	}
	workFlowOperation(taskId,data,parameters,0);
	//保存附件的操作在handler中处理
}

//设置流程变量，弹出工作流对话框
function workFlowOperation(taskId,data,parameters,multiSelect){
	$.post(basePath+'pms/wf/setWFVariable.do',parameters,
			function(result){
			if(result && result.flag=='success'){
				var workFlow = new WorkFlow();
				forbidTipAfterCloseTab();
				//暂时屏蔽流程终止功能
			    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,multiSelect);
			}else{
				FW.error(result.msg || "出错了，请重试");
			}
		});
}

//提交草稿
function submitContract(_this){
	if(!$("#form1").valid()){
		return false;
	}
	buttonLoading(_this);
	var data=$('#form1').iForm('getVal');
	var processInstId = $('#form1').iForm('getVal',"processInstId");
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+"pms/contract/saveOrUpdateContractWithWorkFlow.do",{processInstId:processInstId,contract:FW.stringify(data)},function(result){
		var payplanData=getPayplanListData(dataGrid);
		data.payplans=FW.stringify(payplanData);
		var contract = result.data["contract"];
		$("#form1").iForm('setVal',contract);
		$("#form1").iForm('setVal',{processInstId:result.data["processInstId"]});
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{successFunction:turnToSecStep(data,result.data["taskId"],result.data["processInstId"]),tabOpen:true,resetId:_this});
	});
}
//提交草稿
function turnToSecStep(data,taskId,processInstId){
	var workFlow = new WorkFlow();
	forbidTipAfterCloseTab();
    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,0,buttonReset("#b-save"));
}
//暂存草稿
function tmpSaveContract(){
	if(!$("#form1").valid()){
		return false;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+"pms/contract/tmpSaveContractWithWorkFlow.do",{contract:FW.stringify(data)},function(result){
		var contract = result.data["contract"];
		$("#form1").iForm('setVal',contract);
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{tabOpen:true});
	});
}
//删除草稿
function delContract(){
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/contract/deleteContract.do',{contractId:contractId},function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
}
//获取合同审批信息详情
function showContractWorkflow(){
	var workFlow = new WorkFlow();
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflowSec ){
		var processInstId=pmsPager.opt.data.pri.workflowSec.processInstId;
		var data=$("#form1").iForm('getVal');
		$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
		//能否审批
		if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri && pmsPager.opt.data.pri["pms-contract-approve"]){
			workFlow.showAuditInfo(processInstId,null,1,approveContract,null);
		}else{
			workFlow.showAuditInfo(processInstId);
		}
		
	}
}
/**
 * 查看流程信息
 */
function showWorkFlowPriv(){
	if(pmsPager.opt.data.data["statusApp"]=="draft"){
		return false;
	}else{
		return true;
	}
}

//作废合同
function nullifyContractWithWorkFlow(){
	var taskId=null;
	var processInstId=null;
	var id = null;
	if(pmsPager && pmsPager.opt && pmsPager.opt.data && pmsPager.opt.data.pri &&
			pmsPager.opt.data.pri.workflowSec ){
		taskId = pmsPager.opt.data.pri.workflowSec.taskId;
		processInstId=pmsPager.opt.data.pri.workflowSec.processInstId;
		id=pmsPager.opt.data.data.id;
	}
	if(!taskId||!processInstId||!id){
		return false;
	}
	var options={
		destUrl:basePath+"pms/contract/voidFlow.do",
		businessId:id,
		processInstId:processInstId,
		taskId:taskId,
		tipMessage:"确认作废|确认作废该流程？"
	};
	voidFlow(options);
}