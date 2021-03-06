function submit(_this){
	if(!$("#form1").valid()){
		return ;
	}
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	$.post(basePath+'pms/bid/tmpUpdateBidResult.do',{"bidResult":FW.stringify(data)},function(result){
		
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
}

function tmpSave(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	$.post(basePath+'pms/bid/tmpUpdateBidResult.do',{"bidResult":FW.stringify(data)},function(result){
		
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
	});
}

function del(){
	var data={id:id};
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/bid/deleteBidResult.do',data,function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
}

function shenpi(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	shenpiTemplate(data,stopWorkflow);
}

/**
 * 流程终止函数
 */
function stopWorkflow(reason){
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#bidAttachForm').iForm('getVal'));
	var processInstId=getWorkflowProcessInstId();
	$.post(basePath+"pms/bid/stopWorkflow.do",{bidResult:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
		showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
	});
}
function initBidConDataGrid(data,readOnly){
	if(data && data.bidConVos && data.bidConVos.length){
		$("#supplierListWrapper").iFold();
		initSupplierDataGrid(data.bidConVos);
		if(data.bidResult && data.bidResult.length){
			showDataGridColumn();
		}
	}
	if(readOnly){
		$("#b-add-bid-con").hide();
	}else{
		$("#supplierListWrapper").iFold();
	}
}
function initOther(opt){
	var data=opt.data;
	var form=opt.form;
	form.iForm('endEdit','projectName');
	initRemoteSupplierData(form);
	initBidAttachForm(data.data.attachMap,$('#bidAttachForm'),$('#bidAttachFormWrapper'),opt);
	preparePrint();


}

function preparePrint(){
	var proc_inst_id=getWorkflowProcessInstId();
	
	var url=fileExportPath+"preview?__report=report/TIMSS_PMS_BID_001.rptdesign" +
			"&__format=pdf&siteid=ITC&sheet_id="+id+"&proc_inst_id="+proc_inst_id;
	url=url+"&url="+url+"&author="+loginUserId;
	pmsPrintHelp("#pms-bidresult-print",url,"招标信息");
}

function addNewContract(){
	var projectId=pmsPager.opt.data.data.projectId;
	openTab('pms/contract/addContractJsp.do?projectId='+projectId+"&bidId="+id,'合同','pmsNewContractTab',canOpenMutiTab);
}