var materiReQAssetPriv=function(){
	if(Priv.hasPrivilege("materiReQ_asset") && ("draft"!=process && processInstId!="" && ("collect_supplies" == process || "" == process))){
		return true;
	}
	else{
		return false;
	}
};
var InvMatApplyPriv={
	init:function(){
		InvMatApplyPriv.set();
		InvMatApplyPriv.apply();
	},
	set:function(){//定义权限
		//保存
		Priv.map("privMapping.materiReQ_save","materiReQ_save");
		//编辑
		Priv.map("privMapping.materiReQ_edit","materiReQ_edit");
		//打印
		Priv.map("privMapping.materiReQ_export","materiReQ_export");
		//提交
		Priv.map("privMapping.materiReQ_commit","materiReQ_commit");
		//通知领料
		Priv.map("privMapping.materiReQ_grant","materiReQ_grant");
		//终止领料
		Priv.map("privMapping.materiReQ_stopsend","materiReQ_stopsend");		
		//新建
		Priv.map("privMapping.materiReQ_new","materiReQ_new");
		//删除
		Priv.map("privMapping.materiReQ_delete","materiReQ_delete");
		//退库
		Priv.map("privMapping.materiReQ_refund","materiReQ_refund");
		//资产化
		Priv.map("materiReQAssetPriv()", "materiReQ_asset_virtual");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//设置流程状态
function setProcessStatus(){
	if("" == process || "draft" == process){
		processStatus = "first";//首环节
		if( "draft" == process && "" != processInstId){
			processStatus = "first_save";//首环节暂存
		}else if("" != processInstId){
			processStatus = "over";//流程结束
		}
	}else if(process == "collect_supplies"){
		processStatus = "last";//流程最后环节
	}else{
		processStatus = "process";//流程中
		if("oper_manager"==process){
			processStatus = "om";//经营部经理审批
		}
	}
}

//初始化页面控制
var initPageProcess = {
	init : function() {
		// 是否通过库存查询生成领料单
		if ("" != codes) {// 代码不为空，则是通过库存查询生成的
			$("#pageTitle").html("新建物资领料单");
			// 选中物资带入表单
			listToMatApply();
			$("#btn_delete").hide();
			$("#btn_print").hide();
		} else {
			// 新建生成领料单
			initList();
			if ("" != imaid) {
				initPageProcess.read();
			} else {
				initPageProcess.news();
			}
		}
		$("#btn_edit").hide();
		$("#btn_send").hide();
		$("#btn_stopsend").hide();
		initPrinBtn();
		// 发料列表
		initOutList(imaid);
		// 不是最后环节要注册关闭提示事件
		if (processStatus != "last") {
			addFormCloseEvent();
		}
		// 是否是编辑状态，不是编辑状态添加物资按钮消失
		if ("editable" != isEdit) {
			$("#btn_add").hide();
			$("#btn_addFromPA").hide();
		}
		// 是否流程中，流程中提交按钮要改变名称叫”审批“
		if (processStatus == "process" || processStatus == "om") {
			$("#btn_submit").html("审批");
		}
	},
	read : function() {
		$("#pageTitle").html("物资领料单详情");

		$("#btn_save").hide();
		$("#btn_delete").hide();
		//$("#btn_print").show(); //TIM-1719 去掉按钮强制显示的功能，改为由权限控制
		$("#btn_submit").hide();

		initForm(edit_form);
		$("#autoform").ITC_Form("readonly");

		// 如果当前环节是”物资接收“
		if (processStatus != "last") {  //"over"
			if ((processStatus == "first" || processStatus == "first_save") && "editable" == isEdit) {// 首环节
				$('#matapplydetail_grid').datagrid('showColumn', "del");
			} else if (processStatus == "over") {// 流程结束
				$('#matapplydetail_grid').datagrid('hideColumn', "del");
			} else {
				$('#matapplydetail_grid').datagrid('hideColumn', "del");
			}
		}
	},
	edit : function() {
		$("#btn_submit").show();
		$("#matapplydetail_grid").show();

		if (processStatus != "last") {
			if (processStatus == "first" || processStatus == "first_save") {
				$("#pageTitle").html("编辑物资领料单");
				initForm(edit_form);
				$("#btn_save").show();
				$("#btn_print").hide();
				$("#btn_delete").show();
				if(classType == "Draft"){
					$("#btn_delete").html("删除");
				}
			}
			startEditAll();
			var listData = $("#matapplydetail_grid").datagrid("getRows");
			if (listData.length > 0) {
				$("#btn_add").text("继续添加物资");
			} else {
				$("#btn_add").text("添加物资");
			}
		} else {
			$('#matapplydetail_grid').datagrid('hideColumn', "del");
		}
		var _applyType = $("#autoform").iForm("getVal", "applyType");
		if (siteid == 'ITC') {
			applyTypeChange(_applyType);
			// 若是到经营部经理审批
			if (processStatus == "om") {
				$("#autoform").iForm("beginEdit", "spmaterial");
			}
		}

		if (processStatus != "first" && processStatus != "first_save" && processStatus != "over") {
			$("#pageTitle").html("物资领料单详情");
		}
	},
	news : function() {
		$("#pageTitle").html("新建物资领料单");
		initForm(new_form);
		$('#matapplydetail_grid').datagrid('showColumn', "del");
		$("#btn_delete").hide();
		$("#btn_print").hide();
	}
};

// 自动提交
function autoCommitProcess(){
    var data={};
    data['taskId'] = taskId;
    data['message'] = "审批归档。";
    var url = 'workflow/process_inst/autoComplete.do';
    $.post(url, data, function(data){
        if(data.result=='ok'){
			$("#matapplydetail_grid").datagrid("reload");
			FW.getFrame(FW.getCurrentTabId()).location.reload();
        }
    });
}

//停止流程
function stopProcess(){
	var workFlow = new WorkFlow();
    var flowData = workFlow.getFormData();
    $.ajax({
		type : "POST",
		url: basePath + "inventory/invmatapply/stopProcess.do",
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
	            closeCurTab();
	        }
	        else{
	            FW.error("流程终止失败");
	        }
		}
	});
}

//提交后回调执行
function submitCallBack(){
	closeCurTab();
}

function cancelCallBack(){
	//由于取消按钮没有任何操作，这里给一个空的方法
}

//同意
function agree(){
	var listData = $("#matapplydetail_grid").datagrid("getRows");
	$.ajax({
		type : "POST",
		url: basePath + "inventory/invmatapply/updateMatApply.do",
		async : false,
		data: {
			"status":nextStatus,
			"imaid":imaid,
			"listData":FW.stringify(listData)
			},
		dataType : "json",
		success : function(data) {
			 if(data.result=='success'){
				 closeCurTab();
			 }else{
				 FW.error("物资可用库存更新失败");
			 }
		}
	});
};

//回退
function rollback(){
	var listData = $("#matapplydetail_grid").datagrid("getRows");
	$.ajax({
		type : "POST",
		url: basePath + "inventory/invmatapply/updateMatApply.do",
		async : false,
		data: {
			"taskId":processInstId,
			"imaid":imaid,
			"listData":FW.stringify(listData)
			},
		dataType : "json",
		success : function(data) {
			if(data.result=='success'){
				closeCurTab();
			}else{
				 FW.error("物资可用库存更新失败");
			 }
		}
	});
}

