/* 表单字段定义  */
var fields = [
				{title : "ID", id : "id",type : "hidden"},
				{title : "工单编号", id : "workOrderCode"},
				{title : "维护计划ID", id : "maintainPlanId", type : "hidden"},
			    {title : "维护计划来源",id : "maintainPlanFrom", type : "hidden"},
			    {title : "作业方案ID", id : "jobPlanId", type : "hidden"},
			    {title : "遗留问题Id", id : "nextWoMtp", type : "hidden"},
				{title : "流程ID", id : "workflowId", type : "hidden"},
				{title : "工号", id : "customerCode", rules : {required:true}},
				{title : "申请人姓名", id : "customerName", rules : {required:true}},
				{title : "电话", id : "customerPhone", rules : {required:true}},
				{title : "公司", id : "customerCom"},
			    {title : "部门", id : "customerDept"},
				{title : "位置", id : "customerLocation"},

				{title : "状态", id : "currStatus",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ITSM_STATUS"
				},
				{title : "工单类型", id : "workOrderTypeCode",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ITSM_TYPE"
				},
				{title : "服务目录Id", id : "faultTypeId",type : "hidden"},
			    {title : "服务目录", id : "faultTypeName"},
			    {title : "服务性质Id", id : "serCharacterId",type : "hidden"},
			    {title : "服务性质",id : "serCharacterName"},
				{title : "紧急度", id : "urgentDegreeCode",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ITSM_URGENCY_DEGREE"
				},
				{title : "影响度", id : "influenceScope",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ITSM_INFLUENCE_SCOPE"
				},
				{title : "服务级别",id : "priorityId",type : "combobox",
					options : {
			    		url : basePath + "itsm/woParamsConf/comboboxPriority.do",
			    	    remoteLoadOn : "init"
			    	}			
				},
				{title : "报障时间", id : "discoverTime", type:"datetime", dataType:"datetime",
					rules : {required:true},
					options : {endDate : new Date()}
				},
				{title : "预约上门时间", id : "appointTime", type:"datetime", dataType:"datetime"},
				{title : "计划开工时间", id : "defaultBeginTime", type:"datetime", dataType:"datetime",
	          	  rules : {required:true} 
	            },
	  		  	{title : "计划完工时间", id : "defaultEndTime", type:"datetime", dataType:"datetime",
	  			  rules : {required:true,greaterThan:"#f_defaultBeginTime"}
	            },
	            {title : "当前处理人", id : "currHandUserName",type:"label"},
				{
				    title : "故障描述", 
				    id : "description",
				    type : "textarea",
				    linebreak:true,
				    wrapXsWidth:12,
				    wrapMdWidth:8,
				    height:110
				}
		   
		];
var woPlanFields =[{
					title : "操作类型",
					id : "planOper",
					type : "combobox",
					data : [
					        ["nowHandlerWO","开始处理"],
					        ["delayWO","延时"],
					        ["rollbackWO","退单"]					        
					    ],
				    options:{
				    	allowEmpty : false,
				    	initOnChange:false,
				    	onChange:function(val){
				    		if(val == "nowHandlerWO"){
				    			$("#woPlanForm").iForm("hide",["delayToTime"]);  
				    			$("#woPlanForm").iForm("show",["partner"]);
								$("#title_tool").iFold("show");
								$("#btn_wo_selectJobPlanDiv").show();
								FW.fixRoundButtons("#toolbar");  
				    		}else if(val == "delayWO"){
				    			$("#woPlanForm").iForm("show",["delayToTime"]);
				    			$("#woPlanForm").iForm("hide",["partner"]);
								$("#title_tool").iFold("hide");
								$("#btn_wo_selectJobPlanDiv").hide();
								FW.fixRoundButtons("#toolbar");  
				    		}else{
				    			$("#woPlanForm").iForm("hide",["delayToTime","partner"]);
								$("#title_tool").iFold("hide");
								/*$("#btn_wo_save2").hide();*/
								$("#btn_wo_selectJobPlanDiv").hide();
								FW.fixRoundButtons("#toolbar");  
				    		}
				    	}
					}
				},
				{title : "延时到", id : "delayToTime", type:"datetime", dataType:"datetime",
					rules:{required:true},
					options : {startDate : new Date()}
				},
				{id:"partner",title:"协作人员",render:function(id){
						$("#" + id).attr("placeholder","请选协助人员").iInput("init",{
							icon : "itcui_btn_mag",
							onClickIcon : function(){
								FW.selectPersonByTree({
									single : false,
									onSelect : function(obj){
										var names = [];
										var ids = [];
										if(obj != null){
											for(var k in obj){
												names.push(obj[k]);
												ids.push(k);
											}
										}
										$("#f_partner").val(names.join(","));
										$("#f_partnerNames").val(names.join(","));
										$("#f_partnerIds").val(ids.join(","));
									}
								});
							}				
						});
					}},
					{id:"partnerNames",title:"协作人员"},
					{id:"partnerIds",title:"协助人员IDs", type:"hidden"}
				];

var endWOReportFields = [ 
						  /*{title:"汇报人",id:"reportUserNames"},
						  {title : "汇报时间", id : "endReportTime", type:"datetime", dataType:"datetime"},*/
                          {title : "实际开工时间", id : "beginTime", type:"datetime", dataType:"datetime",
                        	  rules : {required:true,greaterEqualThan:"#f_discoverTime"},
                        	  messages:{greaterEqualThan:"实际开工时间不能早于报障时间"},
                        	  render : function(id){
                        		  $("#" + id).on("blur",function(){
                        			  $("#f_endTime").valid();
                        			  });
                        	   }
                          },
                		  {title : "实际完工时间", id : "endTime", type:"datetime", dataType:"datetime",
                			  rules : {required:true,greaterThan:"#f_beginTime"},
                			  options : {endDate : new Date()}
                          },
                          {title : "处理工程师", id : "endReportUserName"},
                		  {
                		        title : "完工情况报告", 
                		        id : "endReport",
                		        type : "textarea",
                		        linebreak:true,
                		        wrapXsWidth:12,
                		        wrapMdWidth:8,
                		        height:55,
                		        rules : {maxChLength:680}
                		    }
                		 ];


var woAcceptanceFields = [{
              		        title : "服务评价", 
              		        id : "evaluateService",
              		        type : "radio",
              		        wrapXsWidth:12,
              		        wrapMdWidth:8,
              		        data : FW.parseEnumData("ITSM_FB_TYPE",_enum,-1)
              		    },
              		  {
            		        title : "情况说明", 
            		        id : "feedbackRemarks",
            		        type : "textarea",
            		        linebreak:true,
            		        wrapXsWidth:12,
            		        wrapMdWidth:8,
            		        height:55,
            		        rules : {maxChLength:340} 
            		    }];



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
			{field : 'itemsCode',title : '物资编码',width:100,fixed:true},
			{field : 'itemsName',title : '名称', width:200},
			{field : 'itemsModel',title : '规格型号',width:200,fixed:true},
			/*{field : 'bin',title : '货柜',width:100,fixed:true},
			{field : 'warehouse',title : '仓库',width:100,fixed:true},*/
			{field : 'applyCount',title : '申请数量',edit:true,width:60,fixed:true,
				editor:{type:"text","rules" : {required:true,"digits" : true},
					"options":{align:"right"}}},
			{field : 'getCount',title : '领取数量',edit:true,width:60,fixed:true,
				editor:{type:"text","rules" : {required:true,"digits" : true},
					"options":{align:"right"}}},
			/*{field : 'usedCount',title : '使用数量',edit:true,width:60,fixed:true,
				editor:{type:"text","rules" : {required:true,"digits" : true},
					"options":{align:"right"}}},*/
			{field : 'unit',title : '单位',width:60,fixed:true,align:'center'},
			{field : 'oper',title : '',width:55,fixed:true,align:'center', 
				 formatter:function(value,row){
					     return '<img src="'+basePath+'img/workorder/btn_garbage.gif" onclick="deleteGridRow('+
					     		"'toolTable',"+row.toolDelteId+')" width="16" height="16" >';
		   			}
		   		}
		   	]];



function initWoPlanPage(){
	/* form表单初始化 */
	$("#woBaseForm").iForm("init",{"fields":fields});
	$("#title_plan").iFold("init");
	$("#title_plan").iFold("show");
	$("#woPlanForm").iForm("init",{"fields":woPlanFields,"options":{validate:true,initAsReadonly:false}});
	$("#woPlanForm").iForm("hide",['delayToTime']);
	
	$("#toolTable").datagrid({
		    columns:toolGridField,
		    idField:'toolDelteId',
		    singleSelect:true,
		    fitColumns:true,
		    scrollbarSize:0
		}); 
	
	$("#title_tool").iFold("init");
	
	setWOPlanVal(woId); //给页面赋值
}


function joinFormField(woFormData){
	var name = woFormData.customerName;
	var code = woFormData.customerCode;
	var com = woFormData.customerCom;
	var dept = woFormData.customerDept;
	if(dept != null){
		com = com + " / " + dept;
	}
	name = name + " / " + code;
	$("#woBaseForm").iForm("setVal",{"customerName":name,"customerCom":com}).iForm("endEdit");
	$("#woBaseForm").iForm("hide",["customerCode","customerDept"]);
}

function setWOPlanVal(woId){
	$.post(basePath + "itsm/workorder/queryItWODataById.do",{workOrderId:woId},
			function(woFullData){
				var woFormData = JSON.parse(woFullData.workOrderForm );
				var woType = woFormData.workOrderTypeCode;
				$("#woBaseForm").iForm("setVal",woFormData).iForm("endEdit");
				joinFormField(woFormData);  //合并相关字段的显示
				woStatus = woFormData.currStatus;
				woDiscoverTime = woFormData.discoverTime;//报障时间
				faultTypeId = woFormData.faultTypeId;
				if(faultTypeId != null){
					setTimeout(function(){
						window.parent.document.getElementById("itsmFaultTypeTree").contentWindow.expandForHintById(faultTypeId);
					},1000);
				}
				
				var attachmentData = woFullData.attachmentMap ;
				
				createtime = FW.long2time(woFormData.createDate);
				updatetime = FW.long2time(woFormData.modifyDate);
				
				//工具
				var toolDataObj = woFullData.toolData;
				if(toolDataObj.length ==0 && woStatus != "workPlan"){
					$("#woPlanDiv").hide();
				}else{
					$("#woPlanDiv").hide();
				}
				
				//显示附件
				if(attachmentData.length > 0){
					$("#uploadfileTitle").iFold("show"); 
					$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
				}
				
				//工作流中数据信息
				processInstId = woFormData.workflowId;
				taskId = woFullData.taskId;
				candidateUsers = woFullData.candidateUsers;
				ownFlag = isMyActivityWO(candidateUsers,loginUserId);

				if(!ownFlag){  //若工单此时不在自己手上
					FW.toggleSideTree(true);
					$("#btn_wo_audit2").hide(); //审批隐藏
					$("#btn_wo_selectJobPlanDiv").hide(); //审批、暂存隐藏
					 
					if(toolDataObj.length!=0){
						$("#toolTable").datagrid("hideColumn","oper"); //隐藏某一列
						$("#toolBtnDiv").hide();
					}else{
						$("#title_tool").iFold("hide");
					}
					$("#uploadform").iForm("endEdit");  //控制附件的权限（只能下载）
					if(attachmentData.length == 0){
						$("#uploadfileTitle").iFold("hide"); 
					}
					$("#toolBtnDiv").hide();
				}else{
					$("#btn_wo_selectJobPlanDiv").hide(); //审批、暂存隐藏
				}
				
				
				switch(woStatus){
	    		case itsmWoStatus.WORKPLAN:  //工程师处理
	    					$("#toolTable").datagrid("hideColumn","getCount"); //隐藏某一列
	    					if(!ownFlag){
	    						$("#woPlanForm").hide();
	    						$("#title_plan").iFold("hide");
	    					}else{
	    						$("#btn_wo_selectJobPlanDiv").show(); //审批、暂存隐藏
	    						$("#woPlanForm").iForm("hide",['partnerNames']);
	    					}
	    					break;
	    		case itsmWoStatus.DELAYAUDIT:  // 申请延时delayAudit
		    			var delayToTime = woFormData.defaultBeginTime;
		    			$("#woPlanForm").iForm("setVal",{"planOper":"delayWO",
		    					"delayToTime":delayToTime}).iForm("endEdit");
		    			break;
	    		case itsmWoStatus.CHIEFAUDIT:  //主管审批（审批退单申请）
		    			$("#woPlanForm").iForm("setVal",{"planOper":"rollbackWO"}).iForm("endEdit");
	    				$("#toolBtnDiv").hide();
		    			break;
	    		case itsmWoStatus.ENDREPORT:  //完工报告
	    				if(ownFlag){  //工单在自己手上才可以修改“服务目录，服务性质，故障描述”
	    					$("#woBaseForm").iForm("beginEdit",["faultTypeId","faultTypeName","serCharacterId",
		    				                                    "serCharacterName","description"]);
	    				}
	    			    var partnerNamesStr = woFormData.partnerNames;
	    				$("#woPlanForm").iForm("setVal",{"partner":partnerNamesStr}).iForm("endEdit");
	    				$("#toolBtnDiv").hide();
	    				if(woType=='rwxWoType' || woType=='hbWoType' ){
	    					rwxWoHideEle();
	    				}
	    				if(ownFlag){
	    					$("#title_report").iFold("init");
			    			$("#title_report").iFold("show");
		    				$("#endWOReportForm").iForm("init",{"fields":endWOReportFields,"options":{validate:true,initAsReadonly:false}});
		    				$("#endWOReportForm").iForm("hide",["endReportUserName"]);
		    				var beginTime = woFormData.beginTime;
		    				//工程师点击审批时间为默认上门时间
		    				$("#endWOReportForm").iForm("setVal",{"beginTime":beginTime});
    					}
		    			break; 
	    		case itsmWoStatus.APPLICANTCONFIRM:
		    			$("#title_report").iFold("init");
		    			$("#title_report").iFold("show");
	    				$("#endWOReportForm").iForm("init",{"fields":endWOReportFields}).iForm('hide',['isHasRemainFault']);
	    				$("#endWOReportForm").iForm("setVal",woFormData).iForm("endEdit");
	    				$("#title_plan").iFold("hide");
	    				$("#toolBtnDiv").hide();
		    			break; 
	    		case itsmWoStatus.FEEBACK:
	    			    var partnerNamesStr = woFormData.partnerNames;
	    				$("#woPlanForm").iForm("setVal",{"partner":partnerNamesStr}).iForm("endEdit");
		    			$("#title_report").iFold("init");
		    			$("#title_report").iFold("show");
	    				$("#endWOReportForm").iForm("init",{"fields":endWOReportFields}).iForm('hide',['isHasRemainFault']);
	    				$("#endWOReportForm").iForm("setVal",woFormData).iForm("endEdit");
	    				$("#woPlanForm").iForm("setVal",{"partner":partnerNamesStr}).iForm("endEdit");
	    				$("#toolBtnDiv").hide();
	    				if(woType=='rwxWoType' || woType=='hbWoType' ){
	    					rwxWoHideEle();
	    				}
	    				if(ownFlag){
	    					$("#title_feedback").iFold("init");
			    			$("#title_feedback").iFold("show");
		    				$("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields,"options":{validate:true,initAsReadonly:false}});
		    				$("#woAcceptanceForm").iForm("setVal",{"evaluateService":"B"});
    					}
		    			break; 
	    		case itsmWoStatus.FILING:
	    			$("#btn_wo_audit2").hide();
    			    var partnerNamesStr = woFormData.partnerNames;
    				$("#woPlanForm").iForm("setVal",{"partner":partnerNamesStr}).iForm("endEdit");
	    			$("#title_report").iFold("init");
	    			$("#title_report").iFold("show");
    				$("#endWOReportForm").iForm("init",{"fields":endWOReportFields}).iForm('hide',['isHasRemainFault']);
    				$("#endWOReportForm").iForm("setVal",woFormData).iForm("endEdit");
    				$("#woPlanForm").iForm("setVal",{"partner":partnerNamesStr}).iForm("endEdit");
	    			$("#title_feedback").iFold("init");
	    			$("#title_feedback").iFold("show");
    				$("#woAcceptanceForm").iForm("init",{"fields":woAcceptanceFields});
    				woFormData.evaluateService = woFormData.fbResultType;
    				$("#woAcceptanceForm").iForm("setVal",woFormData).iForm("endEdit");
    				$("#toolBtnDiv").hide();
    				if(woType=='rwxWoType' || woType=='hbWoType' ){
    					rwxWoHideEle();
    				}
    				auditInfoShowBtn = 0 ;//控制流程信息弹出框中是否显示“审批”按钮
	    			break; 
	    		case itsmWoStatus.OBSOLETE:
	    			$("#inPageTitle").html("工单已作废");
	    			$("#title_plan").iFold("hide");
	    			$("#btn_wo_audit2").hide();
    				$("#toolBtnDiv").hide();
    				if(woType=='rwxWoType' || woType=='hbWoType' ){
    					rwxWoHideEle();
    				}
    				auditInfoShowBtn = 0 ;//控制流程信息弹出框中是否显示“审批”按钮
	    			break; 
	    		 default:break;
				}
				
//				onlyShowBaseInfo("woBaseForm");
    			FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题	
			},"json");
}	
function rwxWoHideEle(){
	$("#title_plan").iFold("hide");
	
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
	
	 var src = basePath + "itsm/workorder/woPlanStandJPList.do"; 
     var dlgOpts = {
         width : 600,
         height:500,
         closed : false,
         title:"双击选择标准作业方案",
         modal:true
     };
     Notice.dialog(src,dlgOpts,null);
}

function appendTool(){
	$("#toolTable").datagrid("resize");
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
				$("#toolTable").datagrid("showColumn","oper"); //显示某一列
			}
			beginEdit("toolTable");
			
		}
	});
	$("#btn_toolTable").html("继续添加工具");
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


function audit(){  //审批
	//工单基本信息
	var woFormObj = $("#woBaseForm").iForm("getVal"); //取表单值
	woStatus = woFormObj.currStatus;
	woType = woFormObj.workOrderTypeCode;
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	var updateDesc = "";
//表单验证
	switch(woStatus){
	case itsmWoStatus.WORKPLAN:/*updateDesc = "(工作准备)";*/
								if(!$("#woPlanForm").valid()){ 
									return ;
								}
								break;
	case itsmWoStatus.DELAYAUDIT:/*updateDesc = "(延迟开始时间)";*/break;
	case itsmWoStatus.ENDREPORT:/*updateDesc = "(完工汇报)";*/
								if(!$("#endWOReportForm").valid()){	
									return ;
								}
								break;
	case itsmWoStatus.APPLICANTCONFIRM:/*updateDesc = "(申请人确认)";*/
									break;
	case itsmWoStatus.FEEBACK:/*updateDesc = "(回访)";*/
								if(!$("#woAcceptanceForm").valid()){
									return ;
								}
								break;
	default: break;
	}
	//策划阶段输入内容
	var woPlanFormObj = $("#woPlanForm").iForm("getVal");
	var planOperStyle = woPlanFormObj.planOper;
	
	//汇报阶段输入内容
	var woReportFormObj = $("#endWOReportForm").iForm("getVal");
	var isHasRemainFault = woReportFormObj.isHasRemainFault;
	if(isHasRemainFault == undefined){
		isHasRemainFault = null;
	}
	//验收阶段输入内容
	var woAcceptanceFormObj = $("#woAcceptanceForm").iForm("getVal");
	
	//form表单转化为字符串传给handler
	var woFormData = woFormObj; 
	var woPlanFormData = woPlanFormObj;
	var woReportFormData = woReportFormObj; 
	var woAcceptanceFormData = woAcceptanceFormObj;
	
	endEdit("toolTable");
	
	var toolData = $("#toolTable").datagrid("getData");
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = processInstId;
	params['businessId'] = woId;
	var variables = [{'name':'woPlanHandlerStyle','value':planOperStyle},
	                 {'name':'woType','value':woType}];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woData = {"toolData":toolData,"woPlanData":woPlanFormData,"workOrderForm":woFormData,
					"woReportData":woReportFormData,"woAcceptanceForm":woAcceptanceFormData,
					"attachmentIds":attachmentIds};
			var workFlow = new WorkFlow();
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			var flag = woStatus == itsmWoStatus.APPLICANTCONFIRM || 
						woStatus == itsmWoStatus.ENDREPORT || 
						(woStatus == itsmWoStatus.WORKPLAN && planOperStyle==itsmWoStatus.DELAYAUDIT);
			if(flag){
				multiSelect = 1
			}
			workFlow.showAudit(taskId,FW.stringify(woData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}
 
/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	closeCurPage();
};
function rollback(){
	closeCurPage();
    
};
function stop(rowdata){
	    var data={};
	    data['processInstId'] = rowdata.processInstId;
	    data['reason'] = rowdata.reason;
	    data['businessId'] = woId;
	    
	    var url = 'itsm/workorder/stopWorkOrder.do';
	    $.post(url, data, function(data){
	        if(data.result=='success'){
	            FW.success("提交成功");
	            _parent().$("#itcDlg").dialog("close");
	            closeCurPage();
	            homepageService.refresh();
	        }else{
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
	//汇报阶段输入内容
	var woReportFormObj = $("#endWOReportForm").iForm("getVal");
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
	
	 $.post(basePath + "itsm/workorder/saveWOOnPlandata.do",
			 {"isToPTW":isToPTW,"jobPlanId":jobPlanId,"workOrderForm":woFormData,
			  "woReportForm":woReportFormData,"toolData":toolData,
		 	  "taskData":taskData,"workerData":workerData},
			 function(data){
					if(data.result == "success"){
						closeCurPage();
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
	remainFaultObj.specialtyId = woFormObj.woSpecCode;	
	
	var remainFaultData=  encodeURIComponent(JSON.stringify(remainFaultObj));
	
    var src = basePath + "page/itsm/core/operationWO/remainFault.jsp?remainFaultData="+remainFaultData;
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
		                remainFaultFormDate =objToString(formData);
 
		                return true;
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"遗留问题录入"};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

/**
 * 工单详情打印
 */
function printWO(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_ITWOINFO.rptdesign&__format=pdf"+
						"&woId="+woId+"&workflow_id="+processInstId+"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="工单详情信息"
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
		FW.set("WOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}





