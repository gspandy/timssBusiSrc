//按中文字符占字节大小重新计算精度
function fixCnPrec( value ) {
	return (Math.floor(value/3*2));
}

//使指定Table可编辑
function beginEditTable(tableId) {
	var table = "#" + tableId;
	var rows = $(table).datagrid("getRows");
	for ( var i = 0; i < rows.length; i++) {
		$(table).datagrid("beginEdit", i);
	}
}


function cancelEdit() {
	window.location.href = basePath
			+ "finance/financeInfoController/viewFinanceInfo.do?businessId="
			+ fid;
}


//当超过一条记录时,改变"添加明细"按钮为"继续添加明细",反之亦然
function chgAddDtlBtnStyle() {
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

//计算总金额并显示
function cntTtlAmtAndShow() {
	var rows = $('#finTable').datagrid('getRows'); //获取记录行数
	var vTotal = 0;
	for ( var i = 0; i < rows.length; i++) {
		vTotal += parseFloat(rows[i]['amount']);
		vTotal = parseFloat(vTotal.toFixed(2));
	}
	$("#autoform").ITC_Form("loaddata", {"total_amount" : vTotal });
}

//添加明细点击确定调用公共方法
function clkAddDtlSubBtnComm() {
	cntTtlAmtAndShow(); //计算总金额并显示
	var flag = (bussinessStatus != finWorkFlowStatusCodeConstant.APPLICANT_MODIFY)&&
				(bussinessStatus != finWorkFlowStatusCodeConstant.WAIT_JYDEPTMGR_APPROVE)&&
				(bussinessStatus != finWorkFlowStatusCodeConstant.WAIT_SUBMAINMGR_SPPROVE);
	if( flag ){//非申请人修改、行政部经理审批、分管领导审批环节
		chgAddDtlBtnStyle(); //当超过一条记录时,改变"添加明细"按钮为"继续添加明细",反之亦然
	}
}

//删除明细点击确定调用公共方法
function clkDelDtlSubBtnComm(rowIndex) {
	$("#finTable").datagrid("deleteRow", rowIndex);
	cntTtlAmtAndShow(); //计算总金额并显示
	chgAddDtlBtnStyle(); //当超过一条记录时,改变"添加明细"按钮为"继续添加明细",反之亦然
}

//检验总金额是否超出精度
function chkTtlAmtLen(mode, rowIndex, newAmt) {
	var oottlamount=0;
	var rows = $('#finTable').datagrid('getRows'); //获取记录行数
	for ( var i = 0; i < rows.length; i++) {
		//忽略
		if( mode == "edit" ) {
			var selIndex = $('#finTable').datagrid('getRowIndex', ($('#finTable').datagrid('getRows'))[i]);
			if(rowIndex == selIndex) {
				continue;
			}
		}
		
		oottlamount += parseFloat(rows[i]['amount']);
		oottlamount = parseFloat(oottlamount.toFixed(2));
	}
	
	oottlamount += parseFloat(newAmt);
	oottlamount = parseFloat(oottlamount.toFixed(2));

	if( oottlamount > 9999999.99 ) {
		Notice.errorTopNotice("总金额(元)为" + oottlamount + ", 不能超过9999999.99");
		return false;
	}
	
	return true;
}

function deleteThisRow(rowIndex, field, value) {
	if (field == 'delete') {
		Notice.confirm("删除？<br/>确定删除所选项吗？该操作无法撤销。", function() {
			clkDelDtlSubBtnComm(rowIndex); //删除明细点击确定调用公共方法
		});
	}
}

//逻辑删除数据
function deleteFinanceMain(){
	Notice.confirm("删除？<br/>确定删除此报销单吗？该操作无法撤销。",function(){
		$.post(basePath+"finance/financeInfoController/deleteFinanceInfo.do",
		{"fid": fid},function(data){
			if( data.result == "success" ){
				FW.success( "删除成功");
				closeTab();
			}else{
				FW.error( "删除失败");
			}
		});
	});
}

function deleteimg() {
	return '<span style="" onclick=""><img src="'+ basePath +'img/finance/btn_garbage.gif"></span>';
}

//去掉左右空格
function trim(str) {
	return str.replace(/(^\s+)|(\s+$)/g, "");
}

//提交ERP
function submitERP() {
	var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	
	//提交前关闭对表的编辑
	var erpTable = conWin.$("#erpTable");
	var erpRows = erpTable.datagrid("getRows");
	//或者通过 var rows = $("#"+id).datagrid('getRows'); 的rows.length
	//for ( var i = 0; i < all.rows.length; i++) {
	for ( var i = 0; i < erpRows.length; i++) {
		erpTable.datagrid("endEdit", i);
	}
	
	var erpForm = conWin.$("#erpForm").ITC_Form("getdata");
	var erpFormJson = FW.stringify(erpForm);
	var erpDetail = conWin.$("#erpTable").datagrid("getRows");
	var erpDetailJson = FW.stringify(erpDetail);
	
	//检查明细数据必输
	if (erpDetail == "[]") {
		Notice.errorTopNotice("必须添加凭证明细");
		return false;
	}
	
	var postErpUrl = basePath + 'finance/financeInfoController/putGeneralLedgerInfo.do'; //推送ERP数据服务接口
	
	var postErpData = {
		"erpForm": erpFormJson,
		"erpDetail": erpDetailJson
	};
	
	$.post( postErpUrl, postErpData, function(data) {
		if (data.result == "success") {
			submitDone();
			_parent().$("#itcDlg").dialog("close");
			$("#btnInputERP").hide();
		} else {
			//失败后开启对表的编辑
			//或者通过 var rows = $("#"+id).datagrid('getRows'); 的rows.length
			//for ( var i = 0; i < all.rows.length; i++) {
			for ( var i = 0; i < erpRows.length; i++) {
				erpTable.datagrid("beginEdit", i);
			}
			Notice.errorTopNotice("保存失败");
		}
	});
}

//TODO 此函数是否可以删掉，有待测试，个人觉得完全可以删掉ahua
///{"showFormItem":"accType"}
//权限控制(判断表单要素是否需要隐藏)
FW.privilegeOperation = function(functionId, formId) {
	//获取角色的记账类型可视权限
	var isVisabled = privMapping[functionId];
	
	if( isVisabled == "1" ) {
		if( functionId == "finance_showAccType" ) {
			showFormItem(curTaskInputInfo, formId);
		} else {
			
		}
	}
};

//表单可视化
function showFormItem(curTaskInputInfo, formId) {
	if(""==curTaskInputInfo){
		return ;
	}
	var curTaskInput = JSON.parse(curTaskInputInfo); //获取节点配置项
	if("" != curTaskInput) {
		var formItems = curTaskInput.showFormItem;
		if(typeof(formItems) != "undefined" && formItems != "") {
			var formArr = formItems.split('|');
			for( var i = 0; i < formArr.length; i++ ) {
				$("#autoform").iForm( "show", formArr[i] ); //显示账务类型
				$("#autoform").iForm( "beginEdit", formArr[i] ); //账务类型可编辑
			}
		}
	}
}

//初始化按钮事件
function initBtn() {
	//本地环境配置需要放开(SIT,仿真环境,生产环境配置需要注释掉)
	//var fileExportPath='http://localhost:8091/itc_report/';
	//谷传伟本地环境
	//var fileExportPath='http://10.133.96.77:8091/itc_report/';
	
	//var author = encodeURI(encodeURI("梁兆麟"));
	//var url = window.location.href;
	var url = "http://" + window.location.host + "/timss-finance";
	
	var commonPath = "__report=report/TIMSS2_FIN_001.rptdesign&fid=" + fid
		+ "&url=" + url
		+ "&author=" + loginUserId;
	
	//打印doc(暂时无用上)
	$("#printBtn").bindDownload({
		url : fileExportPath+"preview?__format=doc&" + commonPath
	});
	
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
	
	//预览DOC(暂时无用上)
	$("#previewDocBtn").click(function() {
		FW.dialog("init",{
			src: fileExportPath+"preview?" + commonPath,
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

//屏蔽补充医疗保险、家属医药费、备用金、探亲路费的建单和审批功能
function hideFinType() {
	if( finNameEn == "medicalinsurance"
		|| finNameEn == "familymedicinecost" 
		|| finNameEn == "pettycash"
		|| finNameEn == "hometripcost" ) {
		$("#saveBtn").hide();
		$("#submitBtn").hide();
		$("#addDetailBtn").hide();
		$("#autoform").iForm("endEdit");
		$("#autoform").iForm("beginEdit", "finType");
	}
}

//统一格式化金额
function chgAmtFormat(amt) {
	var formatedAmt = amt.toFixed(2);
	return formatedAmt;
}

//获取当前月份(格式:YYYY-MM)
function getCurrMonth() {
	var now = new Date();
	var y = now.getFullYear();
	var m = now.getMonth() + 1;
	m = m<10 ? "0"+m : m;
	return y + "-" + m;
}

//获取当前日期(格式:YYYY-MM-DD)
function getCurrDate() {
	var now = new Date();
	var y = now.getFullYear();
	var m = now.getMonth() + 1;
	var d = now.getDate();
	m = m<10 ? "0"+m : m;
	d = d<10 ? "0"+d : d;
	return y + "-" + m + "-" + d;
}

//定义财务报销按钮权限
var FinBtnPriv = {
	init:function() {
		FinBtnPriv.set();
		FinBtnPriv.apply();
	},
	
	set:function() { //定义权限
		//生成ERP凭证 需要求登陆者 1.有finance_inputERP生成凭证权限且角色是会计(已满足);2.是审批者;3.状态是"会计审批";4.还没有生成过凭证
		Priv.map('privMapping.finance_inputERP==1 && isApprover=="true"'+
				'&& bussinessStatus==finWorkFlowStatusCodeConstant.WAIT_ACCOUNT_APPROVE '+
				'&& (flagItem=="0" || flagItem=="")',"VIRTUAL-INPUTERP");
	},
	
	apply:function() { //应用权限
		Priv.apply(); //应用
	}
};

//作废报销单
function abolish() {
	Notice.confirm("确定作废|确定作废此报销单信息么？", function() {	
		$.post(basePath + "finance/financeInfoController/abolishFinanceInfo.do",{"fid": fid},
			function(data) {
				if(data.result == "success") {
					FW.success("作废成功");
					closeTabUnMsg();
				}else {
					FW.error("作废失败");
				}
			},"json");
	} ,null, "info");	
}