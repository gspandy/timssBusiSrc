var businessentertainFormFields=[
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
		 {
			title : "列支科目", 
			id : "subject",
		    type : "combobox",
			dataType : "enum",
			enumCat : "FIN_SUBJECT"
		 },
		 {title : "接待日期",id : "occurDate",type : "date", dataType: "date",
				rules : {required : true},
				options:{
					startDate: new Date() 
				}
		 },
		 {title : "来访单位", id : "outComName",rules : {required : true} },
		 {title : "来访人数", id : "outPersonNum",
			rules : {required : true,number : true}
		},
		{title : "参与接待人数", id : "ourPersonNum",
			rules : {required : true,number : true}
		 },
		 {title : "接待部门", id : "participateDeptName"},
		 {title : "经办人", id : "participantIds",type:"hidden"},
		 {title : "经办人", id : "participantName", rules : {required : true},
			options:{
				icon:"itcui_btn_mag",
				onClickIcon:function(val){
					selectParticipantToFrom("form1");
				}
			}
		 },	 
		 {
			title : "申请费用(元)", 
			id : "budget",
			rules : {
				required : true,
				number : true
			}
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
	title : "",
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
			$("#inPageTitle").html("业务招待费申请详情");
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
				$("#btn_newbusinessFin").hide();
				$("#fincostTitle").hide();
				$("#form1").iForm("hide",["budget","needZJL"]);
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
				$("#btn_newbusinessFin").hide();
				if(flowStatus=='apply' && booleanflag){
					$("#form1").iForm('beginEdit');
					$("#btn_obsolete").show();
					$("#form1").iForm("hide",["budget","needZJL"]);
				}else if(flowStatus=='bmjlsp'){
					
				}else if(flowStatus=='xzbjlsp' && booleanflag ){
					$("#form1").iForm('show',["budget"]);
					$("#form1").iForm('beginEdit',["budget"]);
					oldBudget = formdata.budget;
				}else if(flowStatus=='fgldsp' && booleanflag){
					$("#form1").iForm('show',["needZJL","budget"]);
					$("#form1").iForm('beginEdit',["needZJL","budget"]);
					oldBudget = formdata.budget;
				}else if(flowStatus=='zjlsp' && booleanflag){
					oldBudget = formdata.budget;
					$("#form1").iForm('beginEdit',["budget"]);
				}else if(flowStatus=='sqrbl' && booleanflag){
					$("#btn_audit").hide();
					$("#btn_newbusinessFin").show();
					initFinCostTable(id);
				}
				
			}else if(_status=="AE"){
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_audit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				if(loginUserId != formdata.createuser){
					$("#btn_newbusinessFin").hide();
				}
				initAttachForm(formdata.attachMap,$('#attachForm'),$('#attachFormWrapper'),true);
				initFinCostTable(id);
			}else if(_status=="V"){
				$("#form1").iForm('endEdit');
				$("#btn_save").hide();
				$("#btn_commit").hide();
				$("#btn_audit").hide();
				$("#btn_delete").hide();
				$("#btn_obsolete").hide();
				$("#btn_newbusinessFin").hide();
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
	        	  $("#btn_newbusinessFin").hide();
	          }
		  }
	});
}
function initApplyUserAndDept($form){
	$form.iForm('setVal',{applyType:"businessentertainment",applyUsername:applyName,applyUser:applyUserId,deptid:deptid,deptname:deptname});
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
			var updateAuditDes = getUpdateAuditDes(currStatus,oldBudget,data);
		    workFlow.showAudit(taskId,FW.stringify(data),closeTab,closeTab,null,updateAuditDes,0);
		}else{
			FW.error(result.msg || "出错了，请重试");
		}
		
	});
}

function getUpdateAuditDes(currStatus,oldBudget,data){
	var result = null;
	var newBudget = data.budget;
	var statusFlag = "xzbjlsp,fgldsp,zjlsp".indexOf(currStatus)
	if(statusFlag>=0 && oldBudget!=null && oldBudget != newBudget){
		result = "申请预算金额（元）由"+oldBudget+"修改为"+newBudget+"。";
	}
	return result;
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

/**
 * 新增业务招待费报销
 */
function newbusinessFinance(){
	var finTypeEn="only";
	var finNameEn = "businessentertainment";
	var oldId = FW.getCurrentTabId(); //获取当前所在选项卡编号
	var nbr = Math.floor(Math.random() * ( 10 + 1)); //随机数
	var newId = finNameEn + finTypeEn + nbr; //新建选项卡编号
	var finNameCn=""; //报销名称(中文)
	var page = "financeNew";
	var flowTypeEnums =  FW.getEnumMap("FIN_FLOW_TYPE")
	finNameCn = flowTypeEnums[finNameEn];
	var pageName = finNameCn;
	var pageUrl =  basePath+"finance/financeInfoController/createPage.do?"
		+"page="+page+"&finNameEn="+finNameEn+"&finTypeEn="+finTypeEn+"&applyId="+id;
	addEventTab( newId, pageName, pageUrl, oldId );
}

function initPrint(data){
	//初始化打印按钮
	    var proc_inst_id=processInstId;
		var commonPath = "__report=report/TIMSS2_FIN_FMA_001.rptdesign&id=" + id
			+ "&siteid=ITC&proc_inst_id="+proc_inst_id+"&author="+loginUserId;
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
				dlgOpts:{ width:800, height:650, closed:false, title:"业务招待费申请单", modal:true }
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
	
	var jumpPath = basePath+'finance/fBusinessApply/insertFinBusinessApplyAndStartWorkflow.do';
	if(id && id!= null && id!=""){
		jumpPath = basePath+'finance/fBusinessApply/updateFinBusinessApplyAndStartWorkflow.do'
	}
	
	$.post(jumpPath,{"financeBusinessApply":FW.stringify(data)},function(result){
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
	var jumpPath = basePath+'finance/fBusinessApply/tmpInsertFinBusinessApply.do';
	if(data.id && data.id!=null ){
		jumpPath = basePath+'finance/fBusinessApply/tmpUpdateFinBusinessApply.do';
	}
	$.post(jumpPath,{"financeBusinessApply":FW.stringify(data)},function(result){
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
	    if(booleanflag && currStatus != 'sqrbl'){    //最后一个节点不要审批按钮，在新建报销单后自动的执行申请单的最后一步
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),1,shenpi,null);
	    }else{
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData));
	    }
		
	}else{
		 workFlow.showDiagram(defKey);
	}
}
