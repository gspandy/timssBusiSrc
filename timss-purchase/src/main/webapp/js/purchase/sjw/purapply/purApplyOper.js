//采购申请提交按钮（通用方法）
function commitApplyForm(obj){
	var type = obj.type;
	var operation = obj.operation;
	var activeStatus = obj.activeStatus;
	
	if (type == "submit") {
		$("#btn-submit").button('loading');
	}
	
	if(!$("#autoform").valid()){
		$("#btn-submit").button('reset');
		return ;
	}
	
	var formData =$("#autoform").ITC_Form("getdata");
	var listData = [];
	
	var allRows = 0;
	var selectRows = 0;
	
	if(processStatus == "last"){
		listData = $("#apply_item").datagrid("getSelections");
		allRows = $("#apply_item").datagrid("getRows").length;
		selectRows = listData.length;
	}else{
		endEditAll();
		listData = $("#apply_item").datagrid("getRows");
	}
	//清掉那些可能出现中文或换行符且用不着的字段
	for(var i =0;i<listData.length;i++){
		listData[i]["cusmodel"]="";
		listData[i]["orderunitid"]="";
		listData[i]["orderunitname"]="";
	}
	//获取修改过的行数据
	var listApplyI = $("#apply_item").datagrid("getChanges");
	for(var i=0;i<listApplyI.length;i++){
		var itemid = listApplyI[i].itemid;
		var itemname = listApplyI[i].itemname;
		var repliednum = listApplyI[i].repliednum;
		var averprice = listApplyI[i].averprice;
		
		if(proMsg.indexOf(itemid) == -1){
			var msg = "物资编号为 "+itemid+" 的 \""+itemname+"\"批复数量修改为"+repliednum+"，当前单价为："+averprice+"元。";
			proMsg += msg;
		}
	}
	
	// 20160106 add by yuanzh 说是要添加附件
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=JSON.stringify(ids.uploadField);
	if (processStatus == "over") {
		autoGenerateOrder();
	}else{
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purapply/commitApply.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData),
			"type":type,
			"oper":operation[1],
			"sheetId":sheetId,
			"taskId":taskId,
			"uploadIds":uploadIds,
			"activeStatus":activeStatus
			},
		dataType : "json",
		success : function(data) {
			taskId = data.taskId;
			sheetId = data.sheetId;
			processInstId = data.processInstanceId;
			$("#f_purchstatus").val(data.status);
			$("#f_sheetno").val(data.sheetNo);
			if( data.result == "success" ){
				saveFlag = true;
				var workFlow = new WorkFlow();
				if(type=="submit"){
					if(processStatus == "first" || processStatus == "first_save"){
						startEditAll();
						workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,null);
					}else{
						if(processStatus == "last"){
							if(allRows == selectRows){
								autoCommitProcess();
							}else{
								submitCallBack();
							}
						}else{
							startEditAll();
							workFlow.showAudit(taskId,null,submitCallBack,pageClose,pageClose,proMsg,0,cancelCallBack);
						}
					}
				}else if(type=="over"){
					autoCommitProcess();
				}else{
					startEditAll();
					$("#btn-submit").button('reset');
					FW.success( "操作成功 ");
				}
			}else{
				$("#btn-submit").button('reset');
				FW.error( "操作失败 ");
			}
		}
	});
	}
}

//删除记录
function delApplyRecord(listId,itemname){
	Notice.confirm("删除？|确定删除所选项吗？该操作无法撤消。",function(){
		var msg = "删除\""+itemname+"\"项。";
		proMsg += msg;
		$('#apply_item').datagrid('deleteRow',$('#apply_item').datagrid('getRowIndex',listId));
		var listData =$("#apply_item").datagrid("getRows");
		if(listData.length == 0){
			$("#btn-add").text("添加物资");
		}else{
			$("#btn-add").text("继续添加物资");
		}
		dynaCalcTotalPrice();
	},null,"info");
};

