
/* 表单字段定义  */
var fields = [
			    {title : "ID", id : "id",type : "hidden"},
			    {title : "作业方案ID", id : "jobPlanId",type : "hidden"},
			    {title : "名称", id : "description", rules : {required:true, maxChLength:680}},
			    {title : "维护设备", id : "equipName",type:"label",value:"请从左边设备树选择"},
			    {title : "设备ID", id : "equipId",type : "hidden"},
			    {title : "设备编号", id : "equipNameCode",type : "hidden"},
			    {
			    	title : "专业",
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
			    {
				    title : "维护周期(天)",
				    id : "maintainPlanCycle",
				    rules : {required:true,number:true}},
			    {
			    	title : "周期开始时间", 
			    	id : "currStartTime",
			    	type:"datetime",
			    	dataType:"datetime",
			    	rules : {required:true}},
			    {
			    	title : "预警期(小时)", 
			    	id : "alertTime",
			    	rules : {required:true,number:true}},
			    {
			    	title : "父维护计划", 
			    	id : "parentMTPCode",
			    	render:function(id){
		            	 var ipt = $("#" + id);
		            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
		            	 ipt.ITCUI_Input();
		            	 ipt.next(".itcui_input_icon").on("click",function(){
		                     var src = basePath + "workorder/maintainPlan/parentMTPList.do";  
		                     var dlgOpts = {
		                         width : 600,
		                         height:500,
		                         closed : false,
		                         title:"双击选择父维护计划",
		                         modal:true
		                     };
		                     Notice.dialog(src,dlgOpts,null);
		                 });
		             }
			    },
			    {
			    	title : "父维护计划ID", 
			    	id : "parentMTPId",
			    	type : "hidden"},
		    	{
			        title : "负责班组", 
			        id : "workTeam",
			        type : "combobox",
			        rules : {required:true},
			    	options:{
			    		onChange:function(val,obj){ //根据选中的负责班组动态加载对应的负责人
				    		var principalUserGroup = siteId+"_WO_principal_"+val; //用户组ID
				    		$("#f_principal").iCombo("init",{
				    			url:basePath+ "workorder/maintainPlan/queryUserByUserGroup.do?userGroup="+principalUserGroup
				    		});
				    		if(formRead == false){
				    			$("#f_principal").iCombo("setTxt","");
				    		}
				    	},
				    	url :  ItcMvcService.getEnumPath() + "?data=WO_WORKTEAM",
				    	allowEmpty : true,
			    		"onRemoteData" : function(val){
			    	          return FW.parseEnumData("WO_WORKTEAM",val);
			    	    },
			    	    remoteLoadOn : formRead?"set":"init"
			        }
			    },
			    {
			        title : "负责人", 
			        id : "principal",
			        type : "combobox",
			        rules : {required:true},
			    	options:{  //SBS_wo_yi_workteamPrincipal
			    		url:"" ,
			    		remoteLoadOn : formRead?"set":"init"
			    	}
			    },
			    {
			    	title : "自动生成工单", 
			        id : "isAutoGenerWo",
			        type : "radio",
			        rules : {required:true},
			        data : [
			            ['0','否',true],
			            ['1','是']
			        ]
			    },
			    {
			        title : "备注", 
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
       	{field : 'precautionDescription',title : '预控措施',editor:{type:"textarea"},width:400},
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
       		{field : 'itemsName',title : '名称', width:200},
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
       				    


function initMTPPage(mtpFullData){
	$("#title_preHazard").iFold("init");
	$("#title_tool").iFold("init");
	$("#title_task").iFold("init");
	//对负责人远程数据的动态加载（根据班组信息）
	if(mtpFullData!=0 ){ 
		var mtpFormData = eval("(" +mtpFullData.maintainPlanForm+ ")");
		if(mtpFormData.maintainPlanFrom == "cycle_maintainPlan"){
			var userGroup = siteId+"_WO_principal_"+mtpFormData.workTeam; //用户组
			fields[13].options.url = basePath+ "workorder/maintainPlan/queryUserByUserGroup.do?userGroup=" + userGroup;
		}
	}
	/* form表单初始化 */
	$("#maintainPlanForm").iForm("init",{"fields":fields,"options":{validate:true,initAsReadonly:formRead}});
	
	if(equipId){ 
		$("#maintainPlanForm").iForm("setVal",{"equipId":equipId,"equipName":equipName,"equipNameCode":equipCode})
		
	}
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
	
	if(mtpFullData!=0){  //jpFullData==0时表示新建作业方案
		 
		var mtpFormData = eval("(" +mtpFullData.maintainPlanForm+ ")");
		mtpType = mtpFormData.maintainPlanFrom;//"cycle_maintainPlan"：周期性维护计划
		 
		if(mtpFormData.maintainPlanCycle == 0){
			mtpFormData.maintainPlanCycle = null;
		}
		if(mtpFormData.alertTime == 0){
			mtpFormData.alertTime = null;
		}
		
		$("#maintainPlanForm").iForm("setVal",mtpFormData);
		
		//按钮控制
		var createUserId = mtpFormData.createuser;  //创建标准作业方案的用户编号
		var principalId = mtpFormData.principal;
		if(mtpFormData.maintainPlanFrom == "cycle_maintainPlan"){
			$("#btn_mtp_toWoDiv").hide();
		}else{  //如果是遗留问题和不立即处理问题，则没有编辑功能，只能生成工单时修改
			$("#btn_mtp_editDiv").hide();
		}
		if(principalId == loginUser){  //如果登录用户是负责人，则可以手动生成工单
			$("#btn_mtp_toWoDiv").show();
		}
		
		$("#btn_mtp_saveDiv").hide();
		if(mtpType != "cycle_maintainPlan"){
			$("#btn_mtp_unavailableDiv").hide();
		}
		
/*		if(loginUser == createUserId){ //是自己建的标准作业方案
			
		}else{
			$("#btn_mtp_saveDiv").hide();
			$("#btn_mtp_editDiv").hide();
			$("#btn_mtp_unavailableDiv").hide();
		}*/
		
		//安全事项
		var preHazardDataStr = mtpFullData.preHazardData;
		var preHazardDataObj = eval("(" + preHazardDataStr + ")");
		if(preHazardDataObj.length!=0){
			$("#preHazardTable").datagrid("loadData", preHazardDataObj );
			$("#preHazardTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#preHazardBtnDiv").hide();
		}else{
			$("#title_preHazard").iFold("hide");
		}
		//物料
		var toolDataStr = mtpFullData.toolData;
		var toolDataObj = eval("(" + toolDataStr + ")");
		if(toolDataObj.length!=0){
			$("#toolTable").datagrid("loadData",toolDataObj );
			$("#toolTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#toolBtnDiv").hide();
		}else{
			$("#title_tool").iFold("hide");
		}
		
		//内容
		var taskDataStr = mtpFullData.taskData;
		var taskDataObj = eval("(" + taskDataStr + ")");
		if(taskDataObj.length!=0){
			$("#taskTable").datagrid("loadData", taskDataObj);
			$("#taskTable").datagrid("hideColumn","oper"); //隐藏某一列
			$("#taskBtnDiv").hide();
		}else{
			$("#title_task").iFold("hide");
		}
		
		
		
	}else{  //新建维护计划
		$("#btn_mtp_editDiv").hide();
		$("#btn_mtp_unavailableDiv").hide();
		$("#btn_mtp_toWoDiv").hide();
	}
	
	FW.fixRoundButtons("#toolbar");
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

	/* 提交维护计划（数据入库，关闭页面，刷新计划列表）*/
function commitMTP(){
	/**表单验证*/
	var tempEquiName = $("#maintainPlanForm").iForm("getVal","equipName");
	if(tempEquiName == '' || tempEquiName == "请从左边设备树选择"){
		FW.error("请从左边设备树选择");
		return;
	}
	if(!$("#maintainPlanForm").valid()){
		return ;
	}
	/**datagrid验证*/
	if(!$("#toolTableForm").valid()){
		return ;
	}
	 
	endEdit("preHazardTable");
	endEdit("toolTable");
	endEdit("taskTable");
	 
	var mtpFormData = $("#maintainPlanForm").iForm("getVal");  //取表单值
	
	var currStartTime = mtpFormData.currStartTime;  //第一次周期开始时间（毫秒）
	/*var woCyc = mtpFormData.maintainPlanCycle*24*60*60*1000;  //周期时长（毫秒）
	var alertLength = mtpFormData.alertTime *60*60*1000;  //预警时长（毫秒）
	mtpFormData.newToDoTime = currStartTime + woCyc - alertLength ;  //下次生成待办时间
*/

	var preHazardData =$("#preHazardTable").datagrid("getData");
	var toolData = $("#toolTable").datagrid("getData");
	var taskData = $("#taskTable").datagrid("getData");
	 
	$("#preHazardTable").datagrid("clearChecked");
	$("#toolTable").datagrid("clearChecked");
	$("#taskTable").datagrid("clearChecked");
	
	var maintainPlanId = mtpFormData.id;
	if(maintainPlanId==""){  //如果是新建作业方案的保存，则暂时设置jobPlanId = 0;
		maintainPlanId = 0;
	};
	
	 $.post(basePath + "workorder/maintainPlan/commitMaintainPlandata.do",
	 		{"maintainPlanForm":JSON.stringify(mtpFormData),"preHazardData":JSON.stringify(preHazardData),
		 	 "toolData":JSON.stringify(toolData),"taskData":JSON.stringify(taskData),
		 	/* "workerData":JSON.stringify(workerData),*/"maintainPlanId":maintainPlanId},
			function(data){
				if(data.result == "success"){
					$("#preHazardBtnDiv").hide();
					$("#toolBtnDiv").hide();
					$("#taskBtnDiv").hide();
					$("#btn_mtp_saveDiv").hide();
					$("#toolTable").datagrid("hideColumn","oper"); //隐藏某一列
					$("#taskTable").datagrid("hideColumn","oper"); //隐藏某一列
					$("#preHazardTable").datagrid("hideColumn","oper"); //隐藏某一列
					FW.success("保存成功")
					closeCurPage();
				}else {
					FW.error("保存失败")
				}
	  },"json");
	  
	$("#maintainPlanForm").iForm("endEdit");  //关闭编辑，
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("eqMTPlistDoNotRefresh",true);
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
/* 编辑维护计划 （按钮）*/
function editMTP(){
	formRead = false ;  //编辑模式
	$("#maintainPlanForm").iForm("beginEdit");  //打开编辑

	$("#title_preHazard").iFold("show");
	$("#title_tool").iFold("show");
	$("#title_task").iFold("show");
	
	//判断按钮该显示的文字
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
	
	$("#btn_mtp_saveDiv").show();
	$("#btn_mtp_editDiv").hide();
	$("#btn_mtp_toWoDiv").hide();
	$("#btn_mtp_unavailableDiv").hide();
}

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
				row["applyCount"] = 1 ;
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
function unavailableMTP(){
	Notice.confirm("确定禁用|确定禁用该维护计划么？",function(){
		var mtpFormData = $("#maintainPlanForm").iForm("getVal");  //取表单值
		var maintainPlanId = mtpFormData.id;
		
		$.post(basePath + "workorder/maintainPlan/unavailableMTP.do",
		 		{"maintainPlanId":maintainPlanId},
				function(data){
					if(data.result == "success"){
						$("#btn_mtp_unavailableDiv").hide();
						$("#btn_mtp_editDiv").hide();
						$("#btn_mtp_toWoDiv").hide();
						FW.success("禁用成功");
						closeCurPage();
					}else {
						FW.error("禁用失败")
					}
		  },"json");
	},null,"info");	
}

function mtpToWoBtn(){
	if(mtpType=="cycle_maintainPlan"){  //周期性维护计划直接生成工单（流程直接走到工作策划那一步）
		Notice.confirm("确定生成工单|确定生成工单么？该操作无法撤销。",function(){
			$.post(basePath + "workorder/workorder/cycMtpToWo.do",
			 		{"mtpId":mtpId,"todoId":todoId},
					function(data){
						if(data.result == "success"){
							FW.success("新建成功");
							closeCurPage();
						}else {
							FW.error("新建失败")
						}
			  },"json");
		},null,"info");	
	}else{
		   var opts = {
		       /* id : "newWO" + rand,*/
		        id : "newWO" + mtpId,
		        name : "新建工单",
		        url : basePath+ "workorder/workorder/openNewWOPage.do?mtpId="+mtpId+"&mtpType="+mtpType,
		        tabOpt : {
		        	closeable : true,
		        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();"
		        }
		    };
		    _parent()._ITC.addTabWithTree(opts); 
	}
	
	
}
