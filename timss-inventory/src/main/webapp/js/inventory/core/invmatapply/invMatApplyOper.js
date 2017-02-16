//保存页面信息
function saveMatApply(obj){
	var type = obj.type;
	
	if (type == "submit") {
		$("#btn_submit").button('loading');
	}
	
	if(!$("#autoform").valid()){
		$("#btn_submit").button('reset');
		return ;
	}
	var formData =$("#autoform").ITC_Form("getdata");
	var listData =$("#matapplydetail_grid").datagrid("getRows");
	var relatePurApplyIdsList = $("#autoform").iForm("getVal","relatePurApplyIdsList");
	var purApplyIds = "";
	if(undefined != relatePurApplyIdsList){
		purApplyIds = relatePurApplyIdsList;
	}
	formData["relatePurApplyIdsList"] = FW.stringify(purApplyIds);
	var listData2 = listData;
	//避免影响领料暂存后的详情列表
	listData2 = FW.parse( FW.stringify(listData2) );
	
	if(type == "submit" && listData.length == 0){
		FW.error( "请先选择要领料的物资 ");
		$("#btn_submit").button('reset');
		return;
	}
	//提一次提交检验
	if(type == "submit" && processStatus != "process" && processStatus != "om" && processStatus != "last"){
		for(var i =0;i<listData.length;i++){
			var nowqty = listData[i]["nowqty"];
			var qtyApply = listData[i]["qtyApply"];
			if(qtyApply>nowqty){
				FW.error( "申请数量超出可用库存上限");
				$("#btn_submit").button('reset');
				return;
			}
		}
	}
	//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
	for(var i =0;i<listData.length;i++){
		listData2[i]["cusmodel"]="";
		listData2[i]["itemname"]="";
	}
	//清掉可能出现中文或换行符且用不着的字段 -- end
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmatapply/saveMatApply.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData2),
			"purApplyIds":purApplyIds,
			"type":type,
			"taskId":taskId,
			"imaid":imaid
			},
		dataType : "json",
		success : function(data) {
			saveFlag = true;
			nextStatus = data.status;
			taskId = data.taskId;
			imaid = data.imaid;
			if( data.result == "success" ){
				var workFlow = new WorkFlow();
				if(type == "submit"){
					if(processStatus == "first" || processStatus == "first_save" ){
						workFlow.submitApply(taskId,null,closeCurTab,cancelCallBack,null);
					}else{
						workFlow.showAudit(taskId,null,agree,rollback,stopProcess,proMsg,0);
					}
					$("#btn_submit").button('reset');
				}else{
					$("#btn_delete").show();
					if(classType == "Processed"){
						$("#btn_delete").html("作废");
					}
					else{
						$("#btn_delete").html("删除");
					}
					startEditAll();
					FW.success("暂存成功 ");
				}
			}else{
				if (type == "submit") {
					FW.error("提交失败 ");
					$("#btn_submit").button('reset');
				}else{
					FW.error("暂存失败 ");
				}
			}
		}
	});
}

//提交保存用方法
function commitMatApply(type){
	var obj = {};
	obj['type'] = type;

	caluPrice();
	if(flag){
		saveMatApply(obj);
	}else{
		if(processStatus == "last"){
			$("#matapplydetail_grid").datagrid("reload");
		}
		flag = true;
	}
	startEditAll();
}

//删除记录
function delRecord(itemid,itemname){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		var msg = "删除\""+itemname+"\"项。";
		proMsg += msg;
		$('#matapplydetail_grid').datagrid('deleteRow',$('#matapplydetail_grid').datagrid('getRowIndex',itemid));
		var listData =$("#matapplydetail_grid").datagrid("getRows");
		if(listData.length == 0){
			$("#btn_add").text("添加物资");
		}else{
			$("#btn_add").text("继续添加物资");
		}	
		dynaCalcTotalPrice();
	},null,"info");	
}

//自动生成领用情况信息
function autoGenerateConsuming(){
	var formData =$("#autoform").ITC_Form("getdata");
	var listData =$("#matapplydetail_grid").datagrid("getRows");
	//清掉可能出现中文或换行符且用不着的字段 -- begin by gchw
	for(var i =0;i<listData.length;i++){
		listData[i]["cusmodel"]="";
		listData[i]["itemname"]="";
	}
	//清掉可能出现中文或换行符且用不着的字段 -- end
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmatrecipients/autoGenerateConsuming.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData),
			"taskId":taskId,
			"imaid":imaid
			},
		dataType : "json",
		success : function() {
			FW.success( "通知领料待办发送成功 ！");
			hasRpts = "Y";
			if(checkSubmitIsAllOut()){
				$("#btn_save").hide();
				$("#btn_delete").hide();
				$("#btn_submit").hide();
				$("#btn_send").hide();
				autoCommitProcess();
			}else{
				$("#matapplydetail_grid").datagrid("reload");
				FW.getFrame(FW.getCurrentTabId()).location.reload();
			}
		},
		error: function(){
			FW.error( "通知领料待办发送失败 ！");
		}
	});
}
// 打开 退库的页面
function openRefundPage(){
	var url = basePath+ "inventory/invmatapply/invMatRefundForm.do?imaid="+imaid;
	var prefix = imaid;
    FW.addTabWithTree({
        id : "editMatRebackTostockForm" + prefix,
        url : url,
        name : "物资退库",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('editMatApplyForm"+imaid+"');"
        }
    });
}

