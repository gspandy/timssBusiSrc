
/* 表单字段定义  */
	var fields = [
			{title : "ID", id : "id",type : "hidden" ,wrapXsWidth:12,wrapMdWidth:8,height:110},
			{title:"编号",id:"code",type:"hidden"},
		    {title:"电话",id:"phone",rules : {required:true}},
		    {title : "工号", id:"complainantCode", rules : {required:true}},
		    {title : "投诉人", id : "complainant", rules : {required:true}},
		    {title:"单位",id:"complainantCom",rules : {required:true}},
		    {title:"部门",id:"complainantDeptId"},
		    {title:"职务",id:"complainantPosition"},
		    {title:"状态",id:"currStatus",type:"hidden"},
		    /* 投诉方式用枚举 */
		    {title:"投诉方式",id:"complainStyle",rules : {required:true},
		    	type : "combobox",
		    	dataType : "enum",
		    	enumCat : "ITSM_COMPLAIN_TYPE"
		    },
		    /* 从投诉类别表中取值  */     
		    {title:"投诉类别",id:"complainType",rules : {required:true},type : "combobox",
		    	options : {
		    		allowEmpty:true,
		    		initOnChange : false,
		    		url : basePath + "itsm/woParamsConf/getComplainType.do",
		    		remoteLoadOn : "init",
		    			onChange:function(val){
		    				if("null" == complainRdId || "draft" == currStatus || "newCp" == currStatus ){
		    						changeComplainContent(val);
		    				}
		    			}
		    	}
		    },
		    {title:"投诉内容",id:"content",/*rules : {required:true, maxChLength:120},*/		    	
		    	type : "richtext",
		        linebreak:true,
		        breakAll:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110
		    },
		    {title:"处理人",id:"complainHandlerUser",type:"hidden"},
		    {title : "流程ID", id : "workflowId", type : "hidden"},
		    {title:"继续处理",id:"complainAccept",type:"combobox", linebreak:true,
		    	rules : {required:true},
		        data : [
		            ['Y','是'],
		            ['N','否']
		        ]
		    },
		    {title:"有效投诉",id:"complainActive",type:"combobox", linebreak:true,
		    	rules : {required:true},
		        data : [
		            ['Y','有效'],
		            ['N','无效']
		        ]
		    }
		    ];
	
	var anlyAndTreaFilds = [
	        {title:"分析与处理",id:"commentInfo",rules : {required:true,maxChLength:1300},
	        	type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110}             
	        ];
	
	var datailsField=[[	
	           		{field : 'cmtId',title : 'ID', hidden:true},  
	           		{field : 'userName',title : '处理人',width:100,fixed:true,align:'left'},
	           		{field : 'creationTime',title : '处理时间',width:120,fixed:true,align:'left',
	           			 formatter: function(value,row,index){
	           							 return FW.long2time(value);
	           						 }},
	           		{field : 'commentInfo',title : '分析与处理',width:500}
	           	]];
	
//根据电话输入提示
	fields[2].render = function(id){
		$("#"+id).iHint('init', getiHintParams('searchByOfficeTel'));
	};
//根据工号输入提示
	fields[3].render = function(id){
		$("#"+id).iHint('init', getiHintParams('searchByUserId'));
	};
//根据投诉人输入提示
fields[4].render = function(id){
	$("#"+id).iHint('init', getiHintParams('searchByUserName'));
};


function getiHintParams(searchStyle){
	var iHintParams ={
			datasource : basePath + "itsm/woUtil/userMultiSearch.do?searchStyle="+searchStyle,
			forceParse : function(){},
			clickEvent : function(id,name,rowdata) {
				if(rowdata != null){
					if(rowdata.name!=null){//投诉人
						$("#f_complainant").val(rowdata.name);
					}
					if(rowdata.id!=null){//投诉人工号
						$("#f_complainantCode").val(rowdata.id);
					}
					if(rowdata.phone!=null){//电话
						$("#f_phone").val(rowdata.phone);
					}else{
						$("#f_phone").val("");
					}
					if(rowdata.comName!=null){//单位
						$("#f_complainantCom").val(rowdata.comName);
					}else{
						$("#f_complainantCom").val("");
					}
					if(rowdata.orgName!=null){//部门
						$("#f_complainantDeptId").val(rowdata.orgName);
					}else{
						$("#f_complainantDeptId").val("");
					}
					if(rowdata.job!=null){//职务
						$("#f_complainantPosition").val(rowdata.job);
					}else{
						$("#f_complainantPosition").val("");
					}
				}
			},
			"highlight":true,
			"formatter" : function(id,name,rowdata){
				var showText = "";
				switch(searchStyle){
					case "searchByOfficeTel":
						showText=rowdata.name + " / " + rowdata.phone;
						break;
					case "searchByUserId": 
						showText=rowdata.name + " / " + rowdata.id;
						break;
					case "searchByUserName":
						showText=rowdata.name;
						break;
					default:break;
				}
				return showText;
			}

		};
	return iHintParams;
}

function changeComplainContent(val){
	$.post(basePath + "itsm/woParamsConf/getComplainReamrks.do",
			{"complainTypeConfId":val},
		function(type){
				$("#complainRdForm").iForm("setVal",{"content":type.remarks}); 
		},"json");
}

/**提交投诉记录*/
function commitcomplainRd(commitStyle){  
	if(commitStyle=="commit"){
		$("#btn_commit").button('提交中');
	}
	/**表单验证*/
	if(!$("#complainRdForm").valid()){
		$("#btn_commit").button('reset');
		return ;
	}
	//取表单值 
	var complainRdFormData = $("#complainRdForm").iForm("getVal");
	var content = complainRdFormData.content;
	if(null==content || content==''){
		FW.error("投诉内容不能为空");
		$("#btn_commit").button('reset');
		return ;
	}else{
		if(content.length>2000){
			FW.error("投诉内容过长，最好不要超过2000字符");	
			$("#btn_commit").button('reset');
			return ;
		}
	}
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	processInstId = complainRdFormData.workflowId;
	currStatus = complainRdFormData.currStatus;
	//退回提交
	if(commitStyle=="commit" ){
		if( processInstId != null && processInstId != ""){
			var workFlow = new WorkFlow();
			var SendFormData = {"complainRdId":complainRdId,"complainRdForm":complainRdFormData,"attachmentIds":ids};
			workFlow.showAudit(taskId,JSON.stringify(SendFormData),closeTab,null,null,"",0);
			return;
		}
	}
	 $.post(basePath + "itsm/complainRecords/commitcomplainRd.do",
	 		{"complainRdForm":JSON.stringify(complainRdFormData),"complainRdId":complainRdId,
	 		 "commitStyle":commitStyle,"uploadIds":ids},
			function(data){
				if(data.result == "success"){
					taskId = data.taskId;
					if(commitStyle == "save"){
						$("#complainRdForm").iForm("setVal",{"id":data.complainRdId,
		    				"code":data.compalinRdCode});
					}else{
						$("#complainRdForm").iForm("setVal",{"id":data.complainRdId,
		    				"code":data.compalinRdCode,"workflowId":data.workflowId});
							$("#btn_audit").show();
							$("#btn_commit").hide();
							$("#btn_save").hide();
							
							FW.fixRoundButtons("#toolbar");  //解决按钮隐藏后的圆角问题
						//	audit();//直接弹出派单审批
							var workFlow = new WorkFlow();
							var SendFormData = {"complainRdId":complainRdId,"complainRdForm":complainRdFormData,"attachmentIds":ids};
							workFlow.submitApply(taskId,JSON.stringify(SendFormData),closeTab,null,0);
					}
					$("#btn_commit").button('reset');
					FW.success("保存成功");
				}else {
					$("#btn_commit").button('reset');
					FW.error("操作失败");
				}
	  },"json");
}	
function audit(){  //审批
	var complainRdFormData = $("#complainRdForm").iForm("getVal");
	var atFormData=$("#anlyAndTreaForm").iForm("getVal");
		 
	var workflowId=complainRdFormData.workflowId;
	var complainRdId=complainRdFormData.id;
	var commentInfo=atFormData.commentInfo;
	
	/**表单验证*/
	if(currStatus =='cpHandle'){
		if(!$("#anlyAndTreaForm").valid()){
			return ;
		}
	}
	//获取附件数据
	var attachmentIds = $("#uploadform").iForm("getVal")["uploadfield"];
	if(attachmentIds == "undefined"){
		attachmentIds = null;
	}
	
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = complainRdId;
	params['commentInfo'] = commentInfo;
	
	var variables = [{'name':'complainAccept','value':$("#complainRdForm").iForm("getVal").complainAccept}]; 
	params['variables'] = FW.stringify(variables);

	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var SendFormData = {"complainRdId":complainRdId,"complainRdForm":complainRdFormData,"attachmentIds":attachmentIds,
								"commentInfo":commentInfo};
			var workFlow = new WorkFlow();
			
			var updateDesc = "";
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(SendFormData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}	
	
function openHandleInfo(rowData){
	var flwId=rowData.flwId;
	var cmtId=rowData.cmtId;
	FW.dialog("init",{
		src: basePath + "itsm/complainRecords/handleInfo.do?flwId="+flwId+"&cmtId="+cmtId,
		btnOpts:[{
				"name" : "关闭",
				"float" : "right",
				"style" : "btn-default",
				"onclick" : function(){
				           _parent().$("#itcDlg").dialog("close");
				         }
				 }],
			dlgOpts:{width:500, height:300, closed:false, title:"分析与处理", modal:true}
		});
	}
	
function showDiagram(){
	var workFlow = new WorkFlow();
	workFlow.showDiagram(defKey);
}	
	
function showAuditInfo(){
	var businessData={};
	var fields = [{
		title : "创建时间", 
		id : "createtime",
		type : "label"
		},{
		title : "修改时间", 
		id : "updatetime",
		type : "label"
	}];
	var data={'createtime':createtime,'updatetime':updatetime};
	businessData['fields'] = fields;
	businessData['data'] = data;
	var workFlow = new WorkFlow();
	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),auditInfoShowBtn,audit);
	}
	
/**
 * 关闭当前tab 页
 */
function closeCurPage(flag){
	if(currStatus == 'newCp' || currStatus == 'draft'){
		flag = true;
	}
	if(flag == true){
		Notice.confirm("确定关闭|确定关闭此页面么？如未暂存更新信息将丢失",function(){
			FW.deleteTabById(FW.getCurrentTabId());
		},null,"info");	
	}else{
		FW.deleteTabById(FW.getCurrentTabId());
	}
}
	
/**
 * 工单详情打印
 */
function print(){
	var printUrl = "http://timss.gdyd.com/";
	var isShow = false;
	if("end" == currStatus || true == hasHandleRds){
		isShow = true;
	}
	var src = fileExportPath + "preview?__report=report/TIMSS2_ITSM_COMPLAIN.rptdesign&__format=pdf"+
						"&id="+complainRdId+"&siteid="+Priv.secUser.siteId+"&isShow="+isShow+
						"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="投诉记录单";
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
 * 审批的三个回调函数，分别是“同意”，“回退”，“终止”
 */
function agree(){
	closeTab();
};
function rollback(){
	closeTab();
};
function stop(){
	closeTab();
};
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
/**  作废工单（非草稿，仅工单发起人可以作废）  */
function obsolete(){
	Notice.confirm("确定作废|确定作废该条工单信息么？",function(){
		var complainRdFormData = $("#complainRdForm").iForm("getVal");
		var complainRdId=complainRdFormData.id;
		
		 $.post(basePath + "itsm/complainRecords/obsoleteWorkOrder.do",{"complainRdId":complainRdId},
					function(data){
						if(data.result == "success"){
							FW.success("作废成功");
							closeTab();
						}else {
							FW.error("作废失败");
						}
			  },"json");
	},null,"info");	
}

/**  删除工单（草稿状态下可以删除） */
function deleteCp(){
	Notice.confirm("确定删除|确定删除该条工单信息么？",function(){
		var complainRdFormData = $("#complainRdForm").iForm("getVal");
		var complainRdId=complainRdFormData.id;
		
		 $.post(basePath + "itsm/complainRecords/deleteWorkOrder.do",{"complainRdId":complainRdId},
					function(data){
						if(data.result == "success"){
							FW.success("删除成功");
							closeTab();
						}else {
							FW.error("删除失败");
						}
			  },"json");
	},null,"info");	
}