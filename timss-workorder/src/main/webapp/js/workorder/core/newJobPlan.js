/* 表单字段定义  */
var fields = [{title : "ID", id : "id",type : "hidden"},
		    {title : "名称", id : "description", rules : {required:true,maxChLength:680}},
		    {title : "专业",
		    	id : "specialtyId",
		    	type : "combobox",
		    	rules : {required:true},
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_SPEC",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_SPEC",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
	    	{title : "备注", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55,
		        rules : {maxChLength:680}
		    }
			    
			];
var preHazardDelteId = 1; 
var	preHazardGridField =[[
	{field : 'preHazardDelteId',title : '临时ID用来指定删除row', hidden:true,
		formatter:function(value,row,index){
			row.preHazardDelteId = preHazardDelteId;
			return preHazardDelteId++;
		}
	},                        
	{field : 'id',title : 'ID', editor:{type:"textarea"}, hidden:true},                     
	{field : 'hazardDescription',title : '安全事项', editor:{type:"textarea"}, width:400},
	{field : 'precautionDescription',title : '预控措施',editor:{type:"textarea"},width:400 },
	{field : 'oper',title : '',width:55,fixed:true,align:'center', 
		 formatter:function(value,row,index){
			     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
		     		"'preHazardTable',"+row.preHazardDelteId+')"  width="16" height="16" >';
		}
	}
	]];
var toolDelteId = 1;
var	toolGridField = [[
		{field : 'toolDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.toolDelteId =toolDelteId;
				return toolDelteId++;
			}
		},  
   	    {field : 'id',title : 'ID', hidden:true},  
   	    {field : 'itemsId',title : '物资ID',hidden:true},
		{field : 'itemsCode',title : '物资编码',width:80,fixed:true}, 
		{field : 'itemsName',title : '名称',width:200},
		{field : 'itemsModel',title : '规格型号',width:200,fixed:true},
		/*{field : 'bin',title : '货柜',width:100,fixed:true},
		{field : 'warehouse',title : '仓库',width:100,fixed:true},*/
		{field : 'applyCount',title : '使用数量',edit:true,width:60,fixed:true,
			editor:{type:"text","rules" : {required:true,"digits" : true},
				"options":{align:"right"}}},
		{field : 'unit',title : '单位',width:60,fixed:true,align:'center'},
		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
			 formatter:function(value,row,index){
				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     		"'toolTable',"+row.toolDelteId+')" width="16" height="16" >';
			}
		}
	]];
var taskDelteId = 1;
var	taskGridField =[[
		{field : 'taskDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.taskDelteId =taskDelteId;
				return taskDelteId++;
			}
		},  
   	    {field : 'id',title : 'ID', editor:{type:"textarea"}, hidden:true},     
		{field : 'proj',title : '大类', editor:{type:"textarea"}, width:160},
		{field : 'item',title : '项目', editor:{type:"textarea"}, width:160},
		{field : 'apply',title : '要求', editor:{type:"textarea"}, width:160},
		{field : 'description',title : '内容及标准', editor:{type:"textarea"}, width:160},
		{field : 'remarks',title : '记录', editor:{type:"textarea"}, width:160},
		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
			 formatter:function(value,row,index){
				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     		"'taskTable',"+row.taskDelteId+')" width="16" height="16" >';
			}
		}
	]]; 
				    
				   
				      
function initJPPage(jpFullData){
	 
	/* form表单初始化 */
	$("#jobPlanForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
	$("#title_preHazard").iFold("init");
	$("#title_tool").iFold("init");
	$("#title_task").iFold("init");
	
	 var preHazardDatagrid =  $("#preHazardTable").datagrid({
		    columns:preHazardGridField,
		    idField:'preHazardDelteId',  //用于删除时用
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		}); 
	
	var toolDatagrid =  $("#toolTable").datagrid({
		    columns:toolGridField,
		    idField:'toolDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		}); 

	var taskDatagrid =  $("#taskTable").datagrid({
		    columns:taskGridField,
		    idField:'taskDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		     
		});
	 
	 
	if(jpFullData!=0){  //jpFullData==0时表示新建作业方案
		var jpFormData = eval("(" +jpFullData.jobPlanForm+ ")")
		$("#jobPlanForm").iForm("setVal",jpFormData);
		
		//按钮控制
		var createUserId = jpFormData.createuser;  //创建标准作业方案的用户编号
		$("#btn_jp_saveDiv").hide();
	/*	if(loginUserId == createUserId){ //是自己建的标准作业方案
			$("#btn_jp_saveDiv").hide();
		}else{
			$("#btn_jp_saveDiv").hide();
			$("#btn_jp_editDiv").hide();
			$("#btn_jp_unavailableDiv").hide();
		}*/
		
		//安全事项
		var preHazardDataStr = jpFullData.preHazardData;
		var preHazardDataObj = eval("(" + preHazardDataStr + ")");
		if(preHazardDataObj.length!=0){
			$("#preHazardTable").datagrid("loadData", preHazardDataObj );
			$("#preHazardTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#preHazardBtnDiv").hide();
		}else{
			$("#title_preHazard").iFold("hide");
		}
		//物料
		var toolDataStr = jpFullData.toolData;
		var toolDataObj = eval("(" + toolDataStr + ")");
		if(toolDataObj.length!=0){
			$("#toolTable").datagrid("loadData",toolDataObj );
			$("#toolTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#toolBtnDiv").hide();
		}else{
			$("#title_tool").iFold("hide");
		}
		
		//内容
		var taskDataStr = jpFullData.taskData;
		var taskDataObj = eval("(" + taskDataStr + ")");
		if(taskDataObj.length!=0){
			$("#taskTable").datagrid("loadData", taskDataObj);
			$("#taskTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#taskBtnDiv").hide();
		}else{
			$("#title_task").iFold("hide");
			
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
	
	/**表单验证*/
	if(!$("#jobPlanForm").valid()){
		return ;
	}
	/**datagrid验证*/
	if(!$("#toolTableForm").valid()){
		return ;
	}
	var preHazardData =$("#preHazardTable").datagrid("getData");
	var toolData = $("#toolTable").datagrid("getData");
	var taskData = $("#taskTable").datagrid("getData");
	
	if(preHazardData.total==0&&toolData.total==0&&taskData.total==0){
		FW.error("请添加相关内容")
		return ;
	}
	endEdit("preHazardTable");
	endEdit("toolTable");
	endEdit("taskTable");
	var jpFormData = $("#jobPlanForm").iForm("getVal");  //取表单值
	var jobPlanId = jpFormData.id;
	if(jobPlanId==""){  //如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
		jobPlanId = 0;
	};
	
	var preHazardData =$("#preHazardTable").datagrid("getData");
	var toolData = $("#toolTable").datagrid("getData");
	var taskData = $("#taskTable").datagrid("getData");
	
	
	$("#preHazardTable").datagrid("clearChecked");
	$("#toolTable").datagrid("clearChecked");
	$("#taskTable").datagrid("clearChecked");
	
	
	$.post(basePath + "workorder/jobPlan/commitJobPlandata.do",
	 		{"jobPlanForm":JSON.stringify(jpFormData),"preHazardData":JSON.stringify(preHazardData),
			 	 "toolData":JSON.stringify(toolData),"taskData":JSON.stringify(taskData),
			 	 "jobPlanId":jobPlanId},
			function(data){
				if(data.result == "success"){
					$("#preHazardBtnDiv").hide();
					$("#toolBtnDiv").hide();
					$("#taskBtnDiv").hide();
					$("#btn_jp_save").hide();
					FW.success("新建成功")
					closeCurPage();
				}else {
					FW.error("新建失败")
				}
	  },"json");
	  
	$("#jobPlanForm").iForm("endEdit");  //关闭编辑，
}

/* 编辑维护计划 （按钮）*/
function editJP(){
	$("#jobPlanForm").iForm("beginEdit");  //打开编辑
	$("#title_preHazard").iFold("show");
	$("#title_tool").iFold("show");
	$("#title_task").iFold("show");
	var preHazardData =$("#preHazardTable").datagrid("getData");
	var toolData = $("#toolTable").datagrid("getData");
	var taskData = $("#taskTable").datagrid("getData");
	if(preHazardData.total != 0){
		$("#btn_preHazardTable").html("继续添加安全注意事项")
	}
	if(toolData.total != 0){
		$("#btn_toolTable").html("继续添加物料")
	}
	if(taskData.total != 0){
		$("#btn_taskTable").html("继续添加工作内容")
	}
	beginEdit("preHazardTable");
	beginEdit("toolTable");
	beginEdit("taskTable");
	$("#preHazardTable").datagrid("showColumn","oper"); //隐藏某一列
	$("#toolTable").datagrid("showColumn","oper"); //隐藏某一列
	$("#taskTable").datagrid("showColumn","oper"); //隐藏某一列
	$("#preHazardBtnDiv").show();
	$("#toolBtnDiv").show();
	$("#taskBtnDiv").show();
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
		FW.set("eqJPlistDoNotRefresh",true);
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
 
/**
 * 在datagrid的末尾添加一行数据
 * @param dataGridId  dataGrid的ID
 */
function appendDataGridRow(dataGridId){
 	$("#"+dataGridId).datagrid("appendRow",{});
    editIndex = $("#"+dataGridId).datagrid("getRows").length-1;
    $("#"+dataGridId).datagrid("selectRow", editIndex)
                .datagrid('beginEdit', editIndex);
    
    var title = $("#btn_"+dataGridId).html();
    var fdStart = title.indexOf("继续");  
    if(fdStart!=0){
    	$("#btn_"+dataGridId).html("继续"+title);
    }else{
    	$("#"+dataGridId).datagrid("r");
    }
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
 * 添加物料弹出框
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
			$("#btn_toolTable").html("继续添加物料")
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

		$.post(basePath + "workorder/jobPlan/unavailableJP.do",
		 		{"jobPlanId":jobPlanId},
				function(data){
					if(data.result == "success"){
						$("#btn_jp_unavailable").hide();
						$("#btn_jp_edit").hide();
						FW.success("禁用成功")
						closeCurPage();
					}else {
						FW.error("禁用失败")
					}
		  },"json");
	},null,"info");	
}