var invMatTransferFields = [
		{title : "名称", id : "name", rules : {maxlength:100,required:true},wrapXsWidth:12, wrapMdWidth:8},
		{title : "IMTID", id : "imtId",type:"hidden"},
		{title : "移出仓库ID", id : "wareHouseFromId",type:"hidden", value:wareHouseFromId, linebreak:true},
		{title : "移出仓库", id : "wareHouseFromName",type:"label", value:wareHouseFromName},
    	{
    		title : "移动到",
    		id : "wareHouseToId",
    		type:"combobox",
    		rules : {required:true},
    		data:wareHouseArray,
			options: {
				initOnChange: true,
				onChange: applyWareHouseChange
			}
    	},	
		{title : "移入仓库", id : "wareHouseToName",type:"hidden"},
	    {title : "备注", id : "remark",type : "textarea",rules : {maxlength:400},linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
		{
			title : "状态", 
			id : "status", 
			type:"label",
			linebreak:true,
			formatter:function(val){
				return FW.getEnumMap("INV_MAT_TRANSFER_STATUS")[val];
			}
		}
];
	
function applyWareHouseChange(val){
	var txt = $("#f_wareHouseToId").iCombo('getTxt');//移入仓库名称
	$("#invMatTransferForm").iForm("setVal",{wareHouseToName:txt});
}

//页面查看时表单赋值
function setFormVal(imtId){
	$.post(basePath + "inventory/invmattransfer/queryInvMatTransferInfo.do",{imtId:imtId},
		function(data){
			$(".priv").hide();
			//申请表单赋值
			var invMatTransferObj = JSON.parse(data.invMatTransferStr);
			$("#invMatTransferForm").iForm("setVal",invMatTransferObj).iForm("endEdit");
			
			//物资表格赋值
			var invMatTransferDetailListObj = JSON.parse(data.invMatTransferDetailListStr);
			$("#toolTable").datagrid("loadData",invMatTransferDetailListObj);
	 		
			//设置全局变量
			instanceId = invMatTransferObj.instanceId;
			taskId = data.taskId;
			createtime =  FW.long2time(invMatTransferObj.createdate);
			updatetime =  FW.long2time(invMatTransferObj.modifydate);
			approveFlag = data.approveFlag;
			if(approveFlag=="approver"){
				auditInfoShowBtn = 1;
			}
			else{
				auditInfoShowBtn = 0;
			}
			
			//设置标题
			status = invMatTransferObj.status;
			if(status == DRAFT){
				$("#pageTitle").html("新建移库申请");
			}
			else{
				$("#pageTitle").html("移库申请详情");
			}
			
			//按钮控制
			if(status == DRAFT || status == TRANSFER_APPLY_COMMIT){
				$("#btn_toolTable").show();
				$("#toolTable").datagrid("showColumn","oper"); //显示操作列
				
				$("#toolTable").datagrid("showColumn","transferQty"); 
				$("#toolTable").datagrid("hideColumn","transferQtyText"); 
				
				$("#toolTable").datagrid("showColumn","cateTypeName"); 
				$("#toolTable").datagrid("showColumn","binName"); 
				
				$("#toolTable").datagrid("hideColumn","toCateTypeName"); 
				$("#toolTable").datagrid("hideColumn","toBinName"); 

				$("#toolTable").datagrid("showColumn","stockQty");
				$("#toolTable").datagrid("showColumn","nowqty");
			}
			else{
				$("#btn_toolTable").hide();
				$("#toolTable").datagrid("hideColumn","oper"); //隐藏操作列
				
				$("#toolTable").datagrid("hideColumn","transferQty"); 
				$("#toolTable").datagrid("showColumn","transferQtyText"); 
				
				$("#toolTable").datagrid("hideColumn","cateTypeName"); 
				$("#toolTable").datagrid("hideColumn","binName"); 
				
				$("#toolTable").datagrid("showColumn","toCateTypeName"); 
				$("#toolTable").datagrid("showColumn","toBinName"); 

			}
			
			//根据状态进行判断
			if(status == DRAFT){
				//按钮控制
				$("#btn_save").show();//暂存
				$("#btn_commit").show();//提交
				if( userId == invMatTransferObj.createuser ){
					$("#btn_delete").show();//删除
				}
				//表单控制
				$("#invMatTransferForm").iForm("beginEdit");
				beginEdit("toolTable");
			}
			else if(status == TRANSFER_APPLY_COMMIT){
				//按钮控制
				if(userId == invMatTransferObj.createuser){
					$("#btn_edit").show();//可编辑
					$("#btn_commit").show();//可提交
					$("#btn_obsolete").show();//可作废
				}
				//表单控制
				$("#invMatTransferForm").iForm("endEdit");
				endEdit("toolTable");
			}
			else if(status == STOREMAN_AUDIT){
				//按钮控制
				if(approveFlag == "approver"){
					$("#btn_audit").show();//可审批
					beginEdit("toolTable");
				}
				else{
					endEdit("toolTable");
				}
				//表单控制
				$("#invMatTransferForm").iForm("endEdit");
			}
			else if(status==DONE){ //16已完成
				//表单控制
				$("#invMatTransferForm").iForm("endEdit");
				endEdit("toolTable");
			}
			FW.fixRoundButtons("#toolbar");
			
		},"json");
	$(window).resize();
}

//提交工单基本信息
function commitApply(commitStyle){
	//表单验证
	if(!$("#invMatTransferForm").valid()){
		FW.error("基本信息校验异常");
		return ;
	}

	//表格先要结束编辑
	endEdit("toolTable");

	//取物资数据
	var invMatTransferObj = $("#invMatTransferForm").iForm("getVal");//取表单值
	var invMatTransferData = JSON.stringify(invMatTransferObj);
	var invMatTransferDetailObj =$("#toolTable").datagrid("getRows");//取表格数据
	var invMatTransferDetailData = JSON.stringify(invMatTransferDetailObj);

	//验证物资列表是否合法
	if(!validateTable(invMatTransferObj.wareHouseFromId)){
		beginEdit("toolTable");//验证失败打开编辑
		return ;		
	}
	
	//按钮显示控制
	var operation = "";
	var path = "";
	if(commitStyle=="commit"){
		if(instanceId==null || instanceId==""){
			$("#btn_commit").button('loading');
		}
		path = basePath + "inventory/invmattransfer/commitInvMatTransfer.do";
		operation = "提交";
	}
	else if(commitStyle=="save"){
		path = basePath + "inventory/invmattransfer/saveInvMatTransfer.do";
		operation = "暂存";
	}
	else{
		FW.error( "操作异常");
		return;
	}
	
	//暂存或提交
	$.post(path,
		{
			"invMatTransferData": invMatTransferData,
			"invMatTransferDetailData": invMatTransferDetailData
		},
		function(data){
			if(data.result == "success"){
				FW.success(operation + "成功");
				imtId = data.imtId;
				taskId = data.taskId;
				instanceId = data.instanceId;
				$("#invMatTransferForm").iForm("setVal",{"imtId":imtId});
				if(commitStyle=="save"){ //暂存
					if(instanceId==null || instanceId==""){	//首次提交暂存后才可删除
						$("#btnDeleteDiv").show();
						$("#btn_delete").show();
						$("#invMatTransferForm").iForm("setVal",{"status":DRAFT});
						FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
					}
				}
				else{	//提交
					var workFlow = new WorkFlow();
					var multiple = 0;
					if(instanceId==null || instanceId==""){//首次提交才用submitApply方法
						workFlow.submitApply(taskId, JSON.stringify(invMatTransferData), submitSuccess,cancel,multiple);
					}
					else{
						audit();
					}
				}
				endEditApply();
			}
			else {
				FW.error(operation + "失败");
				$("#btn_commit").button('reset');
			}
		},"json");
}

//提交成功
function submitSuccess(){
	FW.success("提交成功");
	closeCurPage();
};

//取消
function cancel(){ 
	closeCurPage();
};

//编辑申请和物资
function beginEditApply(){
	//按钮控制
	$("#btn_edit").hide();
	$("#btn_save").show();
	FW.fixRoundButtons("#toolbar");
	
	//表单控制
	$("#invMatTransferForm").iForm("beginEdit");
	beginEdit("toolTable");
}

//结束编辑申请和物资
function endEditApply(){
	//按钮控制
	$("#btn_edit").show();
	$("#btn_save").hide();
	FW.fixRoundButtons("#toolbar");
	
	//表单控制
	$("#invMatTransferForm").iForm("endEdit");
	endEdit("toolTable");
}

//开始审批
function audit(){  
	if(!$("#invMatTransferForm").valid()){
		FW.error("基本信息校验异常");
		return ;
	}

	//获取表单数据
	endEdit("toolTable");
	var invMatTransferObj = $("#invMatTransferForm").iForm("getVal");//取表单值
	var invMatTransferData = JSON.stringify(invMatTransferObj);
	var invMatTransferDetailObj =$("#toolTable").datagrid("getRows");//取表格数据
	
	var invMatTransferDetailData = JSON.stringify(invMatTransferDetailObj);
	beginEdit("toolTable");
	
	var businessData = {
			"imtId":imtId,
			"invMatTransferData":invMatTransferData,
			"invMatTransferDetailData":invMatTransferDetailData
	};
	var workFlow = new WorkFlow();
	var multiple = 0;
	workFlow.showAudit(taskId,JSON.stringify(businessData),agree,rollback,stop,null,multiple);
}

//回退
function rollback(){
	closeCurPage();
};

//终止
function stop(rowdata){
	closeCurPage();
}

//同意
function agree(){
	closeCurPage();
};

//显示审批流程(流程未启动)
function showDiagram(){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}

//显示审批信息(流程已启动)
function showAuditInfo(){
	if( instanceId == null || instanceId == "" ){
		var workFlow = new WorkFlow();
		workFlow.showDiagram(defKey);
	}else{
		var businessData={};
		var fields = [{
			title : "创建时间",
			id : "createtime",
			type : "label"
		},{
			title : "修改时间",
			id : "updatetime",
			type : "label"
		}];

		var data={'createtime':createtime,'updatetime':updatetime};
		businessData['fields'] = fields;
		businessData['data'] = data;
		var workFlow = new WorkFlow();
		workFlow.showAuditInfo(instanceId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
}

//删除工单
function deleteApply(){
	FW.confirm("确定删除|确定删除该条申请吗？",function(){
		$.post(basePath + "inventory/invmattransfer/deleteInvMatTransfer.do",{"imtId":imtId},
			function(data){
				if(data.result == "success"){
					FW.success("删除成功");
					closeCurPage();
				}else {
					FW.error("删除失败");
				}
			},"json");
	},null,"info");
}

//作废工单（非草稿，仅工单发起人可以作废）
function obsoleteApply(){
	FW.confirm("确定作废|确定作废该条申请吗？",function(){
		$.post(basePath + "inventory/invmattransfer/obsoleteInvMatTransfer.do",{"imtId":imtId},
			function(data){
				if(data.result == "success"){
					FW.success("作废成功");
					closeCurPage();
				}else {
					FW.error("作废失败");
				}
			},"json");
	},null,"info");
}

//关闭当前tab 页
function closeCurPage(flag){
	if(flag){
		FW.set("eqWOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

