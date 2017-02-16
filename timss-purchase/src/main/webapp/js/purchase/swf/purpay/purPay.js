/*********   权限设定    ************/
var savePriv=function(){
	if(("backtofirst"==formStatus||"new"==formStatus||"draft"==formStatus)&&"false"!=isCandidate){
		return true;
	}else{
		return false;
	}
}
var delPriv=function(){
	if(("draft"==formStatus)&&"false"!=isCandidate){
		return true;
	}else{
		return false;
	}
}
var revokePriv=function(){
	if(("backtofirst"==formStatus)&&"false"!=isCandidate){
		return true;
	}else{
		return false;
	}
}
var submitPriv=function(){
	if(("new"==formStatus||"draft"==formStatus)&&"false"!=isCandidate){
		return true;
	}else{
		return false;
	}
}
var approvePriv=function(){
	if(("backtofirst"==formStatus||"processing"==formStatus)&&"false"!=isCandidate){
		return true;
	}else{
		return false;
	}
}
var newPriv=function(){
	if(1==privMapping.exampurpay_new){
		return true;
	}else{
		return false;
	}
}
var newQapayPriv=function(){
	if("true"!=hasRelatePay&&1==privMapping.exampurpay_newqapay&&"processed"==formStatus&&"arrivepay"==payType){
		return true;
	}else{
		return false;
	}
}
var printPriv=function(){
	if("new"!=formStatus){
		return true;
	}else{
		return false;
	}
		
}
var ERPPriv=function(){
	if(Priv.hasRole('SWF_CW') && "processed" == status &&("arrivepay" == payType||"settlepay" == payType)){
		return true;
	}else{
		return false;
	}
		
}
//权限控制
//采购付款权限初始化
var PurPayPriv={
	init:function(){
		PurPayPriv.set();
		PurPayPriv.apply();
	},
	set:function(){//定义权限
		//暂存
		Priv.map("savePriv()","btnpurpay_save");
		//删除
		Priv.map("delPriv()","btnpurpay_del");
		//作废
		Priv.map("revokePriv()","btnpurpay_revoke");
		//提交
		Priv.map("submitPriv()","btnpurpay_submit");
		//审批
		Priv.map("approvePriv()","btnpurpay_approve");
		//新建
		Priv.map("newPriv()","btnpurpay_new");
		//新建质保单
		Priv.map("newQapayPriv()","btnpurpay_newqapay");
		//打印按钮
		Priv.map("printPriv()","btnpurpay_print");
		//ERP
		Priv.map("ERPPriv()","btnpurpay_erp1");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};
/*********   权限设定    ************/

/*********   前端可编辑控制设定    ************/
//开始编辑所有行
function startEditAll(){
	var rows = $("#purPays_dtl").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#purPays_dtl").datagrid("beginEdit",i);
	}
}
//结束编辑所有行
function endEditAll(){
	var rows = $("#purPays_dtl").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#purPays_dtl").datagrid("endEdit",i);
	}
}
/*********   前端可编辑控制设定    ************/

/*********   初始化控制设定    ************/
//初始化表单数据
function initPayInfo(inForm){
	$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},inForm);
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async: false,
		url: basePath+"purchase/purpay/queryPurPayInfo.do",
		data: {"sheetId":sheetId,"payId":payId,"payType":payType,"relatepayId":relatepayId},
		//relatepayId只有生成质保金的时候用到
		dataType : "json",
		success : function(data) {
			var formLoaddata = {
					"sheetNo":data.sheetNo,
					"sheetName":data.sheetName,
					"sheetTotal":data.sheetTotal,
					"invoiceNos":data.invoiceNos,
					"payRatio":data.payRatio,
					"pay":data.pay,
					"qaPay":data.qaPay,
					"supplierName":data.supplierName,
					"refusePay":data.refusePay,
					"noTaxTotal":data.noTaxTotal,
					"taxTotal":data.taxTotal,
					"total":data.total,
					"actualPayTotal":data.actualPayTotal,
					"excludeDate":data.excludeDate,
					"qaDeadLine":data.qaDeadLine,
					"relatePayNo":data.relatePayNo,
					"usage":data.usage,
					"remark":data.remark,
					//隐藏域的赋值
					"payId" : data.payId,
					"sheetId" : data.sheetId,
					"payType" : data.payType,
					"relatePayId" : data.relatePayId,
					"auditDate" : data.auditDate,
					"payNo" : data.payNo,
					"transactor" : data.transactor,
					"status" : data.status,
					"procInstId" : data.procInstId,
					"transactorName" : data.transactorName,
					"sheetClassId" : data.sheetClassId ,
					"itemClassid" : data.itemClassid,
					"spNo" : data.spNo
				};
			$("#autoform").ITC_Form("loaddata",formLoaddata);
			if(null==data.spNo){
				$("#autoform").iForm("hide","spNo");
			}
			$("#autoform").iForm("hide",formHideFields);
			if("editable"!=isEdit){
				$("#autoform").iForm("endEdit");
			}
			//将采购合同编号变为超链
			var sheetNo = $("#autoform").iForm("getVal","sheetNo");
			var sheetId = $("#autoform").iForm("getVal","sheetId");
			var sheetNoContent = "<a onclick='FW.showPOPage(\""+sheetId+"\",\""+sheetNo+"\");'>"+sheetNo+"</a>";
			$("#f_sheetNo").html(sheetNoContent);
			$("#readonly_f_sheetNo").html(sheetNoContent);
			//更新页面标题
			var operTypeName = "";
			var payTypeName = "";
			if("new"==formStatus){
				operTypeName = "新建";
			}else if("draft"==formStatus){
				operTypeName = "编辑";
			}else if("false"!=isCandidate&&("backtofirst"==formStatus)){
				operTypeName = "编辑";
				$("#btn-audit").html("提交");
			}else if("false"!=isCandidate&&("processing"==formStatus)){
				operTypeName = "审批";
			}else {
				operTypeName = "查看";
			}
			if("prepay" == payType){
				payTypeName = "预付款";
			}else if ("arrivepay" == payType){
				payTypeName = "到货款";
			}else if ("qualitypay" == payType){
				payTypeName = "质保金";
			}else if ("settlepay" == payType){
				payTypeName = "结算款";
			}
			//打印按钮显示控制
			$("#rpt_title").hide();
			$("#rpt_content").hide();
			$("#rpt_qa").hide();
			$("#rpt_accept").hide();
			if("prepay" == payType||"arrivepay" == payType||"settlepay" == payType){
				$("#rpt_title").show();
			}
			if("arrivepay" == payType||"settlepay" == payType){
				$("#rpt_content").show();
				$("#rpt_accept").show();
			}
			if("qualitypay" == payType){
				$("#rpt_qa").show();
			}	

			$("#pageTitle").html(("审批"!=operTypeName&&"查看"!=operTypeName)?(operTypeName+payTypeName):(payTypeName+"详情"));
			if("new"==formStatus){
				//新建的时候自动填写项目及用途的初始化信息
				var usage = "";
				if("prepay" == payType){
					usage = "根据"+data.sheetName+"支付"+data.supplierName+"付款比例预付款";
				}else if("arrivepay" == payType){
					usage = "根据"+data.sheetName+"支付"+data.supplierName+"付款比例到货款";
				}else if("settlepay" == payType){
					usage = "根据"+data.sheetName+"支付"+data.supplierName+"付款比例结算款";
				}else if("qualitypay" == payType){
					usage = "根据"+data.sheetName+"支付"+data.supplierName+"付款比例质保金";
				}
				$("#autoform").iForm("setVal",{"usage":usage});
			}
		}
	});
}
//初始化数据表格数据
function initPayDtlList(payDtalColumns){
	//付款明细数据表格
	$("#purPays_dtl").datagrid({
		fitColumns : true,
		columns:payDtalColumns,
		scrollbarSize : 0,
		queryParams: {
			'payId': payId,
			'sheetId': sheetId,
			'payType': payType
		},
		url : basePath+"purchase/purpay/queryPurPayDtlList.do",
		onRenderFinish : function() {
			//需要在最后重新确定一下初始表单值
			var listData =$("#purPays_dtl").datagrid("getRows");
			initFormStatus = $("#autoform").iForm("getVal");
			initListStatus = FW.stringify(listData);
			if("editable"==isEdit){
				if( "arrivepay"==payType||"settlepay"==payType){
					startEditAll();
				}
			}
			refreshPrice(this);
			$("#purPays_dtl").datagrid("resize");
		}
	});
}

//附件初始化
function uploadform() {
	//1.生成附件表单组件
	var file = null;	
	if("" != uploadFiles){
		file = JSON.parse(uploadFiles);
	}
	$("#uploadform").iForm('init', {
		"fields" : [
                    	{
                    	 id:"uploadField", 
                    	 title:" ",
                    	 type:"fileupload",
                    	 linebreak:true,
                    	 wrapXsWidth:12,
                    	 wrapMdWidth:12,
                    	 options:{
                    	     "uploader" : basePath+"upload?method=uploadFile&jsessionid="+sessId,
                    	     "delFileUrl" : basePath+"upload?method=delFile&key="+valKey,
	                    	 "downloadFileUrl" : basePath + "upload?method=downloadFile",
	                    	 "swf" : basePath + "js/purchase/common/uploadify.swf",
	                    	 "initFiles" : file,
	                    	 "delFileAfterPost" : true
	                    	 }
                    	}
                    ],
		"options" : {
		"labelFixWidth" : 6,
		"labelColon" : false
		}
	});
	//附件组件控制--start
	//2.获得附件id集合
	var ids = $("#uploadform").ITC_Form("getdata");
	uploadIds=ids.uploadField;
	//3.判断是否显示
	if("" != uploadIds || "new"==formStatus||"draft"==formStatus||"backtofirst"==formStatus ){
		$("#uploadfileTitle").iFold("init");
	}else{
		$("#uploadfileTitle").iFold("hide");
	}
	//4.判断是否可编辑
	if("new"==formStatus||("draft"==formStatus||"backtofirst"==formStatus)&&"true"==isCandidate){
		//处于第一环节，且为处理人，才可以编辑附件
		$("#uploadform").iForm('beginEdit');
		//非常奇怪这里的添加附件的超链居然不受beginEdit和endEdit控制了
		$("#f_uploadField_real").show();
	}else{
		$("#uploadform").iForm('endEdit');
		$("#f_uploadField_real").hide();
	}
	//附件组件控制--end
}
/*********   初始化控制设定    ************/

/*********   功能设定    ************/
//提交按钮
function commitApply(obj){
	//saveType:save,submit,audit
	var saveType = obj.saveType;
	if(!$("#autoform").valid()){
		$("#btn-submit").button('reset');
		return;
	}
	var listData = [];
	if( "arrivepay"==payType||"settlepay"==payType){
		listData = $("#purPays_dtl").datagrid("getRows");
		if(listData.length==0){
			FW.error(("arrivepay"==payType?"到货款":"结算款")+"物资明细不能为空");
			return;
	    }
		if(""==payId){
			for(var i=0;i<listData.length;i++){
				if(listData[i].sendAccount > listData[i].noSendAccount){
					FW.error(listData[i].itemName + "的本次报账数量不能大于未报账数量 ");
					return;
				}
			}
		}
		endEditAll();
	}
	var formData = $("#autoform").ITC_Form("getdata");
	var ids 	 = $("#uploadform").ITC_Form("getdata");
	uploadIds=JSON.stringify(ids.uploadField);
	if ( "submit" == saveType ) {
		$("#btn-submit").button('loading');
	}else if ("audit" == saveType){
		$("#btn-audit").button('loading');
	}else if( "save" == saveType ){
		if( "arrivepay"==payType||"settlepay"==payType){
			startEditAll();
		}
	}	
	$.ajax({
		type: "POST",
		url: basePath+"purchase/purpay/commitApply.do",
		data: {
			"formData":FW.stringify(formData),
			"listData":FW.stringify(listData),
			"uploadIds":uploadIds,
			"saveType":saveType,
			"payId":payId
		},
		dataType : "json",
		success : function(data) {
			taskId = data.taskId;
			payId = data.payId;
			procInstId = data.procInstId;
			//最好用一个方法重新load一次数据
			$("#autoform").iForm("setVal",{"payId":payId,"procInstId":procInstId,"payNo":data.payNo})
			if( data.result == "success" ){
				if( "submit" == saveType ){
					var workFlow = new WorkFlow();
					workFlow.submitApply(taskId,null,submitCallBack,cancelCallBack,0);
				}else if( "audit" == saveType ){
					var workFlow = new WorkFlow();
					workFlow.showAudit(taskId,null,submitCallBack,pageClose,null,null,0,cancelCallBack);
				}else{
					FW.success("暂存成功 ");
				}
			}else{
				if ("submit" == saveType ) {
					FW.error("提交失败");
					$("#btn-submit").button('reset');
				}else if("audit" == saveType){
					FW.error("审批失败 ");
					$("#btn-audit").button('reset');
				}else {
					FW.error("暂存失败 ");
				}
			}
		}
	});
}

//提交后回调执行
function submitCallBack(){
	var curUserSiteId = Priv.secUser.siteId;  
	pageClose();
}

//取消的回调执行
function cancelCallBack(){
	$("#btn-submit").button('reset');
	$("#btn-audit").button('reset');
}

//打印方法
function printRpt(type){
	var title = "";
	var reportfile = "";
	if("title"==type){
		title = "打印报账封面";
		reportfile = "TIMSS2_SWF_PAYTITLE_001";
	}else if("content"==type){
		title = "打印入库报账单";
		reportfile = "TIMSS2_SWF_PAYCONTENT_001";
	}else if("qa"==type){
		title = "打印质保金审批表";
		reportfile = "TIMSS2_SWF_PAYAUD_001";
	}else if("accept"==type){
		title = "打印固定资产领用单";
		reportfile = "TIMSS2_SWF_PAYACCEPT_001";
	}
	var reportUrl = fileExportPath+"preview?__format=pdf&__report=report/"+reportfile+".rptdesign&payId="+payId;
	FW.dialog("init",{
		src: reportUrl,
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
}




/*********   功能设定    ************/