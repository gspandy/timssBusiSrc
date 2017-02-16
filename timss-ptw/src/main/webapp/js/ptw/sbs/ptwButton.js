function initBtns(){
	$("#ptwToolbar .btn").hide();
	if(ptwProcesses){
		var moreMenu = [];
	    for(var i in ptwProcesses){
	    	var title = "";
	    	if(ptwProcesses[i].wtStatus == 400){
	    		title = "签发会签审批信息";
	    	}else if(ptwProcesses[i].wtStatus == 500){
	    		title = "许可会签审批信息";
	    	}else if(ptwProcesses[i].wtStatus == 600){
	    		title = "结束会签审批信息";
	    	}
	    	moreMenu.push({title:title,group:"auditView",onclick:"showAuditInfo("+ptwProcesses[i].processId+");"});
	    }
	    $("#ptw_btn_auditInfo").show();
		$("#ptw_btn_auditInfoDiv").show().iMenu("init",{
	    	menu : moreMenu
	    });
	}
	
	if(taskId){
		if(isBelongToAudit){
			$("#ptw_btn_process").show();
		}
	}else{
		appendImportBtn();
		if(isNewWt || isCopyWt){
			$("#ptw_btn_newSubmit").show();
			if(!isFireWt){
				$("#ptw_btn_attachFire").show();
			}
//			if(isNewWt){
//				$("#ptw_btn_importHisPtw").show(); //显示“由历史票导入”按钮
//			}
//			$("#ptw_btn_importSptw").show();  //显示“标准工作票导入”按钮
			$("#ptw_btn_uploadFile").show();
		}else{
			//查看动火票
			if(fireIds!= null && fireIds.length > 0){
				if(fireIds.length == 1){
					$("#ptw_btn_queryFireWtSingle").show();
					$("#ptw_btn_queryFireWtSingle").click(function(){
						openFireWt(fireIds[0]);
					});
				}else{
					var moreMenu = [];
				    for(var i = 0 ;i < fireIds.length;i++){
				    	moreMenu.push({title:"动火票"+(i+1),group:"groupView",id:"btn_QueryFire"+fireIds[i],onclick:"openFireWt("+fireIds[i]+");"});
				    }
					$("#ptw_btn_queryFireWt").show();
					$("#ptw_btn_queryFireWtDiv").show().iMenu("init",{
				    	menu : moreMenu
				    });
				}
			}
			var attachFireFlag = false;//是否展示附件动火票按钮
			if(ptwStatus <= 300){//草稿、未签发
				if(loginUserId == ptwCreateUser){
					$("#ptw_btn_iuuse").hide();
				}else{
					$("#ptw_btn_iuuse").show();
				}
				attachFireFlag = true;
//				$("#ptw_btn_iuuse").show();
				$("#ptw_btn_modify").show();
				$("#ptw_btn_abort").show();
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
				
				if(ptwInfo.createUser == ItcMvcService.getUser().userId){
					$("#ptw_btn_delete").show();
				}
				
			}else if(ptwStatus == 400){//已签发
				$("#ptw_btn_apprv").show();
				$("#ptw_btn_abort").show();
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
				attachFireFlag = true;
			}else if(ptwStatus == 500){//已许可
				$("#ptw_btn_finish").show();
				$("#ptw_btn_remark").show();
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
				if(!isFireWt){
					//更多办理任务的按钮控制
					var moreMenu = [];
				    if(ptwChangeWpic == null){
				    	moreMenu.push({title:"工作负责人变更",group:"groupView",id:"btn_ChangeWpic",onclick:"changeWpic();"});
				    }
				    if(ptwExtand == null){
				    	moreMenu.push({title:"工作票延期",group:"groupView",id:"btn_ExtandPtw",onclick:"extandPtw();"});
				    }
				    moreMenu.push({title:"工作票收回",group:"groupView",id:"btn_WaitPtw",onclick:"waitPtw();"});
				    moreMenu.push({title:"重新开工",group:"groupView",id:"btn_RestorePtw",onclick:"restorePtw();"});
				    if(param_config.hasRemarkWork == 1 && ptwRemarkTask == null){
				    	moreMenu.push({title:"增加任务",group:"groupView",id:"btn_RemarkTask",onclick:"remarkTask();"});		    
				    }
				    $("#ptw_btn_more").show();
					$("#ptw_btn_moreDiv").show().iMenu("init",{
				    	menu : moreMenu
				    });
				    
				    if(ptwNeedRestore){
						$("#btn_WaitPtw").hide();
					}else{
						$("#btn_RestorePtw").hide();
					}
				}
			}else if(ptwStatus == 600){//已结束
				$("#ptw_btn_end").show();
				$("#ptw_btn_remark").show();
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
			}else if(ptwStatus == 700){//已终结
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
			}else if(ptwStatus == 800){//已作废
				$("#ptw_btn_print").show();
				$("#ptw_btn_copy").show();
			}else if(ptwStatus >= 1000){//标准票
				$("#ptw_btn_copy").show();
				if(isPtwAdmin){
					$("#ptw_btn_abort").show();
				}
			}
			if(!isFireWt && attachFireFlag){  //如果不是动火票就可以附加动火票
				$("#ptw_btn_attachFire").show();
			}else{
				$("#ptw_btn_attachFire").hide();
			}
		}
	}
	
	$("#ptw_btn_close").show();
	Priv.apply();
	if(700 != ptwStatus){ //非终结状态不能生成标准工作票
		$("#ptw_btn_ptwNewSptw").hide();
	} 
	/*if(!(isNewWt || isCopyWt)){
		$("#ptw_btn_importSptw").hide();  //隐藏“标准工作票导入”按钮
	}*/
	
	FW.fixToolbar("#ptwToolbar");
}

/**
*动态添加导入按钮下的标准工作票导入、历史票导入
*/
function appendImportBtn(){
	var appendHtmlHis = '<li><a onclick="importHisPtw()">历史票</a></li>';
	var appendHtmlSptw = '<li><a onclick="importSptw()">标准工作票</a></li>';
	if(isNewWt || isCopyWt){
		$("#importButtonGroup").show();
//		$("#importButton").append(appendHtmlSptw);  //sbs没有标准工作票
		if(isNewWt){
			$("#importButton").append(appendHtmlHis); //显示“由历史票导入”按钮
		}
	}
}

function getFireBaseInfo(){
	var fireBaseInfo = $("#fireBaseInfoForm").iForm('getVal');
	fireBaseInfo.fireWpExec = $("#f_fireWpExecNo").iCombo('getTxt');
	fireBaseInfo.guardXmCo = $("#f_guardXmCoNo").iCombo('getTxt');
	fireBaseInfo.attachWtId = params.attachWtId;
	fireBaseInfo.wtId = ptwId;
	return fireBaseInfo;
}

/**保存工作票*/
function savePtwInfo(notClosePage){
	if(ptwId){
		return tempSavePtwInfo(notClosePage);
	}else{
		return insertPtwInfo(notClosePage);	
	}
}

/**开始编辑工作票*/
function editPtwBaseInfoAndSafe(){
	isEditing = true;
	canSelectTree = true;
	if(ptwStatus == 300){
		//按钮组全部隐藏
		$("#div_btn_group .btn-default").hide();
		$("#ptw_btn_print").hide();
		$("#ptw_btn_tempSave").show();
		if(!isFireWt){
			$("#ptw_btn_attachFire").show();
		}
	}else if(ptwStatus == 400){
		$("#ptw_btn_tempSave").show();
		$("#ptw_btn_modify").hide();
	}
	$("#ptw_btn_uploadFile").show();
	Priv.apply();
	if(700 != ptwStatus){ //非终结状态不能生成标准工作票
		$("#ptw_btn_ptwNewSptw").hide();
	} 
	FW.fixToolbar("#ptwToolbar");
	
	var editFields=["createUserNameAndTime","licWorkClassNum","licWorkClassPeople","licWorkClass",
	   	         "preWorkEndTime","preWorkStartTime","workPlace","workContent","workContentFire",
	   	         "workContentFj","woWorkTask","eqName","wtTypeId","wtNo","licWpicNo"];
	$("#baseInfoForm").iForm("beginEdit",editFields);
	
	var fireEditFields = ["guardXmCoNo","fireWpExecNo","fireWc","fireUnit"];
	$("#fireBaseInfoForm").iForm("beginEdit",fireEditFields);
	
	beginEditSafeItems();
}

/**签发工作票*/
function issuePtw(){
	//如果处于编辑模式，则需要同时将编辑的数据更新上去
	var params = {};
	if(isEditing){
		params = getPtwInfoForSave();
		if(!params){
			return false;	
		}
	}
	
	var src = basePath + "page/ptw/core/popVerifyIssue.jsp";//对话框B的页面
	var dlgOpts = {
        width : 400,
        height:250,
        title:"签发工作票"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	params.id = ptwId;
            	params.password = form.password;
            	params.wtStatus = 400;
            	params.isEditing = isEditing;
            	params.wtNo = ptwNo;
            	params.ptwTypeCode = ptwTypeCode;
            	$.post(basePath + "ptw/ptwInfo/issuePtw.do",params,function(data){
					if(data.result == "ok"){
						FW.success("签发工作票成功");
						_parent().$("#itcDlg").dialog("close");
						//弹出审批框
						var taskId = data.taskId;
						if(taskId){
							var workFlow = new WorkFlow();
							workFlow.submitApply(taskId,"",agree,rollback,stop,"");
						}else{
							closePtw();
						}						
					}else{
						FW.error(data.result);
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}
/**结束工作票*/
function finPtw(){
	var latestWpicInfo = getLatestWpic();
	var latestWpic = latestWpicInfo.wpic;
	var latestWpicNo = latestWpicInfo.wpicNo;
	var src = basePath + "page/ptw/core/popVerifyFinish.jsp?latestWpic="+encodeURI(latestWpic);//对话框B的页面
	var dlgOpts = {
        width : 450,
        height:300,
        title:"结束工作票"
    };
	var btnOpts = [{
        "name" : "取消",
        "onclick" : function(){
            return true;
        }
    },{
        "name" : "确定",
        "style" : "btn-success",
        "onclick" : function(){
            //itcDlgContent是对话框默认iframe的id
            var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
            //获取弹出框的数据，如果验证不通过，返回值为null
            var form = p.getFormData();
            if(form != null){
            	//获取工作负责人
            	var finParams = {id:ptwId,wtStatus:600,wtNo:ptwNo,finWpic:latestWpic,finWpicNo:latestWpicNo,finTime:form.finTime};
            	$.post(basePath + "ptw/ptwInfo/finPtw.do",{password:form.password,finParams:JSON.stringify(finParams)},function(data){
					if(data.result == "ok"){
						FW.success("结束工作票成功");
						_parent().$("#itcDlg").dialog("close");
						//弹出审批框
						var taskId = data.taskId;
						if(taskId){
							var workFlow = new WorkFlow();
							workFlow.submitApply(taskId,"",agree,rollback,stop,"");
						}else{
							closePtw();
						}
					}else{
						FW.error(data.result);
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}
/**复制工作票*/
function copyPtw(){
	if(isFireWt){
		$("#inner-title").html("新建动火票");
	}else{
		$("#inner-title").html("新建工作票");
	}
	canSelectTree = true;
	ptwId = null;
	ptwStatus = isStdWt ? 1000: 200;
	isCopyWt = true;
	initBtns();
	
	//动火信息的展示
	if(isFireWt){
		if($("#fireBaseInfoDiv").css("display") == "none"){
			$("#fireBaseInfoDiv").show().iFold("init");
			$("#fireBaseInfoForm").iForm("init",{"fields":fireBaseInfoField,"options":{validate:true}});
		}else{
			$("#fireBaseInfoForm").iForm("beginEdit");
		}
	}
	
	$("#baseInfoForm").iForm("beginEdit");
	$("#baseInfoForm").iForm("hide","keyBoxNo");
	$("#baseInfoForm").iForm("setVal",{id:null,workOrderId:null,workOrderNo:null,keyBoxNo:null,preWorkStartTime:null,preWorkEndTime:null,isStdWt:0});
	isStdWt = false;
	
	initBaseInfoByConfig();
	
	//补充安全措施隐藏
	$("#safeItem5").iFold("hide");
	
	beginEditSafeItems();
	//许可信息隐藏
	if(hasInitedLicInfo){
		$("#licInfoDiv").iFold("hide");
	}
	
	//附加文件开始编辑
	beginCopyUploadfile();
	
	//隐藏更多任务、备注
	$("#changeWpicDiv").iFold("hide");
	$("#extandDiv").iFold("hide");
	$("#waitRestoreDiv").iFold("hide");
	$("#remarkTaskDiv").iFold("hide");
	$("#remarkDiv").iFold("hide");
}
function openNewFireWt(){
	openNewFireTab("ptwInfoDetail"+ new Date().getTime(),"新建动火票","ptw/ptwInfo/preQueryPtwInfo.do",{opType:"newPtw",isFireWt:1,attachWtId : ptwId});
}

function openFireWt(fireId){
	openNewFireTab("ptwInfoDetail"+fireId,"查看动火票","ptw/ptwInfo/preQueryPtwInfo.do",{opType:"handlePtw",id:fireId});
}
/**
 * 导入标准工作票
 */
function importSptw(){
	showStdPtwDtlIframe();
}
function initSafeItemsFromSptw(ptwSafes){
	if(ptwSafes){
		var safeDatas = ptwSafes.safeDatas;
		ptwSafes.safeDatas = convertToSafeItemStyle(safeDatas);
		for(var i = 0 ; i < ptwSafes.ptwTypes.length;i++){
			var safeType = ptwSafes.ptwTypes[i];
			var safeType = ptwSafes.ptwTypes[i];
			if(safeType == 6){
				$("#safeItem6Content").find(".safe-input-content").show().html(FW.specialchars(safeDatas[j].safeContent));
				$("#safeItem6Content").find(".safe-input").val(safeDatas[j].safeContent).hide();
			}else{
				$("#safeItem"+ safeType).find(".wrap-underline").remove();	//先删掉html
				initSafeItemListByData("safeItem"+safeType,ptwSafes)
				beginEditSafeItemList("safeItem"+safeType);
			}
		/*	$("#safeItem" + safeType).iFold("show");
			$("#safeItem" + safeType).empty();
			for(var j = 0 ; j < safeDatas.length;j++){
				if(safeDatas[j].safeType == safeType){
					if(safeType == 6){
						$("#safeItem6Content").find(".safe-input-content").show().html(FW.specialchars(safeDatas[j].safeContent));
						$("#safeItem6Content").find(".safe-input").val(safeDatas[j].safeContent).hide();
					}else{
						var append = $(safeContentTpl).appendTo($("#safeItem" + safeType));
						$(append).find(".safe-input-content").html(FW.specialchars(safeDatas[j].safeContent));
						$(append).find(".safe-input").val(safeDatas[j].safeContent);
					}
					
					$(append).find(".exec-span").hide();
					$(append).find(".safe-unremove-remarks").hide();
					$(append).find(".remover-span").hide();
					
					param_config={};
					param_config.hasElec = 1;
					beginEditSafeItems();
				}
			}*/
		}
	}
}
function convertToSafeItemStyle(items){
	for ( var int = 0; int < items.length; int++) {
		var array_element = items[int];
		array_element.content = array_element.safeContent;
		array_element.showOrder = array_element.safeOrder;
	}
	return items;
}
