var ftravelFormFields=[
		 {id:"id",type:"hidden"},
		 {
			 title : "事由说明", 
			 id : "description",
			 type : "textarea",
			 linebreak:true,
			 wrapXsWidth:12,
			 wrapMdWidth:8,
			 height:40,
			 rules : {required : true,maxChLength :100}
		 },
		 {title : "申请类型", id:"applyType", type : "combobox",
				dataType : "enum",
				enumCat : "FIN_APPLY_TYPE"
		 },
		 {title : "出差人员", id : "participantIds",type:"hidden",linebreak:true},
		 {title : "出差人员", id : "participantName", rules : {required : true},wrapXsWidth:12,
			 wrapMdWidth:8,
			 options:{
				 icon:"itcui_btn_mag",
				 onClickIcon:function(val){
					 selectParticipantToFrom("form1");
				 }
			 }
		 },
		 {
			 title : "出差地点", 
			 id : "occurAddress",
			 rules : {required : true,maxChLength :20}
		 },
		 {title : "开始时间",id : "strDate", type:"date",dataType:"date",
				rules : {required : true}
		 },
		 {title : "结束时间",id : "endDate", type:"date",dataType:"date",
			rules : {required : true,greaterEqualThan:"#f_strDate"}
		 },
		 {title : "交通工具", id:"vehicle", type : "combobox",
				dataType : "enum",
				enumCat : "FIN_VEHICLE"
		 },
		 {id:"deptid",type:"hidden"},	
		 {id:"proInstId",type:"hidden"},
		 {id:"applyUser",type:"hidden"},
		 {title : "填单人",id : "applyUsername"},
		 {title : "申请部门",id : "deptname"}
		    
	 ];

var fmaAttachMap=[];

var attachFormFields=[
{
	title : " ",
	id : "attach",
	linebreak : true,
	type : "fileupload",
	wrapXsWidth : 12,
	wrapMdWidth : 12,
	options : {
		"uploader" : basePath+"upload?method=uploadFile&jsessionid="+session,
		"delFileUrl" : basePath+"upload?method=delFile&key=" + valKey,
		"downloadFileUrl" : basePath+"upload?method=downloadFile",
		"swf" : basePath+"itcui/js/uploadify.swf",
		//"fileSizeLimit" : 10 * 1024,
		"initFiles" :fmaAttachMap,
		"delFileAfterPost" : true
	}
}
];


//初始化附件
function initAttachForm(data,$form,$wrapper,readOnly){
	var result={
		data:data,
		$form:$form,
		$wrapper:$wrapper,
		attachMap:fmaAttachMap,
		attachFormFields:attachFormFields,
		readOnly:readOnly
	};
	initAttachFormTemplate(result);
}


function initPageData(id){
	$.post(basePath + "finance/fma/queryFinanceManagementApplyById.do",
	{"id":id},
	function(data){
		if(data.flag == "success"){
			$("#inPageTitle").html("出差申请");
			var formdata = data.data;
			var _status = formdata.status;
			var flowStatus = formdata.flowStatus;
			currStatus = flowStatus;
			$("#form1").iForm('setVal',formdata);
			applyName = formdata.applyUsername;
			applyUserId = formdata.applyUser;
			deptname = formdata.deptname;
			deptid = formdata.deptid;	
			processInstId = data.pri.processInstId;
			taskId = data.pri.taskId;
			currHandUser = formdata.currHandUser;
			$("#form1").iForm("setVal",{applyUsername:applyName+"/"+deptname});
			$("#form1").iForm('hide',["deptname"]);
			if(_status=='D'){
				$("#btn_audit").hide();
				$("#fin-print").hide();
				$("#btn_obsolete").hide();
				
				$("#fincostTitle").hide();
				initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),false);
			}else if(_status=='AI'){
				//如果登陆人是当前处理人
				var booleanflag = currHandUser && currHandUser.indexOf(loginUserId)>-1
				if(!booleanflag){
					$("#btn_audit").hide();
					initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
				}else{
					initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),false);
				}
				
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				
				if(flowStatus=='apply' && booleanflag){
					$("#form1").iForm('beginEdit');
					$("#form1").iForm('endEdit',["applyUsername"]);
					$("#btn_obsolete").show();
					$("#btn_save").show();
					$("#btn_commit").show();
					$("#btn_audit").hide();
					$("#fin-print").hide();
					
				}else if(flowStatus=='hxbl' && booleanflag){
					$("#btn_audit").hide();
					$("#btn_handle").show();
				}
			}else if(_status=="AE"){//审批结束
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_audit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
				
			}else if(_status=="V"){//作废
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_audit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
			}
			//打印按钮初始化
			 initPrint(data);
		}else {
			
		}
		FW.fixToolbar("#toolbar1");
	},"json");
}

function initApplyUserAndDept($form){
	$form.iForm('setVal',{applyType:"travelapply",applyUsername:applyName,applyUser:applyUserId,deptid:deptid,deptname:deptname});
	$form.iForm('endEdit',["applyUsername","deptname"]);
	//$form.iForm("hide","applyType");
}

function shenpi(){
	if(!valid()){
		return ;
	}
	var data=$('#form1').iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	
	$.post(basePath+'finance/fma/setVariables.do',{taskId:taskId,processInstId:processInstId,financeManagementApply:FW.stringify(data)},
		function(result){
		if(result && result.flag=='success'){
			var workFlow = new WorkFlow();
		    //workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,stopWorkflow);
		    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,null,0);
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

function zuofei(){
	var options={
		destUrl:basePath+"finance/fma/voidFlow.do",
		businessId:id,
		processInstId:processInstId,
		taskId:taskId,
		tipMessage:"确认作废|确认作废该申请审批流程？"
	};
	voidFlow(options);
}

function initPrint(data){
	//初始化打印按钮
	    var proc_inst_id= processInstId;
		var commonPath = "__report=report/TIMSS2_SWF_FIN_FMA_001.rptdesign&id=" + id
			+ "&siteId=SWF&proc_inst_id="+proc_inst_id+"&author="+loginUserId;
		//预览PDF并提供打印
		$("#fin-print").click(function(){
			FW.dialog("init",{
				src: fileExportPath+"preview?__format=pdf&" + commonPath,
				btnOpts:[{
					"name" : "关闭",
					"float" : "right",
				    "style" : "btn-default",
				    "onclick" : function(){
				    	_parent().$("#itcDlg").dialog("close");
				    }
				}],
				dlgOpts:{ width:800, height:650, closed:false, title:"出差申请单", modal:true }
			});
		});
		
	}

//删除按钮
function del(){
	
	FW.confirm("确定删除吗？删除后数据不能恢复",function(){
		$.post(basePath+'finance/fma/delFMAById.do',{"id":id},function(result){
			showBasicMessageFromServer(result,delSuccessMessage,delFailMessage);
		});
	});
	
}

function submit(_this){
	if(!valid()){
		return ;
	}
	//buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	// 对于回退到第一个节点的提交
	proInstId = data.proInstId;
	if( processInstId != null && processInstId != "" ){
		var workFlow = new WorkFlow();
		workFlow.showAudit(taskId,JSON.stringify(data),closeTab,null,null,"",0);
		return;
	}else{
		buttonLoading(_this);
	}
	
	var jumpPath = basePath+'finance/ftravelApply/insertFinanceTravelApplyAndStartWorkflow.do';
	if(id && id!= null && id!=""){
		jumpPath = basePath+'finance/ftravelApply/updateFinanceTravelApplyAndStartWorkflow.do'
	}
	
	$.post(jumpPath,{"financeTravelApply":FW.stringify(data)},function(result){
		data.id=result && result.data && result.data.id;
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,
				{successFunction:openNewUserDialogFirstInTab,tabOpen:true,data:data,resetId:_this});
	});
	

}

function tmpSave(_this){
	if(!valid()){
		return ;
	}
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	var jumpPath = basePath+'finance/ftravelApply/tmpInsertFinanceTravelApply.do';
	if(data.id && data.id!=null ){
		jumpPath = basePath+'finance/ftravelApply/tmpUpdateFinanceTravelApply.do';
	}
	$.post(jumpPath,{"financeTravelApply":FW.stringify(data)},function(result){
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage,{tabOpen:true});
		//防止重复暂存或提交
		id = result.data;
		$("#form1").iForm('setVal',{"id":id});
	});
}

function valid(){
	if(!$("#form1").valid()){
		return false;
	}
	return true;
}

//显示流程图
function showWorkflow(){
	var workFlow = new WorkFlow();
	
	if(processInstId){
		var businessData={};
		var fields = [{
	        title : "创建时间", 
            id : "createtime",
            type : "label"
	    }];
	    var data={'createtime':FW.long2time(pmsPager.createTime)};
	    businessData['fields'] = fields;
	    businessData['data'] = data;
	    //是否是审批状态，流程信息对话下面显示审批按钮，否则不显示
	    var booleanflag = currHandUser && currHandUser.indexOf(loginUserId)>-1
	    if(booleanflag && currStatus != 'hxbl' && currStatus !='AE'){  //最后一个节点不要审批按钮，在新建报销单后自动的执行申请单的最后一步
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),1,shenpi,null);
	    }else{
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData));
	    }
		
	}else{
		 workFlow.showDiagram(defKey);
	}
}

//办理的三种情况 
//新增考勤异常
function newAbnormity(){
	var pageUrl = basePath+"attendance/abnormity/detail.do?mode=add"+"&finTaskId="+taskId;
	var newId = "newAbnormity"+Math.floor(Math.random() * ( 10 + 1));
	var	pageName = "考勤异常";
	var oldId = FW.getCurrentTabId(); //获取当前所在选项卡编号
	addEventTab( newId, pageName, pageUrl, oldId );
}
//取消办理
function cancelHandle(){
	var destUrl = basePath+"finance/fma/endFlow.do";
	var data={};
	data["businessId"]=id;
	data["taskId"]=taskId;
	data["processInstId"]=processInstId;
	data["message"]="无需办理，流程结束";
	$.ajax({
		type : "POST",
		url: destUrl,
		data: data,
		success:function(){
    		FW.success("操作成功");
    		forbidTipAfterCloseTab();
    		closeTab();
    	},
    	error:function(){
    		FW.error("操作失败");
    	}
	});
}
//取消出差
function cancelTravel(){
	var options={
			destUrl:basePath+"finance/fma/endFlow.do",
			businessId:id,
			processInstId:processInstId,
			taskId:taskId,
			message:"取消出差，流程结束",
			tipMessage:"确认取消出差|取消本次出差，流程将结束，确定？"
		};
		endFlow(options);
}

//结束流程
function endFlow(options){
	var destUrl=options.destUrl;
	var data={};
	data["businessId"]=options.businessId;
	data["taskId"]=options.taskId;
	data["processInstId"]=options.processInstId;
	data["message"]=options.message;
	var tipMessage=options.tipMessage;
	FW.confirm(tipMessage,function(){//取消出差需要二次确认
		$.ajax({
        	url:destUrl,
        	type:"POST",
        	data:data,
        	success:function(){
        		FW.success("操作成功");
        		forbidTipAfterCloseTab();
        		closeTab();
        	},
        	error:function(){
        		FW.error("操作失败");
        	}
        });
	});
}
