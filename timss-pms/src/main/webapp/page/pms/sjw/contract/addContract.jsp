<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>工作票列表</title>

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var managerName = "${managerName}";
var managerTel = "${managerTel}";
var type = "";
var contractId = null;
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/contract/contract.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/contract/addContract.js?ver=${iVersion}"></script>
<script>
	var projectId=getUrlParam('projectId');
	var toComplete = getUrlParam('toComplete');
	var taskId = getUrlParam('taskId');
	var closeTabId = getUrlParam('closeTabId');
	var defKey="pms_sjw_contractapp";
	var firstPartyEditable = true;
	var secondPartyEditable = true;
	$(document).ready(function() {
		if(!projectId){
			initFormWithEmptyData();
		}else {
			initFormWithProjectData(projectId);
		}
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="tmpSave();">暂存</button>
	            <button type="button" class="btn btn-default" onclick="submit(this);" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" style="display:none;" onclick="createContractCode();">生成合同编号</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	    </div>
	</div>
    <div class="inner-title">
		新建合同
	</div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	<div  class="margin-group-bottom">
		<form id="payplanListWrapper" grouptitle="结算计划" class="margin-title-table">
			<table id="payplanList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				 <button type="button" class="btn btn-success" onclick="addPayplan();" id="b-add-payplan">添加结算计划</button>
			</div>
		</div>
        
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>