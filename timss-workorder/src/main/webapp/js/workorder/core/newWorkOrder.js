
/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "工单编号", id : "workOrderCode",type : "hidden"},
		    {title : "名称", id : "description", rules : {required:true}},
		    {title : "维护计划ID", id : "maintainPlanId", type : "hidden"},
		    {title : "维护计划来源",id : "maintainPlanFrom", type : "hidden"},
		    {title : "作业方案ID", id : "jobPlanId", type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "状态", id : "currStatus", type:"combobox",breakAll:true,
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_STATUS",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_STATUS",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {
		    	title : "工单类型", 
		    	id : "workOrderTypeCode", 
		    	type:"combobox",
		    	rules : {required:true},
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_TYPE",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_TYPE",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {
		    	title : "缺陷程度", 
		    	id : "faultDegreeCode", 
		    	type:"combobox",
		    	rules : {required:true},
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_FAULT_DEGREE",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_FAULT_DEGREE",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {
		    	title : "专业",
		    	id : "woSpecCode",
		    	type : "combobox",
		    	rules : {required:true},
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_SPEC",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_SPEC",val);
		    	    },
		    	    allowEmpty : true,
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {title : "报障设备", id : "equipName",type:"label",value:"请从左边设备树选择",
		    	formatter:function(val){
					var text = val;
					if(text=="请从左边设备树选择"){
						text = "<label style='color:red'>"+val+"</label>";
					}
					return text;
				}
		    },
		    {title : "设备ID", id : "equipId",type : "hidden"},
		   {title : "设备编号", id : "equipNameCode",type : "hidden"},
		    
		   /* {title : "设备位置", id : "equipSite", type : "hidden"},
		    {title : "设备位置ID", id : "equipSiteId",type : "hidden"},
		    {title : "设备位置编号", id : "equipSiteCode", type : "hidden"},*/
		    
		    {title : "发现时间", id : "discoverTime", type:"datetime", dataType:"datetime",
			   rules : {required:true} 
		    },
		    {title : "父工单ID", id : "parentWOId",type:"hidden"},
		    {title : "父工单", id : "parentWOCode",
		    	render:function(id){
	            	 var ipt = $("#" + id);
	            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
	            	 ipt.ITCUI_Input();
	            	 ipt.next(".itcui_input_icon").on("click",function(){
	            		 var src = basePath + "workorder/workorder/parentWOList.do";
	                     var dlgOpts = {
	                         width : 600,
	                         height:500,
	                         closed : false,
	                         title:"双击选择父工单",
	                         modal:true
	                     };
	                     Notice.dialog(src,dlgOpts,null);
	                 });
	             }
	        },
	        {title : "缺陷记录ID", id : "woDefectId",type:"hidden"},
	        {title : "缺陷记录编号", id : "woDefectCode"},
		    {
	        	title : "紧急程度",
	        	id : "urgentDegreeCode",
	        	type : "combobox",
	        	options : {
	        		url :  ItcMvcService.getEnumPath() + "?data=WO_URGENCY_DEGREE",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_URGENCY_DEGREE",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
			},
		    {
		        title : "备注", 
		        id : "remarks",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
		    {title : "流程实例ID", id : "workflowId",type : "hidden"}
		    
		];
var handlerStylefields = [
						{title : "当前风速(m/s)",id : "currWindSpeed",rules : {number:true,min:0}},
                        {
              		    	title : "是否立即处理",
              		    	id : "isNowHandlerMonitor",
              		    	type : "combobox",
              		    	rules : {required:true},
              		    	dataType:"enum",
              			    enumCat:"WO_NOW_HANDLER_MONITOR"
              		    },
              		    {
              		    	title : "是否立即处理",
              		    	id : "isNowHandlerAssistant",
              		    	type : "combobox",
              		    	rules : {required:true},
              		    	dataType:"enum",
              			    enumCat:"WO_NOW_HANDLER_ASSISTANT"
              		    },
              		    {
              		    	title : "是否立即处理",
              		    	id : "isNowHandlerPlantLeader",
              		    	type : "combobox",
              		    	rules : {required:true},
              		    	dataType:"enum",
              			    enumCat:"WO_NOW_HANDLER_PLANTLEADER"
              		    }
                    ];
/**  查看工单基本信息，给form表单赋值
 * @param woId
 */
function setFormVal(woId){
	$.post(basePath + "workorder/workorder/queryWODataById.do",{workOrderId:woId},
			function(woBasicData){
				var woFormData = eval("(" +woBasicData.workOrderForm+ ")");
//				console.log(woFormData);
				processInstId = woFormData.workflowId;
				equipId = woFormData.equipId;
				$("#workOrderForm").iForm("setVal",woFormData).iForm("endEdit");
				woStatus = woFormData.currStatus;
				createtime =  FW.long2time(woFormData.createDate);
				updatetime =  FW.long2time(woFormData.modifyDate);
				setTimeout(function(){_itc_addellipsis();},50);
				switch(woStatus){
	        		case "monitorAudit":
	        					$("#handlerStyleForm").iForm("init",{"fields":handlerStylefields,"options":{validate:true}});
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerAssistant");
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerPlantLeader"); 
	        					$("#btn_wo_save1").remove();
	        					$("#btn_wo_edit1").hide();
	        					$("#btn_wo_commit").remove();
	        					$("#btn_flowDiagramDiv").hide();
	        					$("#btn_wo_delete").hide();
	        					$("#btn_wo_obsolete").hide();
	        					break;
	        		case "assistantAudit":
	        					$("#handlerStyleForm").iForm("init",{"fields":handlerStylefields,"options":{validate:true}});
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerMonitor");
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerPlantLeader"); 
	        					$("#handlerStyleForm").iForm("hide","currWindSpeed");
	        					$("#btn_wo_save1").remove();
	        					$("#btn_wo_edit1").hide();
	        					$("#btn_wo_commit").remove();
	        					$("#btn_flowDiagramDiv").hide();
	        					$("#btn_wo_delete").hide();
	        					$("#btn_wo_obsolete").hide();
	        					break;
	        		case "plantLeaderAudit":
	        					$("#handlerStyleForm").iForm("init",{"fields":handlerStylefields,"options":{validate:true}});
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerMonitor"); 
	        					$("#handlerStyleForm").iForm("hide","isNowHandlerAssistant");
	        					$("#handlerStyleForm").iForm("hide","currWindSpeed");
	        					$("#btn_wo_save1").remove();
	        					$("#btn_wo_edit1").hide();
	        					$("#btn_wo_commit").remove();
	        					$("#btn_flowDiagramDiv").hide();
	        					$("#btn_wo_delete").hide();
	        					$("#btn_wo_obsolete").hide();
	        					break;
	        		case "assistantToTeamleader":
	        					$("#handlerStyleForm").iForm("init",{"fields":handlerStylefields,"options":{validate:true}});
	        					$("#btn_flowDiagramDiv").hide();
	        					$("#btn_wo_save1").remove();
	        					$("#btn_wo_edit1").hide();
	        					$("#btn_wo_commit").remove();
	        					$("#btn_wo_delete").hide();
	        					$("#btn_wo_obsolete").hide();
	        					break;
	        		case "newWo":
	        			hasRollback = "yes" ;  //标识是回退的工单
	        			$("#inPageTitle").html("新建工单");
        				$("#btn_wo_edit1").hide();
        				$("#btn_wo_delete").hide();
    					$("#btn_wo_obsolete").hide();
	        			$("#btn_wo_save1").show(); 
	        			
    					if(processInstId && woFormData.createuser == loginUserId){ 
    						$("#btn_flowDiagram").hide();
							$("#btn_auditInfo").show();  
							$("#btn_wo_audit1").show();  //审批按钮
							$("#btn_wo_obsolete").show();  //作废按钮
							$("#btn_wo_commit").hide(); //提交按钮隐藏
							
							//TODO 加作废功能是改为显示
							/*$("#btn_wo_obsolete").hide();  *///作废按钮
							$("#workOrderForm").iForm("beginEdit");
							$("#workOrderForm").iForm("endEdit","currStatus");
						}else{ //流程ID不为空，即退回的情况
							$("#btn_auditInfo").hide();
						}
    					break;
	        		case "draft":
        				$("#btn_wo_obsolete").hide();
        				$("#workOrderForm").iForm("beginEdit");  //打开编辑
        				$("#workOrderForm").iForm("endEdit","currStatus");
        				$("#btn_wo_edit1").hide();
        				$("#btn_wo_save1").show();
	        			
	        			$("#workOrderForm").iForm("endEdit","currStatus");
	        			$("#btn_wo_audit1").hide();
	        			
	        			if(!processInstId){ 
	        				$("#btn_auditInfo").hide();
						}else{ //流程ID不为空，即退回的情况
							$("#btn_flowDiagram").hide();
							$("#btn_auditInfo").show();
						}
	        			
	        			FW.createAssetTree({multiSelect:false});
	        			FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
	        			//展开树，显示选中的树
	        			if(equipId != null){
	           				setTimeout(function(){
	           							document.getElementById("itcEmbbedAssetTree").contentWindow.expandForHintById(equipId);
	           							},500);
	           			}
	        	}
				
				if(woStatus == "assistantToTeamleader"){
					$("#handlerStyleForm").iForm("setVal",woFormData).iForm("endEdit");
					$("#handlerStyleForm").hide();
				}
				processInstId = woFormData.workflowId;
				taskId = woBasicData.taskId;
				candidateUsers = woBasicData.candidateUsers;
				var flag = isMyActivityWO(candidateUsers,loginUserId);
				
				if(woStatus=="draft" && woFormData.createuser == loginUserId ){
					flag = true;
					$("#btn_wo_audit1").remove();
					if(processInstId){  //草稿状态，创建者是自己，并且流程ID不为空（即回退回来的工单）
						$("#btn_wo_delete").hide();
						$("#btn_wo_obsolete").show();
						$("#btn_auditInfo").show();
						
					}
				}
				if(!flag){
					$("#workOrderForm").iForm("endEdit");
					$("#btn_wo_delete").hide();
					$("#btn_wo_operDiv").hide();
					$("#handlerStyleForm").hide();
				}
					
				
				FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
			},"json");
}	

/** 根据维护计划生成新的工单
 * @param mtpId 维护计划ID
 */
function setFormValFromMtp(mtpId){
	
	$.post(basePath + "workorder/maintainPlan/queryMTPDataById.do",{maintainPlanId:mtpId},
			function(mtpFullData){
				var mtpFormData = eval("(" +mtpFullData.maintainPlanForm+ ")");
				mtpId = mtpFormData.id; 
				mtpFormData.maintainPlanId = mtpId; //维护计划ID
				mtpFormData.id= ""; //赋值之后变成工单的ID
				
				mtpFormData.currStatus = null; //将草稿状态变成空
				$("#workOrderForm").iForm("endEdit","currStatus");
				 
				$("#workOrderForm").iForm("setVal",mtpFormData)
				
				$("#inPageTitle").html("新建工单");
       			$("#btn_wo_edit1").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#btn_wo_audit1").remove();
       			
       			$("#btn_auditInfo").hide();
       			FW.createAssetTree({multiSelect:false});
       			FW.setTreeStat("fold");
       			FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
				 
			},"json");
}	


/** 根据维护计划生成新的工单
 * @param mtpId 维护计划ID
 */
function setFormValFromQx(woQxId){
	
	$.post(basePath + "workorder/woQx/queryWoQxDataById.do",{woQxId:woQxId},
			function(woQxData){
				var woQxId = woQxData.id;
				var woQxCode = woQxData.defectCode;
				var woQxDefectDes = woQxData.defectDes;
				var woQxEquipId = woQxData.equipId;
				var woQxEquipName = woQxData.equipName;
				var woQxDefectTime = woQxData.defectTime;
				var woQxDataToWoForm = {"woDefectId":woQxId,
										"woDefectCode":woQxCode,
										"description":woQxDefectDes,
										"equipId":woQxEquipId,
										"equipName":woQxEquipName,
										"discoverTime":woQxDefectTime}
				$("#workOrderForm").iForm("setVal",woQxDataToWoForm)
				
				$("#inPageTitle").html("新建工单");
       			$("#btn_wo_edit1").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#btn_wo_audit1").remove();
       			
       			$("#btn_auditInfo").hide();
//       			FW.createAssetTree({multiSelect:false});
//       			FW.setTreeStat("fold");
//       			FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
				 
			},"json");
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
/**提交工单基本信息*/
function commitWO(commitStyle){  
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
	}
	/**表单验证*/
	var tempEquiName = $("#workOrderForm").iForm("getVal","equipName");
	if(tempEquiName == '' || tempEquiName == "请从左边设备树选择"){
		FW.error("请从左边设备树选择");
		$("#btn_wo_commit").button('reset');
		return;
	}
	if(!$("#workOrderForm").valid()){
		$("#btn_wo_commit").button('reset');
		return ;
	}
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woFormData = JSON.stringify(woFormObj);  //取表单值
	
	 $.post(basePath + "workorder/workorder/commitWorkOrderdata.do",
	 		{"workOrderForm":woFormData,"commitStyle":commitStyle},
			function(data){
				if(data.result == "success"){
						var taskId = data.taskId;
						woId = data.woId
						if(commitStyle=="save"){ //暂存
							$("#workOrderForm").iForm("setVal",{"id":data.woId,
			    				"workOrderCode":data.workOrderCode});
							$("#btn_wo_edit1").hide();
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
							FW.success("保存成功");
							$("#btn_wo_commit").button('reset');
						}else{  //提交
							var workFlow = new WorkFlow();
							var multiSelect = 0;
							workFlow.submitApply(taskId,JSON.stringify(woFormData),callback,cancel,multiSelect);
							
						}
				}else {
					$("#btn_wo_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
	  
	/*$("#workOrderForm").iForm("endEdit");*/  //关闭编辑
}
function callback(){
	FW.success("提交成功");
	closeCurPage();
};
function cancel(){  //提交，启动流程，弹出审批框后，点击取消（不进入handler里面去）
	 $.post(basePath + "workorder/workorder/cancelCommitWO.do",
		 		{"woId":woId,"hasRollback":hasRollback},
				function(data){
					if(data.result == "success"){
//						$("#btn_wo_commit").button('reset');
						closeCurPage();
					}else {
						$("#btn_wo_commit").button('reset');
						FW.error("取消失败");
					}
		  },"json");
	
};
function audit(){  //审批
	var updateDesc = "";  //审批意见（在流程信息列表中显示）
	
	if(woStatus == "monitorAudit"){
		if(!$("#handlerStyleForm").valid() ){
			return ;
		}
	}
	
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woType = woFormObj.workOrderTypeCode;
	var woFormData = JSON.stringify(woFormObj);  //取表单值
	
	var woId = woFormObj.id;
	var workflowId = woFormObj.workflowId;
	
	var handlerStyleFormObj = $("#handlerStyleForm").iForm("getVal");
	var monitorHandlerStyle = handlerStyleFormObj.isNowHandlerMonitor;
	var assistantHandlerStyle = handlerStyleFormObj.isNowHandlerAssistant;
	var plantLeaderHandlerStyle = handlerStyleFormObj.isNowHandlerPlantLeader;
		
	var woFormData = woFormObj;  //取表单值
	var handlerStyleFormData = handlerStyleFormObj;  //取表单值
	
	switch(woStatus)
	{
	case "newWO":updateDesc="新建工单"; break;
	case "monitorAudit":updateDesc="班长审核"; break;
	case "assistantAudit":updateDesc="场长助理审核"; break;
	case "plantLeaderAudit":updateDesc="场长审核"; break;
	case "assistantToTeamleader":updateDesc="助理指定工作负责人"; break;
	}
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woId;
	var variables = [{'name':'woType','value':woType},
	                 {'name':'monitorHandlerStyle','value':monitorHandlerStyle},
	                 {'name':'assistantHandlerStyle','value':assistantHandlerStyle},
	                 {'name':'plantLeaderHandlerStyle','value':plantLeaderHandlerStyle}];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woHandlerFormData = {"handlerStyleFormData":handlerStyleFormData,
				 	"workOrderId":woId,"workOrderForm":woFormData};
			var workFlow = new WorkFlow();
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,JSON.stringify(woHandlerFormData),agree,rollback,stop,updateDesc,multiSelect);
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
function editWO(){
	$("#workOrderForm").iForm("beginEdit");  //打开编辑
	$("#btn_wo_edit1").hide();
	$("#btn_wo_save1").show();
	FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
}
function deleteWO(){
	Notice.confirm("确定删除|确定删除该条工单信息么？",function(){
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
/**  作废工单（非草稿，仅工单发起人可以作废）  */
function obsoleteWO(){
	Notice.confirm("确定作废|确定作废该条工单信息么？",function(){
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
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("eqWOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}