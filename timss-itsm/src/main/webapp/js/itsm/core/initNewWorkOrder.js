
/* 表单字段定义  */
var fields = [
            {title : "名称", id : "woName",
		        wrapXsWidth:12,wrapMdWidth:8,height:110,
		        rules : {required:true,maxChLength:680}},
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "工单编号", id : "workOrderCode",linebreak:true},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "申请人工号", id : "customerCode"},
		    {title : "姓名", id : "customerName", rules : {required:true}},
		    {title : "电话", id : "customerPhone",rules : {required:true}},
		    {title : "公司", id : "customerCom"},
		    {title : "部门", id : "customerDept"},
		    {title : "位置", id : "customerLocation"}	,
		    {title : "服务目录Id", id : "faultTypeId",type : "hidden"},
		    {title : "服务目录",id : "faultTypeName",type:"label",value:"请从左边目录树选择服务",rules : {required:true},
		    	formatter:function(val){
					var text = val;
					if(text=="请从左边目录树选择服务"){
						text = "<label style='color:red'>"+val+"</label>";
					}
					return text;
				}
		    },
		    {title : "维护类型", id : "maintType",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_MAINTTYPE"
			},
			 {title : "状态", id : "currStatus",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_STATUS"
			},
			{title : "下一环节审批单位", 
				id : "assistDept", 
				type : "combobox",
				data:assistDeptResult,
				rules : {required:true},
				options:{
					allowEmpty:true,
					onChange:function(val){
						if(woStatus==itsmWoStatus.APPLICANTAUDIT){
							comNextAudit = val;
							if(val == "YDZ" || val == "ITC"){  //单选中的是“集团信息中心”，显示集团各人负责的系统信息，否则隐藏
								$("#btn_sysUserList").show();
							}else{
								$("#btn_sysUserList").hide();
							}
						}else if(woStatus==itsmWoStatus.CENTERAUDIT){
							centerNextAudit = val;
						}
						
			    	}
				}
				
			},
		    {
		        title : "情况描述", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {required:true,maxChLength:680}
		    }
		];

function openHelpPage(){
   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
   var opts = {
        id : "newHelp"+rand,
        name : "系统负责人",
        url : basePath+ "itsm/workorder/sysUserhelpList.do",
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refresh();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 

}

/**
 * @param userId 根据用户ID自动填充客户的信息
 */
function fillCustomerInfo(userId){
	$.post(basePath + "itsm/woUtil/userInfoSearchById.do",{"userId":userId},
		function(data){
			var userInfo = data[0];
			$("#f_customerName").val(userInfo.name);
			$("#f_customerCode").val(userInfo.id);
			$("#f_customerPhone").val(userInfo.phone);
			$("#f_customerCom").val(userInfo.comName);
			$("#f_customerDept").val(userInfo.orgName);
			$("#f_customerLocation").val(userInfo.location);	
		},"json");
	
}

/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
		if(flag){
			Notice.confirm("确定关闭|确定关闭此页面么？如未暂存更新信息将丢失",function(){
				FW.set("WOlistDoNotRefresh",true);
				FW.deleteTabById(FW.getCurrentTabId());
			},null,"info");	
		}else{
			FW.deleteTabById(FW.getCurrentTabId());
		}
}

/**提交工单基本信息*/
function initCommitWO(commitStyle){  
	
	if(commitStyle=="commit"){
		$("#btn_wo_commit").button('loading');
	}
	var woFormObj = $("#initWorkOrderForm").iForm("getVal");
	var tempfaultTypeName = woFormObj.faultTypeName;
	
	if(tempfaultTypeName == '' || tempfaultTypeName == "请从左边目录树选择服务"){
		FW.error("请从左边树选择服务目录");
		$("#btn_wo_commit").button('reset');
		return;
	}
	/**表单验证*/
	if(!$("#initWorkOrderForm").valid()){
		$("#btn_wo_commit").button('reset');
		return ;
	}
	
	var woFormData = FW.stringify(woFormObj);  //取表单值
	
	//以下四个赋值决定流程的走向
	itcWhbWo = isEngineer;
	infoCenterWo = isInfoCenterUser ; 
	if(infoCenterWo==true){
		centerNextAudit = itsmDeptId.INFOCENTERDEPTID; 
	}else{
		comNextAudit = "own"; 
	}
	
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	 $.post(basePath + "itsm/workorder/commitInitWodata.do",
	 		{"initWorkOrderForm":woFormData,"workOrderId":woId,"commitStyle":commitStyle,"uploadIds":ids,
		 "itcWhbWo":itcWhbWo,"infoCenterWo":infoCenterWo,"comNextAudit":comNextAudit,"centerNextAudit":centerNextAudit},
			function(data){
				if(data.result == "success"){
					taskId = data.taskId;
					if(commitStyle=="save"){
						$("#initWorkOrderForm").iForm("setVal",{"id":data.woId,
		    				"workOrderCode":data.workOrderCode});
						FW.success("保存成功，工单已存入首页“草稿”");
					}else{
						$("#initWorkOrderForm").iForm("setVal",{"id":data.woId,
		    				"workOrderCode":data.workOrderCode,"workflowId":data.workflowId});
						//当时立即派单（自动走一步），或者是任务型工单，需要有审批界面弹出
						$("#btn_wo_audit1").show();
						$("#btn_wo_commit").hide();
						$("#btn_wo_save1").hide();
						FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
						audit();//直接单位审批
						FW.success("保存成功");
					}
					$("#btn_wo_commit").button('reset');
					
					
				}else {
					$("#btn_wo_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
}

function audit(){  //审批
	/**表单验证*/
	if(!$("#initWorkOrderForm").valid()){
		return ;
	}
	var woFormObj = $("#initWorkOrderForm").iForm("getVal");
	var woStaus = woFormObj.currStatus;
	processInstId = woFormObj.workflowId;
	var woId = woFormObj.id;
	var workflowId = woFormObj.workflowId;
	var woFormData = woFormObj;  //取表单值
	
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	if(attachmentIds == "undefined"){
		attachmentIds = null;
	}
	
	var centerAuditBranchFlag = centerNextAudit;
	if(centerNextAudit!="ITC"&&centerNextAudit!="endWo"){
		centerAuditBranchFlag = "YDZ";
	}
	if(comNextAudit == siteId){
		if(siteId == 'ITC'){
			comNextAudit = siteId;
		}else{
			comNextAudit = "own";
		}
	}else{
		comNextAudit = woFormObj.assistDept;
	}
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = woId;
	var variables = [{'name':'itcWhbWo','value':itcWhbWo},
	                 {'name':'infoCenterWo','value':infoCenterWo},
	                 {'name':'comNextAudit','value':comNextAudit},
	                 {'name':'centerNextAuditId','value':centerNextAudit},
	                 {'name':'centerNextAudit','value':centerAuditBranchFlag},
	                 {'name':'woType','value':"qxWoType"}];  //防止回退之后选择其他的工单类型，走其他分支
	params['variables'] = FW.stringify(variables);
	 
	var auditNodeDes = "";
		
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woSendFormData = {"workOrderId":woId,"initWorkOrderForm":woFormData,"attachmentIds":attachmentIds,
					"itcWhbWo":itcWhbWo,"infoCenterWo":infoCenterWo,"comNextAudit":comNextAudit,"centerNextAudit":centerNextAudit};
			var workFlow = new WorkFlow();
			var updateDesc = auditNodeDes;
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			if((comNextAudit=="ITC")||centerNextAudit == "ITC"){
				multiSelect = 1; //新建缺陷工单时为多选，派给每个客服
			}
			workFlow.showAudit(taskId,FW.stringify(woSendFormData),agree,rollback,stop,updateDesc,multiSelect);
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
        }
        else{
        	 FW.error("提交失败");
        }
    });
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
	$("#initWorkOrderForm").iForm("setVal",{"customerName":name,"customerCom":com}).iForm("endEdit");
	$("#initWorkOrderForm").iForm("hide",["customerCode","customerDept"]);
}

/**  查看工单基本信息，给form表单赋值
 * @param woId
 */
function setFormVal(woId){
	FW.toggleSideTree(true); //隐藏左边的服务目录树
	$.post(basePath + "itsm/workorder/queryItWODataById.do",
			{workOrderId:woId},function(woBasicData){
		var attachmentData = woBasicData.attachmentMap ;
		var woFormData = JSON.parse(woBasicData.workOrderForm);
		
		processInstId = woFormData.workflowId;
		woType = woFormData.workOrderTypeCode;
		woStatus = woFormData.currStatus;
		priorityId = woFormData.priorityId;
		faultTypeId = woFormData.faultTypeId;
		$("#initWorkOrderForm").iForm("setVal",woFormData);
		
		createtime =  FW.long2time(woFormData.createDate);
		updatetime =  FW.long2time(woFormData.modifyDate);
		
		if(attachmentData.length > 0){
			$("#uploadfileTitle").iFold("show"); 
			$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
		}
		
		switch(woStatus){
			case itsmWoStatus.APPLICANTAUDIT:
					{$("#btn_wo_commit").hide();
					$("#btn_wo_save1").hide();
					$("#btn_wo_edit1Div").hide();
					$("#btn_flowDiagramDiv").hide();
					$("#btn_wo_delete").hide();
					$("#btn_wo_obsolete").hide();
					joinFormField(woFormData);
					$("#initWorkOrderForm").iForm("show",["assistDept"]); 
					break;}	
			case itsmWoStatus.COMAUDIT:
					{$("#btn_wo_commit").hide();
					$("#btn_wo_save1").hide();
					$("#btn_wo_edit1Div").hide();
					$("#btn_flowDiagramDiv").hide();
					$("#btn_wo_delete").hide();
					$("#btn_wo_obsolete").hide();
					joinFormField(woFormData);
					$("#initWorkOrderForm").iForm("hide",["assistDept"]); 
					break;}
    		case itsmWoStatus.CENTERAUDIT:
					{$("#initWorkOrderForm").iForm("show",["assistDept"]); 
	    			$("#btn_wo_commit").hide();
					$("#btn_wo_save1").hide();
					$("#btn_wo_edit1Div").hide();
					$("#btn_flowDiagramDiv").hide();
					$("#btn_wo_delete").hide();
					$("#btn_wo_obsolete").hide();
					joinFormField(woFormData);
					break;}
    		case itsmWoStatus.GROUPAUDIT:
					{$("#btn_wo_commit").hide();
					$("#btn_wo_save1").hide();
					$("#btn_wo_edit1Div").hide();
					$("#btn_flowDiagramDiv").hide();
					$("#btn_wo_delete").hide();
					$("#btn_wo_obsolete").hide();
					joinFormField(woFormData);
					$("#initWorkOrderForm").iForm("hide",["assistDept"]); 
					break;}
    		case itsmWoStatus.NEWWO:
    				FW.toggleSideTree(false);
	    			{$("#inPageTitle").html("新建工单");
					$("#btn_wo_edit1").hide();
					$("#btn_wo_delete").hide();
					$("#initWorkOrderForm").iForm("hide",["assistDept"]); 
					if(processInstId && woFormData.createuser == loginUserId){  //如果有流程ID，即为退回的单（可以作废）
						$("#btn_wo_audit1").show();
						$("#btn_wo_commit").hide();
						$("#btn_wo_save1").hide();
						$("#initWorkOrderForm").iForm("endEdit","currStatus");
						$("#btn_wo_obsolete").show();
						$("#btn_flowDiagram").hide();
						$("#btn_auditInfo").show();  //显示查看审批信息按钮
					}else{
						$("#btn_wo_delete").hide();
						$("#btn_wo_obsolete").hide();
					}
					break;}
    		case itsmWoStatus.DRAFT:
    				FW.toggleSideTree(false);
	    			{$("#inPageTitle").html("工单信息（草稿）");
	    			$("#initWorkOrderForm").iForm("endEdit","currStatus");
	    			$("#initWorkOrderForm").iForm("hide",["assistDept"]); 
	    			if(!isInfoCenterUser&&siteId!="ITC"){   //如果未非信息中心和科技公司的用户
						$("#initWorkOrderForm").iForm("hide",["sendToITC"]); 
					}else if(siteId=="ITC"){
						$("#initWorkOrderForm").iForm("setVal",{"sendToITC":"true"}).iForm("hide",["sendToITC"]); 
					}
	    			$("#initWorkOrderForm").iForm("endEdit",["customerName","customerCode","customerCom","customerDept"]); 
	    			$("#btn_wo_audit1").hide();
	    			$("#btn_wo_save1").show();
	    			$("#btn_wo_print1").hide();
	    			$("#btn_auditInfo").hide();
	    			break;}
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
			$("#btn_wo_audit1").hide();
			$("#btn_auditInfo").hide();
		}
		if(!flag){  
			$("#btn_wo_delete").hide();
			$("#btn_wo_operDiv").hide();
			$("#handlerStyleForm").hide();
			joinFormField(woFormData);
			$("#initWorkOrderForm").iForm("endEdit");
			if(woStatus=="newWO" && processInstId ){
				$("#btn_flowDiagram").hide();
				$("#btn_auditInfo").show();  //显示查看审批信息按钮
			}
			$("#uploadform").iForm("endEdit"); //控制附件的权限（只能下载）
			if(attachmentData.length == 0){
				$("#uploadfileTitle").iFold("hide"); 
			}
		}else{
			$("#initWorkOrderForm").iForm("endEdit");
			$("#initWorkOrderForm").iForm("beginEdit",["maintType","description","assistDept"]);
			if(woStatus==itsmWoStatus.DRAFT){
				$("#initWorkOrderForm").iForm("beginEdit",["customerPhone"]);
			}
		}
		FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
	},"json");
}	
//删除草稿
function deleteInitWO(){
	Notice.confirm("确定删除|确定删除该条工单草稿信息么？",function(){
		var woFormObj = $("#initWorkOrderForm").iForm("getVal");
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
		var woFormObj = $("#initWorkOrderForm").iForm("getVal");
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