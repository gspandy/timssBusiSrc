/* 表单字段定义  */
var fields = [{title : "ID", id : "id",type : "hidden"},
		    {title : "工单编号", id : "workOrderCode",type : "hidden"},
		    {title : "名称", id : "description", rules : {required:true}},
		    {title : "维护计划ID", id : "maintainPlanId", type : "hidden"},
		    {title : "维护计划来源",id : "maintainPlanFrom", type : "hidden"},
		    {title : "作业方案ID", id : "jobPlanId", type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
			{title : "状态", id : "currStatus",
				type : "combobox",
				dataType : "enum",
				enumCat : "WO_STATUS"
			},
		    {title : "遗留问题Id", id : "nextWoMtp", type : "hidden"},
		    {title : "工单类型", id : "workOrderTypeCode",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "WO_TYPE"
			},
		    {title : "缺陷程度", id : "faultDegreeCode",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "WO_FAULT_DEGREE"
			},
			 {title : "专业", id : "woSpecCode",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "WO_SPEC"
			},
		    {title : "报障设备", id : "equipName", rules : {required:true}},
		    {title : "设备ID", id : "equipId",type : "hidden"},
		    {title : "发现日期", id : "discoverTime", type:"datetime", dataType:"datetime" },
		    {title : "父工单ID", id : "parentWOId",type:"hidden"},
		    {title : "父工单", id : "parentWOCode",
		    	render:function(id){
	            	 var ipt = $("#" + id);
	            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
	            	 ipt.ITCUI_Input();
	            	 ipt.next(".itcui_input_icon").on("click",function(){
	                     var src = basePath + "/page/workorder/core/workOrderList.jsp";                  
	                     var dlgOpts = {
	                         width : 400,
	                         height:350,
	                         closed : false,
	                         title:"双击选择父工单",
	                         modal:true
	                     };
	                     Notice.dialog(src,dlgOpts,null);
	                 });
	             }
	        },
			 {title : "紧急程度", id : "urgentDegreeCode",
				type : "combobox",
				dataType : "enum",
				enumCat : "WO_URGENCY_DEGREE"
			},
		    {title : "备注", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:48
		    }	    
		];
var woPlanFields =[{title : "是否走工作票",
					id : "isToWorkTicket",
					type : "combobox",
					rules : {required:true},
					dataType : "enum",
					enumCat : "WO_ISTOPTW",
				    options:{
				    	allowEmpty : true,
				    	onChange:function(val,obj){ 
				    		if(val == "toPTW"){
				    			$("#title_tool").iFold("show");
				    			$("#toolTable").datagrid("resize");
				    			$("#title_preHazard").iFold("hide");
				    			$("#title_task").iFold("hide");
				    			$("#title_worker").iFold("hide");
				    			$("#btn_wo_selectJobPlanDiv").hide();
				    			$("#btn_wo_save2").hide(); //走工作票不让“暂存”
				    		}else if(val == "noPTW"){
				    			$("#woPlanDiv").show();
				    			$("#title_preHazard").iFold("show");
				    			$("#title_task").iFold("show");
				    			$("#title_tool").iFold("show");
				    			$("#title_worker").iFold("show");
				    			$("#preHazardTable").datagrid("resize");
				    			$("#toolTable").datagrid("resize");
				    			$("#taskTable").datagrid("resize");
				    			$("#workerTable").datagrid("resize");
				    			$("#btn_wo_selectJobPlanDiv").show();
				    		}
				    	}
					}
				}];
var endWOReportFields = [ 
                          {title : "实际开工时间", id : "beginTime", type:"datetime", dataType:"datetime",
                        	  rules : {required:true} 
                          },
                		  {title : "实际完工时间", id : "endTime", type:"datetime", dataType:"datetime",
                			  rules : {required:true,greaterThan:"#f_beginTime"}
                          },
                		  {title : "遗留问题", 
                			  id : "isHasRemainFault", 
                			  type:"combobox", 
                			  rules : {required:true},
                			  dataType : "enum",
	          				  enumCat : "WO_HAS_REMAINFAULT",
	          				  options:{
	          				    	allowEmpty : true,
	          				    	onChange:function(val,obj){ 
	          				    		if(val == "has_remainFault"  && woStatus=="endWOReport"){
		          			    			showRemainFault();  //弹出遗留问题框
		          			    		}
	          				    	}
	          					}
                		  },
                		  {title : "完工情况汇报", 
                		        id : "endReport",
                		        type : "textarea",
                		        linebreak:true,
                		        wrapXsWidth:12,
                		        wrapMdWidth:8,
                		        height:55,
                		        rules : {maxChLength:680}
                		    }
                		 ];
var woAcceptanceFields = [{title : "批准停机时间(H)", id : "approveStopTime",rules : {required:true,digits:true} },
              			  {title : "电量损失(KWH)", id : "loseElectricPower",rules : {required:true,digits:true} }];

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
			{field : 'itemsCode',title : '物资编码',width:100,fixed:true},
			{field : 'itemsName',title : '名称', width:200},
			{field : 'itemsModel',title : '规格型号',width:200,fixed:true},
			{field : 'warehouseid',hidden:true},
			{field : 'cateId',title : '物资类型ID',hidden:true},
			/*{field : 'bin',title : '货柜',width:100,fixed:true},
			{field : 'warehouse',title : '仓库',width:100,fixed:true},*/
			{field : 'applyCount',title : '申请数量',edit:true,width:60,fixed:true,
				editor:{type:"text","options":{"rules" : {required:true,"number" : true},align:"right"}}},
			{field : 'getCount',title : '领取数量',edit:true,width:60,fixed:true},
			/*{field : 'usedCount',title : '使用数量',edit:true,width:60,fixed:true,
				editor:{type:"text","rules" : {required:true,"digits" : true},
					"options":{align:"right"}}},*/
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

var workerDelteId = 1;    				    
var	workerGridField =[[
		{field : 'workerDelteId',title : '临时ID用来指定删除row', hidden:true,
			formatter:function(value,row,index){
				row.workerDelteId =workerDelteId;
				return workerDelteId++;
			}
		},
        {field : 'id',title : 'ID', editor:{type:"textarea"}, hidden:true},     
  		{field : 'proj',title : '大类', editor:{type:"textarea"}, width:160},
  		{field : 'managerInfo',title : '负责人', editor:{type:"textarea"}, width:100,fixed:true},
  		{field : 'workerList',title : '作业人员', editor:{type:"textarea"}, width:160},
  		{field : 'remarks',title : '备注', editor:{type:"textarea"}, width:160},
  		{field : 'oper',title : '',width:55,fixed:true,align:'center', 
  			 formatter:function(value,row,index){
  				     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
  				     		"'workerTable',"+row.workerDelteId+')" width="16" height="16" >';
  			}
  		}
  	]];	

		
function initWoPlanPage(woId){
	$("#woBaseForm").iForm("init",{"fields":fields,options:{initAsReadonly:true}});
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
	var workerDatagrid =  $("#workerTable").datagrid({
			    columns:workerGridField,
			    idField:'workerDelteId',
			    singleSelect:true,
			    fitColumns:true,
			    scrollbarSize:0
		});  
	$("#title_preHazard").iFold("init");
	$("#title_tool").iFold("init");
	$("#title_task").iFold("init");
	$("#title_worker").iFold("init");
	 
	$("#btn_wo_operPtwDiv").hide();
	$("#btn_wo_PrintDiv").hide();
	
	setWOPlanVal(woId); //给页面赋值
	
}

function setWOPlanVal(woId){
	$.post(basePath + "workorder/workorder/queryWODataById.do",{workOrderId:woId},
			function(woFullData){
				var woFormData = eval("(" +woFullData.workOrderForm+ ")");
				
				ptwId = woFormData.ptwId ; //赋值工作票ID
				woStatus = woFormData.currStatus;
				var isToPtw = woFormData.isToWorkTicket;//是否走工作票
				var woType = woFormData.workOrderTypeCode;
				createtime = FW.long2time(woFormData.createDate);
				updatetime = FW.long2time(woFormData.modifyDate);
				//给四个datagrid赋值
				setAllDatagrid(woFullData); 
				//根据当前状态控制页面的显示
				controlPageByWoStatus(woStatus); 
				 
				$("#woBaseForm").iForm("setVal",woFormData);
				
				if(woStatus == "endWOReport"){
					$("#endWOReportForm").iForm("setVal",woFormData);
				}
				
				//与工作票相关的页面控制
				if(woStatus == "inPTWing"){
					$("#title_preHazard").iFold("hide");
					$("#title_task").iFold("hide");
					$("#title_worker").iFold("hide");
					$("#btn_wo_operDiv").hide();
					if(ptwId==0){
						$("#btn_wo_operPtwDiv").show();
						$("#btn_wo_backWoPlan").show();
						$("#btn_wo_queryPtw").hide();
					}else{
						$("#btn_wo_operPtwDiv").show();
						$("#btn_wo_backWoPlan").hide();
						$("#btn_wo_newPtw").hide();
					}
					FW.fixRoundButtons("#toolbar");
				}else{
					if(ptwId!=0){
						$("#btn_wo_operPtwDiv").show();
						$("#btn_wo_backWoPlan").hide();
						$("#btn_wo_newPtw").hide();
					}
				}
				
				//工作流中数据信息
				processInstId = woFormData.workflowId;
				taskId = woFullData.taskId;
				candidateUsers = woFullData.candidateUsers;
				var flag = isMyActivityWO(candidateUsers,loginUserId);

				if(!flag){  //若工单此时不在自己手上
					$("#btn_wo_operDiv").remove();
					$("#btn_wo_selectJobPlanDiv").hide();
					switch(woStatus){
						case "woPlan": $("#woPlanForm").hide();break;
						case "inPTWing":break;
						case "endWOReport":$("#endWOReportForm").hide(); 
							$("#endWOReportForm").iForm("setVal",woFormData).iForm('endEdit');
							break;
						case "woAcceptance":$("#woAcceptanceForm").hide(); 
							$("#endWOReportForm").iForm("setVal",woFormData).iForm('endEdit');
							break;
						default:
							$("#endWOReportForm").iForm("setVal",woFormData).iForm('endEdit');
							$("#woAcceptanceForm").iForm("setVal",woFormData).iForm('endEdit');
					}	
					setAllDatagrid(woFullData);
				    
					$("#preHazardTable").datagrid("hideColumn","oper"); //隐藏某一列
					$("#toolTable").datagrid("hideColumn","oper"); 
					$("#taskTable").datagrid("hideColumn","oper"); 
					$("#workerTable").datagrid("hideColumn","oper"); 
					$("#preHazardBtnDiv").hide();
					$("#toolBtnDiv").hide();
					$("#taskBtnDiv").hide();
					$("#workerBtnDiv").hide();
					
				}else{  //工单在自己手上
					if(woStatus=="inPTWing"){  //在工作票流程中的时候，流程信息里面不能有审批按钮
						auditInfoShowBtn = 0 ;
					}
					if(woStatus=="woPlan" ||woStatus=="endWOReport"){  //策划或汇报阶段
						$("#title_preHazard").iFold("show");
						$("#title_tool").iFold("show");
						$("#title_task").iFold("show");
						$("#title_worker").iFold("show");
						beginEdit("preHazardTable"); $("#preHazardBtnDiv").show();
						beginEdit("toolTable");$("#toolBtnDiv").show();
						beginEdit("taskTable");  $("#taskBtnDiv").show();
						beginEdit("workerTable");  $("#workerBtnDiv").show();

						if(woStatus=="endWOReport"){
							$("#preHazardBtnDiv").hide();
							$("#toolBtnDiv").hide();
							endEdit("preHazardTable");
							endEdit("toolTable");
							$("#preHazardTable").datagrid("hideColumn","oper"); //隐藏某一列
							var preHazardDataStr = woFullData.preHazardData;
							var preHazardDataObj = eval("(" + preHazardDataStr + ")");
							var toolDataStr = woFullData.toolData;
							var toolDataObj = eval("(" + toolDataStr + ")");
							if(preHazardDataObj.length==0){
								$("#title_preHazard").iFold("hide");
							}
							if(toolDataObj.length==0){
								$("#title_tool").iFold("hide");
							}
							if(woType == "hbWoType"){
//								$("#toolBtnDiv").show();  //汇报型工单也需要物料
								$("#title_preHazard").iFold("hide");
								$("#title_tool").iFold("hide");
							}
						}
						if(woStatus=="woPlan"){
							$("#woPlanForm").iForm("setVal",{'isToWorkTicket':isToPtw});
							if(isToPtw == "toPTW" || isToPtw==null){   //走工作票(其他三项没有内容)
								$("#woPlanForm").iForm("setVal",{'isToWorkTicket':"toPTW"});
								$("#title_preHazard").iFold("hide");
								$("#title_task").iFold("hide");
								$("#title_worker").iFold("hide");
							}
						}
					}else{
						$("#btn_wo_save2").remove();
						$("#btn_wo_selectJobPlanDiv").remove();
						$("#preHazardTable").datagrid("hideColumn","oper");
						$("#toolTable").datagrid("hideColumn","oper");
						$("#taskTable").datagrid("hideColumn","oper");
						$("#workerTable").datagrid("hideColumn","oper"); //隐藏某一列
						$("#toolBtnDiv").hide();
						$("#preHazardBtnDiv").hide();
						$("#taskBtnDiv").hide();
						$("#workerBtnDiv").hide();
					}
				}
				if(isToPtw =="toPTW"){
					$("#title_preHazard").iFold("hide");
					$("#title_task").iFold("hide");
					$("#title_worker").iFold("hide");
				}
				
				fromShowControl(woStatus,woFormData);
				FW.fixRoundButtons("#toolbar");	
			},"json");
}	
function controlPageByWoStatus(woStatus){  //根据状态控制页面的显示
	switch(woStatus){
		case "woPlan":$("#woPlanForm").iForm("init",{"fields":woPlanFields,"options":{validate:true}});
					  $("#btn_wo_selectJobPlanDiv").hide();
					  $("#title_preHazard").iFold("hide");
					  $("#title_tool").iFold("hide");
					  $("#title_task").iFold("hide");
					  $("#title_worker").iFold("hide");
					  $("#toolTable").datagrid("hideColumn","getCount");
					  break;
		case "inPTWing":auditInfoShowBtn = 0 ;//控制流程信息弹出框中是否显示“审批”按钮
						break;
		case "endWOReport": $("#endWOReportForm").iForm("init",{"fields":endWOReportFields,"options":{validate:true}});
		 					$("#preHazardBtnDiv").hide();
		 					$("#toolBtnDiv").hide();
		 					$("#taskBtnDiv").hide();
		 					$("#workerBtnDiv").hide();
		 					$("#btn_wo_selectJobPlanDiv").hide();
		 					$("#toolTable").datagrid("hideColumn","oper");break;
		case "woAcceptance": $("#endWOReportForm").iForm("init",{"fields":endWOReportFields});
							 $("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields,"options":{validate:true}});
							break;
		case "woObsolete":;
		case "woFiling": $("#endWOReportForm").iForm("init",{"fields":endWOReportFields});
						 $("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields,"options":{validate:true}});
				         $("#btn_wo_PrintDiv").show();
				         auditInfoShowBtn = 0 ;//控制流程信息弹出框中是否显示“审批”按钮
				         break;
		
	}	
}
function fromShowControl(woStatus,woFormData){
	switch(woStatus){
		case "woAcceptance": $("#endWOReportForm").iForm("init",{"fields":endWOReportFields});
							 $("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields,"options":{validate:true}});
							 $("#endWOReportForm").iForm("setVal",woFormData).iForm('endEdit');
							 $("#woAcceptanceForm").iForm("setVal",{'approveStopTime':'0'});
							 break;
		case "woFiling": $("#endWOReportForm").iForm("init",{"fields":endWOReportFields});
						 $("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields});
						 $("#endWOReportForm").iForm("setVal",woFormData).iForm('endEdit');
						 $("#woAcceptanceForm").iForm("setVal",woFormData).iForm('endEdit');
				         $("#btn_wo_PrintDiv").show();
				         break;
	}	
}
/**给页面中下部的datagrid赋值
 * @param woFullData
 */
function setAllDatagrid(woFullData){
	//安全事项
	var preHazardDataStr = woFullData.preHazardData;
	var preHazardDataObj = eval("(" + preHazardDataStr + ")");
	//物料
	var toolDataStr = woFullData.toolData;
	var toolDataObj = eval("(" + toolDataStr + ")");
	//内容
	var taskDataStr = woFullData.taskData;
	var taskDataObj = eval("(" + taskDataStr + ")");
	//人员
	var workerDataStr = woFullData.workerData;
	var workerDataObj = eval("(" + workerDataStr + ")");
	$("#preHazardTable").datagrid("resize")
	$("#toolTable").datagrid("resize");
	$("#taskTable").datagrid("resize");
	$("#workerTable").datagrid("resize");
	if(preHazardDataObj.length!=0){
		$("#preHazardTable").datagrid("loadData",preHazardDataObj);
		$("#btn_preHazardTable").html("继续添加注意事项");
	}else{$("#title_preHazard").iFold("hide");}
	if(toolDataObj.length!=0){
		$("#toolTable").datagrid("loadData",toolDataObj);
		$("#btn_toolTable").html("继续添加物料");
	}else{$("#title_tool").iFold("hide");}
	if(taskDataObj.length!=0){
		$("#taskTable").datagrid("loadData",taskDataObj);
		$("#btn_taskTable").html("继续添加工作内容");
	}else{$("#title_task").iFold("hide");}
	if(workerDataObj.length!=0){
		$("#workerTable").datagrid("loadData",workerDataObj);
		$("#btn_workerTable").html("继续添加人员");
	}else{$("#title_worker").iFold("hide");}
	
}
/**  判断myId 是否在候选人 candidateUsers 中
 * @param candidateUsers
 * @param myId
 */
function isMyActivityWO(candidateUsers,myId){
	for(var i=0; i<candidateUsers.length; i++){
		if(candidateUsers[i] == myId ){
			auditInfoShowBtn = 1 ;
			return true;
		}
	}
	return false;
}

function selectStandJP(){
	 var src = basePath + "workorder/workorder/woPlanStandJPList.do"; 
     var dlgOpts = {
         width : 600,
         height:500,
         closed : false,
         title:"双击选择标准作业方案",
         modal:true
     };
     Notice.dialog(src,dlgOpts,null);
}
/**
 * 在datagrid的末尾添加一行数据
 * @param dataGridId  dataGrid的ID
 */
function appendDataGridRow(dataGridId){
	var title = $("#btn_"+dataGridId).html();
	var fdStart = title.indexOf("继续");  
	if(fdStart!=0){
    	$("#btn_"+dataGridId).html("继续"+title);
    }
    
 	$("#"+dataGridId).datagrid("appendRow",{});
    editIndex = $("#"+dataGridId).datagrid("getRows").length-1;
    $("#"+dataGridId).datagrid("selectRow", editIndex)
                .datagrid('beginEdit', editIndex);
    
    
}
function appendTool(){
	$("#toolTable").datagrid("resize");
	//调用库存物资树方法
	FW.showInventoryDialog({invmatapply:1,
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
				row["warehouseid"] = data[i]["warehouseid"];
				row["warehouse"] = data[i]["warehouse"];
				row["cateId"] = data[i]["cateId"];
				row["applyCount"] = 1 ;  //默认添加一个
				$("#toolTable").datagrid("appendRow",row );
			}
			beginEdit("toolTable");
		}
	});
	$("#btn_toolTable").html("继续添加物料");
} 
/** 删除dataGrid中的某一行数据
 * @param dataGridId
 * @param index
 */
function deleteGridRow(dataGridId,deleteId){
	 $('#'+dataGridId).datagrid('deleteRow', $('#'+dataGridId).datagrid('getRowIndex',deleteId));
	 var rowsLength = $("#"+dataGridId).datagrid('getRows').length;
	 if(rowsLength == 0){
		 var oldBtnName =  $("#btn_"+dataGridId).html().substring(2);
		 $("#btn_"+dataGridId).html(oldBtnName);
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


function audit(){  //审批
	var updateDesc = "";  //审批意见（在流程信息列表中显示）
	//工单基本信息
	var woFormObj = $("#woBaseForm").iForm("getVal"); //取表单值

	var jobPlanId = woFormObj.jobPlanId;
	if(jobPlanId== null){
		jobPlanId=0;
	}
	woStatus = woFormObj.currStatus;
//表单验证
	if(woStatus == "woPlan"){
		if(!$("#woPlanForm").valid() || !$("#toolTableForm").valid() ){
			return ;
		}
		updateDesc = "工单策划审批";
	}
	if(woStatus == "endWOReport"){
		if(!$("#endWOReportForm").valid() || !$("#toolTableForm").valid()){
			return ;
		}
		updateDesc = "工单汇报审批";
	}
	if(woStatus == "woAcceptance"){
		if(!$("#woAcceptanceForm").valid()){
			return ;
		}
		updateDesc = "工单验收审批";
	}
 	
	//策划阶段输入内容
	var woPlanFormObj = $("#woPlanForm").iForm("getVal");
	var isToPTW = woPlanFormObj.isToWorkTicket;
	if(isToPTW == undefined){
		isToPTW = null;
	}
	//汇报阶段输入内容
	var woReportFormObj = $("#endWOReportForm").iForm("getVal");
	var isHasRemainFault = woReportFormObj.isHasRemainFault;
	if(isHasRemainFault == undefined){
		isHasRemainFault = null;
	}
	//验收阶段输入内容
	var woAcceptanceFormObj = $("#woAcceptanceForm").iForm("getVal");
	/*var woAcceptanceFormData = JSON.stringify(woAcceptanceFormObj); 
	
	var woFormData = JSON.stringify(woFormObj);  
	var woReportFormData = JSON.stringify(woReportFormObj); */
	var woFormData = woFormObj;  
	var woReportFormData = woReportFormObj; 
	var woAcceptanceFormData = woAcceptanceFormObj; 
/*	remainFaultFormData = remainFaultFormData;*/
	
	endEdit("preHazardTable");
	endEdit("toolTable");
	endEdit("taskTable");
	endEdit("workerTable");
	
/*	var preHazardData =FW.stringify($("#preHazardTable").datagrid("getData"));
	var toolData = FW.stringify($("#toolTable").datagrid("getData"));
	var taskData = FW.stringify($("#taskTable").datagrid("getData"));
	var workerData = FW.stringify($("#workerTable").datagrid("getData"));*/
	var preHazardData =$("#preHazardTable").datagrid("getData");
	var toolData = $("#toolTable").datagrid("getData");
	var taskData = $("#taskTable").datagrid("getData");
	var workerData = $("#workerTable").datagrid("getData");
	
	beginEdit("preHazardTable");
	beginEdit("toolTable");
	beginEdit("taskTable");
	beginEdit("workerTable");
	if(woStatus == "woAcceptance"){
		endEdit("toolTable");
	}
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = processInstId;
	params['businessId'] = woId;
	var variables = [{'name':'isToWT','value':isToPTW}];
	params['variables'] = JSON.stringify(variables);
	
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woData = {"isToPTW":isToPTW,"jobPlanId":jobPlanId,"workOrderForm":woFormData,
					"woReportForm":woReportFormData,"woAcceptanceForm":woAcceptanceFormData,
					"isHasRemainFault":isHasRemainFault,"remainFaultFormData":remainFaultFormData,
					"preHazardData":preHazardData,"toolData":toolData,
				 	 "taskData":taskData,"workerData":workerData};
			var workFlow = new WorkFlow();
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(woData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}

/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	//策划阶段输入内容
	var woPlanFormObj = $("#woPlanForm").iForm("getVal");
	var isToPTW = woPlanFormObj.isToWorkTicket;
	if(isToPTW=="toPTW"){
		$("#btn_wo_save2").remove();
		$("#btn_wo_audit2").remove();
		newPtw();
	}else{
		closeCurPage();
	}
};
function rollback(){
	closeCurPage();
    
};
function stop(rowdata){
	 var data={};
	    data['processInstId'] = rowdata.processInstId;
	    data['reason'] = rowdata.reason;
	    data['businessId'] = woId;
	    
	    var url = 'workorder/workorder/stopWorkOrder.do';
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
/** 暂存维护计划 （数据入库）*/
function saveWO(){
	//工单基本信息
	var woFormObj = $("#woBaseForm").iForm("getVal"); //取表单值
	var jobPlanId = woFormObj.jobPlanId;
	if(jobPlanId==null){
		jobPlanId = 0;
	}
	var workflowId = woFormObj.workflowId;
	
	//策划阶段输入内容
	var woPlanFormObj = $("#woPlanForm").iForm("getVal");
	var isToPTW = woPlanFormObj.isToWorkTicket;
	
	//汇报阶段输入内容
	var woReportFormObj = $("#endWOReportForm").iForm("getVal");
	var isHasRemainFault = woPlanFormObj.isHasRemainFault;
	
	//验收阶段输入内容
	var woAcceptanceFormObj = $("#woAcceptanceForm").iForm("getVal");
	
	var woFormData = JSON.stringify(woFormObj);  
	var woReportFormData = JSON.stringify(woReportFormObj); 
	
	endEdit("preHazardTable");
	endEdit("toolTable");
	endEdit("taskTable");
	endEdit("workerTable");
	var preHazardData =FW.stringify($("#preHazardTable").datagrid("getData"));
	var toolData = FW.stringify($("#toolTable").datagrid("getData"));
	var taskData = FW.stringify($("#taskTable").datagrid("getData"));
	var workerData = FW.stringify($("#workerTable").datagrid("getData"));
	beginEdit("preHazardTable");
	beginEdit("toolTable");
	beginEdit("taskTable");
	beginEdit("workerTable");
	
	 $.post(basePath + "workorder/workorder/saveWOOnPlandata.do",
			 {"isToPTW":isToPTW,"jobPlanId":jobPlanId,"workOrderForm":woFormData,
			  "woReportForm":woReportFormData,
			  "isHasRemainFault":isHasRemainFault,"remainFaultFormDate":remainFaultFormData,
			  "preHazardData":preHazardData,"toolData":toolData,
		 	  "taskData":taskData,"workerData":workerData},
			 function(data){
					if(data.result == "success"){
						FW.success("暂存成功");
					}else {
						FW.error("操作失败");
					}
		},"json");
		
		
}
function showRemainFault(){
	//工单基本信息
	var woFormObj = $("#woBaseForm").iForm("getVal"); //取表单值
	var woFormData = JSON.stringify(woFormObj);  
	var remainFaultObj = {};
	remainFaultObj.woId = woFormObj.id;
	remainFaultObj.equipId = woFormObj.equipId;
	remainFaultObj.equipName = woFormObj.equipName;
//	remainFaultObj.specialtyId = woFormObj.woSpecCode;
	remainFaultObj.specialtyId = "jx_wo_spec";	//默认选择“机械”
	
	var remainFaultData=  encodeURIComponent(JSON.stringify(remainFaultObj));
	
    var src = basePath + "page/workorder/core/operationWO/remainFault.jsp?remainFaultData="+remainFaultData;
    var btnOpts = [{"name" : "取消",
		            "onclick" : function(){
		                $("#f_isHasRemainFault").iCombo("setVal","no_remainFault");
		                return true;}
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                var formData = p.getRegForm();  //将表单值提取
		                remainFaultFormData = formData;
		                return true;
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"遗留问题录入"};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

//object to String 
function objToString (obj) {
    var tabjson=[];
    for (var p in obj) {
        if (obj.hasOwnProperty(p)) {
            tabjson.push('"'+p +'"'+ ':"' + obj[p] + '"');
        }
    }
    return '{'+tabjson.join(',')+'}';
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("eqWOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}



/**
 * 跳转到相关工作票页面（打开工作票页面）
 */
function queryPtw(){
	FW.setTreeStat("fold");
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var params = {opType:"handlePtw",id:ptwId};
   params = JSON.stringify(params);
   var currTabId = FW.getCurrentTabId();
   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
   var urlPath = basePath+ "ptw/ptwInfo/preQueryPtwInfo.do?params="+params;
   var opts = {
        id : "ptw" + rand,
        name : "关联工作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');FW.setTreeStat('expand');FW.getFrame('equmaintain').refresh();" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}
/**
 * 跳转到新建工作票页面（打开工作票页面）
 */
function newPtw(){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var currTabId = FW.getCurrentTabId();
   var params = {opType:"newPtw",workOrderId:woId,currTabId:currTabId};
   params = JSON.stringify(params);
   var urlPath = basePath+ "ptw/ptwInfo/preQueryPtwInfo.do?params="+params;
   var opts = {
        id : "ptw" + currTabId,
        name : "新建工作票",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

function backToPlan(){
	Notice.confirm("确定回退|确定回退到“工作策划”环节么？",function(){
		var woFormObj = $("#woBaseForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woId = woFormObj.id;
		
		$.post(basePath + "workorder/workorder/wobackToPlan.do",{"woId":woId,"woStepFlag":"woPlan"},
					function(data){
						if(data.result == "success"){
							FW.success("回退成功");
							closeCurPage();
						}else {
							FW.error("回退失败");
						}
			  },"json");
	},null,"info");	
}


function printWind(){
	var src = fileExportPath + "preview?__report=report/TIMSS2_WO_FJJL.rptdesign&__asattachment=true&__format=doc"+
			"&woId="+woId;
	$("#btn_wo_printWind").bindDownload({
		url : src
	});
}

function printBdz(){
	var src = fileExportPath + "preview?__report=report/TIMSS2_WO_BDZJL.rptdesign&__asattachment=true&__format=doc"+
			"&woId="+woId;
	$("#btn_wo_printBdz").bindDownload({
		url : src
	});
}

function woPrintHelp(buttonId,printType){
	var url = "";
	var title = "";
	switch(printType){
	case "windPrint":title="工单（风机）信息打印";
					 url= fileExportPath + "preview?__report=report/TIMSS2_WO_FJJL.rptdesign&__format=pdf"+
						  "&woId="+woId;
					 break;
	case "bdzPrint":
					title="工单（变电站）信息打印";
					url = fileExportPath + "preview?__report=report/TIMSS2_WO_BDZJL.rptdesign&__format=pdf"+
							"&woId="+woId;
					break;
	}
	$(buttonId).click(function(){
		FW.dialog("init",{
			src: url,
			btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		                _parent().$("#itcDlg").dialog("close");
		             }
		        }],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	});

}
