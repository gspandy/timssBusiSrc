var	fields=[
			{id:"caId",type:"hidden"},
			{title:"申请单编号",id:"caNum"},
			{id:"createUser",type:"hidden"},
			{title:"申请人",id:"createUserName"},
			{id:"deprId",type:"hidden"},
			{title:"申请部门",id:"deptName"},
			{title:"申请日期",id:"createDate",type:"date"},
			{title:"目的地类别",id:"destinationType", type:"combobox",rules:{required : true},linebreak:true,
			    	dataType : "enum",
					enumCat : "ATD_CARAPPLY_DESTTYPE"
			},
			{title:"目的地",id:"destination",rules:{required : true,maxChLength:40}},
			{title:"用车人电话",id:"phone",rules:{required : true,maxChLength:20}},
			{title:"用车开始时间",id:"startTime",type:"datetime",dataType:"datetime",
					rules:{required : true}},
		    {title:"用车结束时间",id:"endTime",type:"datetime",dataType:"datetime",
			    	rules:{required : true,greaterEqualThan:"#f_startTime"}
		    },
			{title:"用车理由",id:"reason",rules:{required : true,maxChLength:200},
		    	    type:'textarea',
				    linebreak:true,
				    wrapXsWidth:12,
				    wrapMdWidth:8,
				    height:40
			},
			{title:"同车人姓名",id:"togethers",rules:{required : true,maxChLength:200},
				   type:'textarea',
				   linebreak:true,
				   wrapXsWidth:12,
				   wrapMdWidth:8,
				   height:40
			},
			{title:"用车种类(车牌号)",id:"carType",rules:{required : true,maxChLength:40},linebreak:true},
			{title:"驾驶员",id:"driver",rules:{required : true,maxChLength:40}},
			{id:"workflowId",type:"hidden"},
			{id:"status",type:"hidden"},
			{id:"currHandUser",type:"hidden"}
			];

/**
 * 关闭当前tab 页
 */
function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
}
/**
 * 详情相应按钮显示
 */
function initPageData(caId){
	$.post(basePath + "attendance/carApply/queryCarApplyById.do",
			{"caId":caId},
	function(result){
			data = result.carApplyBean;
			taskId =result.taskId;
			fileMaps = result.fileMaps;
			status = data.status;
			processInstId = data.workflowId;
			currHandUser = data.currHandUser;
			if(fileMaps && fileMaps.length > 0){//显示附件
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("setVal",{uploadfield:fileMaps});
				$("#uploadform").iForm("endEdit");
			}else{
				$("#uploadfileTitle").iFold("hide"); 
			};
			$("#carApplyForm").iForm('setVal',data);
			$("#carApplyForm").iForm("endEdit");
			$("#btn_obsolete").hide();
			$("#btn_delete").hide();
			$("#btn_audit").hide();
			$("#btn_save").hide();
			$("#btn_commit").hide();
			if(status == 'draft'){
				$("#inPageTitle").html("编辑用车申请");
				$("#uploadfileTitle").iFold("show"); 
				$("#uploadform").iForm("beginEdit");
				$("#btn_save").show();
				$("#btn_commit").show();
				$("#btn_delete").show();
				$("#fin-print").hide();
				$("#btn_flowInfo").show();
				$("#carApplyForm").iForm("beginEdit");
				$("#carApplyForm").iForm("endEdit",["caNum","createUserName","deptName","createDate"]);
			}
			if(currHandUser!=null){
				if(currHandUser.indexOf(loginUserId)>-1){//当前登录人与当前处理人相同才显示审批
					$("#btn_audit").show();
				}
			}
			if(status == "newApply"){
				$("#btn_save").show();
				$("#btn_commit").show();
				$("#btn_audit").hide();
				$("#fin-print").hide();
				$("#carApplyForm").iForm("beginEdit");
				$("#carApplyForm").iForm("endEdit",["caNum","createUserName","deptName","createDate"]);
			 if(currHandUser == loginUserId && processInstId){
				 $("#inPageTitle").html("编辑用车申请");
				 $("#btn_obsolete").show();
				 $("#uploadfileTitle").iFold("show"); 
				 $("#uploadform").iForm("beginEdit");
			 }else{
				 $("#btn_save").hide();
				 $("#btn_commit").hide();
				 $("#carApplyForm").iForm("endEdit");
			 }
			};
			if(status == 'sendCar' && currHandUser == loginUserId){//车辆主管派车编辑用车类别和驾驶员
				 $("#carApplyForm").iForm("show",["carType","driver"]);
				 $("#carApplyForm").iForm("beginEdit",["carType","driver"]);	
			};
			if(status == 'end'){//结束状态不用审批，且显示用车类别和驾驶员
				$("#btn_audit").hide();
				$("#carApplyForm").iForm("show",["carType","driver"]);
			};
			if(status == 'obsolete'){//作废不用审批
				$("#btn_audit").hide();
			};
			FW.fixToolbar("#toolbar1");
			},"json");
		}

/**
 * 暂存
 */
function save(){
	if(!valid()){
		return ;
	}
	var formData=$("#carApplyForm").iForm('getVal');
	caId = formData.caId;
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	var jumpPath = basePath+'attendance/carApply/saveCarApply.do';
	if(caId && caId!= 'null' && caId!=null){
		jumpPath = basePath+'attendance/carApply/updateCarApply.do';
	}
	$.ajax({
		type : "POST",
		url: jumpPath,
		data: {"carApplyBean":FW.stringify(formData),"uploadIds":ids},
		dataType : "json",
		success : function(result) {				
			if(result.status == 1){
				caId = result.caId;
				$("#carApplyForm").iForm('setVal',{"caId":caId});
				FW.success("暂存成功");
			//	closeCurPage();
			}else{
				FW.error("暂存失败");
			}
		}
	});
}
/**
 * 提交
 */
function submit(_this){
	if(!valid()){
		return ;
	}
	
	var formData=$("#carApplyForm").iForm('getVal');
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	
	processInstId = formData.workflowId;
	//退回提交
	if( processInstId != null && processInstId != "" ){
		var workFlow = new WorkFlow();
		var SendFormData = {"caId":caId,"carApplyData":formData,"uploadIds":ids};
		workFlow.showAudit(taskId,JSON.stringify(SendFormData),closeTab,null,null,"",0);
		return;
	}else{
		buttonLoading(_this);
	}
	
	var jumpPath = basePath+'attendance/carApply/insertAndStartWorkflow.do';
	if(caId && caId!= 'null' && caId!=null){
		jumpPath = basePath+'attendance/carApply/updateAndStartWorkflow.do'
	}
	$.ajax({
		type : "POST",
		url: jumpPath,
		data: {"carApplyBean":FW.stringify(formData),"uploadIds":ids},
		dataType : "json",
		success : function(result) {
			if(result.status == 1){
				FW.success("提交成功");
				data = result.carApplyVo;
				bean =data.carApplyBean; 
				taskId = data.taskId;
				currHandUser = bean.currHandUser;
				$("#carApplyForm").iForm("setVal",{"caId":bean.caId,
    				"code":bean.caNum,"workflowId":bean.workflowId});
				$("#btn_save").hide();
				
				var workFlow = new WorkFlow();
				var SendFormData = {"caId":caId,"carApplyData":formData,"uploadIds":ids};
				workFlow.submitApply(taskId,JSON.stringify(SendFormData),closeTab,null,0);
			}else{
				FW.error("提交失败");
			}
		}
	});
}

function audit(){
	var formData = $("#carApplyForm").iForm("getVal");
	var workflowId = formData.workflowId;
	var caId = formData.caId;
	var status = formData.status;
	
	if(status == 'sendCar'){
		if(!valid()){
			return ;
		}
	}
	
	//获取附件数据
	var ids = $("#uploadform").iForm("getVal")["uploadfield"];
	if(ids == "undefined"){
		ids = null;
	}
	//审批弹出框
	var url = basePath + "workflow/process_inst/setVariables.do";
	var params = {};
	params['processInstId'] = workflowId;
	params['businessId'] = caId;
	
	var variables = [{'name':'destinationType','value':$("#carApplyForm").iForm("getVal").destinationType}]; 
	params['variables'] = FW.stringify(variables);
	
	$.post(url,params,function(data){
		if(data.result == 'ok'){
			var SendFormData = {"caId":caId,"carApplyData":formData,"uploadIds":ids};
			var workFlow = new WorkFlow();
			var updateDesc = "";
			var multiSelect = 0;  //multiselect为1时多选，为0时单选，不传递这个参数则默认为1.
			workFlow.showAudit(taskId,FW.stringify(SendFormData),agree,rollback,stop,updateDesc,multiSelect);
		}
	});
}
function valid(){
	if(!$("#carApplyForm").valid()){
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
function closeTab(){
	FW.deleteTabById(FW.getCurrentTabId());
}
/*作废工单（非草稿，仅工单发起人可以作废） */
function obsolete(){
	Notice.confirm("确定作废|确定作废该条信息么？",function(){
		var formData = $("#carApplyForm").iForm("getVal");
		var caId=formData.caId;
		 $.post(basePath + "attendance/carApply/obsoleteCarApply.do",{"caId":caId},
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

/**  删除工单（草稿状态下删除） */
function del(){
	Notice.confirm("确定删除|确定删除该条信息么？",function(){
		var formData = $("#carApplyForm").iForm("getVal");
		var caId=formData.caId;
		
		 $.post(basePath + "attendance/carApply/deleteCarApply.do",{"caId":caId},
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

//按钮灰化，防止重复提交
function buttonLoading(id){
	$(id).button('loading');
}

//按钮恢复
function buttonReset(id){
	$(id).button('reset');
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
	    var data={'createtime':createtime};
	    businessData['fields'] = fields;
	    businessData['data'] = data;
	    //是否是审批状态，流程信息对话下面显示审批按钮，否则不显示
	    var booleanflag = currHandUser && currHandUser.indexOf(loginUserId)>-1
		if(booleanflag && processInstId && status =='newApply'){//退回到第一个节点打开申请信息不应该显示申请按钮
			workFlow.showAuditInfo(processInstId,JSON.stringify(businessData));
		}else if(booleanflag && status != 'end' && status !='obsolete'){  //最后一个节点不要审批按钮，在新建报销单后自动的执行申请单的最后一步
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData),1,audit,null);
	    }else{
	    	workFlow.showAuditInfo(processInstId,JSON.stringify(businessData));
	    }
	}else{
		 workFlow.showDiagram(defKey);
	}
}
/**
 * 打印
 */
function print(){
	var printUrl = "http://timss.gdyd.com/";
	
	var src = fileExportPath + "preview?__report=report/TIMSS2_DPP_YCSQ_001.rptdesign&__format=pdf"+
						"&caId="+caId+"&siteId="+Priv.secUser.siteId+"&workflowid="+processInstId
						"&author="+loginUserName+"&url="+printUrl;
	var url = encodeURI(encodeURI(src));
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
		dlgOpts:{ width:800, height:650, closed:false, title:"用车申请单", modal:true }
	});
	
}