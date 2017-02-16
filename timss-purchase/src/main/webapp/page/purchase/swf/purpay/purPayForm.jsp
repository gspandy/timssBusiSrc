<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String payId = request.getParameter("payId")==null||"".equals(request.getParameter("payId"))?String.valueOf(request.getAttribute("payId")):String.valueOf(request.getParameter("payId"));
	String operType = request.getParameter("operType")==null?"":String.valueOf(request.getParameter("operType"));
	String sheetId =  request.getParameter("sheetId")==null?"":String.valueOf(request.getParameter("sheetId"));
	String payType =  request.getParameter("payType")==null?"":String.valueOf(request.getParameter("payType"));
	String curUserId = request.getAttribute("curUserId")==null?"":String.valueOf(request.getAttribute("curUserId"));
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
	String uploadFiles = request.getAttribute("uploadFiles")==null?"":String.valueOf(request.getAttribute("uploadFiles"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>采购合同表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script>
	var payId = '<%=payId%>';//获取全局的sheetid
	var operType = '<%=operType%>';      //new或edit
	var sheetId =  '<%=sheetId%>';   //采购合同Id
	var payType =  '<%=payType%>';   //付款类型
	var process = 	  '${process}';		 //环节名称
	var processName = '${processName}';//环节名称
	var procInstId =  '${procInstId}';//流程实例Id
	var taskId = 	  '${taskId}';			 //任务id
	var defKey =	  '${defKey}';            //流程名称 
	var classType =   '${classType}';    //??
	var isEdit = 	  '${isEdit}';			 //是否可编辑	
	var status = 	  '${status}';          //状态
	var isCandidate = '${isCandidate}';//是当前环节处理人
	var siteId = 	  '${siteId}';          //站点
	var isEditableTask = '${isEditableTask}'; //是否编辑的环节
	var refreshTabId ='${refreshTabId}';      //关闭标签页后要刷新的标签页的id 只有生成质保金付款才会用到
	var relatepayId  ='${relatepayId}';       //生成质保金时，传递当前付款id    
	var hasRelatePay ='${hasRelatePay}';      //当前到货款是否有关联质保金
	var erpStatus = '${erpStatus}';
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	/************************判断页面是否修改************************/
	var uploadIds = "";
	var uploadFiles = '<%=uploadFiles%>';
	var sessId = '<%=sessId%>';
	var valKey = '<%=valKey%>';
	//表单状态 之后将把这段逻辑放到后台，现在为了调试方便暂时放在此处
	var formStatus = "";
	if(""==status){//新建
		formStatus = "new";
	}else if(process.indexOf("_commit")>-1&&"draft"==status){//草稿编辑(流程为提交环节,且状态为草稿时为草稿)
		formStatus = "draft";
	}else if(process.indexOf("_commit")>-1&&"draft"!=status){//退回首环节(流程为提交环节,且状态不为草稿时为退回到第一环节)
		formStatus = "backtofirst";
	}else if("processing"==status){//一般审批
		formStatus = "processing";
	}else if("processed"==status){//审批结束
		formStatus = "processed";
	}
	
	var formHideFields = [];//表单隐藏的字段
	if("prepay" == payType){
		formHideFields= ["invoiceNos","qaPay","refusePay","noTaxTotal","taxTotal","total","actualPayTotal","excludeDate","qaDeadLine","relatePayNo"];
	}else if ("arrivepay" == payType){
		formHideFields= ["refusePay","actualPayTotal","excludeDate","relatePayNo","qaDeadLine"];
	}else if ("qualitypay" == payType){
		formHideFields= ["invoiceNos","payRatio","qaPay","noTaxTotal","taxTotal","total"];
	}else if ("settlepay" == payType){
		formHideFields= ["qaPay","refusePay","actualPayTotal","excludeDate","qaDeadLine","relatePayNo"];
	}
	
	
	//初始化界面
	$(document).ready(function() {
		//按钮事件绑定
		PayBtn.init();
		//按钮权限控制
		PurPayPriv.init();
		//初始化表单
		initPayInfo(formfield);
		if ("arrivepay" == payType||"settlepay" == payType){
			//初始化表格
			initPayDtlList(payDtalColumns);
			//折叠区标题
			$("#foldable_area").iFold("init");	
		}
		//初始化附件
		//uploadform();
		FW.fixToolbar("#toolbar1");
		//addFormCloseEvent();
	});
	//关闭页面
	function pageClose(){
		homepageService.refresh();
		FW.deleteTabById(FW.getCurrentTabId());
	}
	//标识表单数据表格是否初始化
	var formInited = false;
	var datagridInited = false;
</script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purpay/purPay.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purpay/purPayForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purpay/purPayList.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/swf/purpay/purPayBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>
<link rel="stylesheet" type="text/css" href="${basePath}css/purchase/ptoSafe.css"></link>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div class="btn-toolbar" role="toolbar" id="toolbar1">
			<div class="btn-group btn-group-sm">
			    <button type="button" class="btn btn-default" id="btn-close">关闭</button>
			</div>
			<div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-save" privilege="btnpurpay_save">暂存</button>
		    	<button type="button" class="btn btn-default priv" id="btn-submit" privilege="btnpurpay_submit">提交</button>
		    	<button type="button" class="btn btn-default priv" id="btn-audit" privilege="btnpurpay_approve">审批</button>
		    	<button type="button" class="btn btn-default priv" id="btn-newqapay" privilege="btnpurpay_newqapay">生成质保金</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		    	<button type="button" class="btn btn-default priv" id="btn-delete" privilege="btnpurpay_del">删除</button>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" id="btn-revoke" privilege="btnpurpay_revoke">作废</button>
		    </div>
		    <div class="btn-group btn-group-sm">
			    <button type="button" class="btn btn-default priv dropdown-toggle" " id="btn-purpayPrint" privilege="btnpurpay_print" data-toggle="dropdown">
			                            打印
			        <span class="caret"></span>
			        </button>
			        <ul class="dropdown-menu">
			            <li id="rpt_title"><a href="javascript:void(0)" onclick="printRpt('title')">报账封面</a></li>
			            <li id="rpt_content"><a href="javascript:void(0)" onclick="printRpt('content')">报账单</a></li>
			            <li id="rpt_qa"><a href="javascript:void(0)" onclick="printRpt('qa')">质保金单</a></li>
			            <li id="rpt_accept"><a href="javascript:void(0)" onclick="printRpt('accept')">固定资产领用单</a></li>
			        </ul>
		    </div>
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default" id="btn-process">审批信息</button>
		    </div>
		    
		    <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default priv" privilege="btnpurpay_erp1" id="btn-erp">导入ERP</button>
		    </div>
		</div>
	</div>
	<div class="inner-title" id="pageTitle">采购付款 </div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform">
	</form>
	<div id="foldable_area" grouptitle="采购付款列表" style="padding-bottom: 15px">
		<div id="purPay_dtlGrid" class="margin-title-table">
			<form id="purPayDtlform">
				<table id="purPays_dtl" class="eu-datagrid"></table>
			</form>
		</div>
	    <div class="margin-group"></div>
		<div grouptitle="附件"  id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform" style=""></form>
			</div>
		</div>	    
	</div>
</body>
</html>