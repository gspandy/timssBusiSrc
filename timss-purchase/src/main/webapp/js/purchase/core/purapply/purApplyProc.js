function initPageProcess(){
	$("#btn-business").hide();
	$("#btn-delete").hide();
	$("#btn-nobusiness").hide();
	$("#btn-stop").hide();
	$("#btn-nullifyStop").hide();
	FW.hideBtnGroup("btn-group-process");
	uploadform();
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=ids.uploadField;
	
	//判断用户是新建操作还是新建操作
	if ('edit' == operation[0]) {
		$("#btn-edit").show();
		$("#btn-save").hide();
		$("#btn-submit").hide();
		$("#pageTitle").html("采购申请详情");
		if(processStatus == "first_save"&&"editable"==isEdit){
			$("#pageTitle").html("编辑采购申请");
			$("#apply_item").datagrid("showColumn", "del");
		}else{
			$("#apply_item").datagrid("hideColumn", "del");
		}
	} else {
		$("#pageTitle").html("新建采购申请");
		$("#apply_item").datagrid("showColumn", "del");
	}
	
	//当前页面是否可以被编辑
	if ("editable" != isEdit && "" != processInstId) {
		$("#btn-edit").hide();
		$("#btn-save").hide();
		$("#btn-delete").hide();
		$("#btn-submit").hide();
		$("#btn-add").hide();
		endEditAll();
	} else {
		if ('edit' == operation[0]) {
			$("#btn-edit").show();
			editStatus = true;
		} else {
			$("#btn-edit").hide();
		}
		if("editable" == isEdit && "first"!=processStatus &&"first_save"!=processStatus){
			$("#btn-add").hide();
		}
	}

	//首环节
	if ("" == sheetId) {
		$("#btn_print").hide();
		$("#apply_item").datagrid("hideColumn","repliednum");
	}else{//首环节之外环节
		//采购申请转采购单环节
		if (processStatus == "last") {
			initBusiList(operation[1]);
			
			$("#btn-submit").html("生成采购合同");
			
			if ("editable" != isEdit) {
				$("#apply_item").datagrid("hideColumn", "ck");
			} else {
				$("#apply_item").datagrid("showColumn", "ck");
				$("#btn-business").show();
			}
			
			$("#uploadform").iForm('endEdit');
			activeStatus = 'approve';
		}else if("over"==processStatus&&("true"==hasItemApplying&&"true"==isLastStepAssignee)){
			//有明细状态为1 且处于over状态，且是最后一步处理人打开，开放生成采购单和发送商务网功能
			$("#btn-submit").html("生成采购合同");
			$("#apply_item").datagrid("showColumn", "ck");
			FW.enableBtn("btn-submit");
			FW.enableBtn("btn-business");
			FW.showBtnGroup("btn-group");
		}else if(processStatus != "first" && processStatus != "first_save"){
			activeStatus = 'approve';
			$("#btn-submit").html("审批");
			$("#uploadform").iForm('endEdit');
		}
		$("#apply_item").datagrid("showColumn", "repliednum");
	}
	//页面不可编辑的时候一定是关闭附件的编辑状态
	if ("editable" != isEdit) {
		$("#uploadform").iForm('endEdit');
	}
	if("" != uploadIds || "" == sheetId || null == sheetId ){
		$("#uploadfileTitle").iFold("init");
	}else if ((processStatus == "first" || processStatus == "first_save")&&("editable" == isEdit)){
			$("#uploadfileTitle").iFold("init");
	}else{
		$("#uploadfileTitle").iFold("hide");
	}
}


//提交后回调执行
function submitCallBack(){
	//如果是最后环节则提交并自动生成采购单
	if(processStatus == "last" && type!="over"){
		autoGenerateOrder();
	}else{
		pageClose();
	}
}

//取消的回调执行
function cancelCallBack(){
	$("#btn-submit").button('reset');
}

//隐藏提交归档方法
function autoCommitProcess(type) {
	var data = {};
	data['taskId'] = taskId;
	data['message'] = "审批归档。";
	var url = 'workflow/process_inst/autoComplete.do';
	$.post(url, data, function(data) {
		if (data.result == 'ok') {
			if("busi" != type){
				submitCallBack();
			}else{
				$.ajax({
					url : basePath + "purchase/purapply/buiss2UpdatePAStatus.do",
					dataType : "json",
					type : "POST",
					async: false,
					data : { "sheetId" : sheetId },
					success : function() {}
				});
				pageClose();
			}
		}
	});
}

//停止流程
function stopProcess() {
	var workFlow = new WorkFlow();
	var flowData = workFlow.getFormData();
	var data = {};
	data['taskId'] = taskId;
	data['message'] = flowData.reason;
	data['sheetId'] = sheetId;
	var url = basePath + "purchase/purapply/stopProcess.do";
	$.post(url, data, function(data) {
		if (data.result == 'success') {
			_parent().$("#itcDlg").dialog("close");
			FW.success("流程终止成功");
			homepageService.refresh();
			pageClose();
		} else {
			FW.error("流程终止失败");
		}
	});
}