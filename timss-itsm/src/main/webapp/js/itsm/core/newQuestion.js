/* 表单字段定义  */
var fields = [
		    {title : "ID", id : "id",type : "hidden"},
		    {title : "问题编号", id : "code",type : "hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title : "taskId", id : "taskId", type : "hidden"},
		    {title : "创建人", id : "createUserInfo",type : "label"},
		    {title : "状态", id : "status", type : "label",formatter:function(val){return FW.getEnumMap("ITSM_QUESTION_STATUS")[val]}},
		    {title : "标题", id : "title", linebreak : true, rules : {maxChLength:60,required:true},wrapXsWidth:12,wrapMdWidth:12},
		    {title : "来源", id : "resourceid", linebreak : true, rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_QUESTION_SOURCE",
				options: {
					initOnChange: true,
					onChange: function(val){
						if("event"==val){
							$("#questionForm").iForm("show","eventQuestionCode");
						}else{
							$("#questionForm").iForm("hide","eventQuestionCode");
						}
					}
				}
			},
		    {title : "优先级", id : "priorityid", rules : {required:true},
				type : "combobox",
				dataType : "enum",
				enumCat : "ITSM_URGENCY_DEGREE"
			},
		    {title : "分类", id : "category", type:"hidden"},
		    {title : "分类", id : "categoryName",type : "label", rules : {required:true}
		    ,formatter:function(val){
					var text = val;
				if(text=="请从左边目录树选择分类"){
					text = "<label style='color:red'>"+val+"</label>";
				}
				return text;
				}
		    },
		    {title : "工单编号", id : "eventQuestionCode" ,type: "text",wrapXsWidth:12,wrapMdWidth:8
		    	,render:function(id){
		    		var datasource =  basePath + "itsm/questionrd/queryWorkOrderByName.do";
		    		var remoteDataInit={ datasource : datasource,clickEvent : function(id, name) {
		    			$("#f_eventQuestionCode").val(name);
		    			$("#questionForm").iForm("setVal",{"desp":id});
		    		}
		    	};
		    	$("#" + id).iHint('init', remoteDataInit);
		     }},
		    {title : "问题描述", id : "desp", rules : {maxChLength:600,required:true},wrapXsWidth:12,wrapMdWidth:8,type:"textarea"},
		    {title : "是否形成问题", id : "na",type : "radio",data : [['Y',"是",true],['N',"否"]],wrapXsWidth:12,wrapMdWidth:12},
		    {title : "问题经理审批意见", id : "opinion", rules : {maxChLength:600,required:true},wrapXsWidth:12,wrapMdWidth:8,type:"textarea"},
		    {title : "是否进入知识库", id : "ns",type : "radio",data : [['Y',"是",true],['N',"否"]],wrapXsWidth:12,wrapMdWidth:12},
		    {title : "问题根本原因", id : "reason", rules : {maxChLength:600,required:true},wrapXsWidth:12,wrapMdWidth:8,type:"textarea"},
		    {title : "解决方案", id : "solve", rules : {maxChLength:600,required:true},wrapXsWidth:12,wrapMdWidth:8,type:"textarea"}
		];
/**  判断myId 是否在候选人 candidateUsers 中
 * @param candidateUsers
 * @param myId
 */
function isMyActivityQuestion(candidateUsers,myId){
	for(var i=0; i<candidateUsers.length; i++){
		if(candidateUsers[i] == myId ){
			auditInfoShowBtn = 1 ;
			return true;
		}
	}
	return false;
}
/**
 * 提交问题
 */
/**提交问题基本信息*/
function commitQuestion(commitStyle){  
	var category = $("#questionForm").iForm("getVal").category;
	var categoryName = $("#questionForm").iForm("getVal").categoryName;
	if(category == '' || categoryName == "请从左边目录树选择服务"){
		FW.error("请从左边树选择服务目录");
		return;
	}
	/**表单验证*/
	if(!$("#questionForm").valid()){
		return ;
	}
	var questionFormObj = $("#questionForm").iForm("getVal");
	var questionFormData = FW.stringify(questionFormObj);  //取表单值
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	$.post(basePath + "itsm/questionrd/commitQuestiondata.do",
	 		{"questionForm":questionFormData,"commitStyle":commitStyle,"uploadIds":ids},
			function(data){
				if(data.result == "success"){
					taskId = data.taskId;
					if(commitStyle=="save"){
						$("#questionForm").iForm("setVal",{"id":data.id,"code":data.code,"status":data.status});
					}else{
						$("#questionForm").iForm("setVal",{"id":data.id,"code":data.code,"workflowId":data.workflowId,"status":data.status});
						audit();
					}
					FW.success("保存成功");
				}else {
					FW.error("保存失败");
				}
	 },"json");
}
/**
 * 审批按钮
 */
function auditBtn(){
	//表单校验
	if(!$("#questionForm").valid()){
		return ;
	}
	var questionFormData = $("#questionForm").iForm("getVal");
	var status = questionFormData.status;
	if("new"==status||"qesolving"==status){
		//退回和问题专家解决问题环节都需要保存附件
		commitQuestion('commit');
	}else{
		audit();
	}	
}
/**
 * 审批问题记录
 */

function audit(){  
	//表单校验
	if(!$("#questionForm").valid()){
		return ;
	}
	var questionFormData = $("#questionForm").iForm("getVal");
	processInstId = questionFormData.workflowId;
	var id = questionFormData.id;
	var status = questionFormData.status;
	$.ajaxSetup({'async':false});  //下面的修改服务目录分类必须要在审批框弹出之前执行完，所有这里要用同步
	if("draft"==status||"new"==status||"suspended"==status){
		var variables = FW.stringify([{}]); 
		workFlowAudit(questionFormData,processInstId,variables,id,status);
	}else if("qmapproving"==status){
		workFlowAudit(questionFormData,processInstId,variables,id,status);
		//var taskId = questionFormData.taskId;
		//selectNextStep(taskId,processInstId,questionFormData,null,0,id,status);
	}else if("qesolving"==status){
		workFlowAudit(questionFormData,processInstId,variables,id,status);
		//var taskId = questionFormData.taskId;
		//selectNextStep(taskId,processInstId,questionFormData,null,1,id,status);
	}
	$.ajaxSetup({'async':true});
	
}
/**
 * 审批弹出框草稿
 */
function workFlowAudit(questionFormData,processInstId,variables,id,status){
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	if(undefined ==taskId){
		taskId=questionFormData.taskId;
	}
	if("qmapproving"==status){
		var na = questionFormData.na;
		variables = FW.stringify([{'name':'isQuestion','value':na}]) ;
    }else if("qesolving"==status){
    	var ns = questionFormData.ns;
    	variables = FW.stringify([{'name':'isKL','value':ns}]) ;
    }
	params['processInstId'] = processInstId;
	params['businessId'] = id;
	params['variables'] = variables;
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var workFlow = new WorkFlow();
			workFlow.showAudit(taskId,FW.stringify(questionFormData),agree,rollback,stop,null,0);
		}
	});
}
/**
 * 选择下一步操作
 */
function selectNextStep(taskId,processInstId,data,parameters,index,id,status){
    var src = basePath + "itsm/questionrd/selectNextStep.do?index="+index;
    var btnOpts = [{
            "name" : "确定",
            "style" : "btn-success",
            "onclick" : function(){
                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
                var flag = p.$("#form1").iForm("getVal","flag");
                //param会让后台从variables中获取值，具体获取哪些key的值，则要从流程的属性设置的template项中定义
                _parent().$("#itcDlg").dialog("close"); 
                var variables = [];
                if("qmapproving"==status){
                	variables = [{'name':'isQuestion','value':flag}] ;
                }else if("qesolving"==status){
                	variables = [{'name':'isKL','value':flag}] ;
                }
        		var parameters = {taskId:taskId,processInstId:processInstId,data:FW.stringify(data),variables:FW.stringify(variables),id:id};
        		$.post(basePath+'workflow/process_inst/setVariables.do',parameters,
        			function(result){
        				if(result && result.result=='ok'){
        					var workFlow = new WorkFlow();
        					var multiSelect = 0;  
        					workFlow.showAudit(taskId,FW.stringify(data),agree,rollback,stop,null,multiSelect);
        				}else{
        					FW.error(result.msg || "出错了，请重试");
        				}
        		});
            }
        }
    ];
    var dlgOpts = {
        width :360,
        height:150,
        title:"下一步操作"
    };
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}
/**
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	var questionFormData = $("#questionForm").iForm("getVal");
	var id = questionFormData.id;
	var status = questionFormData.status;
	var ns = questionFormData.ns;
	if("Y"==ns&&"qesolving"==status){
		$("#btn_question_audit1").hide();//隐藏审批按钮
		FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
		$("#questionForm").iForm("endEdit");
		openKnowledgeInfo(id,FW.getCurrentTabId());
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
    data['businessId'] = id;
    var url = 'itsm/questionrd/stopQuestion.do';
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
/**
 * 删除问题信息
 */
function deleteQuestion(){
	Notice.confirm("确定删除|确定删除该条问题信息么？",function(){
		var questionFormObj = $("#questionForm").iForm("getVal");
		var id = questionFormObj.id;
		$.post(basePath + "itsm/questionrd/deleteQuestionDraft.do",{"id":id},
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
/**
 * 作废问题信息
 */
/**  作废问题（非草稿，仅问题发起人可以作废）  */
function obsoleteQuestion(){
	Notice.confirm("确定作废|确定作废该条问题信息么？",function(){
		var questionFormObj = $("#questionForm").iForm("getVal");
		var id = questionFormObj.id;
		$.post(basePath + "itsm/questionrd/obsoleteQuestion.do",{"id":id},
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
 * 打开知识页面
 */
function openKnowledgeInfo(id,closeTabId){
   var opts = {
        id : "openItsmKnowledgeQu"+id,
        name : "知识单详情",
        url : basePath+ "itsm/knowledge/openKnowledgePage.do?prombleId="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('"+closeTabId+"');FW.getFrame('"+closeTabId+"').refresh();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(flag){
		FW.set("QuestionlistDoNotRefresh",true);
	}
	FW.deleteTabById(FW.getCurrentTabId());
}
/**  
 * 查看问题基本信息，给form表单赋值
 */
function setFormVal(id){
	$.post(basePath + "itsm/questionrd/queryItQuestionDataById.do",
			{id:id},function(questionBasicData){
		var attachmentData = questionBasicData.attachmentMap ;
		var taskId = questionBasicData.taskId;
		var candidateUsers = questionBasicData.candidateUsers;
		var questionFormData = eval("(" +questionBasicData.questionForm+ ")");
		processInstId = questionFormData.workflowId;
		status = questionFormData.status;
		if(questionFormData.category==null){
			questionFormData.categoryName = "请从左边目录树选择分类";
		}
		$("#questionForm").iForm("setVal",questionFormData);
		$("#questionForm").iForm("setVal",{"createUserInfo":questionFormData.createUserName+"/"+questionFormData.deptName});
		$("#questionForm").iForm("setVal",{"categoryName":questionFormData.categoryName});
		$("#questionForm").iForm("setVal",{"taskId":taskId});
		if(attachmentData.length > 0){
			$("#uploadfileTitle").iFold("show"); 
			$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
		}
		if(null==candidateUsers){
			candidateUsers = [];
		}
		//是否为候选人的标识
		var flag = isMyActivityWO(candidateUsers,loginUserId);
		hideAllButtons();
		
		$("#questionForm").iForm("endEdit");
		$("#uploadform").iForm("endEdit"); //控制附件的权限（只能下载）
		if(attachmentData.length == 0){
			$("#uploadfileTitle").iFold("hide"); 
		}
		$("#questionForm").iForm("hide","na");
		$("#questionForm").iForm("hide","ns");
		//$("#questionForm").iForm("hide","eventQuestionCode");
		switch(status){
			case "new": 
				if(questionFormData.createuser == loginUserId){  //如果有流程ID，即为退回的单（可以作废）
					$("#btn_question_audit1").show();//显示审批按钮
					$("#btn_question_obsolete").show();//显示作废按钮
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
					$("#questionForm").iForm("beginEdit");
					$("#uploadfileTitle").iFold("show"); 
					$("#uploadform").iForm("beginEdit"); 
				}else{
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
				}
				$("#questionForm").iForm("hide","opinion");
				$("#questionForm").iForm("hide","reason");
				$("#questionForm").iForm("hide","solve");
				$("#questionForm").iForm("hide","na");
				$("#questionForm").iForm("hide","ns");
				break;	
			case "qmapproving": 
				if(flag){  //是候选人
					$("#btn_question_audit1").show();//显示审批按钮
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
					$("#questionForm").iForm("beginEdit","opinion");
					$("#questionForm").iForm("beginEdit","na");
					$("#questionForm").iForm("show","na");
				}else{
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
				}
				$("#questionForm").iForm("hide","reason");
				$("#questionForm").iForm("hide","solve");
				
				break;
    		case "qesolving":
    			if(flag){  //是候选人
					$("#btn_question_audit1").show();//显示审批按钮
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
					$("#questionForm").iForm("beginEdit","reason");
					$("#questionForm").iForm("beginEdit","solve");
					$("#questionForm").iForm("beginEdit","ns");
					$("#questionForm").iForm("show","ns");
					//问题专家解决问题环节可以上传附件
					$("#uploadfileTitle").iFold("show"); 
					$("#uploadform").iForm("beginEdit"); 
				}else{
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
				}
    			
    			break;
    		case "suspended":
    			if(flag){  //是候选人
					$("#btn_question_audit1").show();//显示审批按钮
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
				}else{
					$("#btn_auditInfo").show();  //显示查看审批流程信息按钮
				}
    			$("#questionForm").iForm("hide","reason");
    			$("#questionForm").iForm("hide","solve");
    			break;
    		case "draft": 
    			$("#inPageTitle").html("问题记录（草稿）");
    			if(questionFormData.createuser == loginUserId){
    				flag=true;
    				$("#btn_question_commit").show();//显示提交按钮
        			$("#btn_question_save1").show();//显示暂存按钮
        			$("#btn_flowDiagramDiv").show();//显示无当前流程节点的流程信息图
        			$("#btn_question_delete").show();//显示删除按钮
        			$("#questionForm").iForm("beginEdit");
        			$("#uploadfileTitle").iFold("show");
        			$("#uploadform").iForm("beginEdit"); 
    			}else{
    				$("#btn_flowDiagramDiv").show();//显示无当前流程节点的流程信息图
    			}
    			$("#questionForm").iForm("hide","opinion");
    			$("#questionForm").iForm("hide","reason");
    			$("#questionForm").iForm("hide","solve");
    			$("#questionForm").iForm("hide","na");
    			$("#questionForm").iForm("hide","ns");
    			break;
    		case "finished":
    			$("#btn_printDiv").show();
    			$("#btn_auditInfo").show();
    		default:$("#btn_auditInfo").show();break;
    	}
		if(!flag){  
			$("#btn_question_operDiv").hide();//关闭 暂存 提交区域隐藏
			$("#btn_question_delete").hide();//不可删除
		}
		FW.fixRoundButtons("#toolbar");   //解决按钮隐藏后的圆角问题
	},"json");
}
/**
 * 隐藏所有按钮
 */
function hideAllButtons(){
	$("#btn_question_save1").hide();
	$("#btn_question_commit").hide();
	$("#btn_question_audit1").hide();
	$("#btn_question_delete").hide();
	$("#btn_question_obsolete").hide();
	$("#btn_flowDiagram").hide();
	$("#btn_auditInfo").hide();
	$("#btn_printDiv").hide();
}
function print(){
	var printUrl = "http://timss.gdyd.com/";
	var url = fileExportPath + "preview?__report=report/TIMSS2_ITSM_QUESTION.rptdesign&__format=pdf"+
						"&id="+questionId+"&author="+loginUserId+"&url="+printUrl;
	var title ="问题信息";
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