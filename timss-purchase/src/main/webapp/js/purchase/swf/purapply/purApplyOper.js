function resetEdit(){
	startEditAll();
	$("#btn-submit").button('reset');
}
//采购申请提交按钮（通用方法）
function commitApplyForm(obj) {
	var type = obj.type;
	var operation = obj.operation;
	var activeStatus = obj.activeStatus;
	
	if (type == "submit") {
		$("#btn-submit").button('loading');
	}
	if("SCLWZ"!=$("#autoform").iForm("getVal","sheetclassid")){
	    $("#autoform").iForm("setVal",{"major":"-"});
	}
	if (!$("#autoform").valid()) {
		$("#btn-submit").button('reset');
		resetEdit();
		return;
	}
	//如果是非生产物资是不需要选择专业的
	if("SCLWZ"!=$("#autoform").iForm("getVal","sheetclassid")){
	    $("#autoform").iForm("setVal",{"major":""});
	}
	var formData = $("#autoform").ITC_Form("getdata");
	var listData = [];
	var listDataSend =[];

	var allRows = 0;
	var selectRows = 0;

	if (processStatus == "last") {
		listData = $("#apply_item").datagrid("getSelections");
		allRows = $("#apply_item").datagrid("getRows").length;
		selectRows = listData.length;
	} else {
		endEditAll();
		listData = $("#apply_item").datagrid("getRows");
	}
	//避免影响提交失败后的详情列表
	listData = FW.parse( FW.stringify(listData) );
	//获取没有出现换行符的字段 发送后端
	for(var i =0;i<listData.length;i++){
		listDataSend[i] = new Object();
		listDataSend[i]["averprice"]=undefined!=listData[i]["averprice"]?listData[i]["averprice"]:"";
		listDataSend[i]["classname"]=undefined!=listData[i]["classname"]?listData[i]["classname"]:"";
		listDataSend[i]["claze"]=undefined!=listData[i]["claze"]?listData[i]["claze"]:"";
		listDataSend[i]["commitcommecnetwk"]=undefined!=listData[i]["commitcommecnetwk"]?listData[i]["commitcommecnetwk"]:"";
		listDataSend[i]["descriptions"]=undefined!=listData[i]["descriptions"]?listData[i]["descriptions"]:"";
		listDataSend[i]["eamPrlineId"]=undefined!=listData[i]["eamPrlineId"]?listData[i]["eamPrlineId"]:"";
		listDataSend[i]["istool"]=undefined!=listData[i]["istool"]?listData[i]["istool"]:"";
		listDataSend[i]["itemid"]=undefined!=listData[i]["itemid"]?listData[i]["itemid"]:"";
		listDataSend[i]["itemname"]=undefined!=listData[i]["itemname"]?listData[i]["itemname"]:"";
		listDataSend[i]["itemnum"]=undefined!=listData[i]["itemnum"]?listData[i]["itemnum"]:"";
		listDataSend[i]["olditemnum"]=undefined!=listData[i]["olditemnum"]?listData[i]["olditemnum"]:"";
		listDataSend[i]["prestore"]=undefined!=listData[i]["prestore"]?listData[i]["prestore"]:"";
		listDataSend[i]["priceTotal"]=undefined!=listData[i]["priceTotal"]?listData[i]["priceTotal"]:"";
		listDataSend[i]["remark"]=undefined!=listData[i]["remark"]?listData[i]["remark"]:"";
		listDataSend[i]["repliednum"]=undefined!=listData[i]["repliednum"]?listData[i]["repliednum"]:"";
		listDataSend[i]["siteid"]=undefined!=listData[i]["siteid"]?listData[i]["siteid"]:"";
		listDataSend[i]["status"]=undefined!=listData[i]["status"]?listData[i]["status"]:"";
		listDataSend[i]["storedate"]=undefined!=listData[i]["storedate"]?listData[i]["storedate"]:"";
		listDataSend[i]["storenum"]=undefined!=listData[i]["storenum"]?listData[i]["storenum"]:"";
		listDataSend[i]["warehouseid"]=undefined!=listData[i]["warehouseid"]?listData[i]["warehouseid"]:"";
		listDataSend[i]["invcateid"]=undefined!=listData[i]["invcateid"]?listData[i]["invcateid"]:"";
	}
	//如果非草稿时，是否提交商务网必填
	if("draft"!=process&&''==formData.isToBusiness){
		FW.error("请选择是否提交商务网");
		$("#btn-submit").button('reset');
		resetEdit();
		return;
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

	//加载用户表单数据
	$.ajax({
		type : "POST",
		async : false,
		url : basePath + "purchase/purapply/commitApply.do",
		data : {
			"formData" : FW.stringify(formData),
			"listData" : FW.stringify(listDataSend),
			"type" : type,
			"oper" : operation[1],
			"sheetId" : sheetId,
			"taskId" : taskId,
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
			if (data.result == "success") {
				saveFlag = true;
				var workFlow = new WorkFlow();
				if (type == "submit") {
					if (processStatus == "first" || processStatus == "first_save") {
						startEditAll();
						//候选人全部可选
						workFlow.submitApply(taskId, null, submitCallBack, cancelCallBack, 1);
					} else {
						 if("true"==isLastStep){
							//仿照processStatus == "last"的逻辑
							//原来itc站点下在总经理/主管副总经理审批后还有一个执行的环节，那个环节无退回直接发送商务网或自动生成采购单
							//swf站点总经理/主管副总经理审批作为最后一个环节，需要有退回，且能完成itc最后一个站点的功能。
							var isToBusiness = formData.isToBusiness;
							var url = basePath + '/workflow/process_inst/setVariables.do';
							var params={};
							params['processInstId'] = processInstId;
							var variables = [{'name':"isToBusiness",'value':isToBusiness}];
							params['variables'] = JSON.stringify(variables);
							$.post(url,params,function(data){
								if(data.result=='ok'){
							    	workFlow.showAudit(taskId, null, laststepSubmitCallBack, pageClose, stopProcess, proMsg, 0,resetEdit);
							    }
							});
						} else {
							workFlow.showAudit(taskId, null, submitCallBack, pageClose, stopProcess, proMsg, 0,resetEdit);
						}
					}
				} else if (type == "over") {
					autoCommitProcess(null);
				} else {
					startEditAll();
					FW.success("暂存成功 ");
				}
			} else {
				if (type == "submit") {
					FW.error(data.msg);
					startEditAll();
					$("#btn-submit").button('reset');
				}else{
					FW.error("暂存失败 ");
				}
			}
		}
	});
}
//最后一个环节自动生成采购合同
function laststepSubmitCallBack(){
	//默认全选
	submitCallBack();
}
//最后一个环节发送数据到商务网
function laststepSendToBusinessCallBack(){
	var itemidArr = [];
	var rowData = $("#apply_item").datagrid("getRows");
	var counter = rowData.length;
	for ( var i = 0; i < counter; i++) {
		itemidArr.push(rowData[i].itemid+"_"+rowData[i].invcateid);
	}
	$.ajax({
		url : basePath + "purchase/purapply/sendToBusiness.do",
		dataType : "json",
		type : "POST",
		async: false,
		data : {
			"itemids" : FW.stringify(itemidArr),
			"sheetId" : sheetId
		},
		success : function(data) {
			if (data.result == "success") {
				FW.success("发送成功 ");
				$.ajax({
					url : basePath + "purchase/purapply/buiss2UpdatePAStatus.do",
					dataType : "json",
					type : "POST",
					async: false,
					data : { "sheetId" : sheetId },
					success : function() {}
				});
				pageClose();
			} else {
				FW.error("发送失败 ");
			}
		}
	});
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

