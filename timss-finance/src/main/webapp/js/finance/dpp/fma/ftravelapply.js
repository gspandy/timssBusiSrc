var ftravelFormFields=[
		 {id:"id",type:"hidden"},
		 {title : "申请类型", id:"applyType", type : "combobox",
				dataType : "enum",
				enumCat : "FIN_APPLY_TYPE"
		 },
		 {id:"deptid",type:"hidden"},	
		 {id:"proInstId",type:"hidden"},
		 {id:"applyUser",type:"hidden"},
		 {
			title : "名称", 
			id : "name",
			rules : {
				required : true
			}
		 },
		 {title : "出差人员", id : "participantIds",type:"hidden"},
		 {title : "出差人员", id : "participantName", rules : {required : true},
			options:{
				icon:"itcui_btn_mag",
				onClickIcon:function(val){
					selectParticipantToFrom("form1");
				}
			}
		 },
		 {
				title : "费用预算(元)", 
				id : "budget",
				rules : {
					number:true
				}
			 },
		 {title : "开始日期",id : "strDate", type:"date", dataType: "date", 
				rules : {required : true}
		 },
		 {title : "结束日期",id : "endDate", type:"date", dataType: "date",
			rules : {required : true,greaterEqualThan:"#f_strDate"}
		 },
		 {
			title : "出差地点", 
			id : "occurAddress"				
		 },
			
		 {
			title : "申请人",
			id : "applyUsername",
			rules : {
				required : true
			}
		},
		{
			title : "申请部门",
			id : "deptname"
		},
		
		 {
		        title : "事由说明", 
		        id : "description",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:48
		    },
		    {
				title : "总经理审批",
				id : "needZJL",
				type : "combobox",
				linebreak:true,
				data : [ [ "Y", "是" ], [ "N", "否" ] ],
				rules : {
					required : true
				}
			}
	    
];

var fincosttableFields = [[
               	        {field:'fid',title:'编号',width:160,fixed:true},
            	        {field:'fname',title:'名称',width:40},
            	        {field:'creatorname',title:'申请人',width:80,fixed:true},
            	        {field:'createdate',title:'申请日期',width:90,fixed:true,
            				formatter : function(value, row, index) {
            					return FW.long2date(value);
            				}
            			},
            	        {field:'total_amount',title:'总金额',width:60,fixed:true},
            	        {field:'finance_type',title:'报销类型',width:80,fixed:true}
            	    ]];
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
			$("#inPageTitle").html("出差申请详情");
			var formdata = data.data;
			var _status = formdata.status;
			var flowStatus = formdata.flowStatus;
			currStatus = flowStatus;
			$("#form1").iForm('setVal',formdata);
			applyName = data.dataapplyUsername;
			applyUserId = data.dataapplyUser;
			deptname = data.datadeptname;
			deptid = data.datadeptid;	
			processInstId = data.pri.processInstId;
			taskId = data.pri.taskId;
			currHandUser = formdata.currHandUser;
			
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
					$("#form1").iForm('endEdit',["applyUsername","deptname"]);
					$("#btn_obsolete").show();
					$("#form1").iForm("hide",["needZJL"]);
				}else if(flowStatus=='bmjlsp'){
					
				}else if(flowStatus=='fgfzsp' && booleanflag){
					$("#form1").iForm('show',["needZJL"]);
					$("#form1").iForm('beginEdit',["needZJL"]);
				}else if(flowStatus=='zjlsp'){
					
				}else if(flowStatus=='sqrbl' && booleanflag){
					$("#btn_audit").hide();
					
					initFinCostTable(id);
				}
				
			}else if(_status=="AE"){
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_audit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				
				initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
				initFinCostTable(id);
			}else if(_status=="V"){
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

function initFinCostTable(applyId){
	$("#fincostTitle").iFold("init");
	$("#fincostTable").iDatagrid( "init", {
		  url : basePath +"finance/financeInfoController/queryListByApplyId.do?applyId="+applyId,
		  columns:fincosttableFields,
		  singleSelect:true,
		  resizeHandle:'both',
		  fitColumns:true,
		  collapsible:true,
		  scrollbarSize:0,
		  onDblClickRow : function(rowIndex, rowData){
	    	 //同一张报销单不能同时打开两个tab编辑
		     var newId="edit"+rowData.fid;
		     var pageName=rowData.finance_flow;
		     var pageUrl= basePath+"finance/financeInfoController/viewFinanceInfo.do"+"?businessId="+rowData.fid;
		     var oldId = FW.getCurrentTabId();
		     addEventTab(newId, pageName, pageUrl,oldId);
		  },
		  onLoadSuccess : function(data){
			  if(data && data.total==0){
	             $("#fincostTitle").iFold("hide");
	          }else{
	        	  $("#fincostTitle").iFold("show");
	          }
		  }
	});
}
function initApplyUserAndDept($form){
	$form.iForm('setVal',{applyType:"travelapply",applyUsername:applyName,applyUser:applyUserId,deptid:deptid,deptname:deptname});
	$form.iForm('endEdit',["applyUsername","deptname"]);
	$form.iForm("hide","applyType");
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
		var commonPath = "__report=report/TIMSS2_DPP_FIN_FMA_001.rptdesign&id=" + id
			+ "&siteid=DPP&proc_inst_id="+proc_inst_id+"&author="+loginUserId;
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
	buttonLoading(_this);
	var data=$("#form1").iForm('getVal');
	$.fn.extend(true,data,$('#attachForm').iForm('getVal'));
	
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
		showBasicMessageFromServer(result,tmpSaveSuccessMessage,tmpSaveFailMessage);
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
	    if(booleanflag && currStatus != 'sqrbl'){  //最后一个节点不要审批按钮，在新建报销单后自动的执行申请单的最后一步
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),1,shenpi,null);
	    }else{
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData));
	    }
		
	}else{
		 workFlow.showDiagram(defKey);
	}
}
