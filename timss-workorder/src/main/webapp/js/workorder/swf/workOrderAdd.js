//工单提交阶段字段
var fields = [
	{title : "ID", id : "id", type : "hidden"},
	{title : "工单编号", id : "workOrderCode", type : "hidden"},
	{title : "维护计划ID", id : "maintainPlanId", type : "hidden"},
	{title : "维护计划来源",id : "maintainPlanFrom", type : "hidden"},
	{title : "作业方案ID", id : "jobPlanId", type : "hidden"},
	{title : "流程ID", id : "workflowId", type : "hidden"},
	{title : "缺陷描述", id : "description",  rules : {required:true},wrapXsWidth:12, wrapMdWidth:12},
	{
		title : "工单类型",
		id : "workOrderTypeCode",
		type:"combobox",
		//value:"DEFECT",
		rules : {required:true},
    	dataType:"enum",
    	linebreak:true,
	    enumCat:"WO_TYPE"
	},
	{
		title : "优先级",
		id : "priorityId",
		type:"combobox",
		rules : {required:true}
	},
	{
		title : "专业",
		id : "woSpecCode",
		//value: "boiler",
		type : "combobox",
		rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_SPEC"
	},		
	{
		title : "科目",
		id : "faultTypeId",
		//value: "zjh-xx-01",
		type : "combobox",
		rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_FAULT_TYPE",
		options : {
			wrapWidth: 220
		}
	},	
	{title : "责任部门", id : "customerDept"},
	{title : "关联工单ID", id : "parentWOId",type:"hidden"},
	{title : "关联工单", id : "parentWOCode",
		render:function(id){
			var ipt = $("#" + id);
			ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
			ipt.ITCUI_Input();
			ipt.next(".itcui_input_icon").on("click",function(){
				var src = basePath + "workorder/workorder/parentWOList.do";
				var dlgOpts = {
					width : 620,
					height: 500,
					closed : false,
					title:"双击选择父工单",
					modal:true
				};
				Notice.dialog(src,dlgOpts,null);
			});
		}
	},	
	{title : "设备位置", id : "equipSite", rules : {required:true}},	
	{title : "设备名称", id : "equipName", type:"label", value:"请从左边设备树选择",rules : {required:true},
		formatter:function(val){
			var text = val;
			if(text=="请从左边设备树选择"){
				text = "<label style='color:red'>"+val+"</label>";
			}
			return text;
		}
	},
	{title : "设备资产ID", id : "equipId",type : "hidden"},
	{title : "设备名称编码", id : "equipNameCode",type : "hidden"},
	{title : "发现时间", id : "discoverTime", type:"datetime", dataType:"datetime",rules : {required:true},value:new Date()},
	{
		title : "是否紧急",
		id : "urgentDegreeCode",
		type : "combobox",
		value:"N",
    	dataType:"enum",
	    enumCat:"WO_URGENCY_DEGREE"
	},
	{
		title : "状态", 
		id : "currStatus", 
		type:"label",
		formatter:function(val){
			return FW.getEnumMap("WO_STATUS")[val];
		}
	},
	{
		title : "是否缺陷",//值长确认缺陷
		id : "faultDegreeCode",
		type : "combobox",
    	dataType:"enum",
	    enumCat:"WO_FAULT_DEGREE"
	},	
	{
		title : "处理方式",//专工下发缺陷
		id : "woExpertHandleStyle",
		type : "combobox",
    	dataType:"enum",
	    enumCat:"WO_EXPERT_HANDLE_STYLE"
	},
	{
		title : "重新分配",//策划组重新分配
		id : "woIsReAssign",
		type : "combobox",
    	dataType:"enum",
	    enumCat:"WO_IS_REASSIGN"
	},
	{
		title : "详细描述",
		//value:"备注信息",
		id : "remarks",
		type : "textarea",
		linebreak:true,
		rules : {maxlength:500,required:true},
		wrapXsWidth:12,
		wrapMdWidth:8,
		height:55
	},
	{title : "缺陷记录ID", id : "woDefectId",type:"hidden"},
	{title : "缺陷记录编号", id : "woDefectCode",type : "hidden"},
	{title : "流程实例ID", id : "workflowId",type : "hidden"}
];

//策划阶段字段
var planFields = [
	{title : "维护单位",id : "woMaintainCom",rules : {required:true}},
	{title : "负责班组",id : "woMaintainTeam",rules : {required:true}},
	{title : "工作负责人",id : "woMaintainExecutorName",rules : {required:true}}
	/*{
		title : "工作负责人",
		id : "woMaintainExecutor",
		type:"combobox",
		rules : {required:true},
		options : {
    		//url : basePath + "workorder/woParamsConf/comboboxMaintainExecutor.do",
    	    //remoteLoadOn : "init",
    	    multiselect:false,
    	    allowSearch:true
		}
	}*/
];

//回填阶段字段
var reportFields = [
    {title : "是否高优先级", id : "isHighLevel", type : "hidden"},
    {
    	title : "是否延期",
    	id : "woIsDelay",
    	type : "combobox",
    	rules : {required:true},
    	value: "N",
    	dataType:"enum",
	    enumCat:"WO_IS_DELAY",
	    options :{
    	    onChange:function(val){
    	    	if(woStatus == supervisorReportStr){
    	    		if(val=="Y"){
    					$("#reportForm").iForm("show","woDelayType");
    					$("#reportForm").iForm("show","woDelayReason");
    					$("#reportForm").iForm("show","woDelayLenEnum");
    					$("#reportForm").iForm("hide","beginTime");
    					$("#reportForm").iForm("hide","endTime");
    					$("#reportForm").iForm("hide","endReport");
    					$("#reportForm").iForm("hide","feedbackRemarks");
    					$("#reportForm").iForm("setVal",{beginTime:null,endTime:null,endReport:"",feedbackRemarks:""});
    	    		}
    	    		else{
    					$("#reportForm").iForm("hide","woDelayType");
    					$("#reportForm").iForm("hide","woDelayReason");
    					$("#reportForm").iForm("hide","woDelayLenEnum");
    					
    					if(woIsDelay!="Y"){
    						$("#reportForm").iForm("setVal",{woDelayType:"",woDelayReason:"",woDelayLenEnum:"",woDelayLen:""});
    					}
    					else{
        					$("#reportForm").iForm("show","woDelayType");
        					$("#reportForm").iForm("show","woDelayReason");
    					}
    					$("#reportForm").iForm("show","beginTime");
    					$("#reportForm").iForm("show","endTime");
    					$("#reportForm").iForm("show","endReport");
    					$("#reportForm").iForm("show","feedbackRemarks");
    	    		}    	    		
    	    	}

    	    }	
	    }
    },  
    {
    	title : "延期类型",
    	id : "woDelayType",
    	type : "combobox",
    	rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_DELAY_TYPE",
	    options :{
	    	onChange:function(val){
	    		var woDelayTypeLabel = $("#f_woDelayType").iCombo("getTxt");
	    		if(woStatus==supervisorReportStr && woIsDelay !="Y"){
	    			$("#reportForm").iForm("setVal",{woDelayReason:woDelayTypeLabel});
	    		}
	    		
	    		if(val == "STOP_MACHINE" || val ==""){ //延期类型为停机处理，则延期时长字段默认为空并隐藏，不需要填写
	    			$("#reportForm").iForm("setVal",{woDelayLenEnum:"",woDelayLen:""});
	    			$("#reportForm").iForm("hide","woDelayLenEnum");
	    		}else{
	    			$("#reportForm").iForm("show","woDelayLenEnum");
	    		}
	    	}	
	    }
    }, 
    {
    	title : "延期时长",
    	id : "woDelayLenEnum",
    	type : "combobox",
    	rules : {required:true},
    	dataType:"enum",
	    enumCat:"WO_DELAY_LEN",
	    options :{
	    	allowEmpty:true,
	    	onChange:function(val){
	    		if(val != ""){
	    			var tempdelayType = $("#f_woDelayType").iCombo("getVal");
	    			var tempPriority = $("#f_priorityId").iCombo("getVal");
	    			
	    			$.post(basePath + "workorder/woParamsConf/queryWoDelayConfig.do",
	    					{"delayType":tempdelayType,"priority":tempPriority},
    					function(data){
	    					var woDelayConfig = data.woDelayConfig
							if( woDelayConfig != "null"){
								woDelayConfig = JSON.parse(woDelayConfig);
								var maxLength = woDelayConfig.maxLength;  //最大延时时长
								//用户选择的延时时长
								var delayHourLen = val.substring(val.lastIndexOf("_")+1,val.length);  
								if(delayHourLen <= maxLength){
									$("#reportForm").iForm("setVal",{woDelayLen:delayHourLen});
								}else{
									$("#reportForm").iForm("setVal",{woDelayLenEnum:"",woDelayLen:""});
									FW.error("超过了此类工单的最大延时时长： "+maxLength+" 小时");
								}
							}
						},"json");
	    			
	    		}
	    		
	    	}	
	    }
    },
    {title : "延期时长", id : "woDelayLen", type : "hidden"},
	{
		title : "延期原因",
		id : "woDelayReason",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:8,
		height:55,
		rules : {required:true,maxlength:500}
	},
	{title : "实际开始时间", id : "beginTime", type:"datetime", dataType:"datetime",rules : {required:true},linebreak:true},
	{title : "实际完成时间", id : "endTime", type:"datetime", dataType:"datetime",
		 rules : {required:true,greaterThan:"#f_beginTime"}
	},
	{
		title : "实际处理情况",
		id : "endReport",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:8,
		height:55,
		rules : {maxlength:500}
	},
	{
		title : "验收意见",
		id : "feedbackRemarks",
		type : "textarea",
		linebreak:true,
		wrapXsWidth:12,
		wrapMdWidth:8,
		height:55,
		rules : {maxlength:500}
	}
];

//页面查看时表单赋值
function setFormVal(woId){
	$.post(basePath + "workorder/workorder/queryWODataById.do",{workOrderId:woId},
		function(woBasicData){
			//通用按钮控制
			setWoButtonPriv();
			$(".priv").hide();
			
			//通用表单初始化
			$("#workOrderForm").iForm("init",{"fields":fields,"options":{validate:true}});
			$("#f_priorityId").iCombo("init",{
			    data : priorityArray
			});
			//通用表单赋值
			var woFormData = eval("(" +woBasicData.workOrderForm+ ")");
			$("#workOrderForm").iForm("setVal",woFormData).iForm("endEdit");
			//console.log(woFormData);
			
			//设置全局变量
			processInstId = woFormData.workflowId;
			taskId = woBasicData.taskId;
			createtime =  FW.long2time(woFormData.createDate);
			updatetime =  FW.long2time(woFormData.modifyDate);
			approveFlag = woBasicData.approveFlag;
			woIsDelay = woFormData.woIsDelay;
			if(approveFlag=="approver"){
				auditInfoShowBtn = 1;
			}
			else{
				auditInfoShowBtn = 0;
			}

			//设置标题
			woStatus = woFormData.currStatus;
			if(woStatus == draftStr)
				$("#workOrderFormTitle").html("新建工单");
			else
				$("#workOrderFormTitle").html("工单详情");
			
			//控制处理选择方式，先隐藏
			$("#workOrderForm").iForm("hide","faultDegreeCode");
			$("#workOrderForm").iForm("hide","woExpertHandleStyle");
			$("#workOrderForm").iForm("hide","woIsReAssign");
			
			//var executorsArray = woBasicData.executorsArray;//工作负责人
			//根据状态进行判断
			if(woStatus == draftStr){	//0 草稿状态
				//按钮控制
				$("#btn_wo_save").show();//暂存
				$("#btn_wo_commit").show();//提交
				if(loginUserId == woFormData.createuser){
					$("#btn_wo_delete").show();//删除
				}
				FW.fixRoundButtons("#toolbar");
				//表单控制
				$("#workOrderForm").iForm("beginEdit");
				//生成和选择设备树
				equipId = woFormData.equipId;
				initWoInfoAssetTree();
			}
			else if(woStatus == woCommitStr){	//1 工单提交状态
				$("#workOrderForm").iForm("endEdit");
				if(loginUserId == woFormData.createuser){
					//按钮控制
					$("#btn_wo_edit").show();//可编辑
					$("#btn_wo_commit").show();//可提交
					$("#btn_wo_obsolete").show();//可作废
					FW.fixRoundButtons("#toolbar");
					//生成和选择设备树
					equipId = woFormData.equipId;
					initWoInfoAssetTree();
				}
			}
			else if(woStatus == dutyConfirmDefectStr){	//2值长确认缺陷状态
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#workOrderForm").iForm("show","faultDegreeCode");
					$("#workOrderForm").iForm("beginEdit","faultDegreeCode");
					$("#btn_wo_edit").show();//可编辑
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
					//生成和选择设备树
					equipId = woFormData.equipId;
					initWoInfoAssetTree();
				}
			}
			else if(woStatus == safeClearAuditStr || woStatus== dutyClearAuditStr){ //14生安分部审批  15值长清除缺陷
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus == expertDealStr){	//3专工下发缺陷
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#workOrderForm").iForm("show","woExpertHandleStyle");
					$("#workOrderForm").iForm("beginEdit","woExpertHandleStyle");
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus == planningGroupAuditStr){	//13 策划组重新分配
				$("#workOrderForm").iForm("endEdit");
				if(approveFlag == "approver"){
					$("#workOrderForm").iForm("show","woIsReAssign");				
					$("#workOrderForm").iForm("beginEdit","woIsReAssign");
					$("#workOrderForm").iForm("beginEdit","woSpecCode");
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus == supervisorPlanStr){ //4项目负责人策划
				$("#workOrderForm").iForm("endEdit");
				//策划div显示
				$("#planFormDiv").show();
				$("#planForm").iForm("init",{"fields":planFields,"options":{validate:true}});
				/*$("#f_woMaintainExecutor").iCombo("init",{
				    data : executorsArray,
		    	    allowSearch:true
				});*/
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
					$("#planForm").iForm("setVal",woFormData).iForm("beginEdit");
				}
				else{
					$("#planForm").iForm("setVal",woFormData).iForm("endEdit");
				}
			}
			else if(woStatus == supervisorReportStr){ //5项目策划人回填
				$("#workOrderForm").iForm("endEdit");
				//策划div只读
				$("#planFormDiv").show();
				$("#planForm").iForm("init",{"fields":planFields,"options":{validate:true}});
				/*$("#f_woMaintainExecutor").iCombo("init",{
				    data : executorsArray,
		    	    allowSearch:true
				});*/
				$("#planForm").iForm("setVal",woFormData).iForm("endEdit");
				//回填div
				$("#reportFormDiv").show();
				$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
				$("#reportForm").iForm("setVal",woFormData);
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
					if(woFormData.woIsDelay!=null && woFormData.woIsDelay=="Y"){	//延期过则不允许再延期
						$("#f_woIsDelay").iCombo("setVal","N");
						//$("#f_woIsDelay").iCombo("setTxt","否（已延期过）");
						$("#reportForm").iForm("hide","woIsDelay");
						$("#reportForm").iForm("show","woDelayType");
						$("#reportForm").iForm("show","woDelayReason");
						$("#reportForm").iForm("show","woDelayLenEnum");
						$("#reportForm").iForm("endEdit", "woDelayType");
						$("#reportForm").iForm("endEdit", "woDelayReason");
						$("#reportForm").iForm("endEdit", "woDelayLenEnum");
						$("#reportForm").iForm("endEdit", "woIsDelay");
					}
					else{
						$("#reportForm").iForm("beginEdit", "woIsDelay");
						$("#reportForm").iForm("hide","woDelayLenEnum");
					}
				}
				else{
					$("#reportForm").iForm("endEdit");
				}
			}
			else if(woStatus == obseleteStr){ //16已作废
				$("#workOrderForm").iForm("endEdit");
			}
			else if(woStatus==delayEquipAuditStr || woStatus==delaySafeAuditStr ||
					woStatus==delayOperationAuditStr || woStatus==delayLeaderAuditStr ||
					woStatus==delayDutyAuditStr || woStatus==delayDutyRestartStr ||
					woStatus==delayConfirmOverStr){ //7设备部审批 8生安部审批 9运行部审批 10分管生产领导审批 11值长确认延期 12值长启动工单  6值长确认结束  17已完成
				$("#workOrderForm").iForm("endEdit");
				//策划div只读
				$("#planFormDiv").show();
				$("#planForm").iForm("init",{"fields":planFields,"options":{validate:true}});
				/*$("#f_woMaintainExecutor").iCombo("init",{
				    data : executorsArray,
		    	    allowSearch:true
				});*/
				$("#planForm").iForm("setVal",woFormData).iForm("endEdit");
				//回填div只读
				$("#reportFormDiv").show();
				$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
				$("#reportForm").iForm("setVal",{isHighLevel:woBasicData.isHighLevel});//是否高优先级
				$("#reportForm").iForm("setVal",woFormData).iForm("endEdit");
				if(woFormData.woIsDelay!=null && woFormData.woIsDelay=="Y"){	//延期过则不允许再延期
					$("#reportForm").iForm("show","woDelayType");
					$("#reportForm").iForm("show","woDelayReason");
					$("#reportForm").iForm("show","woDelayLenEnum");
				}
				else{
					$("#reportForm").iForm("hide","woDelayType");
					$("#reportForm").iForm("hide","woDelayReason");
					$("#reportForm").iForm("hide","woDelayLenEnum");
				}
				if(approveFlag == "approver"){
					$("#btn_wo_audit").show();//可审批
					FW.fixRoundButtons("#toolbar");
				}
			}
			else if(woStatus==doneStr){ //17已完成
				$("#workOrderForm").iForm("endEdit");
				if(woFormData.feedbackTime!=null){
					//策划div只读
					$("#planFormDiv").show();
					$("#planForm").iForm("init",{"fields":planFields,"options":{validate:true}});
					/*$("#f_woMaintainExecutor").iCombo("init",{
					    data : executorsArray,
			    	    allowSearch:true
					});*/
					//回填div只读
					$("#planForm").iForm("setVal",woFormData).iForm("endEdit");
					$("#reportFormDiv").show();
					$("#reportForm").iForm("init",{"fields":reportFields,"options":{validate:true}});
					$("#reportForm").iForm("setVal",woFormData).iForm("endEdit");
					if(woFormData.woIsDelay!=null && woFormData.woIsDelay=="Y"){	//延期过则不允许再延期
						$("#reportForm").iForm("show","woDelayType");
						$("#reportForm").iForm("show","woDelayReason");
						$("#reportForm").iForm("show","woDelayLenEnum");
					}
					else{
						$("#reportForm").iForm("hide","woDelayType");
						$("#reportForm").iForm("hide","woDelayReason");
						$("#reportForm").iForm("hide","woDelayLenEnum");
					}
				}
			}
			
			if(approveFlag != "approver"){
				if(woFormData.feedbackTime==null || woFormData.feedbackTime==""){
					$("#reportFormDiv").hide();
				}
				if(woFormData.cycleBeginTime==null || woFormData.cycleBeginTime==""){
					$("#planFormDiv").hide();
				}	
			}
	
			FW.fixRoundButtons("#toolbar");
		},"json");
	$(window).resize();
}

//提交工单基本信息
function commitWO(commitStyle){
	//表单验证
	var tempEquiName = $("#workOrderForm").iForm("getVal","equipName");
	if(tempEquiName == '' || tempEquiName == "请从左边设备树选择"){
		FW.error("请从左边设备树选择");
		return;
	}
	if(!$("#workOrderForm").valid()){
		return ;
	}
	if(!$("#planForm").valid()){
		return ;
	}
	if(!$("#reportForm").valid()){
		return ;
	}
	var operation = "";
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
		operation = "提交";
	}
	else{
		operation = "暂存";
	}
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woFormData = JSON.stringify(woFormObj);  //取表单值

	$.post(basePath + "workorder/workorder/commitWorkOrderdata.do",
		{"workOrderForm":woFormData,"commitStyle":commitStyle},
		function(data){
			if(data.result == "success"){
				FW.success(operation + "成功");
				woId = data.woId;
				if(commitStyle=="save"){ //暂存
					$("#workOrderForm").iForm("setVal",{"id":data.woId,"workOrderCode":data.workOrderCode});
					if(!woFormObj.currStatus){
						$("#btn_wo_deleteDiv").show();
						$("#btn_wo_delete").show();
					}
					FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
				}
				else{//提交
					var workFlow = new WorkFlow();
					var taskId = data.taskId;
					workFlow.submitApply(taskId,JSON.stringify(woFormData), submitSuccess,cancel,1);
				}
			}
			else {
				FW.error(operation + "失败");
				$("#btn_wo_commit").button('reset');
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

//开始审批
function audit(){  
	if(!$("#workOrderForm").valid()){
		return ;
	}
	if(!$("#planForm").valid()){
		return ;
	}
	if((woStatus == supervisorReportStr) && !$("#reportForm").valid()){
		return ;
	}
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woFormData = JSON.stringify(woFormObj);
	var planFormObj = $("#planForm").iForm("getVal");
	var planFormData = JSON.stringify(planFormObj);
	var reportFormObj = $("#reportForm").iForm("getVal");
	var reportFormData = JSON.stringify(reportFormObj);
	
	var woId = woFormObj.id;
	var workflowId = woFormObj.workflowId;
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woId;
	var variables = [{'name':'woSpecCode','value':woFormObj.woSpecCode},
	                 {'name':'isDefect','value':woFormObj.faultDegreeCode},
	                 {'name':'woExpertHandleStyle','value':woFormObj.woExpertHandleStyle},
	                 {'name':'isReAssign','value':woFormObj.woIsReAssign},
	                 {'name':'isDelay','value':reportFormObj.woIsDelay},
	                 {'name':'isHighLevel','value':reportFormObj.isHighLevel}];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var businessData = {
					"workOrderId":woId,
					"workOrderForm":woFormData,
					"planForm":planFormData,
					"reportForm":reportFormData,
					"isReAssign":woFormObj.woIsReAssign //策划重新分配环节  如果不重新分配就结束了，需要更新状态为已完成，否则不更新状态。
			};
			var workFlow = new WorkFlow();
			var multiple = 0;
			if(woStatus == dutyConfirmDefectStr || woStatus == planningGroupAuditStr){
				multiple = 1;
			}
			workFlow.showAudit(taskId,JSON.stringify(businessData),agree,rollback,stop,null,multiple);
		}
	});
}

//同意
function agree(){
	closeCurPage();
};

//回退
function rollback(){
	closeCurPage();
};

//终止
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

//编辑工单
function editWO(){
	$("#workOrderForm").iForm("beginEdit");
	$("#btn_wo_edit").hide();
	$("#btn_wo_save").show();
	FW.fixRoundButtons("#toolbar");
}

//删除工单
function deleteWO(){
	FW.confirm("确定删除|确定删除该条工单信息么？",function(){
		var woFormObj = $("#workOrderForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woId = woFormObj.id;
		$.post(basePath + "workorder/workorder/deleteWorkOrderDraft.do",{"woId":woId},
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
function obsoleteWO(){
	FW.confirm("确定作废|确定作废该条工单信息么？",function(){
		var woFormObj = $("#workOrderForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woId = woFormObj.id;

		$.post(basePath + "workorder/workorder/obsoleteWorkOrder.do",{"woId":woId},
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

//选中设备树事件
function passAssetSelect(data){
	$("#workOrderForm").iForm("setVal",{
		equipId : data.id,
		equipNameCode:data.assetCode,
		equipName : data.text
	});
}

//显示审批流程(流程未启动)
function showDiagram(){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}

//显示审批信息(流程已启动)
function showAuditInfo(){
	if( processInstId == null || processInstId == "" ){
		var defKey="workorder_"+siteId.toLowerCase()+"_wo";
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
		workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
}

//关闭当前tab 页
function closeCurPage(flag){
	if(flag){
		FW.set("eqWOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}

//工单权限
function setWoButtonPriv(){
	//var pri=_parent().privMapping;
	//console.log(pri);
	Priv.map("privMapping.OPR_PATROl_ADD","WO_ADD");
	Priv.map("privMapping.WO_SAVE","WO_SAVE");
	Priv.map("privMapping.WO_COMMIT","WO_COMMIT");
	Priv.map("privMapping.WO_EDIT","WO_EDIT");
	
	Priv.map("privMapping.WO_DELETE","WO_DELETE");
	Priv.map("privMapping.WO_INVALID","WO_INVALID");
	
	Priv.map("privMapping.WO_AUDIT","WO_AUDIT");
	Priv.apply();
	FW.fixRoundButtons("#toolbar");	
}

//跳转到关联工单
function openWoPageByCode(woCode){
	//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	var currTabId = FW.getCurrentTabId();
	var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	var urlPath = basePath+ "workorder/workorder/openWorkOrderAddPage.do?woCode=" + woCode;
	var opts = {
        id : "wo" + rand,
        name : "工单详情",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    };
    _parent()._ITC.addTabWithTree(opts); 
}

//树加载完成
function assetTreeRollBackFunc(){
	if(equipId != null && equipId.length>0){
		setTimeout(function(){
			window.parent.document.getElementById("woInfoAssestTree").contentWindow.expandForHintById(equipId);
		},500);
	}
}	