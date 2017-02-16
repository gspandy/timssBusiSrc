/*
 * serType:{"equipmentBorrow","faultMaintenance","businessApply"}
 * businessType:{"A","B"}
 * handlerType:{"endWork","reSend"}
 */

/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "名称", id : "name",rules : {required:true,maxChLength:50},
		    	linebreak:true,
		    	wrapXsWidth:12,
		        wrapMdWidth:8
		    },
		    {title:"工单编号",id:"infoWoCode",type:"label", linebreak:true},
		    {title : "工号", id : "applyUser", type : "hidden"},
		    {title:"申请人",id:"applyUserName",type:"label"},
			{title : "申请人部门", id : "applyDeptName", type : "hidden"},
			{title : "申请时间", id : "applyTime", type : "hidden"},
			{title : "申请人电话", id : "applyUserPhone", rules : {required:true,maxChLength:20}},	
		    {title : "服务目录Id", id : "serCata",type : "hidden", linebreak:true},
		    {title : "服务目录",id : "serCataName",type:"label",value:"请从左边目录树选择服务目录",
  				rules : {required:true},
  				formatter:function(val){
  					var text = val;
					if(text=="请从左边目录树选择服务目录"){
						text = "<label style='color:red'>"+val+"</label>";
					}
					return text;
  				}
  			},
		    {title : "维护类型", id : "serType",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_INFOWO_SERTYPE",
				options:{
					initOnChange: false,
					allowEmpty:true,
					onChange:function(val){
						if(val == itsmInfoWoSerType.BUSINESS){  //业务申请
							$("#infoWoForm").iForm("show",["businessType"]);
						}if(val == itsmInfoWoSerType.FAULT){   //故障报修
							$("#infoWoForm").iForm("hide",["businessType"]);
						}else if(val == itsmInfoWoSerType.BORROW){ //设备借出
							$("#infoWoForm").iForm("hide",["businessType"]);
						}
						
					}
				}
		    },
		    {title : "业务类型", id : "businessType",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_INFOWO_BUSINESSTYPE",
				options:{allowEmpty:true}
		    },
		    {title : "状态", id : "status",
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_INFOWO_STATUS"
		    },
			{title : "下一环节处理方式", id : "handlerType",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_INFOWO_HANDLERTYPE",
				options:{allowEmpty:true}
		    },
		    {
		        title : "问题描述", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {required:true,maxChLength:680}
		    }
		];
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}

function initFormData(infoWoId){
	 $.post(basePath + "itsm/infoWo/queryInfoWoById.do",{"id":infoWoId},
				function(data){
			var infoWoFormData = JSON.parse(data.infoWoForm);
			infoWoFormData.applyUserName = infoWoFormData.applyUserName+" / "+infoWoFormData.applyDeptName;
			var attachmentData = data.attachmentMap ;
			status = infoWoFormData.status;
			var serType = infoWoFormData.serType;
			processInstId = infoWoFormData.workflowId;
			taskId = data.taskId;
			createtime =  FW.long2time(infoWoFormData.createDate);
			updatetime =  FW.long2time(infoWoFormData.modifyDate);
			var currHandler = infoWoFormData.currHandler;
			//是否有权限审批
			var flag = false;
			if(currHandler != null && currHandler != ""){
				flag = isMyActivityWO(currHandler.split(','),loginUserId);
				if(flag){
					auditInfoShowBtn = 1;
				}
			}
			
			$("#infoWoForm").iForm("setVal",infoWoFormData);
			$("#infoWoForm").iForm("endEdit");

			var infoWoEquipmentDatagridField = infoWoEquipmentField;
			if(status=="itCenterConfirm"||status=="end"){
				infoWoEquipmentDatagridField= infoWoEquipmentField2;				
			}
			$("#title_infoWoEquipment").iFold("init");
			var woPriConfDatagrid =  $("#infoWoEquipmentTable").datagrid({
				    columns:infoWoEquipmentDatagridField,
				    idField:'infoWoEquipmentDeleteId',
				    singleSelect:true,
				    fitColumns:true,
				    scrollbarSize:0
				}); 
			
			var equipmentListObj = JSON.parse( data.equipmentList );
			if(equipmentListObj != null && equipmentListObj.length!=0){
				$("#infoWoEquipmentTable").datagrid("loadData",equipmentListObj );
				$("#infoWoEquipmentTable").datagrid("hideColumn","oper"); //隐藏某一列
				$("#btn_infoWoEquipmentTable").hide();
				if(status == "itCenterLend"){
					$("#infoWoEquipmentTable").datagrid("showColumn","oper"); //隐藏某一列
					$("#infoWoEquipmentTable").datagrid("hideColumn","returnStatus");
					$("#btn_infoWoEquipmentTable").show();
					beginEdit("infoWoEquipmentTable");
				}else if(status == "applicantReturn"){  //申请人归还
					 $("#infoWoEquipmentTable").datagrid("hideColumn","returnStatus");
				}else if(status == "itCenterConfirm"){  //信息中心确认
					if(flag){
						beginEdit("infoWoEquipmentTable");
					}else{
						$("#infoWoEquipmentTable").datagrid("hideColumn","returnStatus");
					}
				}
			}else{
				if(status == "itCenterLend"){ //信息中心借出
					$("#infoWoEquipmentTable").datagrid("hideColumn","returnStatus"); //隐藏某一列
					if(!flag){  //不是自己审批
						$("#title_infoWoEquipment").hide();
					}
				}else{
					$("#title_infoWoEquipment").iFold("hide");
				}
			}
			
			if(status=="itEngineerHandler"){
				$("#infoWoForm").iForm("beginEdit",["handlerType"]);
			}else{
				$("#infoWoForm").iForm("hide",["handlerType"]);
			}
			if(attachmentData.length > 0){
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
			}
			
			//如果单不在自己手上，则关闭编辑
			if(!flag){  
				$("#btn_audit").hide();
				$("#infoWoForm").iForm("endEdit");
				$("#uploadform").iForm("endEdit");  //控制附件的权限（只能下载）
				
				$("#btn_showPrincipal").hide();
			}else{
				if(status != "itCenterSend"){
					$("#btn_showPrincipal").hide();
				}
			}
			if(attachmentData.length == 0){
				$("#uploadfileTitle").iFold("hide"); 
			}
			$("#uploadform").iForm("endEdit"); 
			FW.fixRoundButtons("#toolbar");
		},"json");
	
}

function audit(){  //审批
	/**表单验证*/
	if(!$("#infoWoForm").valid()){
		return ;
	}
	var infoWoFormObj = $("#infoWoForm").iForm("getVal");
	var handlerType = infoWoFormObj.handlerType;
	endEdit("infoWoEquipmentTable");
	var equipmentObj = $("#infoWoEquipmentTable").datagrid("getData");
	if(status == "itCenterLend" && equipmentObj.total==0){
		FW.error("请添加借出的物资")
		return ;
	}
	if(status == "itCenterLend"){
		beginEdit("infoWoEquipmentTable");
	}
	if(!$("#mydatagridform").valid()){
		return ;
	}
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = processInstId;
	params['businessId'] = infoWoId;
	var variables = [{'name':'handlerType','value':handlerType}]; 
	params['variables'] = FW.stringify(variables);

	
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var infoWoData = {"infoWoForm":FW.stringify(infoWoFormObj),
							  "attachmentIds":attachmentIds,
							  "equipmentData":FW.stringify(equipmentObj),
							  "handlerType":handlerType};
			var workFlow = new WorkFlow();
			var updateDesc = "";
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(infoWoData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}

/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	
	closeTab();
};
function rollback(){
	closeTab();
}
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
function stop(rowdata){
    var data={};
    data['processInstId'] = rowdata.processInstId;
    data['reason'] = rowdata.reason;
    data['businessId'] = knowledgeId;
    
    var url = 'itsm/workorder/stopWorkOrder.do';
    $.post(url, data, function(data){
        if(data.result=='success'){
            FW.success("提交成功");
            _parent().$("#itcDlg").dialog("close");
            closeCurPage();
            homepageService.refresh();
        }
        else{
        	 FW.error("提交失败");
        }
    });
}


var infoWoEquipmentDeleteId = 1;
var	infoWoEquipmentField = [[
		{field : 'infoWoEquipmentDeleteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.infoWoEquipmentDeleteId =infoWoEquipmentDeleteId;
				return infoWoEquipmentDeleteId++;
			}
		},  
   	    {field : 'id',title : 'ID', hidden:true},  
		{field : 'name',title : '物资名称（型号）',width:200,edit:true,
   	    	editor:{
   	    		type:'text',
   	    		options:{
					rules : {
						 "required": true
					}
				}
   	    	},fixed:true}, 
		{field : 'num',title : '数量',width:60,edit:true,
			editor:{				
					type:'text',
					options:{
						align:"right",
						dataType:"number",
						rules : {
							 "number" : true,
							 "required": true
						},
					    messages : {
							 "number" : "请输入有效数字"
					    }
					}
				},fixed:true},
		{field : 'lendStatus',title : '借出状态',width:200,edit:true,
					editor:{type:'text'}
		},
		{field : 'returnStatus',title : '归还状态',width:200,edit:true,editor:{type:'text'}},
		{field : 'oper',title : '',width:30,fixed:true,align:'center', 
			 formatter:function(value,row,index){
				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
				     		row.infoWoEquipmentDeleteId+",'"+row.id+"'"+')" width="16" height="16" >';
			}
		}
	]];
var	infoWoEquipmentField2 = [[
						{field : 'infoWoEquipmentDeleteId',title : '临时ID用来指定删除row', hidden:true,
							formatter:function(value,row,index){
								row.infoWoEquipmentDeleteId =infoWoEquipmentDeleteId;
								return infoWoEquipmentDeleteId++;
							}
						}, 
   	                 	{field : 'id',title : 'ID', hidden:true},  
   	              		{field : 'name',title : '物资名称（型号）',width:200,fixed:true}, 
   	              		{field : 'num',title : '数量',width:60,fixed:true},
   	              		{field : 'lendStatus',title : '借出状态',width:200},
   	              		{field : 'returnStatus',title : '归还状态',width:200,edit:true,editor:{type:'text'}},
		   	            {field : 'oper',title : '',width:30,fixed:true,align:'center', 
		   	    			 formatter:function(value,row,index){
		   	    				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
		   	    				     		row.infoWoEquipmentDeleteId+",'"+row.id+"'"+')" width="16" height="16" >';
		   	    			}
		   	    		}
   	              	]];
/**
 * 在datagrid的末尾添加一行数据
 * @param dataGridId  dataGrid的ID
 */
function appendInfoWoEquipment(){
		var title = $("#btn_infoWoEquipmentTable").html();
		var fdStart = title.indexOf("继续");  
		if(fdStart!=0){
	    	$("#btn_infoWoEquipmentTable").html("继续"+title);
	    }
	    
	 	$("#infoWoEquipmentTable").datagrid("appendRow",{});
	    editIndex = $("#infoWoEquipmentTable").datagrid("getRows").length-1;
	    $("#infoWoEquipmentTable").datagrid("selectRow", editIndex)
	                .datagrid('beginEdit', editIndex);
	}
/**
 * 删除datagrid的一行数据
 * @param deleteId  dataGrid的行ID
 */
function deleteGridRow(deleteId,equipmentId){
//	var deleteId = row.infoWoEquipmentDeleteId;
//	var equipmentId = row.id;
	if(equipmentId!=null &&equipmentId!="" && equipmentId!="undefined"){
		FW.error("此设备不能删除");
		return ;
	}else{
		 $("#infoWoEquipmentTable").datagrid('deleteRow', $("#infoWoEquipmentTable").datagrid('getRowIndex',deleteId));
		 var rowsLength = $("#infoWoEquipmentTable").datagrid('getRows').length;
		 if(rowsLength == 0){
			 var oldBtnName =  $("#infoWoEquipmentTable").html().substring(2);
			 $("#infoWoEquipmentTable").html(oldBtnName);
		 }
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