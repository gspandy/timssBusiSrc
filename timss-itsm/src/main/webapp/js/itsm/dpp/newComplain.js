var	fields=[
			{id:"id",type:"hidden"},
			{id:"createUser",type:"hidden"},
			{title:"填写人",id:"createUserName"},
			{id:"deprId",type:"hidden"},
			{title:"部门",id:"deptName"},
			{title:"填写时间",id:"createdate",type:"datetime"},
			{title:"编号",id:"code"},
			{title:"意见和建议",id:"content",/*rules:{required : true,maxChLength:2000},*/
			    type : "richtext",
	    	            height:120,
			    linebreak:true,
			    breakAll:true,
			    wrapXsWidth:12,
			    wrapMdWidth:8
			},
			{title:"选择部门",id:"handleDept", type:"combobox",rules:{required : true},linebreak:true,
				options : {
		    		url : basePath + "itsm/woUtil/getDeptments.do",
		    		allowEmpty:true,
		    		remoteLoadOn : "init",
		    		onChange:function(val){
		    			getBmfzrIds(val);
		    		}
		    	}
			},
			{title:"是否满意",id:"complainActive",type:"combobox", linebreak:true,
		    	rules : {required:true},
		        data : [
		            ['Y','满意'],
		            ['N','不满意']
		        ]
		    },
			{id:"workflowId",type:"hidden"},
			{id:"currStatus",type:"hidden"},
			{id:"handleUser",type:"hidden"}
			];

var anlyAndTreaFilds = [
           {title:"分析与处理",id:"commentInfo",rules : {required:true,maxChLength:1300},
            	   type : "textarea",
            	   linebreak:true,
            	   wrapXsWidth:12,
            	   wrapMdWidth:8,
            	   height:110
            }];
 var datailsField=[[	
            	  {field : 'cmtId',title : 'ID', hidden:true},  
            	  {field : 'userName',title : '处理人',width:100,fixed:true,align:'left'},
            	  {field : 'creationTime',title : '处理时间',width:120,fixed:true,align:'left',
            	      formatter: function(value,row,index){
            	           return FW.long2time(value);
            	   }},
            	   {field : 'commentInfo',title : '分析与处理',width:500}
            	  ]];
 
 /**
  * @param userId 根据用户ID自动填充客户的信息
  */
 function fillCustomerInfo(userId){
 	$.post(basePath + "itsm/woUtil/userInfoSearchById.do",{"userId":userId},
 		function(data){
 			if(data.length>0){
 				var userInfo = data[0];
 				$("#complainRdForm").iForm("setVal",{"createUserName":userInfo.name,"deptName":userInfo.orgName});
 			}else{
 				$("#complainRdForm").iForm("setVal",{"createUserName":Priv.secUser.userName,"deptName":" "});
 			}
 		},"json");
 }
 /**
  * 根据选择的部门获取部门负责人
  */
 function getBmfzrIds(val){
	 $.post(basePath + "itsm/woUtil/getBmfzrIds.do",{"orgId":val},
		 function(data){
		 if(data.length>0){
			 bmferId='';
			 for(var i=0;i<data.length;i++){
				 var result = data[i];
				 bmferId += result.Id+',';
			 }
		 }
	},"json");
 }
 
 function setFormField(complainRdFormData){
		var createUserName = complainRdFormData.createUserName;
		var deptName = complainRdFormData.deptName;
		if(deptName != null){
			createUserName = createUserName + " / " + deptName;
		}
		$("#complainRdForm").iForm("setVal",{"createUserName":createUserName}).iForm("endEdit");
		$("#complainRdForm").iForm("hide",["deptName"]);
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
}
/**
 * 详情相应按钮显示
 */
function initPageData(complainRdId){
	$.post(basePath + "itsm/complainRecords/querycomplainRdById.do",
			{"complainRdId":complainRdId},
	function(result){
			var data = result.resultMap;
			$("#btn_save").hide(); 
			$("#btn_commit").hide();
			$("#btn_audit").hide();
			$("#btn_obsolete").hide();
			$("#btn_delete").hide();
			$("#btn_flowDiagram").hide();
			var complainRdFormData = JSON.parse(data.complainRdForm);
			taskId = data.taskId;
			processInstId = complainRdFormData.workflowId;
			currStatus = complainRdFormData.currStatus;
			complainHandlerUser=complainRdFormData.complainHandlerUser;
			var processRd = JSON.parse(data.processRd);
			var attachmentData = data.attachmentMap;
			if(attachmentData.length > 0){//附件显示
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("setVal",{uploadfield:attachmentData});
				$("#uploadform").iForm("endEdit");
			}else{
				$("#uploadfileTitle").iFold("hide"); 
			};
			if(complainHandlerUser!=null){
				if(complainHandlerUser.indexOf(loginUserName)>-1){//当前登录人与当前处理人相同才显示审批
					auditInfoShowBtn = 1;
					$("#btn_audit").show();
				}
			};
			if(processRd != null){//处理记录的显示
				hasHandleRds = true ;
				$("#handleRds").show();
				$("#inPageTitle1").show();
				$("#formDiv").hide();
				$("#datagridDiv").show(); 
				$("#detailsTable").datagrid("resize");
				$("#detailsTable").datagrid("loadData",processRd);
			};
			$("#complainRdForm").iForm("setVal",complainRdFormData);
			$("#complainRdForm").iForm("endEdit");
			if(currStatus == "obsolete"){  //作废工单不用审批
				$("#btn_audit").hide();
				auditInfoShowBtn = 0;
				setFormField(complainRdFormData);
			};
			if(currStatus == "newCp"){
				$("#btn_save").show();
				$("#btn_commit").show();
				$("#btn_audit").hide();
				$("#complainRdForm").iForm("beginEdit");
				$("#complainRdForm").iForm("endEdit",["code","createUserName","deptName","createdate"]);
			 if(complainHandlerUser.indexOf(loginUserName)>-1 && processInstId){
				 $("#inPageTitle").html("编辑意见和建议");
				 $("#btn_obsolete").show();
				 $("#uploadfileTitle").iFold("show"); 
				 $("#uploadform").iForm("beginEdit");
			 }
			};
			if(currStatus == 'draft'){
				$("#inPageTitle").html("编辑意见和建议");
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("beginEdit");
				$("#btn_save").show();
				$("#btn_commit").show();
				$("#btn_delete").show();
				$("#btn_flowInfo").show();
				$("#complainRdForm").iForm("beginEdit");
				$("#complainRdForm").iForm("endEdit",["code","createUserName","deptName","createdate"]);
			}
			if(currStatus == "bmfzrConfirm" ){
				setFormField(complainRdFormData);
				if(complainHandlerUser.indexOf(loginUserName)>-1){//显示处理部门
					$("#complainRdForm").iForm("show",["handleDept"]);
					$("#complainRdForm").iForm("beginEdit",["handleDept"]);	
				}
			};
			if(currStatus == "bmfzrHandle"){//处理阶段
				setFormField(complainRdFormData);
				if(complainHandlerUser.indexOf(loginUserName)>-1){
						$("#handleRds").show();
					    $("#inPageTitle1").show();
					if(processRd ==null){
						$("#formDiv").show();
						$("#datagridDiv").hide();
					}else{
						$("#formDiv").show();
						$("#datagridDiv").show();
					}
				}
			}; 
			if(currStatus == "sqrConfirm" ){
				setFormField(complainRdFormData);
				if( loginUserName == complainHandlerUser){ //结果确认显示是否满意
					$("#complainRdForm").iForm("show",["complainActive"]);
					$("#complainRdForm").iForm("beginEdit",["complainActive"]);	
				}
			};
			if(currStatus == "end"){ //结束 :1、处理后结束 2、不处理结束
				auditInfoShowBtn = 0;
				setFormField(complainRdFormData);
				if(processRd != null){
					$("#complainRdForm").iForm("show",["complainActive"]);
					$("#complainRdForm").iForm("endEdit",["complainActive"]);
					$("#handleRds").show();
					$("#inPageTitle1").show();
					$("#formDiv").hide();
					$("#btn_audit").hide();
					$("#datagridDiv").show(); 
					$("#detailsTable").datagrid("resize");
					$("#detailsTable").datagrid("loadData",processRd);	
				}else{
					$("#btn_audit").hide();
				}
			};
			FW.fixToolbar("#toolbar1");
			},"json");
		}



function valid(){
	if(!$("#complainRdForm").valid()){
		return false;
	}
	return true;
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
function stop(){
	closeCurPage();
};

/**提交意见和建议*/
function commitcomplainRd(commitStyle){  
	if(commitStyle=="commit"){
		$("#btn_commit").button('loading');
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
		FW.error("意见和建议不能为空");
		$("#btn_commit").button('reset');
		return ;
	}else{
		if(content.length>2000){
			FW.error("意见和建议内容过长，最好不要超过2000字符");	
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
			workFlow.showAudit(taskId,JSON.stringify(SendFormData),closeTab,null,null,"",0,function(){$("#btn_commit").button('reset');});
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
	if(currStatus =='bmfzrConfirm' || currStatus =='sqrConfirm'){
		if(!$("#complainRdForm").valid()){
			return ;
		}
	}
	if(currStatus =='bmfzrHandle'){
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
	
	var variables = [{'name':'var','value':bmferId}]; 
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
 * 打印
 */
/*function print(){
	var printUrl = "http://timss.gdyd.com/";
	var isShow = false;
	if("end" == currStatus || true == hasHandleRds){
		isShow = true;
	}
	var src = fileExportPath + "preview?__report=report/TIMSS2_ATD_COMPLAIN.rptdesign&__format=pdf"+
						"&id="+complainRdId+"&siteid="+Priv.secUser.siteId+"&isShow="+isShow+
						"&author="+loginUserId+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
	var title ="意见和建议单";
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
}*/

function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
/**  作废（非草稿，仅工单发起人可以作废）  */
function obsolete(){
	Notice.confirm("确定作废|确定作废该条记录信息么？",function(){
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

/**  删除（草稿状态下可以删除） */
function deleteCp(){
	Notice.confirm("确定删除|确定删除该条记录信息么？",function(){
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