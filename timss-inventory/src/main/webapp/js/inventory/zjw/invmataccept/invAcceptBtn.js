var InvAcceptBtn={
	initList:function(){
		InvAcceptBtn.search();
		InvAcceptBtn.create();
		InvAcceptBtn.initInput();
	},
	initForm:function(data){
		InvAcceptBtn.close(data);
		InvAcceptBtn.submit(data);
		InvAcceptBtn.approve(data);
		InvAcceptBtn.processinfo(data);
		InvAcceptBtn.voidFlow(data);
		InvAcceptBtn.del(data);
		InvAcceptBtn.initPrint(data);
	},
	initPrint:function(data){
		var processId= data.flow && data.flow.processId;
		var url= fileExportPath+"preview?__format=pdf&__report=report/TIMSS_INV_ZJW_ACCEPT_001.rptdesign&inacId="+data.inacId+"&procInstId="+processId+"&userid="+Priv.secUser.userId;
		FW.initPrintButton("#btn_print",url,"物资验收单");
	},
	search:function(){
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#accept_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#accept_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	},
	create:function(){
		$("#btn_new").click(function(){
			createNewAccept();
		});
	},
	initInput:function(data){
		$("#quickSearchInput").keypress(function(e) {
		    if(e.which == 13) {
		         InvAcceptBtn.initListWithPo(data);
		    }
		});
		$("#quickSearchInput").iInput("init",{
			"onClickIcon":function(){
				InvAcceptBtn.initListWithPo(data);
			}
		});
	},
	//根据采购合同号模糊搜索
	initListWithPo:function(data){
		var item=$("#quickSearchInput")[0].value;
		isSearchMode = true;
		$("#accept_grid").iDatagrid("init",{
			queryParams: {
				search: FW.stringify({itemCode:item, itemName:item})
			}
		});
		
	},
	//------- 以下是详情页面按钮
	approve:function(data){
		$("#btn_approve").click(function(){
			InvAcceptBtn.approveEvent(data);
		});
	},
	approveEvent:function(data){
		if(!InvAcceptBtn.valid(data, "")){
			return false;
		}
		var taskId=data.flow && data.flow.taskId;
		var processInstId=data.flow && data.flow.processId;
		var workFlow = new WorkFlow();
		var formData =$("#autoform").iForm("getVal");		
	    var params={};
	    params['processInstId'] = processInstId;
	    var variables = [{'name':'acptCnlus','value':formData.acptCnlus}];
	    params['variables'] = JSON.stringify(variables);
		
		$.post(
			basePath+'workflow/process_inst/setVariables.do',
			params,
            function(result){
				if(result && result.result=='ok'){
					var listData =InvAcceptDetailList.getDataThenRestore(data);
					var param={
							invMatAccept:FW.stringify(formData),
							invMatAcceptDetails:FW.stringify(listData)
					};
					workFlow.showAudit(taskId,FW.stringify(param),InvAcceptBtn.closeTab,InvAcceptBtn.closeTab,null,null,0);
				}else{
					FW.error(result.msg || "出错了，请重试");
				}
		});
	},
	
	//关闭按钮	
	close:function(){
		$("#btn_close").click(function(){
			FW.deleteTabById(FW.getCurrentTabId());
		});
	},
	closeTab:function(){
		FW.deleteTabById(FW.getCurrentTabId());
	},
	
	//暂存按钮
	save:function(){
		var buttonId="#btn_save";
		$(buttonId).click(function(){
			InvAcceptBtn.commitMatApply("save",buttonId);
		});
	},
	//提交按钮
	submit:function(){
		var buttonId="#btn_submit";
		$(buttonId).click(function(){
			InvAcceptBtn.commitMatApply("submit",buttonId);
		});
	},
	valid:function(data,buttonId){
		if(!$("#autoform").valid()){
			$(buttonId).button('reset');
			return ;
		}
		
		if(!$("#matacceptdetail_list").valid()){
			return false;
		}
		var listData =$("#matapplydetail_grid").datagrid("getRows");
		
		if( listData.length == 0){
			FW.error( "请先选择要验收的物资 ");
			$(buttonId).button('reset');
			return false;
		}
		if(buttonId.indexOf("submit") < 0){
			var acceptDetails =InvAcceptDetailList.getDataThenRestore(data);
			var allZero = true;
			for(var i=0;i<acceptDetails.length;i++){
				var acceptnum = acceptDetails[i].acceptnum;
				if(acceptnum!==null && parseFloat(acceptnum)!==0){
					allZero = false;
				}
			}
			if(allZero){
				FW.error("验收数量不能全为0");
				$(buttonId).button('reset');
				return false;
			}
		}
		return true;
	},
	commitMatApply:function(type,buttonId){
		
		if (type == "submit") {
			$(buttonId).button('loading');
		}
		//验证权限
		if(!InvAcceptBtn.valid(data, buttonId)){
			return false;
		}
		
		
		var formData =$("#autoform").iForm("getVal");
		var listData =$("#matapplydetail_grid").datagrid("getRows");
		
		
		var url=basePath+ "inventory/invmataccept/insertInvMatAccept.do";
		var param={
				"invMatAccept":FW.stringify(formData),
				"invMatAcceptDetails":FW.stringify(listData),
				"startWorkflow":type=="submit"
				};
		//加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: url,
			data: param,
			dataType : "json",
			complete : function(data) {
				data=data.responseJSON;
				taskId = data.taskId;
				
				if( data.result == "success" ){
					var inacId=data.inacId;
					var inacNo=data.inacNo;
					$("#autoform").iForm('setVal',{
						inacNo:inacNo,
						inacId:inacId
					});
					param.invMatAccept=FW.stringify($("#autoform").iForm("getVal"));
					
					var workFlow = new WorkFlow();
					if(type == "submit"){
						curProcess = data.curProcess;
						//首环节之后需要全选仓管员
						if(curProcess == "cgytcyssq"){
							workFlow.submitApply(taskId,FW.stringify(param),InvAcceptBtn.closeTab,null,1);
						}else{
							workFlow.submitApply(taskId,FW.stringify(param),InvAcceptBtn.closeTab,null,0);
						}
						
						$(buttonId).button('reset');
					}else{
						
						$(buttonId).button('reset');
						
						FW.success("暂存成功 ");
							
					}
				}else{
					if (type == "submit") {
						FW.error("提交失败 ");
						$(buttonId).button('reset');
					}else{
						FW.error("暂存失败 ");
						$(buttonId).button('reset');
					}
				}
			}
		});
	},
	
	//预览按钮
	processinfo:function(data){
		
		//流程信息
		$("#btn_processinfo").click(function(){
			var workFlow = new WorkFlow();
			var processId= data && data.flow && data.flow.processId;
	    	if(!processId){
				workFlow.showDiagram(defKey);
			}else{
				//是否是出于审批状态
				var approval=InvAcceptPriv.isApprovalStatus(data);
				
				if(!approval){
					workFlow.showAuditInfo(processId,null,0,InvAcceptBtn.approveEvent,null);
				}else{
					workFlow.showAuditInfo(processId,null,1,InvAcceptBtn.approveEvent,data);
				}
			}
		});
		
	},
	voidFlow:function(data){
		$("#btn_void").click(function(){
			var url=basePath+"inventory/invmataccept/voidFlow.do";
			Notice.confirm("确认作废该验收申请？|确定作废吗？该操作无法撤消。",function(){
				//加载用户表单数据
				$.ajax({
					type : "POST",
					async: false,
					url: url,
					data: {
						"inacId":data.inacId,
						"message":"作废"
						},
					dataType : "json",
					complete : function(data) {
						data=data.responseJSON;
						if( data.result == "success" ){
							FW.success("删除成功");
							InvAcceptBtn.closeTab();
							
						}else{
							FW.error("系统错误，作废失败");
						}
					}
				});
			});	
		});
	},
	//删除草稿
	del :function(){
		$("#btn_delete").click(function(){
			var url=basePath+"inventory/invmataccept/deleteInvMatAcceptById.do";
			Notice.confirm("确认删除该验收申请？|确定删除吗？该操作无法撤消。",function(){
				//加载用户表单数据
				$.ajax({
					type : "POST",
					async: false,
					url: url,
					data: {"inacId":data.inacId},
					dataType : "json",
					complete : function(data) {
						data=data.responseJSON;
						if( data.result == "success" ){
							FW.success("删除成功");
							InvAcceptBtn.closeTab();
							
						}else{
							FW.error("系统错误，删除失败");
						}
					}
				});
			});	
		});
	}
};
