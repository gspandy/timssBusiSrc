var formFields2 = [];
//datagrid列
var datagrid_columns = [[ ]];
/** 动态添加form表单
 * @param formFields 原有的form表单
 * @param addFormField 需要添加的信息字段
 * @param formBaseFields  基础信息库
 */
function appendFormFields(formFields,addFormField,formBaseFields){
	//动态添加字段到form表单中
	var temp = addFormField.split(",");
	for(var i=0; i<temp.length;i++){
		var tempFieldid = temp[i].trim();
		for(var j=0 ;j<formBaseFields.length; j++){
			var baseFieldObj = formBaseFields[j];
			if(baseFieldObj.id == tempFieldid){
				if(finNameEn =='businessentertainment' && tempFieldid =='accType'){  //业务招待费报销的时候，记账类型必须走公司层面
					baseFieldObj.data = [["company","公司"]];
				}
				formFields.push(baseFieldObj);
			}
		}
	}
}


//对表单赋值
function initFormVal() { // 
	if(apply_budget == 0 && oprModeEn != "add"){
		apply_budget = null;
		if(bussinessStatus == "finance_draft"){
			apply_budget = "";
		}
	}
	$("#autoform").ITC_Form("loaddata", {
		"finance_typeid":finTypeEn,
		"fname" : fname,
		"createid" : createid,
		"creatorname" : creatorname,
		"beneficiary" : beneficiary,
		"beneficiaryid":beneficiaryid,
		"formDay" : formDay,
		"strdate":strdate||null,
		"enddate":enddate||null,
		"total_amount" : total_amount,
		"description":description,
		"join_boss":join_boss,
		"join_bossid":join_bossid,
		"join_nbr":join_nbr,
		"applyId":applyId,
		"applyName":applyName,
		"apply_budget":apply_budget
	});
	if(applyName != '' && applyName != null){
		showOpenApplyInfo("applyId","applyName");
	}
}

/**
 * 打开申请单详情链接
 * applyId 申请单ID控件
 * applyName 申请单名称控件
 */
function showOpenApplyInfo(applyObjId,applyObjName){
	$("div[fieldid='"+applyObjName+"']").each(function(){
	      var span = $(this).find("span");
	      span.wrap("<a onclick='openApplyInfoFromFinance(\""+$("#f_"+applyObjId+"").val()+"\");' style='display:block;width:100%'></a>");
	});
};
function openApplyInfoFromFinance(applyId){
	var applyType = getApplyType();
	var newId="finEditFMATab"+applyId;
	var pageName="查看费用申请";
	var pageUrl=basePath + "finance/fma/editFMAJsp.do?id="+applyId+"&applyType="+applyType;
	var oldId=FW.getCurrentTabId();
	addEventTab( newId, pageName, pageUrl, oldId );
};
function getApplyType(){
	var result = null;
	switch(finNameEn){
	    case 'travelcost':
	    	 result = 'travelapply';
	        break;
	    case 'businessentertainment':
	    	 result = 'businessentertainment';
	        break;
	    default:
	    	 result = null;
	}
	return result;
};
function setFormValFromApply(applyId){
	//提交操作url
	var temppostOprUrl = basePath + "finance/fma/queryFinanceManagementApplyById.do";
	var temppostOprData = {"id": applyId};
	$.post( temppostOprUrl, temppostOprData, function(data) {
		if (data.flag == "success") {
			var applyformdata = data.data;
			$("#autoform").iForm("setVal",{"applyId":applyformdata.id,"fname":applyformdata.name,
				"applyName":applyformdata.name,"apply_budget":applyformdata.budget});
		} 
	});
}



//暂存/提交报销单
function saveData(_this,submitType) {
	//表单有效性检验
	if (!$("#autoform").valid()) {
		return false;
	}
	
	$(_this).button('loading');
	//检验明细数据必输、获取datagrid数据
	var detail = JSON.stringify($("#finTable").datagrid("getRows"));
	if (detail == "[]") {
		Notice.errorTopNotice("必须添加明细");
		$(_this).button('reset');
		return false;
	}
	
	//获取主表单数据(字符串形式)
	var formObj = $("#autoform").iForm("getVal");
	var formData = JSON.stringify(formObj);
	if(formObj.applyId != "" && formObj.applyId != null&&
			formObj.apply_budget!="" && new Number(formObj.total_amount) > new Number(formObj.apply_budget)){
		FW.error("报销金额大于批复金额，请修改报销明细金额");
		$(_this).button('reset');
		return false;
	}
	
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=ids.field3;

	//获取主表单数据(字符串形式)
	var formData = JSON.stringify($("#autoform").iForm("getVal"));
	
	var postOprData="";
	
	if( oprModeEn == "add" ) {
		postOprData = {
			"formData": formData,
			"detail": detail,
			"submitType": submitType,
			"beneficiaryid": beneficiaryid,
			"finNameEn": finNameEn,
			"finTypeEn": finTypeEn,
			
			"uploadIds": uploadIds
		};
	} else if( oprModeEn == "edit" ) {
		//提交操作url
		pri_postOprUrl = basePath + "finance/financeInfoController/editFinanceInfo.do";
		postOprData = {
			"formData": formData,
			"detail": detail,
			"submitType": submitType,
			"beneficiaryid": beneficiaryid,
			"finNameEn": finNameEn,
			"finTypeEn": finTypeEn,
			"fid": fid,
			"flowName": flowName,
			"uploadIds": uploadIds
		};
	}
	
	$.post( pri_postOprUrl, postOprData, function(data) {
		if (data.result == "success") {
			if( oprModeEn == "add" ) {
				//在创建页面中提交后,以后的提交变成更新数据
				oprModeEn = "edit";
				//提交操作url
				pri_postOprUrl = basePath + "finance/financeInfoController/editFinanceInfo.do";
				fid = data.fid;
				taskId = data.taskId;
				
				//令报销类型变成只读
				$("#autoform").iForm("endEdit", "finance_typeid");
			} else if( oprModeEn == "edit" ) {
				if( data.fid != undefined ) {
					fid = data.fid;
				}
				if( data.taskId != undefined ) {
					taskId = data.taskId;								
				}
			}
				
			if( submitType == "save" ) { //暂存
				_parent().Notice.successTopNotice("保存成功");
				$(_this).button('reset');
			} else if (submitType == "submit") { //提交
				_parent().Notice.successTopNotice("提交成功");
				
				//提交后,"暂存/提交/添加明细/继续添加明细"按钮隐藏,表单关闭编辑,弹出审批
				$("#saveBtn").hide();
				$("#submitBtn").hide();
				$("#deleteBtn").hide();
				$("#abolishBtn").hide();
				$("#addDetailBtn").hide();
				$("#goOnAddDetailBtn").hide();
				
				$("#autoform").iForm("endEdit");

				editDatagrid="false";
				$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
				
				FW.fixRoundButtons("#btn_toolbar"); //修复工具栏按钮的圆角问题
				
				createFirstApprove(finTypeEn);
			}
		} else {
			Notice.errorTopNotice("保存失败");
		}
	});
}


//初始化按钮事件
function initPrintBtn() {
	var url = "http://" + window.location.host + "/timss-finance";
	var commonPath = "__report=report/TIMSS2_FIN_001.rptdesign&fid=" + fid
		+ "&url=" + url
		+ "&author=" + loginUserId;
	//预览PDF并提供打印
	$("#previewPdfBtn").click(function(){
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
			dlgOpts:{ width:800, height:650, closed:false, title:"报销单", modal:true }
		});
	});
}

function fixPageStyle() {
	$("#detailTitle").iFold("init");
	$("#uploadfileTitle").iFold("init");
	$("#previewPdfBtn").hide();
	$("#autoform").iForm( "hide", "accType" ); //隐藏账务类型
}

//根据规则生成流程定义Key
function getDefKeyByfinTypeEn(finTypeEn) {
	for(var i=0 ; i<finPageConfData.length;i++){
		if(finTypeEn == finPageConfData[i].reimburseType){ 
			defKey = finPageConfData[i].flowKey;
			break;
		}
	}
}

//录入ERP对话框
function inputERP() {
	var getReimbursementMan;   //取报销款的人
	
	var finTableJson;
	
	var rowLen = $("#finTable").datagrid("getRows").length;
	var beneficiaryStr = "";
	for( var i=0; i<rowLen; i++ ) {  //获取datagrid里面的报销人信息，并连接起来
		if( i != rowLen-1 ) {
			beneficiaryStr += $("#finTable").datagrid("getRows")[i].beneficiary + ",";
		} else {
			beneficiaryStr += $("#finTable").datagrid("getRows")[i].beneficiary;
		}
	}
	
	//如果填单人在报销人列中,则报销款给填单人
	if(beneficiaryStr.indexOf(creatorname) != -1) {
		getReimbursementMan = creatorname;
	} else { //如果填单人不在报销人列中,则报销款给第一个报销人
		getReimbursementMan = (beneficiaryStr.split(","))[0];
	}
	
	var fDocNbr = 0;  //附件数量
	for( var j=0; j<rowLen; j++ ) {
		fDocNbr += parseInt($("#finTable").datagrid("getRows")[j].doc_nbr);
	}
	
	fDocNbr += 2; //ERP显示的附件张数为报销系统基础之上加2
	
	FW.set( "getReimbursementMan", getReimbursementMan );
	FW.set( "finNameEn", finNameEn );
	FW.set( "finTypeEn", finTypeEn );
	FW.set( "fid", fid );
	FW.set( "fDocNbr", fDocNbr );
	
	var src = basePath + "page/finance/core/financeDetail/inputERP.jsp";
	
	var btnOpts = [{
		"name" : "取消",
		"float" : "right",
		"style" : "btn-default",
		"onclick" : function() {
			return true;
		}
	},{
		"name" : "确定",
		"float" : "right",
		"style" : "btn-success",
		"onclick" : function() {
			return submitERP(); //提交ERP
		}
	}];
	
	//ERP对话框
	var erpDlgOpts = {
		width : 1000,
		height : 500,
		closed : false,
		title : "凭证预览",
		modal : true
	};

	FW.dialog("init", {
		"src" : src,
		"dlgOpts" : erpDlgOpts,
		"btnOpts" : btnOpts
	});
}



function authInit2() {// 编辑页面进入的五种情况
	//如果没有附件，就隐藏
	if((uploadids==''||uploadids==null||uploadids=="[]")
			&&(bussinessStatus != finWorkFlowStatusCodeConstant.APPLICANT_MODIFY)) {
		$("#uploadfileTitle").iFold("hide");
		$("#uploadfile").hide();
	}
	$("#deleteBtn").hide();//不可删除报销单
	$("#innerTitle").html( finNameCn + "详情"); //显示标题
	if ( createid == loginUserId && bussinessStatus == finWorkFlowStatusCodeConstant.FIN_DRAFT ) {
		// ⑴当创建人姓名=当前登录者姓名，且创建人id=当前登录人id,且数据为草稿，且流程未开启时
		$("#innerTitle").html( "编辑" +  finNameCn); //显示标题
		isDarftStatus2();
	} else if (createid == loginUserId && bussinessStatus == finWorkFlowStatusCodeConstant.FIN_FLOW_STR
				&& processInstId != "" && taskId != "") {
		// ⑵当创建者=当前登录者姓名，且创建人id=当前登录人id,且流程开启，状态为申请开始时
		$("#innerTitle").html( "编辑" +  finNameCn); //显示标题
		beginFlowStatus2();
	}else if (processInstId != "" && taskId != "" && checkApprover == "approver") {
		// ⑶当前登录人==当前环节的审批者
		isApprover = "true";
		atApprovedStatus2();
		FW.privilegeOperation("finance_showAccType", "autoform"); //加载记账类型可视权限
	} else if (processInstId != "" && taskId != "" 
			&& createid == loginUserId && bussinessStatus != finWorkFlowStatusCodeConstant.FIN_DRAFT) {
		//⑷当创建者！= 当前登录者，且状态为不草稿时
		atOtherStatusFlowStarted2();
	} else {
		//⑸其他可能性,当创建者！=当前登录者，且状态为草稿
		atOtherStatusFlowUnStart2();
	}
	
	FW.fixRoundButtons("#btn_toolbar");
}


function beginFlowStatus2() {
	$("#autoform").iForm("endEdit");
	$("#uploadform").iForm("endEdit");
	$("#editBtn").hide();//申请状态时隐藏
	$("#saveBtn").show();//未点编辑前，不允许保存
	$("#cancelBtn").hide();//未点编辑前，不需要取消
	$("#submitBtn").show();//不可提交并创建信流程
	$("#abolishBtn").show(); //申请开始允许作废报销单
	$("#deleteBtn").hide();
	$("#firstApproveBtn").hide();//允许直接弹出选择审批人,显示为 提交
	$("#approveBtn").hide();//无法直接弹出选择审批人
	$("#printBtn").show();
	$("#previewPdfBtn").show();
	$("#flowInfoWithoutStatus").hide();//已进入流程,不需要使用流程图
	$("#flowInfoWithStatus").show();//允许查看流程信息
	$("#flowInfoWithStatusAndApprove").hide();//带审批按钮流程信息，不可查看
	$("#inputERPBtn").hide();//会计角色+审批状态+未生成凭证,才需要显示生成凭证
	
	$("#addDetailBtn").hide();//未编辑，不允许添加明细
	$("#goOnAddDetailBtn").hide();//未编辑，不允许继续添加明细
	
	$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
	beginEdit2(); //进去页面就为编辑状态
	
	oprModeCn = "编辑";
}

//草稿状态
function isDarftStatus2() {
	$("#autoform").iForm("endEdit");
	$("#uploadform").iForm("endEdit");
	$("#editBtn").hide(); //申请状态时隐藏
	$("#saveBtn").show(); //未点编辑前，不允许保存
	$("#cancelBtn").hide(); //未点编辑前，不需要取消
	$("#submitBtn").show(); //不可提交并创建信流程
	$("#abolishBtn").hide(); //不允许作废报销单
	$("#firstApproveBtn").hide(); //允许直接弹出选择审批人,显示为 提交
	$("#approveBtn").hide(); //无法直接弹出选择审批人
	$("#printBtn").show();
	$("#deleteBtn").show();
	$("#previewPdfBtn").show();
	//$("#flowInfoWithoutStatus").hide(); //已进入流程,不需要使用流程图
	$("#flowInfoWithoutStatus").show(); //允许查看流程图
	//$("#flowInfoWithStatus").show(); //允许查看流程信息
	$("#flowInfoWithStatus").hide(); //不允许查看流程信息
	$("#flowInfoWithStatusAndApprove").hide(); //带审批按钮流程信息，不可查看
	$("#inputERPBtn").hide(); //会计角色+审批状态+未生成凭证,才需要显示生成凭证
	
	$("#addDetailBtn").hide(); //未编辑，不允许添加明细
	$("#goOnAddDetailBtn").hide(); //未编辑，不允许继续添加明细
	
	$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
	beginEdit2(); //进去页面就为编辑状态
	
	oprModeCn = "编辑";
}


//登录者为审批者,拥有审批权限
function atApprovedStatus2() {
	$("#autoform").iForm("endEdit");
	if(finNameEn ==finFlowKeyConstant.BUSINESS && bussinessStatus ==finWorkFlowStatusCodeConstant.WAIT_DEPTMGR_APPROVE
			&&(finTypeEn=="only"||finTypeEn=="other")){
		//只有自己或者他人业务招待费报销中的"部门经理审批"环节才有此信息
		setTimeout(function(){
			$("#autoform").iForm("show", "accType");
			$("#autoform").iForm("beginEdit", "accType");
		},200);
	}
	if(bussinessStatus == finWorkFlowStatusCodeConstant.MAIN_ACCOUNT_APPROVE){
		$("#autoform").iForm("beginEdit", "needModify");
	}
	
	$("#uploadform").iForm("endEdit");
	
	if(bussinessStatus == finWorkFlowStatusCodeConstant.APPLICANT_MODIFY){
		$("#autoform").iForm("beginEdit", ["fname","strdate","enddate","description","join_boss",
		                                   "join_bossid","join_nbr"]);
		editDatagrid = "true";
		$("#uploadform").iForm("beginEdit");
	}
	//TODO 如果是差旅费报销，则在行政部经理、领导审批时，出差类型为可修改
	if(finNameEn ==finFlowKeyConstant.TRAVEL && 
			(bussinessStatus == finWorkFlowStatusCodeConstant.WAIT_JYDEPTMGR_APPROVE || bussinessStatus == finWorkFlowStatusCodeConstant.WAIT_SUBMAINMGR_SPPROVE)){
		editDatagrid = "true";
	}
	$("#editBtn").hide();//审批状态时不可编辑
	$("#saveBtn").hide();//审批状态时不允许保存
	$("#cancelBtn").hide();//审批状态时不需要取消
	$("#submitBtn").hide();//不可提交并创建信流程
	$("#abolishBtn").hide(); //不允许作废报销单
	$("#deleteBtn").hide();//不可删除报销单
	$("#firstApproveBtn").hide();//不允许直接弹出选择审批人,显示为 提交
	$("#approveBtn").show();//允许直接弹出选择审批人,显示为审批
	$("#printBtn").show();//允许直接弹出打印按钮,显示为打印
	$("#previewPdfBtn").show();//允许直接弹出打印预览按钮,显示为预览
	$("#flowInfoWithoutStatus").hide();//已进入流程,不需要使用流程图
	$("#flowInfoWithStatus").hide();//允许查看流程信息
	$("#flowInfoWithStatusAndApprove").show();//带审批按钮流程信息，不可查看
	showGenCertBtn2();
	
	$("#addDetailBtn").hide();//审批时，不允许增加明细
	$("#goOnAddDetailBtn").hide(); //未编辑，不允许继续添加明细
	
	$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
	
	oprModeCn = "审批";
}

function atOtherStatusFlowStarted2() {
	$("#editBtn").hide();//审批状态时不可编辑
	$("#saveBtn").hide();//审批状态时不允许保存
	$("#cancelBtn").hide();//审批状态时不需要取消
	$("#submitBtn").hide();//不可提交并创建信流程
	$("#abolishBtn").hide(); //不允许作废报销单
	$("#firstApproveBtn").hide();//不允许直接弹出选择审批人,显示为 提交
	$("#approveBtn").hide();//允许直接弹出选择审批人,显示为审批
	$("#previewPdfBtn").show();
	
	$("#flowInfoWithoutStatus").hide();//已进入流程,不需要使用流程图
	$("#flowInfoWithStatus").show();//允许查看流程信息
	$("#flowInfoWithStatusAndApprove").hide();//带审批按钮流程信息，不可查看
	$("#inputERPBtn").hide();//会计角色+审批状态+未生成凭证,才需要显示生成凭证
	
	$("#addDetailBtn").hide();//流程开启，不允许增加明细
	$("#goOnAddDetailBtn").hide();//未编辑，不允许继续添加明细
	
	$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
	$("#autoform").iForm("endEdit");
	$("#uploadform").iForm("endEdit");
	//oprModeCn = "浏览";
	oprModeCn = ""; //如果只有浏览权限,则改为不显示"浏览"字眼
}

function atOtherStatusFlowUnStart2() {
	$("#autoform").iForm("endEdit");
	$("#uploadform").iForm("endEdit");
	
	$("#editBtn").hide();//审批状态时不可编辑
	$("#saveBtn").hide();//审批状态时不允许保存
	$("#cancelBtn").hide();//审批状态时不需要取消
	$("#submitBtn").hide();//不可提交并创建信流程
	$("#abolishBtn").hide(); //不允许作废报销单
	$("#deleteBtn").hide();//不可删除报销单
	$("#firstApproveBtn").hide();//不允许直接弹出选择审批人,显示为 提交
	$("#approveBtn").hide();//允许直接弹出选择审批人,显示为审批
	$("#printBtn").show();
	$("#previewPdfBtn").show();
	$("#flowInfoWithoutStatus").hide();//已进入流程,不需要使用流程图
	$("#flowInfoWithStatus").show();//允许查看流程信息
	$("#flowInfoWithStatusAndApprove").hide();//带审批按钮流程信息，不可查看
	$("#inputERPBtn").hide();//会计角色+审批状态+未生成凭证,才需要显示生成凭证
	
	$("#addDetailBtn").hide();//非创建者，不允许增加明细
	$("#goOnAddDetailBtn").hide(); //未编辑，不允许继续添加明细
	
	$("#finTable").datagrid("hideColumn", "delete"); //隐藏回收站按钮
	
	//oprModeCn = "浏览";
	oprModeCn = ""; //如果只有浏览权限,则改为不显示"浏览"字眼
}
function beginEdit2() {
	$("#autoform").iForm("beginEdit");
	$("#autoform").iForm("endEdit", "finance_typeid");
	$("#uploadform").iForm("beginEdit");
	$("#finTable").datagrid("showColumn", "delete");
	$("#editBtn").hide();
	$("#saveBtn").show();
	$("#cancelBtn").hide();
	chgAddDtlBtnStyle2(); //当超过一条记录时,改变"添加明细"按钮为"继续添加明细",反之亦然
	beginEditTable2("finTable");
	editDatagrid="true";
	//$("#uploadfileTitle").show();
	$("#uploadfileTitle").iFold("show");
	$("#uploadfile").show();
}

//当超过一条记录时,改变"添加明细"按钮为"继续添加明细",反之亦然
function chgAddDtlBtnStyle2() {
	var rows = $("#finTable").datagrid("getRows"); //获取记录行数
	//如果记录行数大于等于1,按钮变成"继续添加明细"
	if( rows.length < 1 ) {
		$("#addDetailBtn").show();
		$("#goOnAddDetailBtn").hide();
		FW.fixRoundButtons("#addDetailBtn_toolbar");
	} else if( rows.length >= 1 ) { //如果记录行数小于1,按钮变成"添加明细"
		$("#addDetailBtn").hide();
		$("#goOnAddDetailBtn").show();
		FW.fixRoundButtons("#addDetailBtn_toolbar");
	}
}

//使指定Table可编辑
function beginEditTable2(tableId) {
	var table = "#" + tableId;
	var rows = $(table).datagrid("getRows");
	for ( var i = 0; i < rows.length; i++) {
		$(table).datagrid("beginEdit", i);
	}
}
//屏蔽补充医疗保险、家属医药费、备用金、探亲路费的建单和审批功能
function hideFinType2() {
	$("#innerTitle").html( "新建" +  finNameCn); //显示标题
	$("#deleteBtn").hide()
	if( finNameEn == finFlowKeyConstant.MEDICAL  //"medicalinsurance"
		|| finNameEn == finFlowKeyConstant.FAMILYMEDICINE  //"familymedicinecost" 
		|| finNameEn == finFlowKeyConstant.PETTY   //"pettycash"
		|| finNameEn == finFlowKeyConstant.HOMETRIP ) {   //"hometripcost"
		$("#saveBtn").hide();
		$("#submitBtn").hide();
		$("#cancelBtn").hide();
		$("#firstApproveBtn").hide();
		$("#abolishBtn").hide();
		$("#approveBtn").hide();
		$("#flowInfoWithStatusAndApprove").hide();
		$("#flowInfoWithStatus").hide();
		$("#editBtn").hide();
		$("#addDetailBtn").hide();
		$("#goOnAddDetailBtn").hide();
		$("#autoform").iForm("endEdit");
		$("#autoform").iForm("beginEdit", "finType");
	}else{
		$("#firstApproveBtn").hide();
		$("#editBtn").hide();
		$("#abolishBtn").hide();
		$("#cancelBtn").hide();
		$("#approveBtn").hide();
		$("#flowInfoWithStatusAndApprove").hide();
		$("#flowInfoWithStatus").hide();
		$("#goOnAddDetailBtn").hide();
		$("#autoform").iForm("hide", "needModify");
	}
}

//显示生成凭证按钮
function showGenCertBtn2() {
	//获取生成ERP凭证标志
	var genCertFlag = "N"; //未生成凭证
	// TODO 每个站点下需要配置siteId +"_KJ"角色
	//如果不是会计角色或已生成凭证,则隐藏"生成凭证"按钮 
	if(bussinessStatus != finWorkFlowStatusCodeConstant.WAIT_ACCOUNT_APPROVE){  
		$("#inputERP").hide();  //如果不是会计复核环节
	}else if( loginRoleIds.indexOf(siteId +"_KJ") < 0 || genCertFlag == "Y" ) {
		$("#inputERP").hide();
	}
}


