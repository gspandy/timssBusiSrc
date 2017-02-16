
/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "知识编号", id : "knowledgeCode",type : "hidden"},
		    {title : "来源", id : "source",rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_KL_SOURCE",
				options:{
					allowEmpty:false,
					onChange:function(val){
						if(val == itsmKlSource.DAILYKL){
							$("#konwledgeForm").iForm("hide",["eventWoCode","problemWoCode"]);
						}
						if(val == itsmKlSource.EVENTKL){
							$("#konwledgeForm").iForm("hide",["problemWoCode"]);
							$("#konwledgeForm").iForm("show",["eventWoCode"]);
						}
						if(val == itsmKlSource.PROBLEMKL){
							$("#konwledgeForm").iForm("hide",["eventWoCode"]);
							$("#konwledgeForm").iForm("show",["problemWoCode"]);
						}
					}
				}
		    },
		    {title : "类别Id", id : "typeId",type:"hidden"},
		    {title : "类别", id : "typeName",type:"label",value:"请从左边目录树选择",rules : {required:true}},
		    {title : "关联事件单", id : "eventWoCode",linebreak:true,rules : {required:true}},
		    {title : "关联问题单", id : "problemWoCode",rules : {required:true}},
			{title : "状态", id : "currStatus",
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_KL_STATUS"
			},
			{title : "创建人", id : "createuserName",type : "label"},
			{title : "创建时间", id : "createdate",type:"datetime", dataType:"datetime"},			
		    {title : "标题", id : "name",rules : {required:true},
		    	linebreak:true,
		    	wrapXsWidth:12,
		        wrapMdWidth:8},
	        {title : "关键字(，分隔)", id : "keywords",rules : {required:true},
	        	linebreak:true,
	        	wrapXsWidth:12,
	        	wrapMdWidth:8
		    },
		    
		    {
		        title : "问题描述", 
		        id : "troubleDescription",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110,
		        rules : {required:true,maxChLength:300}
		    },
		    {
		        title : "解决方案描述", 
		        id : "solutionDescription",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:180,
		        rules : {required:true,maxChLength:1000}
		    }
		];
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}

function commitKL(commitStyle){
	var knowledgeTypeName = $("#konwledgeForm").iForm("getVal").typeName;
	if(knowledgeTypeName == '' || knowledgeTypeName == "请从左边目录树选择"){
		FW.error("请从左边树选择知识类别");
		return;
	}
	
	/**表单验证*/
	if(!$("#konwledgeForm").valid()){
		return ;
	}
	var knowledgeFormObj = $("#konwledgeForm").iForm("getVal");
	 
	
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	 $.post(basePath + "itsm/knowledge/commitKnowledgedata.do",
		 		{"knowledgeForm":FW.stringify(knowledgeFormObj),"commitStyle":commitStyle,"uploadIds":ids},
				function(data){
					if(data.result == "success"){
						taskId = data.taskId;
						knowledgeId = data.knowledgeId;
						if(commitStyle=="save"){
							$("#konwledgeForm").iForm("setVal",{"id":data.knowledgeId,
			    				"knowledgeCode":data.knowledgeCodeString});
						}else{
							knowledgeId = data.knowledgeId;
							processInstId = data.workflowId;
							taskId = data.taskId;
							$("#konwledgeForm").iForm("setVal",{"id":knowledgeId,
			    				"knowledgeCode":data.knowledgeCode,"workflowId":processInstId});
								$("#btn_wo_audit1").show();
								$("#btn_wo_commit").hide();
								$("#btn_wo_save1").hide();
								FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
								audit();//直接弹出派单审批
						}
						FW.success("保存成功");
					}else {
						FW.error("操作失败");
					}
		  },"json");
	
}

function audit(){  //审批
	var knowledgeFormObj = $("#konwledgeForm").iForm("getVal");
	 
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = processInstId;
	params['businessId'] = knowledgeId;
	//'name':'businessId','value':knowledgeId
	var variables = [{}]; 
	params['variables'] = FW.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var woSendFormData = {"knowledgeId":knowledgeId,"knowledgeForm":knowledgeFormObj,"attachmentIds":attachmentIds};
			var workFlow = new WorkFlow();
			var updateDesc = "新建知识";
			switch(klStatus){
				case itsmKLStatus.KL_NEW : updateDesc = "新建知识";
						break;
				case itsmKLStatus.KL_AUDIT : updateDesc = "审批知识";
						break;
				default:break;
			}
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			
			workFlow.showAudit(taskId,FW.stringify(woSendFormData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}

/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	 if(prombleId!="null" && typeof(prombleId)!="undefined" && prombleId!=0){
			$.post(basePath + "itsm/knowledge/knowledgeIdAddToPromble.do",
					{prombleId:prombleId,knowledgeId:knowledgeId},function(data){
						if(data.result != "success"){
							FW.errer("更新问题单失败");
						}
				
			},"json");
	}
	closeCurPage();
};
function rollback(){
	closeCurPage();
    
};
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
function setKLFormVal(knowledgeId){
	$.post(basePath + "itsm/knowledge/queryKnowledgeById.do",
				{itsmKnowledgeId:knowledgeId},function(konwledgeBasicData){
			var knowledgeFormData = JSON.parse(konwledgeBasicData.itsmKnowledgeForm);
			var attachmentData = konwledgeBasicData.attachmentMap ;
			var klSource = knowledgeFormData.source;
			klStatus = knowledgeFormData.currStatus;
			processInstId = knowledgeFormData.workflowId;
			taskId = konwledgeBasicData.taskId;
			candidateUsers = konwledgeBasicData.candidateUsers;
			createtime =  FW.long2time(knowledgeFormData.createDate);
			updatetime =  FW.long2time(knowledgeFormData.modifyDate);
			
			switch(klSource){
				case itsmKlSource.DAILYKL:$("#konwledgeForm").iForm("hide",["eventWoCode","problemWoCode"]);
											break;
				case itsmKlSource.EVENTKL:$("#konwledgeForm").iForm("hide",["problemWoCode"]);
											$("#konwledgeForm").iForm("show",["eventWoCode"]);
											break;
				case itsmKlSource.PROBLEMKL:$("#konwledgeForm").iForm("hide",["eventWoCode"]);
											$("#konwledgeForm").iForm("show",["problemWoCode"]);
											break;
				default:break;
			}
			$("#konwledgeForm").iForm("setVal",knowledgeFormData);
			
			if(attachmentData.length > 0){
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
			}
			//根据状态控制界面的显示
			switch(klStatus){
				case itsmKLStatus.KL_NEW: $("#inPageTitle").html("新建知识");
					$("#btn_kl_commit").hide();
					$("#btn_kl_print1").hide();
					$("#btn_flowDiagram").hide();
					$("#btn_kl_obsolete").show();
					$("#btn_auditInfo").show();
					formRead = false ;
					break;
				case itsmKLStatus.KL_AUDIT: $("#inPageTitle").html("知识审批");
					$("#konwledgeForm").iForm("endEdit","currStatus");
					$("#btn_kl_commit").hide();
					$("#btn_kl_obsolete").hide();
					$("#btn_kl_print1").hide();
					$("#btn_flowDiagram").hide();
					$("#btn_kl_audit1").show();
					$("#btn_auditInfo").show();
					break;
				case itsmKLStatus.KL_AUDIT_END: $("#inPageTitle").html("标准知识");
					$("#btn_kl_commit").hide();
					$("#btn_kl_obsolete").hide();
					$("#btn_kl_print1").show();
					$("#btn_flowDiagram").hide();
					$("#btn_kl_audit1").hide();
					$("#btn_auditInfo").show();
					$("#konwledgeForm").iForm("endEdit");
					break;
				default: $("#inPageTitle").html("作废知识");
					$("#btn_kl_commit").hide();
					$("#btn_kl_obsolete").hide();
					$("#btn_kl_print1").show();
					$("#btn_flowDiagram").hide();
					$("#btn_kl_audit1").hide();
					$("#btn_auditInfo").show();
					$("#konwledgeForm").iForm("endEdit");
					break;
			}
			$("#konwledgeForm").iForm("endEdit",["currStatus","createdate"]);
			FW.fixRoundButtons("#toolbar");
			
			var flag = isMyActivityWO(candidateUsers,loginUserId);
			//如果单不在自己手上，则关闭编辑
			if(!flag){  
				$("#konwledgeForm").iForm("endEdit");
				$("#btn_kl_commit").hide();
				$("#btn_kl_obsolete").hide();
				$("#btn_flowDiagram").hide();
				$("#btn_kl_audit1").hide();
				$("#btn_auditInfo").show();
				$("#uploadform").iForm("endEdit"); //控制附件的权限（只能下载）
				if(attachmentData.length == 0){
					$("#uploadfileTitle").iFold("hide"); 
				}
			}
		},"json");

}	

/** 问题单新生成知识单
 * @param prombleId
 */
function setKLFormByPrombleVal(prombleId){
	$.post(basePath + "itsm/questionrd/queryItQuestionDataById.do",
				{id:prombleId},function(questionBasicData){
			var questionFormData = JSON.parse(questionBasicData.questionForm);
			var knowledgeFormData ={};
			knowledgeFormData.source = itsmKlSource.PROBLEMKL;
			knowledgeFormData.problemWoCode = questionFormData.code;
			knowledgeFormData.name = questionFormData.title;
			knowledgeFormData.troubleDescription = questionFormData.desp;
			knowledgeFormData.solutionDescription = questionFormData.solve;
			knowledgeFormData.typeId = questionFormData.category;
			knowledgeFormData.typeName = questionFormData.categoryName;
			$("#konwledgeForm").iForm("hide",["eventWoCode"]);
			$("#konwledgeForm").iForm("show",["problemWoCode"]);
											
			$("#konwledgeForm").iForm("setVal",knowledgeFormData);
			
			//根据状态控制界面的显示
			$("#inPageTitle").html("新建知识");
			$("#btn_kl_commit").show();
			$("#btn_kl_print1").hide();
			$("#btn_flowDiagram").show();
			$("#btn_kl_obsolete").hide();
			$("#btn_auditInfo").hide();
			$("#btn_kl_audit1").hide();
			$("#konwledgeForm").iForm("hide",["createuserName","createdate"]);
			formRead = false ;
					
			FW.fixRoundButtons("#toolbar");
			
		},"json");

}	

function obsoleteKL(){
	Notice.confirm("确定作废|确定作废该条知识信息么？",function(){
		 $.post(basePath + "itsm/knowledge/obsoleteKnowledge.do",{"klId":knowledgeId},
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

function printKL(){
	var printUrl = "http://timss.gdyd.com/";
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_ITWOKL.rptdesign&__format=pdf"+
						"&klId="+knowledgeId+"&workflow_id="+processInstId+"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="知识单详情信息";
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