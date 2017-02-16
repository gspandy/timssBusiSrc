/**
 * 提交修改年度计划信息到后台,暂存
 */
function submit(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/project/tmpUpdateProject.do',{"project":FW.stringify(data)},function(result){
		
		showBasicMessageFromServer(result,"修改成功","修改成功",
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data});
	});
}

function shenpi(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var taskId=getWorkflowTaskId();
	var processInstId=getWorkflowProcessInstId();
	$.post(basePath+'pms/project/setWFVariable.do',{taskId:taskId,processInstId:processInstId,project:FW.stringify(data)},
		function(result){
		if(result && result.flag=='success'){
			openNewUserDialog(data,stopWorkflow);
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

/**
 * 流程终止函数
 */
function stopWorkflow(reason){
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var processInstId=getWorkflowProcessInstId();
	$.post(basePath+"pms/project/stopWorkflow.do",{project:FW.stringify(data),processInstId:processInstId,reason:reason},function(result){
		showBasicMessageFromServer(result,"流程终止成功","流程终止失败",{successFunction:closeWorkflowWindow});
	});
}
/**
 * 删除年度计划
 */
function del(){
	var data={id:id};
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'pms/project/deleteProject.do',data,function(result){
			showBasicMessageFromServer(result,"删除成功","删除失败");
		});
	});
	
}

/**
 * 提交修改年度计划信息到后台,只是暂存
 */
function tmpSave(){
	if(!$("#form1").valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	$.post(basePath+'pms/project/tmpUpdateProject.do',{"project":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,"暂存成功","暂存失败");
	});
}


/**
 * 初始化招标列表
 * @param data 列表数据
 */
function initBidForm(data){
	if (data.bidResultVos && data.bidResultVos.length) {
		$("#bidList").iFold();
		$("#test_grid1").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
		
			onDblClickRow : function(rowIndex, rowData) {
				var id=rowData["id"];
				openTab(basePath+"pms/bid/editBidResultJsp.do?id="+rowData['bidResultId'],'招标','pmsViewBidTab'+id);
			},
			data:data.bidResultVos,
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'bidId',
				hidden : true
			}, {
				field : 'type',
				hidden : true
			}, {
				field : 'name',
				title : '招标名称',
				width : 400,
				align : 'left',
				fixed:true
			}, {
				field : 'typeName',
				title : '招标方式',
				width : 135,
				align : 'left',
				fixed : true
			}, {
				field : 'budget',
				title : '中标价(元)',
				width : 105,
				align : 'right',
				fixed : true
			}, {
				field : 'statusName',
				title : '状态',
				width : 90,
				align : 'left'
			}] ]

		});
		
	}else{
		$('#bidListWrapper').hide();
	}
}

/**
 * 初始化合同列表
 * @param data
 */
function initContractForm(data){
	if (data.contracts && data.contracts.length) {
		$("#contractList").iFold();
		$("#contractTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			pageSize : pageSize,// pageSize为全局变量，自动获取的
			onDblClickRow : function(rowIndex, rowData) {
				var id=rowData["id"];
				openTab(basePath+"pms/contract/editContractJsp.do?contractId="+rowData['id'],'合同','pmsViewContractTab'+id);
			},
			data:data.contracts,
			columns : [ [
			/* {field:'ck',checkbox:true}, */
			{
				field : 'id',
				hidden : true
			},{
				field : 'name',
				title : '合同名称',
				width : 400,
				align : 'left',
				fixed:true
			}, {
				field : 'contractCode',
				title : '合同编号',
				width : 135,
				align : 'left',
				fixed : true
			}, {
				field : 'typeName',
				title : '合同类型',
				width : 90,
				align : 'left',
				fixed : true
			}, {
				field : 'signTime',
				title : '签订时间',
				width : 105,
				align : 'left',
				fixed : true,
				formatter: function(value,row,index){
 					//时间转date的string，还有long2date(value)方法
 					return FW.long2date(value);
 				}
			}, {
				field : 'totalSum',
				title : '合同金额(元)',
				width : 105,
				align : 'right',
				fixed : true
			}, {
				field : 'statusValue',
				title : '状态',
				width : 90,
				align : 'left'
			} ] ]

		});
		
	}else{
		$('#contractListWrapper').hide();
	}
}

/**
 * 打开新建招标信息页面
 */
function openBidTab(){
	var projectId=id;
	openTab('pms/bid/addBidJsp.do?projectId='+projectId,'招标','pmsNewBidTab');
}

function openContractTab(){
	var projectId=id;
	openTab('pms/contract/addContractJsp.do?projectId='+projectId,'合同','pmsNewContractTab');
}

function initOther(opt){
	
	var data=opt.data;
	var form=$(opt.form);
	var formFields=opt.formFields;
	initAttachForm(data.data.attachMap,$('#attachForm'),$('#attachFormWrapper'),opt.readOnly && !opt.attach);
	initRemoteField(form);
	initBidForm(data.data);
	initContractForm(data.data);
	initCheckoutGrid(data);
	initPayGrid(data);
}

function initCheckoutGrid(data){
	if (data && data.data&&data.data.checkoutVos && data.data.checkoutVos.length) {
		$("#checkoutList").iFold();
		$("#checkoutTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			data:data.data.checkoutVos,
	        onDblClickRow:function(rowIndex, rowData){
	        	var payplanId=rowData["payplanId"];
	        	var contractId=rowData["contractId"];
	        	var url="pms/checkout/editCheckoutJsp.do?payplanId="+payplanId+"&contractId="+contractId;
	        	var id=rowData["id"];
	        	openTab(url,'验收','pmsViewCheckoutTab'+id);
	        },
	        columns : [ [
	         			/* {field:'ck',checkbox:true}, */
	         			{
	         				field : 'id',
	         				hidden : true
	         			},{
	         				field : 'contractName',
	         				title : '合同名称',
	         				width : 400,
	         				align : 'left',
	         				fixed:true
	         			}, {
	         				field : 'contractCode',
	         				title : '合同编号',
	         				width : 135,
	         				align : 'left',
	         				fixed : true
	         			}, {
	         				field : 'checkTypeName',
	         				title : '验收类型',
	         				width : 90,
	         				align : 'left',
	         				fixed : true
	         			}, {
	         				field : 'payTypeName',
	         				title : '结算阶段',
	         				width : 110,
	         				align : 'left',
	         				fixed : true
	         				
	         			}, {
	         				field : 'time',
	         				title : '验收日期',
	         				width : 105,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	          					//时间转date的string，还有long2date(value)方法
	          					return FW.long2date(value);
	          				}
	         			}, {
	         				field : 'statusName',
	         				title : '状态',
	         				width : 90,
	         				align : 'left'
	         			} ] ]


		});
		
	}else{
		$('#checkoutListWrapper').hide();
	}
}

function initPayGrid(data){
	if (data && data.data&&data.data.payVos && data.data.payVos.length) {
		$("#payList").iFold();
		$("#payTable").datagrid({
			fitColumns:true,
			scrollbarSize:0,
			singleSelect:true,
			data:data.data.payVos,
			 onDblClickRow:function(rowIndex, rowData){
		        	var payplanId=rowData["payplanId"];
		        	var contractId=rowData["contractId"];
		        	var url="pms/pay/editPayJsp.do?payplanId="+payplanId+"&contractId="+contractId;
		        	var id=rowData["id"];
		        	openTab(url,'结算','pmsViewPayTab'+id);
		        },
		        columns : [ [
		         			/* {field:'ck',checkbox:true}, */
		         			{
		         				field : 'id',
		         				hidden : true
		         			},{
		         				field : 'contractName',
		         				title : '合同名称',
		         				width : 400,
		         				align : 'left',
		         				fixed:true
		         			}, {
		         				field : 'contractCode',
		         				title : '合同编号',
		         				width : 135,
		         				align : 'left',
		         				fixed : true
		         			}, {
		         				field : 'contractType',
		         				title : '合同类型',
		         				width : 90,
		         				align : 'left',
		         				fixed : true
		         			}, {
		         				field : 'payTypeName',
		         				title : '结算阶段',
		         				width : 110,
		         				align : 'left',
		         				fixed : true
		         			}, {
		         				field : 'actualpay',
		         				title : '结算金额(元)',
		         				width : 105,
		         				align : 'right',
		         				fixed : true
		         			}, {
		         				field : 'statusName',
		         				title : '状态',
		         				width : 90,
		         				align : 'left'
		         			} ] ]

		});
		
	}else{
		$('#payListWrapper').hide();
	}
}