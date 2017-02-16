
/* 表单字段定义  */
var fields = [
              {title : "名称", id : "woName",
  		        wrapXsWidth:12,wrapMdWidth:8,height:110},
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "工单编号", id : "workOrderCode"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "申请人电话", id : "customerPhone", rules : {required:true}},
		    {title : "工号", id : "customerCode"},
		    {title : "姓名", id : "customerName", rules : {required:true}},
		    {title : "申请人公司", id : "customerCom"},
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
				enumCat : "ITSM_TYPE",
				options:{
					allowEmpty:false,
					onChange:function(val){
						setTimeout(function(){
				    		if(val == "hbWoType"){
				    			if(!isEngineer && woId==null){
				    				$("#workOrderForm").iForm("setVal",{"workOrderTypeCode":"qxWoType"});
				    				FW.error("您无权新建此类工单");
				    				return ;
				    			}
				    			/*setTimeout(function(){*/
					    			$("#workOrderForm").iForm("hide",["defaultBeginTime","defaultEndTime","needInfoCenterAudit",
					    			                                  "sendWoNow","appointTime"]);
					    			$("#workOrderForm").iForm("show",["customerPhone","customerCode","customerName",
					    			                                  "customerLocation","workOrderTypeCode","faultTypeName", 
					    			                                  "urgentDegreeCode","influenceScope", "priorityId",
					    			                                  "description","discoverTime"]);
				    			/*},200);*/
								FW.fixRoundButtons("#toolbar");  
				    		}else if(val == "whWoType"){
				    			if(!isItsmAdmin){ //IT服务管理员（ITC的）
				    				$("#workOrderForm").iForm("setVal",{"workOrderTypeCode":"qxWoType"});
				    				FW.error("您无权新建此类工单");
				    				return ;
				    			}
				    			$("#workOrderForm").iForm("show",["urgentDegreeCode","influenceScope",
				    			                                  "faultTypeName","appointTime","sendWoNow"]);
				    			$("#workOrderForm").iForm("hide",["sendWoNow","discoverTime"]);
				    			//TODO 异步时解决办法（不好的办法）
				    			/*setTimeout(function(){*/
				    				$("#workOrderForm").iForm("hide",["customerPhone","customerCode","customerName",
				    				                                  "customerLocation", "customerCom","customerDept",
				    				                                  "priorityId","defaultBeginTime","defaultEndTime"]);
				    			/*},200);*/
								FW.fixRoundButtons("#toolbar");  
				    		}else if(val == "rwxWoType"){
				    			if(!isMtpCharge){
				    				$("#workOrderForm").iForm("setVal",{"workOrderTypeCode":"qxWoType"});
				    				FW.error("您无权新建此类工单");
				    				return ;
				    			}
				    			/*setTimeout(function(){*/
				    				$("#workOrderForm").iForm("show",["urgentDegreeCode","influenceScope",
				    				                                  "faultTypeName","defaultBeginTime","defaultEndTime"]);
				    			/*},200);	*/
				    			$("#workOrderForm").iForm("hide",["customerPhone","customerCode","customerName",
				    			                                  "customerCom","customerDept","customerLocation","priorityId",
				    			                                  "appointTime","sendWoNow","discoverTime"]); 
				    			
				    		}else{
				    			$("#workOrderForm").iForm("show",["customerPhone","customerCode","customerName",
				    			                                 "customerLocation", "faultTypeName","needInfoCenterAudit",
				    			                                 "urgentDegreeCode", "influenceScope","priorityId",
				    			                                  "appointTime"]);
				    			/*setTimeout(function(){*/
				    				$("#workOrderForm").iForm("hide",["defaultBeginTime","defaultEndTime","discoverTime"]);
				    			/*},200);	*/
				    			if(isITCSer){
				    				$("#workOrderForm").iForm("show",["sendWoNow"]);
				    				$("#workOrderForm").iForm("setVal",{"sendWoNow":"yes"});
				    			}else{
				    				$("#workOrderForm").iForm("hide",["sendWoNow"]);
				    				$("#workOrderForm").iForm("setVal",{"sendWoNow":"no"});
				    			}
								FW.fixRoundButtons("#toolbar");  
				    		}
				    	},200);
				    }
				}
			},
  			{title : "服务目录Id", id : "faultTypeId",type : "hidden"},
		    {title : "服务目录",id : "faultTypeName",type:"label",value:"请从左边目录树选择服务目录",
  				rules : {required:true},
  				formatter:function(val){
  					var text = val;
					if(text=="请从左边目录树选择服务目录"){
						text = "<label style='color:red'>"+val+"</label>";
					}
					return text;
  				}
  			},
		    {title : "服务性质Id", id : "serCharacterId",type : "hidden"},
		    {title : "服务性质",id : "serCharacterName",type:"label",value:"请从左边目录树选择性质",rules : {required:true},
			    formatter:function(val){
					var text = val;
					if(text=="请从左边目录树选择性质"){
						text = "<label style='color:red'>"+val+"</label>";
					}
					return text;
				}
		    },
			
			{title : "影响度", id : "influenceScope",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_INFLUENCE_SCOPE",
				options:{
					allowEmpty:false,
					onChange:function(val){
						var urgentVal = $("#f_urgentDegreeCode").iCombo("getVal");
				    	setPriIdValByUrgentInfluence(urgentVal,val);
			    	}
				}
			},
			{title : "紧急度", id : "urgentDegreeCode",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_URGENCY_DEGREE",
				options:{
					allowEmpty:false,
					onChange:function(val){
						var influenceVal = $("#f_influenceScope").iCombo("getVal");
			    		setPriIdValByUrgentInfluence(val,influenceVal);
			    	}
				}
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
			{title : "预约开工时间", id : "appointTime", type:"datetime", dataType:"datetime",
				rules : {greaterThan:FW.long2time(new Date().getTime())}
			},
			{title : "计划开工时间", id : "defaultBeginTime", type:"datetime", dataType:"datetime",
          	  rules : {required:true} 
            },
  		  	{title : "计划完工时间", id : "defaultEndTime", type:"datetime", dataType:"datetime",
  			  rules : {required:true,greaterThan:"#f_defaultBeginTime"}
            },
            {
		        title : "需信息中心审批", 
		        id : "needInfoCenterAudit",
		        type : "combobox",
		        rules : {required:true},
		        data : [
		            ['yes','需要'],
		            ['no','不需要',true]
		        ],
		        options:{
					allowEmpty:false,
					onChange:function(val,obj){
						var needInfoCenterAudit = $("#f_needInfoCenterAudit").iCombo("getVal");
						if(isITCSer){  //是客服登录
							if(needInfoCenterAudit=="no"){
				    			$("#workOrderForm").iForm("show",["sendWoNow"]);
				    		}else{
				    			$("#workOrderForm").iForm("hide",["sendWoNow"]);
				    		}
						}
			    		
			    	}
				}
		    },
		    {
		        title : "立即派单", 
		        id : "sendWoNow",
		        type : "radio",
		        rules : {required:true},
		        data : [
		            ['yes','是'],
		            ['no','否']
		        ]
		    },
		    {title : "当前处理人", id : "currHandUserName",type:"label"},
		    
		    {
		        title : "故障/任务描述", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {maxChLength:680}
		    }
		];

//根据电话号码输入提示
fields[4].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByOfficeTel'));
};
//根据工号输入提示
fields[5].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByUserId'));
};
//根据姓名输入提示
fields[6].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByUserName'));
};
function getiHintParams(searchStyle){
	var iHintParams ={
			datasource : basePath + "itsm/woUtil/userMultiSearch.do?searchStyle="+searchStyle,
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				if(rowdata != null){
					if(rowdata.name!=null){
						$("#f_customerName").val(rowdata.name);
					}
					if(rowdata.id!=null){
						$("#f_customerCode").val(rowdata.id);
						var faultTypeId = $("#f_faultTypeId").val();
						getInitPriority(rowdata.id,faultTypeId);
					}
					if(rowdata.phone!=null){
						$("#f_customerPhone").val(rowdata.phone);
					}else{
						$("#f_customerPhone").val("");
					}
					if(rowdata.location!=null){
						$("#f_customerLocation").val(rowdata.location);
					}else{
						$("#f_customerLocation").val("");
					}
					if(rowdata.comName!=null){
						$("#f_customerCom").val(rowdata.comName);
					}else{
						$("#f_customerCom").val("");
					}
					if(rowdata.orgName!=null){
						$("#f_customerDept").val(rowdata.orgName);
					}else{
						$("#f_customerDept").val("");
					}
				}
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = "";
				switch(searchStyle){
					case "searchByOfficeTel": showText=rowdata.name + " / " + rowdata.phone;
												break;
					case "searchByUserId": showText=rowdata.name + " / " + rowdata.id;
												break;
					case "searchByUserName":showText=rowdata.name;
												break;
					default:break;
				}
				return showText;
			}

		};
	return iHintParams;
}

function getInitPriority(customerCode,faultTypeId){
	if(faultTypeId==""){  //如果没有选择服务目录
		faultTypeId="NaN";
	}
	$.post(basePath + "itsm/woParamsConf/getInitPriority.do",
			{"customerCode":customerCode,"faultTypeId":faultTypeId},
		function(data){
				priorityId = data.initPriorityId;
				if(priorityId!=null){
					$("#workOrderForm").iForm("setVal",{"priorityId":priorityId}); 
				}
		},"json");
}

function setPriIdValByUrgentInfluence(urgentVal, influenceVal){
	if(urgentVal!=null &&influenceVal!=null){
		$.post(basePath + "itsm/woParamsConf/getPriIdValByUrgentInfluence.do",
				{"urgentVal":urgentVal,"influenceVal":influenceVal},
			function(data){
				var prioritIdVal = data.woPriorityId;
				if(prioritIdVal!= 0){
		    		setTimeout(function(){
		    			$("#f_priorityId").iCombo("setVal",prioritIdVal);
	    			},200);	
		    	}
			},"json");
	}
	
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
	$("#workOrderForm").iForm("setVal",{"customerName":name,"customerCom":com}).iForm("endEdit");
	$("#workOrderForm").iForm("hide",["customerCode","customerDept"]);
}


/**  查看工单基本信息，给form表单赋值
 * @param woId
 */
function setFormVal(woId){
	$.post(basePath + "itsm/workorder/queryItWODataById.do",
			{workOrderId:woId},function(woBasicData){
		var attachmentData = woBasicData.attachmentMap ;
		var woFormData = JSON.parse(woBasicData.workOrderForm);
		
		processInstId = woFormData.workflowId;
		woType = woFormData.workOrderTypeCode;
		woStatus = woFormData.currStatus;
		priorityId = woFormData.priorityId;
		faultTypeId = woFormData.faultTypeId;
		var customerCode = woFormData.customerCode;
		if(woFormData.serCharacterId==null){
			woFormData.serCharacterName = "请从左边目录树选择性质";
		}
		if(woFormData.faultTypeId==null){
			woFormData.faultTypeName = "请从左边目录树选择服务";
		}
		
		$("#workOrderForm").iForm("setVal",woFormData);
		if(woStatus != "draft"){
			joinFormField(woFormData);
		}
		
		$("#workOrderForm").iForm("setVal",{"priorityId":189});
		if(priorityId==null){  //如果没有服务级别，则配上默认服务级别
			var tempFaultTypeId = faultTypeId;
			if(tempFaultTypeId==null){
				tempFaultTypeId = "NaN";
			}
			setTimeout(function(){
				getInitPriority(customerCode,tempFaultTypeId);
			},500);
		}
		
		$("#workOrderForm").iForm("beginEdit",["faultTypeName","serCharacterName","influenceScope",
		                                       "description","urgentDegreeCode","priorityId"]);
		createtime =  FW.long2time(woFormData.createDate);
		updatetime =  FW.long2time(woFormData.modifyDate);
		if(attachmentData.length > 0){
			$("#uploadfileTitle").iFold("show"); 
			$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
		}
		switch(woStatus){
    		case "sendWO": formRead = false ;
				$("#btn_wo_commit").hide();
				$("#btn_wo_save1").hide();
				$("#btn_wo_edit1Div").hide();
				$("#btn_flowDiagramDiv").hide();
				$("#btn_wo_delete").hide();
				$("#btn_wo_obsolete").hide();
				$("#workOrderForm").iForm("hide",["sendWoNow","needInfoCenterAudit"]);
				$("#workOrderForm").iForm("endEdit",["currStatus","customerName","customerCode",
				                                     "customerPhone","customerCom","customerDept","customerLocation"]);
				break;
    		case "newWO": $("#inPageTitle").html("新建工单");
				$("#btn_wo_edit1").hide();
				$("#btn_wo_delete").hide();
				if(processInstId && woFormData.createuser == loginUserId){  //如果有流程ID，即为退回的单（可以作废）
					$("#btn_wo_1").show();
					$("#btn_wo_commit").hide();
					$("#btn_wo_save1").hide();
					$("#workOrderForm").iForm("endEdit","currStatus");
					$("#workOrderForm").iForm("hide","sendWoNow");
					$("#btn_wo_obsolete").show();
					$("#btn_flowDiagram").hide();
					$("#btn_Info").show();  //显示查看审批信息按钮
				}else{
					$("#btn_wo_delete").hide();
					$("#btn_wo_obsolete").hide();
				}
				break;
    		case "draft": $("#inPageTitle").html("工单信息（草稿）");
    			$("#workOrderForm").iForm("endEdit","currStatus");
    			$("#workOrderForm").iForm("setVal",{"sendWoNow":"no"});
    			$("#btn_wo_1").hide();
    			$("#btn_wo_save1").hide();
    			$("#btn_wo_audit1").hide();
    			$("#btn_auditInfo").hide();
    			$("#btn_Info").hide();
    			break;
    		default:break;
    	}
		processInstId = woFormData.workflowId;
		taskId = woBasicData.taskId;
		candidateUsers = woBasicData.candidateUsers;
		var flag = isMyActivityWO(candidateUsers,loginUserId);
		if(woStatus=="draft" && woFormData.createuser == loginUserId ){
			flag = true;
			$("#btn_wo_delete").show();  //自己的草稿，可以删掉
			$("#btn_wo_obsolete").hide();  //草稿不能作废
			$("#btn_wo_1").hide();
			$("#btn_Info").hide();
		}
		if(!flag){  
			$("#btn_wo_delete").hide();
			$("#btn_wo_operDiv").hide();
			$("#handlerStyleForm").hide();
			$("#workOrderForm").iForm("endEdit");
			
			if(woStatus=="newWO" && processInstId ){
				$("#btn_flowDiagram").hide();
				$("#btn_Info").show();  //显示查看审批信息按钮
			}
			$("#uploadform").iForm("endEdit"); //控制附件的权限（只能下载）
			if(attachmentData.length == 0){
				$("#uploadfileTitle").iFold("hide"); 
			}
		}
		FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
	},"json");
}	

/** 根据维护计划生成新的工单
 * @param mtpId 维护计划ID
 */
function setFormValFromMtp(mtpId){
	
	$.post(basePath + "itsm/maintainPlan/queryMTPDataById.do",{maintainPlanId:mtpId},
			function(mtpFullData){
				var mtpFormData = JSON.parse(mtpFullData.maintainPlanForm );
				mtpId = mtpFormData.id; 
				mtpFormData.maintainPlanId = mtpId; //维护计划ID
				mtpFormData.id= ""; //赋值之后变成工单的ID
				
				mtpFormData.currStatus = null; //将草稿状态变成空
				$("#workOrderForm").iForm("endEdit","currStatus");
				 
				$("#workOrderForm").iForm("setVal",mtpFormData);
				
				$("#inPageTitle").html("新建工单");
       			$("#btn_wo_edit1Div").hide();
       			$("#btn_wo_delete").hide();
       			$("#btn_wo_obsolete").hide();
       			$("#btn_wo_1").hide();
       			
       			$("#btn_Info").hide();
       			FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
				 
			},"json");
}	

/**  判断myId 是否在候选人 candidateUsers 中
 * @param candidateUsers
 * @param myId
 */
function isMyActivityWO(candidateUsers,myId){
	for(var i=0; i<candidateUsers.length; i++){
		if(candidateUsers[i] == myId ){
			InfoShowBtn = 1 ;
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
	
	var tempfaultTypeName = $("#workOrderForm").iForm("getVal").faultTypeName;
	var tempSerCharacterName = $("#workOrderForm").iForm("getVal").serCharacterName;
	
	if(tempfaultTypeName == '' || tempfaultTypeName == "请从左边目录树选择服务目录"){
		FW.error("请从左边树选择服务目录");
		$("#btn_wo_commit").button('reset');
		return;
	}
	if(tempSerCharacterName == '' || tempSerCharacterName == "请从左边目录树选择性质"){
		FW.error("请从左边树选择服务性质");
		$("#btn_wo_commit").button('reset');
		return;
	}
	/**表单验证*/
	if(!$("#workOrderForm").valid()){
		$("#btn_wo_commit").button('reset');
		return ;
	}
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var needInfoCenterAudit = woFormObj.needInfoCenterAudit;
	var sendWoNow = woFormObj.sendWoNow;
	var woFormData = FW.stringify(woFormObj);  //取表单值
	
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	 $.post(basePath + "itsm/workorder/commitWorkOrderdata.do",
	 		{"workOrderForm":woFormData,"workOrderId":woId,"commitStyle":commitStyle,"uploadIds":ids,
		 	 "isEngineer":isEngineer,"needInfoCenterAudit":needInfoCenterAudit,"sendWoNow":sendWoNow},
			function(data){
				if(data.result == "success"){
					taskId = data.taskId;
					if(commitStyle=="save"){
						$("#workOrderForm").iForm("setVal",{"id":data.woId,
		    				"workOrderCode":data.workOrderCode});
					}else{
						$("#workOrderForm").iForm("setVal",{"id":data.woId,
		    				"workOrderCode":data.workOrderCode,"workflowId":data.workflowId});
						var auditboxflag = woFormObj.workOrderTypeCode!="qxWoType" || 
											(woFormObj.workOrderTypeCode=="qxWoType" && woFormObj.sendWoNow=="yes")||
											(woFormObj.workOrderTypeCode=="qxWoType" && needInfoCenterAudit=="yes");
						if(auditboxflag){
							//当时立即派单（自动走一步），或者是任务型工单，需要有审批界面弹出
							$("#workOrderForm").iForm("endEdit",["workOrderTypeCode"]);
							$("#btn_wo_audit1").show();
							$("#btn_wo_commit").hide();
							$("#btn_wo_save1").hide();
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
							audit();//直接弹出派单审批
						}else{
							closeCurPage();	
						}
					}
					$("#btn_wo_commit").button('reset');
					FW.success("保存成功");
				}else {
					$("#btn_wo_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
}

function audit(){  //审批
	var woFormObj = $("#workOrderForm").iForm("getVal");
	var woStaus = woFormObj.currStatus;
	processInstId = woFormObj.workflowId;
	var woId = woFormObj.id;
	var woType = woFormObj.workOrderTypeCode;
	var workflowId = woFormObj.workflowId;
	var woFormData = woFormObj;  //取表单值
	
	var checkfaultTypeId = woFormObj.faultTypeId;
	var checkserCharacterId = woFormObj.serCharacterId;
	var checkpriorityId = woFormObj.priorityId;
	
	$.ajaxSetup({'async':false});  //下面的修改服务目录性质必须要在审批框弹出之前执行完，所有这里要用同步
	if(woStaus =="sendWO"){  //如果是派单环节
		var valideflag = checkfaultTypeId==null||checkserCharacterId==null || 
						 checkserCharacterId==""||checkfaultTypeId=="";
		if(valideflag){  //如果是派单环节，检查是否补充了“服务目录”和“服务性质”
			FW.error("请从左边目录树中选择服务目录和服务性质");
			return ;
		}else{ //将选中的“服务目录”和“服务性质”和“服务级别”插入到数据库中
			 $.post(basePath + "itsm/workorder/updateWoOnSendWo.do",{"woId":woId,"faultTypeId":checkfaultTypeId,
				 "serCharacterId":checkserCharacterId,"priorityId":checkpriorityId},
						function(data){
							if(data.result != "success"){
								FW.success("更新服务目录性质失败");
								return ;
							}
				  },"json");
		}
	}
	
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	
	//TODO 若是回退到发起人的单，状态为新建，且有流程ID,则先需要更新工单的基本信息
	if(woStaus=="newWO" && processInstId!=null){
		 $.post(basePath + "itsm/workorder/updateWoBaseInfo.do",
			 		{"workOrderForm":FW.stringify(woFormObj)},
					function(data){
						if(data.result == "success"){
							FW.success("更新成功");
						}else {
							FW.error("更新失败");
							return ;
						}
			  },"json");
	}
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woId;
	var variables = [{'name':'woType','value':woType}];  //防止回退之后选择其他的工单类型，走其他分支
	params['variables'] = FW.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woSendFormData = {"workOrderId":woId,"workOrderForm":woFormData,"attachmentIds":attachmentIds};
			var workFlow = new WorkFlow();
//			var updateDesc = "(科技公司派单)";
			var updateDesc = "";
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(woSendFormData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
	$.ajaxSetup({'async':true});
	
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
        }
        else{
        	 FW.error("提交失败");
        }
    });
}
/** 暂存维护计划 （数据入库）*/
function editWO(){
	$("#workOrderForm").iForm("beginEdit");  //打开编辑
	$("#workOrderForm").iForm("endEdit",["currStatus"]);  //打开编辑
	isCusSer();  //如果不是客户就隐藏”立即派单“
	$("#btn_wo_edit1Div").hide();
	$("#btn_wo_save1").show();
	FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
}
function deleteWO(){
	Notice.confirm("确定删除|确定删除该条工单信息么？",function(){
		var woFormObj = $("#workOrderForm").iForm("getVal");
		var woId = woFormObj.id;
		
		 $.post(basePath + "itsm/workorder/deleteWorkOrderDraft.do",{"woId":woId},
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
		var woId = woFormObj.id;
		
		 $.post(basePath + "itsm/workorder/obsoleteWorkOrder.do",{"woId":woId},
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
 * 工单详情打印
 */
function printWO(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_ITWOINFO.rptdesign&__format=pdf"+
						"&woId="+woId+"&workflow_id="+processInstId+"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="工单详情信息";
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
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("WOlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}