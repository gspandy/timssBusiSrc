
/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "申请单号", id : "applyCode",type : "label"},
		    {title : "状态", id : "applyStatus", type:"combobox",breakAll:true,
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_TOWERAPPLY_STATUS",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_TOWERAPPLY_STATUS",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {title : "单位名称", id : "applyCompany", rules : {required:true,maxChLength:130},wrapXsWidth:12,wrapMdWidth:8},
		    {title : "登塔人员", id : "towerPeople", rules : {required:true,maxChLength:130},wrapXsWidth:12,wrapMdWidth:8},
		    {title : "出机舱顶人员", id : "cabinRoof", wrapXsWidth:12,wrapMdWidth:8,rules : {maxChLength:130}},
		    {title : "风电场监护人", id : "windGuardian", linebreak:true,rules : {required:true,maxChLength:130}},
		    {title : "安全带培训人", id : "safetyTrainer", rules : {required:true,maxChLength:130}},
		    {title : "防坠滑块培训人", id : "slideBlockTrainer", rules : {required:true,maxChLength:130}},
		    {title : "助爬器培训人", id : "climbTrainer", rules : {required:true,maxChLength:130}},
		    {
		        title : "登塔原因", 
		        id : "applyReason",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:12,
		        height:70,
		        rules : {required:true,maxChLength:660}
		    }
		];

function setFormVal(towerApplyId){
	$.post(basePath + "workorder/towerApply/queryTowerApplyDataById.do",{id:towerApplyId},
			function(towerApplyData){
				var towerApplyFormData = eval("(" +towerApplyData.towerApplyForm+ ")");
				processInstId = towerApplyFormData.workflowId;
				taskId = towerApplyData.taskId;
				applyStatus = towerApplyFormData.applyStatus;
				//检查是否拥有操作权限
				var currHandlerIds = towerApplyFormData.currHandler;
				if(currHandlerIds != null && currHandlerIds.indexOf(loginUserId) >= 0){
					operPriv = true ;
				}
				
				$("#towerApplyForm").iForm("setVal",towerApplyFormData).iForm("endEdit");
				
				if(operPriv == false){  //如果申请单不在我手上
					$("#btn_wo_save1").hide();
	       			$("#btn_wo_commit").hide();
	       			$("#btn_wo_audit1").hide();
	       			$("#btn_wo_delete").hide();
	       			if(processInstId != null && processInstId!=""){
	       				$("#btn_flowDiagram").hide();
	       			}else{
	       				$("#btn_auditInfo").hide();
	       			}
				}else{  //如果申请单在我手上
					switch(applyStatus){
	        		case "draft":$("#btn_wo_save1").show();
					   			$("#btn_wo_commit").show();
					   			$("#btn_wo_audit1").hide();
					   			$("#btn_auditInfo").hide();
					   			$("#btn_flowDiagram").show();
					   			$("#btn_wo_delete").show();
					   			$("#btn_wo_obsolete").hide();
					   			$("#btn_towerApply_print").hide();
					   			$("#towerApplyForm").iForm("beginEdit");
					   			$("#towerApplyForm").iForm("endEdit","applyStatus");
					   			break;
	        		case "apply": $("#btn_wo_save1").show();
	       						   $("#btn_wo_commit").hide();
	       						   $("#btn_flowDiagram").hide();
	       						   $("#btn_wo_delete").hide();
	       						   $("#btn_wo_obsolete").show();
	       						   $("#btn_towerApply_print").hide();
	       						   $("#towerApplyForm").iForm("beginEdit");
	       						   $("#towerApplyForm").iForm("endEdit","applyStatus");
	       						   	break;
	        		case "finish":break;
	        		case "invalidate":$("#btn_wo_save1").hide();
						       			$("#btn_wo_commit").hide();
						       			$("#btn_flowDiagram").hide();
						       			$("#btn_wo_delete").hide();
						       			$("#btn_wo_audit1").hide();
						       			break;
	        		default:$("#btn_wo_save1").hide();
			       			$("#btn_wo_commit").hide();
			       			$("#btn_flowDiagram").hide();
			       			$("#btn_wo_delete").hide();
			       			break;
					}
				}
				
				createtime =  FW.long2time(towerApplyFormData.createDate);
				updatetime =  FW.long2time(towerApplyFormData.modifyDate);
				
				
				FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
			},"json");
}	


/**提交登塔基本信息*/
function commitTowerApply(commitStyle){  
	/**表单验证*/
	if(!$("#towerApplyForm").valid()){
		$("#btn_wo_commit").button('reset');
		return ;
	}
	
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
	}else{
		$("#btn_wo_save1").button('loading');
	}
	
	var towerApplyFormObj = $("#towerApplyForm").iForm("getVal");
	var cabinRoof = $.trim(towerApplyFormObj.cabinRoof);
	if(cabinRoof === ""){
		towerApplyFormObj.cabinRoof = cabinRoof;
	}
	var towerApplyFormData = JSON.stringify(towerApplyFormObj);  //取表单值
	
	 $.post(basePath + "workorder/towerApply/commitTowerApplydata.do",
	 		{"towerApplyForm": towerApplyFormData,"commitStyle":commitStyle},
			function(data){
				if(data.result == "success"){
						var taskId = data.taskId;
						towerApplyId = data.towerApplyId;
						workflowId = data.workflowId;
						if(commitStyle=="save"){ //暂存
							$("#towerApplyForm").iForm("setVal",{"id":towerApplyId});
							$("#btn_wo_edit1").hide();
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
							FW.success("保存成功");
							$("#btn_wo_save1").button('reset');
						}else{  //提交
							$("#btn_wo_commit").button('reset');
							var workFlow = new WorkFlow();
							var multiSelect = 0;
							workFlow.submitApply(taskId,towerApplyFormData,callback,cancel,multiSelect);
						}
				}else {
					if(commitStyle=='save'){
						$("#btn_wo_save1").button('reset');
					}else{
						$("#btn_wo_commit").button('reset');
					}
					FW.error("操作失败");
				}
	  },"json");
}

function deleteTowerApply(){
	Notice.confirm("确定删除|确定删除该条登塔申请信息？",function(){
		var woFormObj = $("#towerApplyForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var towerApplyId = woFormObj.id;
		
		 $.post(basePath + "workorder/towerApply/deleteTowerApplyDraft.do",{"id":towerApplyId},
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

function callback(){
	FW.success("提交成功");
	closeCurPage();
};
function cancel(){  //提交，启动流程，弹出审批框后，点击取消（不进入handler里面去）
	 $.post(basePath + "workorder/workorder/cancelCommitWO.do",
		 		{"towerApplyId":towerApplyId,"hasRollback":hasRollback},
				function(data){
					if(data.result == "success"){
						closeCurPage();
					}else {
						$("#btn_wo_commit").button('reset');
						FW.error("取消失败");
					}
		  },"json");
	
};
function audit(){  //审批
	/**表单验证*/
	if(!$("#towerApplyForm").valid()){
		return ;
	}
	var updateDesc = "";  //审批意见（在流程信息列表中显示）
	var towerApplyFormObj = $("#towerApplyForm").iForm("getVal");//取表单值
	var cabinRoofTrim = $.trim(towerApplyFormObj.cabinRoof);
	if(cabinRoofTrim === ""){
		towerApplyFormObj.cabinRoof = cabinRoofTrim;
	}
	var towerApplyForm = JSON.stringify(towerApplyFormObj);  //取表单值
	var towerApplyId = towerApplyFormObj.id;
	var cabinRoof = towerApplyFormObj.cabinRoof;
	if(cabinRoof===""){
		cabinRoof = "N";
	}else{
		cabinRoof = "Y";
	}
	workflowId = towerApplyFormObj.workflowId;
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = towerApplyId;
	var variables = [{"name":"cabinRoof","value":cabinRoof}];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var workFlow = new WorkFlow();
			var multiSelect = 1;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			if(applyStatus == "apply"){
				multiSelect = 0; 
			}
			workFlow.showAudit(taskId,towerApplyForm,agree,rollback,stop,updateDesc,multiSelect);
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


/**  作废登塔申请（非草稿，仅登塔发起人可以作废）  */
function obsoleteTowerApply(){
	Notice.confirm("确定作废|确定作废该条信息？",function(){
		var woFormObj = $("#towerApplyForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var towerApplyId = woFormObj.id;
		
		 $.post(basePath + "workorder/towerApply/obsoleteTowerApply.do",{"towerApplyId":towerApplyId},
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

function printTowerApply(){
	var src = fileExportPath + "preview?__report=report/TIMSS2_WO_TOWERAPPLY.rptdesign&__format=pdf"+
		"&id="+towerApplyId;
	var title = "登塔申请";
	FW.dialog("init",{
		src: src,
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