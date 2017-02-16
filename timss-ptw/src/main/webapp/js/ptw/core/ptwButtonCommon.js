function getBaseInfo(){
	var baseInfo = $("#baseInfoForm").iForm('getVal');
	if($("#baseInfoForm [fieldId='workContentFire']").is(":visible")){
		baseInfo.workContent = baseInfo.workContentFire;
	}
	if($("#baseInfoForm [fieldId='workContentFj']").is(":visible")){
		baseInfo.workContent = baseInfo.workContentFj;
	}
	baseInfo.licWpic = $("#f_licWpicNo").iCombo('getTxt');
	baseInfo.wtStatus = ptwStatus;
	return baseInfo;
}

/**获取工作票的基本信息和许可信息，同时做验证*/
function getPtwInfoForSave(){
	if($("#baseInfoForm").iForm("getVal","eqName")=="请从左边设备树选择" && !(ptwInfo && ptwInfo.eqName)){
		FW.error("请在设备树中先选择一个设备");
		return false;
	}
	
	if(! $("#baseInfoForm").valid()){
		return false;
	}
	
	if(isFireWt && ! isStdWt){
		if(! $("#fireBaseInfoForm").valid()){
			return false;
		}
	}
	
	var safeResult = getSafeInputs();
	
	if(!safeResult.valid){
		return false;
	}
	
	//修改工作内容
	var baseInfo = getBaseInfo();	
	//工作票类型在不在编辑状态时，为中文，需要修改为原来的ID
	baseInfo.wtTypeId = ptwTypeId;
	
	var params = {ptwTypeCode:ptwTypeCode,safeItems:FW.stringify(safeResult.safeItems)};
	
	//已签发时，编辑后需要更新许可信息
	if(ptwStatus >= 400 && ptwStatus <= 500 && !isCopyWt){
		if(!$("#licInfoForm").valid()){
			return false;
		}
		var licInfo = getLicInfo();
		baseInfo = jQuery.extend( {},baseInfo,licInfo);
	}
	
	//附加文件信息和动火信息的增加
	if(!isStdWt){
		var attachIds = $("#uploadFileForm").iForm("getVal");
		if(isFireWt){
			var fireInfo = getFireBaseInfo();
			fireInfo = jQuery.extend( {},fireInfo,attachIds);
			//动火票，且为非标准票时，保存动火信息
			params.fireInfo = JSON.stringify(fireInfo);
		}else{
			baseInfo = jQuery.extend( {},baseInfo,attachIds);
		}
	}
	
	params.baseInfo = JSON.stringify(baseInfo);
	return params;
}
/**新建工作票*/
function insertPtwInfo(notClosePage){
	ptwCreateUser = loginUserId;
	var params = getPtwInfoForSave(); 
	if(!params){
		return false;
	}
	$("#ptw_btn_newSubmit").button('loading');
	var result = false;
	$.post(basePath + "ptw/ptwInfo/savePtwInfo.do",params,function(data){
		if(data && data.result == "ok"){
			isNewWt = false;
			isCopyWt = false;
			$("#ptw_btn_newSubmit").button('reset');
			ptwId = data.ptwId;
			ptwStatus = data.ptwStatus;
			ptwNo = data.ptwNo;
			$("#baseInfoForm").iForm('setVal',{wtNo:data.ptwNo,id:ptwId});
			var msg = "新建工作票成功";
			if(isFireWt){
				msg = "新建动火票成功";
			}
			if(isStdWt){
				msg = "新建标准票成功";
			}
			FW.success(msg);
			ptwInfo = {};
			ptwInfo.createUser = ItcMvcService.getUser().userId;
			if(!notClosePage  && ptwStatus != 300){
				closePtw();
			}
			if(!notClosePage && ptwStatus == 300){
				initBtns();
				$("#baseInfoForm").iForm('endEdit');
				$("#fireBaseInfoForm").iForm('endEdit');
				ptwSafes = data.ptwSafes;
				endEditSafeItems();
				$("#uploadFileForm").iForm("endEdit");
				FW.set("PtwAfterInsert",true);
			}
			result = true;
		}
	},"json");
	
	return result;
}
/**更新工作票*/
function tempSavePtwInfo(notClosePage){
	var params = getPtwInfoForSave();
	if(!params){
		return false;	
	}
	$("#ptw_btn_tempSave").button('loading');
	var result = false;
	$.post(basePath + "ptw/ptwInfo/updatePtwBaseAndLicInfo.do",params,function(data){
		if(data && data.result == "ok"){
			var msg = "更新工作票成功";
			if(isFireWt){
				msg = "更新动火票成功";
			}
			if(isStdWt){
				msg = "更新标准票成功";
			}
			FW.success(msg);
			
			if(!notClosePage && ptwStatus != 300){
				closePtw();
			}
			result = true;
			$("#ptw_btn_tempSave").button('reset');
		}
	},"json");
	return result;
}

//获取最新的工作负责人
function getLatestWpic(){
	if(ptwChangeWpic != null){
		return {wpic:ptwChangeWpic.chaNewWpic,wpicNo:ptwChangeWpic.chaNewWpicNo};
	}
	return {wpic:ptwInfo.licWpic,wpicNo:ptwInfo.licWpicNo};
}

function getEndSafeData(){
	return getSafeInputs().safeItems;
}

/**终结工作票*/
function endPtw(){
	var hasJdInfo = param_config.hasElec == 1 ? true : false;
	var src = basePath + "page/ptw/core/popVerifyEnd.jsp?hasJdInfo="+hasJdInfo;//对话框B的页面
	var dlgOpts = {
        width : 800,
        height:450,
        title:"终结工作票"
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
            	var updateParams = {password:form.formData.password};
            	var latestWpicInfo = getLatestWpic();
            	updateParams.safeItems = FW.stringify(form.safeItems);
            	updateParams.endParams = FW.stringify({wtStatus:700,id:ptwId,workOrderId:ptwInfo.workOrderId,
            		endTime:form.formData.endTime,endJdxNum:form.endJdxNum,endJdxNo:form.endJdxNo,licWpicNo:latestWpicInfo.wpicNo,licWpic:latestWpicInfo.wpic});
            	$.post(basePath + "ptw/ptwInfo/endPtw.do",updateParams,function(data){
					if(data.result == "ok"){
						FW.success("终结工作票成功");
						_parent().$("#itcDlg").dialog("close");
						closePtw();
					}else{
						FW.error(data.result);
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

/**作废工作票*/
function cancelPtw(){
	var src = basePath + "page/ptw/core/popVerifyCancel.jsp?isStdWt=" + isStdWt;//对话框B的页面
	var title = isStdWt ? "作废标准票" : "作废工作票";
	var dlgOpts = {
        width : 400,
        height:250,
        title:title
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
            	var params = {id:ptwId,password:form.password};
            	if(isStdWt){
            		params.wtStatus = 4000;
            		params.isStdWt = 1;
            	}else{
            		params.wtStatus = 800;
            		params.isStdWt = 0;
            	}
            	$.post(basePath + "ptw/ptwInfo/cancelPtw.do",params,function(data){
					if(data.result == "ok"){
						FW.success(title + "成功");
						_parent().$("#itcDlg").dialog("close");
						closePtw();
					}else{
						FW.error(data.result);
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}

/**附加动火票*/
function newFirePtwInfo(){
	if(!ptwId){
		//第一个参数中|表示双行文字
		FW.confirm("确认|新建工作票未保存，是否先保存工作票？",function(){
			$.ajaxSetup({'async':false}); 
			var saved = savePtwInfo(true);
			if(saved){
				openNewFireWt();
			}
			$.ajaxSetup({'async':true}); 
		});
	}else{
		openNewFireWt();
	}
}

function verifyWpic(){
	var userId = $("#baseInfoForm").iForm('getVal',"licWpicNo");
	var result = false;
	$.post(basePath + "ptw/ptwInfo/verifyWpic.do",{"userId" : userId},function(data){
		if(data.valid == true){
			result = true;
		}else{
			FW.error("目前工作负责人手上持"+data.list[0].wtNo+"工作票");
			result = false;
		}
	},"json");
	return result;
}

/**
* @param sptwId 查询标准工作票的数据，并初始化新建页面
*/
function initDataFromSptw(sptwId){
	$.post(basePath + "ptw/ptwInfo/queryInitdataFromSptw.do", {"sptwId" : sptwId},
			function(data) {
				if(data.result == "success"){
					var ptwBaseInfoJsonData = JSON.parse(data.ptwBaseInfo) ;
					
					//获取工单编号、工单复制人、工作任务信息 用于保留工单编号带过来的信息
					var workOrderNo = $("#baseInfoForm").iForm("getVal","workOrderNo");
					var licWpicNo = $("#baseInfoForm").iForm("getVal","licWpicNo");
					var woWorkTask = $("#baseInfoForm").iForm("getVal","woWorkTask");
					ptwBaseInfoJsonData.workOrderNo = workOrderNo
					ptwBaseInfoJsonData.licWpicNo = licWpicNo;
					ptwBaseInfoJsonData.woWorkTask = woWorkTask;
					
					//表单赋值
					$("#baseInfoForm").iForm("setVal",ptwBaseInfoJsonData);
					//安全措施项赋值
					ptwSafes = data.ptwSafe ;
					initSafeItemsFromSptw(ptwSafes);
				}else{
					FW.error("导入失败");
				}
			}, "json");
}

/**
 * 导入历史票
 */
function importHisPtw(){
	var ptwFilterType = $("#f_wtTypeId").iCombo('getVal');
	var pri_dlgOpts = {
			width : 750,
			height : 500,
			closed : false,
			title : "双击选择历史票",
			modal : true
		};
	var src = basePath + "ptw/ptwInfo/queryAllHisPtwList.do?ptwFilterType="+ptwFilterType;
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	}];

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : pri_dlgOpts,
		"btnOpts" : btnOpts
	});
}

function initSafeItemsFromPtw(ptwSafes){
	if(ptwSafes){
		var safeDatas = ptwSafes.safeDatas;
		ptwSafes.safeDatas = convertToSafeItemStyle(safeDatas);
		$("#safeItem1,#safeItem2,#safeItem3").empty();
		for(var i = 0 ; i < ptwSafes.ptwTypes.length;i++){
			var safeType = ptwSafes.ptwTypes[i];
			if(safeType == 4 || safeType == 5 || safeType == 6){
				continue;
			}
			var inputPtwSafes = {};
			var safeItems = new Array();
			for(var j = 0 ; j < safeDatas.length;j++){
				if(safeDatas[j].safeType == safeType){
					safeItems.push(safeDatas[j]);
				}
			}
			inputPtwSafes.safeDatas = safeItems;
			$("#safeItem"+ safeType).find(".wrap-underline").remove();	//先删掉html
			initSafeItemListByData("safeItem"+safeType,inputPtwSafes)
			beginEditSafeItemList("safeItem"+safeType);
		}
		beginEditSafeItems();
	}
}

/**
* @param ptwId 查询历史票的数据，并初始化新建页面
*/
function initDataFromPtw(ptwId){
	$.post(basePath + "ptw/ptwInfo/queryInitdataFromPtw.do", {"ptwId" : ptwId},
			function(data) {
				if(data.result == "success"){
					var ptwBaseInfoJsonData = JSON.parse(data.ptwInfo) ;
					
					//表单赋值
					$("#baseInfoForm").iForm("setVal",{"woWorkTask":ptwBaseInfoJsonData.woWorkTask,"workContent":ptwBaseInfoJsonData.workContent,
						"workContentFj":ptwBaseInfoJsonData.workContent,"workContentFire":ptwBaseInfoJsonData.workContent,"workPlace":ptwBaseInfoJsonData.workPlace});
					//安全措施项赋值
					ptwSafes = data.ptwSafe ;
					initSafeItemsFromPtw(ptwSafes);
				}else{
					FW.error("导入失败");
				}
			}, "json");
}

/**
 * 从工作票新建标准工作票
 */
function ptwNewSptw(){
	newSptwPageWithData(ptwId);
}

/*审核工作票*/
function auditingPtw(){
	var src = basePath + "page/ptw/core/popVerifyAudit.jsp?ptwStatus="+ptwStatus;//对话框B的页面
	var auditTitle = "审核工作票";
	if(ptwTypeCode === 'HY'){
		if(ptwStatus === 410){
			auditTitle = "消防部门负责人审核";
		}else if(ptwStatus === 420){
			auditTitle = "安监部门负责人审核";
		}else if(ptwStatus === 430){
			auditTitle = "厂级负责人审核";
		}
	}else if(ptwTypeCode === 'HE'){
		if(ptwStatus === 410){
			auditTitle = "消防专责审核";
		}else if(ptwStatus === 420){
			auditTitle = "安监专责审核";
		}else if(ptwStatus === 430){
			auditTitle = "动火部门负责人审核";
		}
	}
	
	var dlgOpts = {
        width : 400,
        height:250,
        title:auditTitle
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
            	if(ptwStatus == 410){
            		params.wtStatus = 420;
            	}else if(ptwStatus == 420){
            		params.wtStatus = 430;
            	}else if(ptwStatus == 430){
            		params.wtStatus = 400;
            	}
            	params.ptwTypeCode = ptwTypeCode;
            	$.post(basePath + "ptw/ptwInfo/auditPtw.do",params,function(data){
					if(data.result == "ok"){
						FW.success("审核工作票成功");
						_parent().$("#itcDlg").dialog("close");
						closePtw();			
					}else{
						FW.error(data.result);
					}
				},"json");
            }
        }
    }];
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts});
}