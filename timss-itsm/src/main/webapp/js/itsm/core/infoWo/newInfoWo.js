/*
 * serType:{"equipmentBorrow","faultMaintenance","businessApply"}
 * businessType:{"A","B"}
 * handlerType:{"endWork","reSend"}
 */

/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "申请单号", id : "infoWoCode",type : "hidden"},
		    {title : "名称", id : "name",rules : {required:true,maxChLength:50},
		    	linebreak:true,
		    	wrapXsWidth:12,
		        wrapMdWidth:8
		    },
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
//					initOnChange: false,
					allowEmpty:true,
					onChange:function(val){
						if(val == itsmInfoWoSerType.BUSINESS){  //业务申请
							$("#infoWoForm").iForm("show",["businessType"]);
							$("#btn_showPrincipal").hide();
							FW.fixRoundButtons("#toolbar");
						}if(val == itsmInfoWoSerType.FAULT){   //故障报修
							$("#infoWoForm").iForm("hide",["businessType"]);
							$("#btn_showPrincipal").show();
						}else if(val == itsmInfoWoSerType.BORROW){ //设备借出
							$("#infoWoForm").iForm("hide",["businessType"]);
							$("#btn_showPrincipal").hide();
							FW.fixRoundButtons("#toolbar");
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
		    {title : "工号", id : "applyUser", type : "hidden"},
		    {title:"申请人",id:"applyUserName",type:"label", linebreak:true},
			{title : "申请人部门", id : "applyDeptName", type : "hidden"},
			{title : "申请时间", id : "applyTime", type : "hidden"},
			{title : "申请人电话", id : "applyUserPhone", rules : {required:true,maxChLength:20}},	  
		    {
		        title : "问题描述", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {required:true,maxChLength:500}
		    }
		];

function initFormData(infoWoId){
	 $.post(basePath + "itsm/infoWo/queryInfoWoById.do",{"id":infoWoId},
				function(data){
			var infoWoFormData = JSON.parse(data.infoWoForm);
			infoWoFormData.applyUserName = infoWoFormData.applyUserName+" / "+infoWoFormData.applyDeptName;
			var attachmentData = data.attachmentMap ;
			status = infoWoFormData.status;
			var serType = infoWoFormData.serType;
			createtime =  FW.long2time(infoWoFormData.createdate);
			updatetime =  FW.long2time(infoWoFormData.modifydate);
			processInstId = infoWoFormData.workflowId;
			taskId = data.taskId;
			var currHandler = infoWoFormData.currHandler;
			//是否有权限审批
			var flag = false;
			if(currHandler != null && currHandler != ""){
				flag = isMyActivityWO(currHandler.split(','),loginUserId);
				if(flag){
					auditInfoShowBtn = 1;
				}
			}
			if(processInstId){
				$("#btn_flowDiagram").hide();
				$("#btn_delete").hide();
			}else{
				$("#btn_auditInfo").hide();
				$("#btn_obsolete").hide();
			}
			$("#infoWoForm").iForm("setVal",infoWoFormData);
			$("#infoWoForm").iForm("endEdit",["serType","businessType"]);
			$("#uploadfileTitle").iFold("show"); 
			$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
			FW.fixRoundButtons("#toolbar");
		},"json");
	
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}

function commit(commitStyle){
	var serCataName = $("#infoWoForm").iForm("getVal").serCataName;
	if(serCataName == '' || serCataName == "请从左边目录树选择服务目录"){
		FW.error("请从左边树选择服务目录");
		return;
	}
	
	/**表单验证*/
	if(!$("#infoWoForm").valid()){
		return ;
	}
	var infoWoFormObj = $("#infoWoForm").iForm("getVal");
	
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	infoWoFormObj.uploadIds = ids ;
	if(commitStyle=="commit" && processInstId!=null && processInstId!=""){
		 audit();
	}else{
		$.post(basePath + "itsm/infoWo/commitInfoWodata.do",
		 		{"infoWoForm":FW.stringify(infoWoFormObj),"commitStyle":commitStyle},
				function(data){
					if(data.result == "success"){
						taskId = data.taskId;
						infoWoId = data.infoWoId;
						if(commitStyle=="save"){
							$("#infoWoForm").iForm("setVal",{"id":data.infoWoId,
			    				"infoWoCode":data.infoWoCode});
						}else{
							processInstId = data.workflowId;
							taskId = data.taskId;
							$("#infoWoForm").iForm("setVal",{"id":infoWoId,
			    				"infoWoCode":data.infoWoCode,"workflowId":processInstId});
							$("#btn_wo_audit1").show();
							$("#btn_wo_commit").hide();
							$("#btn_wo_save1").hide();
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题\
							infoWoFormObj = $("#infoWoForm").iForm("getVal");
							var infoWoFormData = {"infoWoForm":infoWoFormObj,"attachmentIds":ids};
							var workFlow = new WorkFlow();
							workFlow.submitApply(taskId,FW.stringify(infoWoFormData),closeTab,null,0);
						}
						FW.success("保存成功");
					}else {
						FW.error("操作失败");
					}
		  },"json");
	}
	 
	
}

function audit(){  //审批
	var infoWoFormObj = $("#infoWoForm").iForm("getVal");
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	infoWoFormObj.uploadIds = attachmentIds ;
	
	
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = processInstId;
	params['businessId'] = infoWoId;
	var variables = [{}]; 
	params['variables'] = FW.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woSendFormData = {"infoWoForm":FW.stringify(infoWoFormObj),"attachmentIds":attachmentIds};
			var workFlow = new WorkFlow();
			var updateDesc = "";
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(woSendFormData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}

function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
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

function stop(rowdata){
	closeTab();
}

function obsoleteInfoWo(){
	Notice.confirm("确定作废|确定作废该信息工单么？",function(){
		 $.post(basePath + "itsm/infoWo/obsoleteInfoWo.do",{"id":infoWoId},
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
function deleteInfoWo(){
	Notice.confirm("确定作废|确定删除该信息工单么？",function(){
		 $.post(basePath + "itsm/infoWo/deleteInfoWo.do",{"id":infoWoId},
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
