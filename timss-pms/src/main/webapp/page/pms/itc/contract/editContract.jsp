<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>合同信息</title>

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/contract/contract.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/contract/editContract.js?ver=${iVersion}"></script>
<script>
	var defKey="pms_itc_contractapp";
	var contractId=getUrlParam('contractId');
	var changeContractStatus=getUrlParam("changeContractStatus");
	var flowId=getUrlParam("processInstId");
	var nullifyprocessInstId = getUrlParam("nullifyprocessInstId"); 
	var userId = '${userId}';
	pmsPager.editContractFirstInit = true;
	pmsPager.editContractFirstInitCC = true;
	$(document).ready(function() {
			initContractForm(contractId);
			preparePrint();
	});
	
	function preparePrint(){
		var proc_inst_id=getWorkflowProcessInstId();
		var printFileName="TIMSS2_PMS_CONTRACT_001.rptdesign";
		var url=fileExportPath+"preview?__report=report/"+printFileName+"&__format=pdf&siteid=ITC&id="+contractId;
		url=url+"&url="+url+"&author="+userId;
		pmsPrintHelp("#pms-contract-print",url,"打印合同");
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <%--这个页面包含两个流程的 一个是合同审批流程一个是合同变更审批流程 --%>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="tmpSaveContract(this);" data-loading-text="暂存" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="submitContract(this);" data-loading-text="提交" id="b-save">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="delContract(this);" data-loading-text="删除" id="b-del">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-button" onclick="nullifyContractWithWorkFlow();" id="pms-workflwo-del">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-button" onclick="applyNullifyContract(this);" id="pms-workflow-nullify">作废申请</button>
	        </div>
	        <%-- 下面的作废不经过审批流程的审批 --%>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default" onclick="delWorkflow();" style="display:none;" id="b-workflwo-del">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default pms-button" onclick="approveContract();" id="pms-contract-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-complex-privilege" onclick="shenpi();" style="display:none;"  id="pms-b-change-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-button" onclick="auditNullifyContract();" id="pms-workflow-nullify-audit">作废审批</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
				<button type="button" class="btn btn-default dropdown-toggle pms-complex-privilege"  id="pms-b-contract-complex"
					data-toggle="dropdown" >新建&nbsp;<span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a onclick="openNewCheckoutTab();">新建验收</a>
					</li>
					<li ><a onclick="openNewPpayTab();">新建结算</a>
					</li>
				</ul>
			</div>
			<div class="btn-group btn-group-sm "  >
	            <button type="button" class="btn btn-default pms-complex-privilege" id="pms-b-checkout-add"
	               onclick="openNewCheckoutTab();" >新建验收</button>
	        </div>
	        <div class="btn-group btn-group-sm "  >
	            <button type="button" class="btn btn-default pms-complex-privilege"  id="pms-b-pay-add"
	               onclick="openNewPpayTab();" >新建结算</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default pms-complex-privilege" id="pms-b-contract-change-payplan"
	             onclick="changePay();" >变更结算计划</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
				<button type="button" class="btn btn-default dropdown-toggle pms-complex-privilege"  id="pms-b-contract-complex2"
					data-toggle="dropdown" >查看&nbsp;<span class="caret"></span>
				</button>
				<ul class="dropdown-menu">
					<li><a class="pms-complex-privilege" id="pms-b-checkout-query" onclick="openViewPmsCheckOutTab();">查看验收</a></li>
					<li><a class="pms-complex-privilege" id="pms-b-pay-query" onclick="openViewPmsPayTab();">查看结算</a></li>
				</ul>
			</div>
	        <%-- 
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-complex-privilege"   id="pms-b-checkout-query"
	             onclick="openViewPmsCheckOutTab();" >查看验收</button>
	        </div>
	        <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default pms-complex-privilege" id="pms-b-pay-query"
	             onclick="openViewPmsPayTab();" >查看结算</button>
	        </div>
	        --%>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default " id="pms-contract-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default pms-button" onclick="showContractChangeWorkflow();" id="b-contract-change-workflow">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default pms-button" onclick="showContractNullifyWorkflow();" id="b-contract-nullify-workflow">作废审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default priv" onclick="showContractWorkflow();" privilege="pms-workflow-show" id="pms-contractApp-wfinfo">审批信息</button>
	        </div>
	        
			
			
	    </div>
	</div>
	<div class="inner-title">
		合同详情
	</div>

	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	<div class="margin-group-bottom" id="payplanListWrp">
		<form id="payplanListWrapper" grouptitle="结算计划" class="margin-title-table">
			<table id="payplanList"  class="eu-datagrid"></table>
		</form>
		<div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				 <button type="button" class="btn btn-success" onclick="addPayplan();" id="b-add-payplan">添加结算计划</button>
			</div>
		</div>
	</div>
	<div class="margin-group-bottom" id="payplanTmpListWrp">
		<form id="payplanListTmpWrapper" grouptitle="变更后结算计划" class="margin-title-table">
			<table id="payplanTmpList"  class="eu-datagrid"></table>
		</form>
		
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>