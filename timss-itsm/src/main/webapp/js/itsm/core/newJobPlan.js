/* 表单字段定义  */
var fields = [
			{title : "ID", id : "id",type : "hidden"},
		    {title : "名称", id : "description", rules : {required:true,maxChLength:680}},
		    {title : "服务目录Id", id : "faultTypeId",type : "hidden"},
		    {
		    	title : "服务目录", 
		    	id : "faultTypeName",
		    	type:"label",
		    	value:"请从左边服务目录树选择",
		    	rules : {required:true}
		    },
	    	{
		        title : "备注", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {maxChLength:680}
		    }
			    
			];

var toolDelteId = 1;
var	toolGridField = [[
		{field : 'toolDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row){
				row.toolDelteId =toolDelteId;
				return toolDelteId++;
			}
		},  
   	    {field : 'id',title : 'ID', hidden:true},  
   	    {field : 'itemsId',title : '物资ID',hidden:true},
		{field : 'itemsCode',title : '物资编码',width:80,fixed:true}, 
		{field : 'itemsName',title : '名称',width:200},
		{field : 'itemsModel',title : '规格型号',width:200,fixed:true},
	/*	{field : 'bin',title : '货柜',width:100,fixed:true},
		{field : 'warehouse',title : '仓库',width:100,fixed:true},*/
		{field : 'applyCount',title : '使用数量',edit:true,width:60,fixed:true,
			editor:{type:"text","rules" : {required:true,"digits" : true},
				"options":{align:"right"}}},
		{field : 'unit',title : '单位',width:60,fixed:true,align:'center'},
		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
			 formatter:function(value,row){
				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     		"'toolTable',"+row.toolDelteId+')" width="16" height="16" >';
			}
		}
	]];
				   
function initJPPage(jpFullData){
	 
	/* form表单初始化 */
	$("#jobPlanForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
	
	if(faultTypeId){ 
		$("#jobPlanForm").iForm("setVal",{"faultTypeId":faultTypeId,"faultTypeName":faultTypeName});
	}
	$("#title_tool").iFold("init");
	
	$("#toolTable").datagrid({
		    columns:toolGridField,
		    idField:'toolDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		}); 

	 
	if(jpFullData!=0){  //jpFullData==0时表示新建作业方案
		var jpFormData = JSON.parse(jpFullData.jobPlanForm)
		$("#jobPlanForm").iForm("setVal",jpFormData);
		$("#btn_jp_saveDiv").hide();
		
		//工具
		var toolDataStr = jpFullData.toolData;
		var toolDataObj = JSON.parse(toolDataStr);
		if(toolDataObj.length!=0){
			$("#toolTable").datagrid("loadData",toolDataObj );
			$("#toolTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#toolBtnDiv").hide();
		}else{
			$("#title_tool").iFold("hide");
		}
		
	}else{  //如果是新建标准作业方案
		$("#btn_jp_editDiv").hide();
		$("#btn_jp_unavailableDiv").hide();
	}
	FW.fixRoundButtons("#toolbar");
}
 
/**
 * 提交维护计划（数据入库，关闭页面，刷新计划列表）
 */
function commitJP(){
	var tempfaultTypeName = $("#jobPlanForm").iForm("getVal","faultTypeName");
	if(tempfaultTypeName == '' || tempfaultTypeName == "请从左边服务目录树选择"){
		FW.error("请从左边选择服务目录");
		return;
	}
	/**表单验证 + datagrid验证*/
	if(!$("#jobPlanForm").valid() || !$("#toolTable").iValidDatagrid()){
		return ;
	}
	var toolData = $("#toolTable").datagrid("getData");
	if(toolData.total==0){
		FW.error("请添加工具");
		return ;
	}
	
	endEdit("toolTable");
	$("#toolTable").datagrid("clearChecked");
	
	var jpFormData = $("#jobPlanForm").iForm("getVal");  //取表单值
	var jobPlanId = jpFormData.id;
	//如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
	if(jobPlanId==""){  
		jobPlanId = 0;
	};
	
	$.post(basePath + "itsm/jobPlan/commitJobPlandata.do",
	 		{"jobPlanForm":JSON.stringify(jpFormData), "toolData":JSON.stringify(toolData),"jobPlanId":jobPlanId},
			function(data){
				if(data.result == "success"){
					$("#toolBtnDiv").hide();
					$("#btn_jp_save").hide();
					FW.success("新建成功");
					closeCurPage();
				}else {
					FW.error("新建失败");
				}
	  },"json");
	  
	$("#jobPlanForm").iForm("endEdit");  //关闭编辑，
}

/* 编辑维护计划 （按钮）*/
function editJP(){
	formRead = false;
	$("#jobPlanForm").iForm("beginEdit");  //打开编辑
	$("#title_tool").iFold("show");
	var toolData = $("#toolTable").datagrid("getData");
	if(toolData.total != 0){
		$("#btn_toolTable").html("继续添加工具");
	}
	beginEdit("toolTable");
	$("#toolTable").datagrid("showColumn","oper"); //隐藏某一列
	$("#toolBtnDiv").show();
	//按钮控制
	$("#btn_jp_editDiv").hide();
	$("#btn_jp_unavailableDiv").hide();
	$("#btn_jp_saveDiv").show();
	FW.fixRoundButtons("#toolbar");	
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("JPlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

/** 删除dataGrid中的某一行数据
 * @param dataGridId
 * @param index
 */
function deleteGridRow(dataGridId,deleteId){
	$('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
 }
 
function beginEdit(id){
   	var rows = $("#"+id).datagrid('getRows');
   	for(var i=0;i<rows.length;i++){
		$("#"+id).datagrid('beginEdit',i);
	}
}
function endEdit(id){
   var rows = $("#"+id).datagrid('getRows');
   for(var i=0;i<rows.length;i++){
	   $("#"+id).datagrid('endEdit',i);
   }
}

/**
 * 添加工具弹出框
 */
function appendTool(){
	//调用库存物资树方法
	FW.showInventoryDialog({
		onParseData : function(data){
			var size = data.length;
			for(var i=0;i<size;i++){
				var row = {};
				row["itemsCode"] = data[i]["itemcode"];
				row["itemsName"] = data[i]["itemname"];
				row["itemsId"] = data[i]["itemid"];
				row["itemsModel"] = data[i]["cusmodel"];
				row["unit"] = data[i]["unit1"];
				row["bin"] = data[i]["bin"];
				row["warehouse"] = data[i]["warehouse"];
				row["applyCount"] = 1 ;  //默认添加一个
				$("#toolTable").datagrid("appendRow",row );
			}

			beginEdit("toolTable");
			$("#btn_toolTable").html("继续添加工具");
		}
	});
}  
/**
 * 禁用标准作业方案
 */
function unavailableJP(){
	Notice.confirm("确定禁用|确定禁用该作业方案么？",function(){
		var jpFormData = $("#jobPlanForm").iForm("getVal");  //取表单值
		var jobPlanId = jpFormData.id;

		$.post(basePath + "itsm/jobPlan/unavailableJP.do",
		 		{"jobPlanId":jobPlanId},
				function(data){
					if(data.result == "success"){
						$("#btn_jp_unavailable").hide();
						$("#btn_jp_edit").hide();
						FW.success("禁用成功");
						closeCurPage();
					}else {
						FW.error("禁用失败");
					}
		  },"json");
	},null,"info");	
}