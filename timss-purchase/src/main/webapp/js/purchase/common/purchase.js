//弹出供应商窗口
FW.queryVendorInfo = function(companyNo){
	FW.dialog("init",{
		src: basePath+"purchase/purvendor/queryVendorInfo.do?companyNo="+companyNo,
		btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		               return true;
		             }
		        }],
		dlgOpts:{width:923, height:150, closed:false, title:"供应商详细信息", modal:true,idSuffix:"Vendors"}
	});
};

/**
 * 外部调用的供应商链接
 * companyNo 供应商编码控件
 * companyName 供应商名称控件
 */
FW.showVendorInfo = function(companyNo,companyName){
	$("div[fieldid='"+companyName+"']").each(function(){
	      var span = $(this).find("span");
	      span.wrap("<a onclick='FW.queryVendorInfo(\""+$("#f_"+companyNo+"").val()+"\");' style='display:block;width:100%'></a>");
	});
};

//权限控制（判断是否具有列表操作权限）
FW.privilegeOperation = function(functionId,gridId){
	var isAllowEdit = privMapping[functionId];
	if(isAllowEdit == "1" && functionId.indexOf("listAllowEdit")>-1){
		listAllowEdit(oper,gridId);
	}else if(isAllowEdit == "1" && functionId.indexOf("btnAllowEdit")>-1){
		btnAllowEdit(oper);
	}else if(isAllowEdit == "1" && functionId.indexOf("attachAllowEdit")>-1){
		attachAllowEdit(oper);
	}
};
//附件上传是否编辑
function attachAllowEdit(oper){
	if("" != oper){
		var form = JSON.parse(oper);
		if("" != form){
			var sForm = form.stopAttach;
			if(typeof(sForm) != "undefined" && sForm != ""){
				$("#"+sForm).iForm('endEdit');
			}
		}
	}
}
//列表是否可用操作
function listAllowEdit(oper,gridId){
	if("" != oper){
		var column = JSON.parse(oper);
		var dcolumns = column.disableColumn;
		if(typeof(dcolumns) != "undefined" && dcolumns != ""){
			var colArr = dcolumns.split("||");
			for(var i = 0;i < colArr.length; i++){
				$("#"+gridId).datagrid("getColumnOption",colArr[i]).editor = "{'type':'label'}";
			}
		}
		
		var hcolumns = column.hiddenColumn;
		if(typeof(hcolumns) != "undefined" && hcolumns != ""){
			var colArrHidden = hcolumns.split("||");
			for(var i = 0;i < colArrHidden.length; i++){
				$("#"+gridId).datagrid("hideColumn",colArrHidden[i]);
			}
		}
	}
}
//按钮是否可视
function btnAllowEdit(oper){
	if("" != oper){
		var btn = JSON.parse(oper);
		if("" != btn){
			var ebtn = btn.btnHide;
			if(typeof(ebtn) != "undefined" && ebtn != ""){
				var btnArr = ebtn.split("||");
				for(var i = 0;i < btnArr.length; i++){
					$("#"+btnArr[i]).hide();
				}
			}
		}
	}
}

//增加关闭页面事件
function addFormCloseEvent(){
	var evtMap = _parent()._ITC.navTab.getEventMap();
    var tabId = FW.getCurrentTabId();
    evtMap[tabId].beforeClose = function(){
    	
    	if(null != formName){
    		currFormStatus = $("#"+formName).iForm("getVal");
    	}
    	
    	if(null != listName){
    		endEditAll();
    		currListStatus = FW.stringify($("#"+listName).datagrid("getRows"));
    		startEditAll();
    	}
    	
    	if(saveFlag){
    		return true;
    	}else{
    		if((JSON.stringify(initFormStatus)!=JSON.stringify(currFormStatus)) || 
        			(initListStatus!=currListStatus)){
    			if("editable"==isEdit){
	        		FW.confirm("关闭？|确定关闭当前页面？关闭后未保存信息将丢失。",function(){
	    	            delete(evtMap[tabId].beforeClose);
	    	            FW.deleteTabById(tabId);
	    	        });
    			}else{
    				delete(evtMap[tabId].beforeClose);
    	            FW.deleteTabById(tabId);
    			}
        	}else{
        		 return true;
        	}
    	}
    };
}

//自动生成采购单
FW.autoGenerateMatTran = function(sheetId){
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"inventory/invmattran/autoGenerateMatTran.do",
		data: {"appSheetId":sheetId },
		dataType : "json",
		success : function(data) {
			if(data.result == 'success'){
				FW.success("已发送待办到仓管员 ");
			}else{
				FW.error( "待办发送失败 ");
			}
		}
	});
};

FW.deleteDraft = function(url){
	var tip = null;
	if(classType == "Draft"){
		tip = "删除草稿";
	}else{
		tip = "作废审批";
	}
	
	Notice.confirm(tip+"|是否确定要"+tip+"？",function(){
		$.ajax({
			url : url,
			dataType : "json",
			type : "POST",
			async: false,
			data : {
				"taskId" : taskId,
				"message" : processName + "--" + tip,
				"sheetId" : sheetId
			},
			success : function(data) {
				if (data.result == "success") {
					FW.success(tip+"成功 ");
					saveFlag = true;
					pageClose();
				} else {
					FW.error(tip+"失败 ");
				}
			}
		});
	},null,"info");
};

/**
 * 初始化打印按钮
 * @param buttonId 打印按钮id，用于获取打印按钮
 * @param url 要打印的报表url，请注意url的__format参数要设置为pdf，同时不要带有__asattachment参数，如果设置该参数将不能预览.
 * @param title 打开的预览对话框的标题
 * @param extParams 预留的参数，暂时不使用
 */
FW.initPrintButton = function(buttonId,url,title,extParams){
	$(buttonId).click(function(){
		FW.dialog("init",{
			src: url,
			btnOpts:[
					{
					    "name" : "关闭",
					    "float" : "right",
					    "style" : "btn-default",
					    "onclick" : function(){
					        _parent().$("#itcDlg").dialog("close");
					    }
					}
		        ],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	});
};

FW.showOrderInfo = function(orderSheetNo){
	var url = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetNo="+orderSheetNo+"&sheetId=";
	var prefix = orderSheetNo;
    FW.addTabWithTree({
        id : "showOrderForm" + prefix,
        url : url,
        name : "采购合同信息",
        tabOpt : {
            closeable : true,
			afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
        }
    });
};

FW.showRevert=function(taskId,businessData,agree,rollback,stop,updateDesc,multiSelect,onCloseFunc){
	if(multiSelect == undefined){
		multiSelect=1;
	}
	var src = basePath+"workflow/page/auditJsp.do?taskId="+taskId+"&updateDesc="+updateDesc+"&multiSelect="+multiSelect;
	var param = {};
	param['businessData'] = businessData;
    var btnOpts =  [{
        "name" : "取消",
        "float" : "right",
        "style" : "btn-default",
        "onclick" : function(){
            _parent().$("#itcDlgShowRevert").dialog("close");
        }
    },{
        "name" : "确定",
        "float" : "right",
        "style" : "btn-success",
        "onclick" : function(){
            var p = _parent().window.document.getElementById("itcDlgShowRevertContent").contentWindow;
            p.submit(businessData,agree,rollback,stop);
        }
    }
   ];
   $.post(src,param,function(data){
	   var dialogsrc = basePath +"page/purchase/core/showRevert.jsp?taskId="+taskId+"&updateDesc="+updateDesc+"&multiSelect="+multiSelect;
	   var dlgOpts = {		
			   width : 480,
	            height:data.height,
	            idSuffix : "ShowRevert",
	            closed : false,
	            title:"退回",
	            modal:true,
	            onClose:onCloseFunc
	   };
	    Notice.dialog(dialogsrc,dlgOpts,btnOpts);
   });
    
};
FW.disableLi= function(id){
	$("#"+id).css("opacity",0.65);
	$("#"+id).attr('forbid',true);
}
FW.enableLi= function(id){
	$("#"+id).css("opacity",1);
	$("#"+id).attr('forbid',false);
}
FW.disableBtn= function(id){
	$('#'+id).attr('disabled','disabled');
}
FW.enableBtn= function(id){
	$('#'+id).show();
	$('#'+id).removeAttr('disabled');
}
FW.hideBtnGroup= function(id){
	$('#'+id).hide();
	$('#'+id+"-btn").hide();
}
FW.showBtnGroup= function(id){
	$('#'+id).show();
	$('#'+id+"-btn").show();
}
//终止采购申请
function applyStop(){
	$("#btn-stop").button('loading');
	$.ajax({
		type : "POST",
		async : false,
		url : basePath + "purchase/purapply/applyStop.do",
		data : {
			"sheetId" : sheetId,
			"orgProcessInstId" : ""!=process?processInstId:""
		},
		dataType : "json",
		success : function(data) {
			var workFlow = new WorkFlow();
			var taskId  = data.stopTaskId;
			var processInstId  = data.stopProcInstId;
			$("#btn-stop").button('reset');
			if(data.msg.length>0){
				FW.error(data.msg);
			}else{
				if ((data.result == "success" ||data.result == "true")&&data.isNew!="true") {
					workFlow.showAudit(taskId, null, pageClose, pageClose, stopProcess, proMsg, 0,stopCancelCallBack);
				}else if ((data.result == "success" ||data.result == "true")&&data.isNew=="true"){
					workFlow.closeTabFlag = false;
					workFlow.submitApply(taskId, null, pageClose, pageClose, 0, pageClose,{onBeforeCloseFun:function(){stopCancelCallBackWhileNew(workFlow,processInstId);}});
				}
			}
		}
	});
}
//采购申请作废终止
function nullifyStop(){
	$("#btn-nullifyStop").button('loading');
	$.ajax({
		type : "POST",
		async : false,
		url : basePath + "purchase/purapply/nullifyStopPurApply.do",
		data : {
			"sheetId" : sheetId
		},
		dataType : "json",
		success : function(data) {
			if("true"==data.result){
				FW.success("作废成功");
				pageClose();
			}else{
				FW.success("作废异常");
			}
		}
	});
}
//终止采购合同取消回调函数
function stopCancelCallBack(){
	$("#btn-stop").button('reset');
}
//终止采购合同新建时取消
function stopCancelCallBackWhileNew(workFlow,procInstId){
	var closeTabFlag=workFlow.closeTabFlag || false;
	if(!closeTabFlag){
		$.ajax({
			type : "POST",
			async : false,
			url : basePath + "purchase/purapply/removeStopPurApply.do",
			data : {
				"sheetId" : sheetId,
				"procInstId" : procInstId
			},
			dataType : "json",
			success : function(data) {
				workFlow.closeTabFlag = true;
			}
		});
	}
	
}