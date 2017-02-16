//流程相关

//显示未启动流程的流程实例图
function showFlowImg(){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}

//显示已启动流程的流程实例图(带红框显示流程进度)
function showFlowImgInfo(){
	var workFlow = new WorkFlow();
	//pid,业务表单数据
	workFlow.showAuditInfo(processInstId,null);
}

//显示已启动流程的流程实例图(带红框显示流程进度,带审批按钮)
function showFlowImgInfoWithApprove() {
	var workFlow = new WorkFlow();
	//pid,业务表单数据
	workFlow.showAuditInfo(processInstId,null,1,createApprove);
}
		
//首次提交成功后弹出的审批框
function createFirstApprove(finTypeEn) {
	var workFlow = new WorkFlow();
	//taskId,表单业务数据,回调方法
	//workFlow.submitApply(taskId,null,closeTab);
	if( finTypeEn == "only" || finTypeEn == "other" ) {
		workFlow.submitApply(taskId, null, submitApplyOpr, cancelApplyOpr, 0);
	} else if( finTypeEn == "more" ) { //多人时可多选
		workFlow.submitApply(taskId, null, submitApplyOpr, cancelApplyOpr, 1);
	}
}

//打开审批框进行审批
function createApprove() {
	//获取表单数据(字符串形式)
	var formObj = $("#autoform").ITC_Form("getdata");
	formObj.fid = fid;
	var formData = JSON.stringify(formObj);
	if(bussinessStatus == finWorkFlowStatusCodeConstant.MAIN_ACCOUNT_APPROVE || 
		bussinessStatus == finWorkFlowStatusCodeConstant.APPLICANT_MODIFY ){
		if (!$("#autoform").valid()) {
			return false;
		}
	}
	//检验明细数据必输、获取datagrid数据
	var detail = JSON.stringify($("#finTable").datagrid("getRows"));
	if (detail == "[]") {
		Notice.errorTopNotice("必须添加明细");
		$(_this).button('reset');
		return false;
	}
	//附件
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=ids.field3;
	
	var postFlowUrl = basePath + '/finance/financeInfoController/submitWorkFlowInfo.do';
    
    //postSubFlowData的格式为{'processInstId':'...','variables':[{'name':'...','value':'...'},{'name':'...','value':'...'},...]}
    var postFlowData = {
		"formData": formData,
		"detail": detail,
		"uploadIds": uploadIds,
		"fid": fid,
		"pid": pid
	};
    
    //提交审批数据
    $.post(postFlowUrl, postFlowData, function(data) {
        if(data.result == "success") {
        	var workFlow = new WorkFlow();
    		
    		if( finTypeEn == "only" || finTypeEn == "other" ) {
    			//workFlow.showAudit(taskId, null, closeTab, closeTab, null, "", 0);
    			workFlow.showAudit(taskId, null, closeTab, closeTab, stopWorkFlow, "", 0);
    		} else if( finTypeEn == "more" ) {
    			//弹出页面前，需要先对流程的变量进行设置，因为流程的下一步可能需要根据变量来判断
    		    //这里建议将流程所用到的所有变量都在这里设置，这样能确保通用，而且所有变量的值从业务上来讲都是从表单中获取的
    		    var postSubFlowUrl = basePath + '/finance/financeInfoController/setSubFlowVariables.do';
    		    
    		    //postSubFlowData的格式为{'processInstId':'...','variables':[{'name':'...','value':'...'},{'name':'...','value':'...'},...]}
    		    var postSubFlowData = {
    				"processInstId": pid
    			};
    		    
    		    $.post(postSubFlowUrl, postSubFlowData, function(data){
    		        if(data.result == "success") {
    		             var workFlow = new WorkFlow();//新建一个workflow对象
    		             //调用方法弹出审批对话框。taskId为任务id，
    		             //businessData为业务数据，提交流程时，会把业务数据也传到后台，在handler可以接收这些数据并做业务处理；
    		             //callback为回调函数
    		             workFlow.showAudit(taskId, null, closeTab, closeTab, stopWorkFlow, "", 0);
    		             //workFlow.showAudit(taskId,JSON.stringify(businessData),agree,rollback,stop);
    		        }
    		    }); 
    		}
        }
    }); 
}

//终止流程
function stopWorkFlow(rowdata) {
    //alert("callback stopWorkFlow: ["+ rowdata.reason + "]");
    var data={};
    data['businessId'] = fid;
    data['taskId'] = rowdata.taskId;
    data['assignee'] = rowdata.assignee;
    data['owner'] = rowdata.owner;
    data['message'] = rowdata.message;
    var url = 'finance/financeInfoController/stopWorkFlow.do';
    $.post(url, data, function(data) {
        if(data.result=='success') {
        	_parent().$("#itcDlgShowAudit").dialog("close");
        	_parent().$("#itcDlgAuditInfo").dialog("close");
            closeTab();
        } else {
            FW.success("提交失败");
        }
    });
}

//提交申请成功后触发动作
function submitApplyOpr() {
	closeTab();
	return;
}

//取消申请后触发动作
function cancelApplyOpr() {
	$("#saveBtn").show();
	$("#submitBtn").show();
	$("#goOnAddDetailBtn").show();
	
	$("#autoform").iForm("beginEdit");
	$("#autoform").iForm("endEdit", "finType");
	
	editDatagrid="true";
	$("#finTable").datagrid("showColumn", "delete"); //隐藏回收站按钮
	
	FW.fixRoundButtons("#btn_toolbar"); //修复工具栏按钮的圆角问题
	FW.fixRoundButtons("#addDetailBtn_toolbar"); //修复工具栏按钮的圆角问题
	
	//后台一系列回滚操作(1.删除业务和pid关联;2.调用接口删除工作流信息;3.置为草稿状态;4.删除待办信息;5.增加一条草稿信息)
	$.post(basePath + "/finance/financeInfoController/rollbackWorkFlowOpr.do",
		{"fid":fid, "status":bussinessStatus},
		function(data) {
			if(data.result == "success") {

			} else {
				FW.error("取消失败");
		}
	},"json");

	return;
}

function showDiagram() {
	var src = basePath+"workflow/page/diagramJsp.do";
    var btnOpts =  [{
	        "name" : "取消",
	        "float" : "right",
	        "style" : "btn-default",
	        "onclick" : function(){
	            _parent().$("#itcDlg").dialog("close");
	        }
	    },{
	        "name" : "确定",
	        "float" : "right",
	        "style" : "btn-success",
	        "onclick" : function(){
	            //请特别注意这里的写法 其中getRegForm是定义在feditprof方法对应页面下的函数，用于返回表单信息
	            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	            var form = p.submit();
	        }
	    }
	];
    
    var dlgOpts = {		
		width : 500,
	    height:650,
	    closed : false,
	    title:"流程图",
	    modal:true
	};
    
    Notice.dialog(src,dlgOpts,btnOpts);
}