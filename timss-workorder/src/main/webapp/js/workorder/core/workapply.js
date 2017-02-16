
/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "工单ID", id : "woId", type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "申请单号", id : "workapplyCode",type : "hidden"},
		    {title : "工程名称", id : "name", rules : {required:true}},
		    {title : "工单ID", id : "woId",type:"hidden"},
		    {title : "工单编号", id : "woCode",
		    	render:function(id){
					var ipt = $("#" + id);
					ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
					ipt.ITCUI_Input();
					ipt.next(".itcui_input_icon").on("click",function(){
						var src = basePath + "workorder/workorder/parentWOList.do?type=woapply";
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
		    {title : "状态", id : "applyStatus", type:"combobox",breakAll:true,
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_WOAPPLY_STATUS",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_WOAPPLY_STATUS",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    {title : "施工单位", id : "workCom", rules : {required:true}},
		    {title : "施工负责", id : "workPrincipal", rules : {required:true}},
		    {title : "安全负责", id : "safePrincipal", rules : {required:true}},
		    {title : "验收等级", id : "checkLevel", type:"combobox",rules : {required:true},breakAll:true,
		    	options : {
		    		url :  ItcMvcService.getEnumPath() + "?data=WO_CHECK_LEVEL",
		    		"onRemoteData" : function(val){
		    	          return FW.parseEnumData("WO_CHECK_LEVEL",val);
		    	    },
		    	    remoteLoadOn : formRead?"set":"init"
		    	}
		    },
		    
		    {title : "开工日期", id : "startTime", type:"date", 
				   rules : {required:true} 
			    },
		    {title : "预计竣工日期", id : "endTime", type:"date", 
			    	rules : {required:true,greaterThan:"#f_startTime"}
			},
		    {
		        title : "施工条件及准备情况", 
		        id : "workCondition",
		        type : "textarea",
		        rules : {required:true,maxChLength:340},
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
		    {title : "施工条件确认人", id : "conditionConfirmUser", rules : {required:true}},
		    {
		        title : "安全措施", 
		        id : "safeItems",
		        type : "textarea",
		        rules : {required:true,maxChLength:340},
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:55
		    },
		    {title : "安全措施确认人", id : "safeConfirmUser", rules : {required:true}}
		    
		];
//交底人
var safeinformfields =  [
            		    {title : "安全交底人", id : "safeInformUser"}
            		];


function getEnumValueByCode(enumList,enumCode){
	var r="";
	for(var i in enumList){
		if(enumList[i][0]==enumCode){
			r=enumList[i][1];
			break;
		}
	}
	return r;
}


function setFormVal(woapplyId){
	$.post(basePath + "workorder/workapply/queryWoapplyDataById.do",{woapplyId:woapplyId},
			function(woapplyData){
				var woapplyFormData = eval("(" +woapplyData.workapplyForm+ ")");
				processInstId = woapplyFormData.workflowId;
				taskId = woapplyData.taskId;
				applyStatus = woapplyFormData.applyStatus;
				var woAttachment = woapplyData.attachmentMap;
				//检查是否拥有操作权限
				var currHandlerIds = woapplyFormData.currHandler;
				if(currHandlerIds != null && currHandlerIds.indexOf(loginUserId) >= 0){
					operPriv = true ;
					auditInfoShowBtn = 1 ;
				}
				$("#workapplyForm").iForm("setVal",woapplyFormData).iForm("endEdit");
				if (woAttachment.length > 0) {
					$("#uploadfileTitle").iFold("show");
					$("#uploadform").iForm("setVal", {"uploadfield" : woAttachment});
				} else if (woAttachment.length === 0) {
					$("#uploadfileTitle").iFold("hide");
				}
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
	       			$("#uploadform").iForm("endEdit");
	       			fillListData(woapplyData);
				}else{  //如果申请单在我手上
					switch(applyStatus){
	        		case "draft":$("#btn_wo_save1").show();
					   			$("#btn_wo_commit").show();
					   			$("#btn_wo_audit1").hide();
					   			$("#btn_auditInfo").hide();
					   			$("#btn_flowDiagram").show();
					   			$("#btn_wo_delete").show();
					   			$("#btn_wo_obsolete").hide();
					   			$("#btn_woapply_print").hide();
					   			$("#workapplyForm").iForm("beginEdit");
					   			$("#workapplyForm").iForm("endEdit",["applyStatus"]);
					   			$("#uploadfileTitle").iFold("show");
					   			fillListDataWithEdit(woapplyData);
					   			break;
	        		case "txkgsq": $("#btn_wo_save1").hide();
	       						   $("#btn_wo_commit").hide();
	       						   $("#btn_flowDiagram").hide();
	       						   $("#btn_wo_delete").hide();
	       						   $("#btn_woapply_print").hide();
	       						   $("#btn_wo_obsolete").show();
	       						   $("#workapplyForm").iForm("beginEdit");
	       						   $("#workapplyForm").iForm("endEdit",["applyStatus"]);
	       						   $("#uploadfileTitle").iFold("show");
	       						   fillListDataWithEdit(woapplyData);
	       						   	break;
	        		case "finish":$("#uploadform").iForm("endEdit");
	        						break;
	        		case "invalidate":$("#btn_wo_save1").hide();
						       			$("#btn_wo_commit").hide();
						       			$("#btn_flowDiagram").hide();
						       			$("#btn_wo_delete").hide();
						       			$("#btn_wo_audit1").hide();
						       			fillListData(woapplyData);
						       			$("#uploadform").iForm("endEdit");
						       			break;
	        		default:$("#btn_wo_save1").hide();
			       			$("#btn_wo_commit").hide();
			       			$("#btn_flowDiagram").hide();
			       			$("#btn_wo_delete").hide();
			       			fillListData(woapplyData);
			       			$("#uploadform").iForm("endEdit");
			       			break;
					}
				}
				createtime =  FW.long2time(woapplyFormData.createDate);
				updatetime =  FW.long2time(woapplyFormData.modifyDate);
				FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
			
			},"json");
}	

function fillListData(woapplyData){
	var woapplyFormData = eval("(" +woapplyData.workapplyForm+ ")");
	// 安全交底赋值
	$("#safeInformForm").iForm("setVal",woapplyFormData).iForm("endEdit");
	var operItemList = {};
	operItemList.safeDatas = eval("(" +woapplyData.safeInform+ ")");
	if(operItemList.safeDatas.length == 0){
		$("#title_safeInform").iFold("hide");
	}else{
		initSafeItemListByData("safeItemTest",operItemList);
	}
	fillDataGrid(woapplyData);
}

function fillListDataWithEdit(woapplyData){
	var woapplyFormData = eval("(" +woapplyData.workapplyForm+ ")");
	//安全交底赋值
	$("#safeInformForm").iForm("setVal",woapplyFormData);
	var operItemList = {};
	operItemList.safeDatas = eval("(" +woapplyData.safeInform+ ")");
	initSafeItemListByData("safeItemTest",operItemList);
	beginEditSafeItemList("safeItemTest");
	fillDataGrid(woapplyData);
	$("#title_worker").iFold("show");
	$("#title_risk").iFold("show");
	$("#workerTable").datagrid("showColumn","oper"); 
	$("#riskTable").datagrid("showColumn","oper"); 
	$("#btn_workerTable").show();
	$("#btn_riskTable").show();
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

/**提交工单基本信息*/
function commitWoapply(commitStyle){  
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
	}
	/**表单验证*/
	if(!$("#workapplyForm").valid()){
		$("#btn_wo_commit").button('reset');
		return ;
	}
	var woapplyFormObj = $("#workapplyForm").iForm("getVal");
	var woapplyFormData = JSON.stringify(woapplyFormObj);  //取表单值
	//取风险交底数据
	var safeInformFormObj = $("#safeInformForm").iForm("getVal");
	var safeinformObj = getSafeItemInputs("safeItemTest");
	var safeinform = JSON.stringify(safeinformObj);
	
	//取外来队伍施工人员数据
	var workerDataObj = $("#workerTable").datagrid("getData");
	var workerData =JSON.stringify(workerDataObj);
	//取风险评估数据
	var riskAssessmentDataObj = $("#riskTable").datagrid("getData")
	var riskAssessmentData = JSON.stringify(riskAssessmentDataObj);
	
	/*if(workerDataObj.total<1||riskAssessmentDataObj<1){
		$("#btn_wo_commit").button('reset');
		FW.error("");
		return ;
	}*/
	
	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	 $.post(basePath + "workorder/workapply/commitWoapplydata.do",
	 		{"workapplyForm":woapplyFormData,"workerData":workerData,"riskAssessmentData":riskAssessmentData,
		 "safeInformUser":safeInformFormObj.safeInformUser,"safeinform":safeinform,"commitStyle":commitStyle,"uploadIds" : ids},
			function(data){
				if(data.result == "success"){
						var taskId = data.taskId;
						woapplyId = data.woapplyId;
						workflowId = data.workflowId;
						if(commitStyle=="save"){ //暂存
							$("#workapplyForm").iForm("setVal",{"id":data.woapplyId,
			    				"workOrderCode":data.workOrderCode});
							$("#btn_wo_edit1").hide();
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
							FW.success("保存成功");
							$("#btn_wo_commit").button('reset');
						}else{  //提交
							var workFlow = new WorkFlow();
							var multiSelect = 0;
							var woapplyData = {"woapplyFormData":woapplyFormObj,"safeInformFormData":safeInformFormObj,
									"safeinformData":safeinformObj,"workerData":workerDataObj,
									"riskAssessmentData":riskAssessmentDataObj,"uploadIds":ids};
							workFlow.submitApply(taskId,JSON.stringify(woapplyData),callback,cancel,multiSelect);
							//workFlow.submitApply(taskId,woapplyFormData,callback,cancel,multiSelect);
						}
				}else {
					$("#btn_wo_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
}
function callback(){
	FW.success("提交成功");
	closeCurPage();
};
function cancel(){  //提交，启动流程，弹出审批框后，点击取消（不进入handler里面去）
	 $.post(basePath + "workorder/workorder/cancelCommitWO.do",
		 		{"woapplyId":woapplyId,"hasRollback":hasRollback},
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
	if(!$("#workapplyForm").valid()){
		return ;
	}
	var updateDesc = "";  //审批意见（在流程信息列表中显示）
	var woapplyFormObj = $("#workapplyForm").iForm("getVal");//取表单值
	//var woapplyFormData = JSON.stringify(woapplyFormObj);  //取表单值
	var woapplyId = woapplyFormObj.id;
	workflowId = woapplyFormObj.workflowId;
	
	//取风险交底数据
	var safeInformFormObj = $("#safeInformForm").iForm("getVal");
	//var safeinform = JSON.stringify(getSafeItemInputs("safeItemTest"));
	var safeinformObj = getSafeItemInputs("safeItemTest");
	
	endEdit("workerTable");
	endEdit("riskTable");
	//取外来队伍施工人员数据
	var workerDataObj =$("#workerTable").datagrid("getData");
	//取风险评估数据
	var riskAssessmentDataObj = $("#riskTable").datagrid("getData");
	// 获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	if(applyStatus == "txkgsq"){
		beginEdit("workerTable");
		beginEdit("riskTable");
	}
	
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woapplyId;
	var variables = [];
	params['variables'] = JSON.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woapplyData = {"woapplyFormData":woapplyFormObj,"safeInformFormData":safeInformFormObj,
					"safeinformData":safeinformObj,"workerData":workerDataObj,
					"riskAssessmentData":riskAssessmentDataObj,"uploadIds":ids};
			var workFlow = new WorkFlow();
			var multiSelect = 1;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			if(applyStatus == "txkgsq"){
				multiSelect = 0; 
			}
			workFlow.showAudit(taskId,JSON.stringify(woapplyData),agree,rollback,stop,updateDesc,multiSelect);
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
    data['businessId'] = woapplyId;
    
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
	$("#workapplyForm").iForm("beginEdit");  //打开编辑
	$("#btn_wo_edit1").hide();
	$("#btn_wo_save1").show();
	FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
}
function deleteWoapply(){
	Notice.confirm("确定删除|确定删除该条开工申请信息么？",function(){
		var woFormObj = $("#workapplyForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woapplyId = woFormObj.id;
		
		 $.post(basePath + "workorder/workapply/deleteWorkapplyDraft.do",{"woapplyId":woapplyId},
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
/**  作废开工申请（非草稿，仅工单发起人可以作废）  */
function obsoleteWoapply(){
	Notice.confirm("确定作废|确定作废该条信息么？",function(){
		var woFormObj = $("#workapplyForm").iForm("getVal");
		var woFormData = JSON.stringify(woFormObj);  //取表单值
		var woapplyId = woFormObj.id;
		
		 $.post(basePath + "workorder/workapply/obsoleteWorkapply.do",{"woapplyId":woapplyId},
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

function printWoapply(){
	//var fileExportPath = "http://10.0.17.153:8080/itc_report/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_WO_WOAPPLY.rptdesign&__format=pdf"+
		"&WoapplyId="+woapplyId;
	var title = "开工申请";
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
 * 初始化附件
 */
function initUploadform() {
	var uploadFiles="";
	$("#uploadform").iForm('init', {
		"fields" : [
           	{
           		id:"uploadfield", 
           		title:" ",
           		type:"fileupload",
           		linebreak:true,
           		wrapXsWidth:12,
           		wrapMdWidth:12,
           		options:{
           		    "uploader" : basePath+"upload?method=uploadFile&jsessionid="+sessionid,
           		    "delFileUrl" : basePath+"upload?method=delFile&key="+delKey,
           			"downloadFileUrl" : basePath + "upload?method=downloadFile",
           			"swf" : basePath + "js/workorder/common/uploadify.swf",          			
           			"initFiles" : uploadFiles,
           			"delFileAfterPost" : true
           		}
           	}
           ],
		"options" : {
			"labelFixWidth" : 6,
			"labelColon" : false
		}
	});
}