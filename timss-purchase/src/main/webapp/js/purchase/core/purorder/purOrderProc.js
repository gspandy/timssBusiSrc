function initPageProcess(){
	if(processStatus == "first" || processStatus == "first_save"){
		queryMode = "1";
	}else{
		queryMode = "0";
		if(processStatus == "last"){
			$("#btn-submit").text("通知入库");
		}else if(processStatus == "process"){
			$("#btn-submit").html("审批");
		}
	}
	//新建时，不要显示出打印按钮
	if(""==sheetId){
		$("#btn-print").hide();
	}
	//附件组件控制--start
	//1.初始化附件表单
	uploadform();
	//2.解析附件id集合
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=ids.uploadField;
	//显示或隐藏附件组件
	if("" != uploadIds || "" == sheetId || null == sheetId || "null" == sheetId || processStatus == "first" || processStatus == "first_save"){
		$("#uploadfileTitle").iFold("init");
	}else{
		$("#uploadfileTitle").iFold("hide");
	}
	if("" == uploadIds && ""==isEdit ){
		$("#uploadfileTitle").iFold("hide");
	}
	//3.但只有在首环节的时候才可以上传附件
	if(processStatus != "first" && processStatus != "first_save"){
		$("#uploadform").iForm('endEdit');
	}else if(""!=sheetId && null!=sheetId && "null"!=sheetId){
		var createUser = $("#autoform").iForm("getVal","username");
		var curUser = Priv.secUser.userName;
		//处于第一环节时，申请人和当前用户不为同一用户，不可编辑附件
		if( createUser!=curUser){
			$("#uploadform").iForm('endEdit');
		}
	}
	//附件组件控制--end
	
	
	$("#btn-delete").hide();
	//当前页面是否可以被编辑
	if("editable"!=isEdit && ""!=processInstId){
		$("#btn-edit").hide();
		$("#btn-save").hide();
		$("#btn-submit").hide();
		$("#btn-add").hide();
	}else{
		if('edit' == type){
			$("#btn-edit").show();
		}
	}
}

//隐藏提交归档方法
function autoCommitProcess(){
    var data={};
    data['taskId'] = taskId;
    data['message'] = "审批归档。";
    var url = 'workflow/process_inst/autoComplete.do';
    $.post(url, data, function(data){
        if(data.result=='ok'){
            submitCallBack();
        }
    });
}

//停止流程
function stopProcess(){
	var workFlow = new WorkFlow();
    var flowData = workFlow.getFormData();
    $.ajax({
		type : "POST",
		url: basePath + "purchase/purorder/stopProcess.do",
		async : false,
		data: {
			"taskId":taskId,
			"message":flowData.reason
			},
		dataType : "json",
		success : function(data) {
			 if(data.result=='success'){
	        	_parent().$("#itcDlg").dialog("close");
	            FW.success("流程终止成功");
	            homepageService.refresh();
	            pageClose();
	        }
	        else{
	            FW.error("流程终止失败");
	        }
		}
	});
}