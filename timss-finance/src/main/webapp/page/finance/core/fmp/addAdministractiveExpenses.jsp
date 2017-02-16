<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html>
<html style="height: 99%;">
<head>
<%
 	//valKey是上传组件删除时的key，sessionid是上传组件初始化时的属性
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<title>新增行政报销</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
//添加注解
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/finance/core/fma/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fma/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fmp/administractiveExpenses.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fmp/addAdministractiveExpenses.js?ver=${iVersion}"></script>
<script>
	//新建时，需要获取当前登录用户的用户信息 作为自己报销的收款人
	var userId = "${userId}";
	var userName = "${userName}";
	var defKey = "${defKey}";
	$(document).ready(function() {
		initFormWithEmptyData();
	});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="tmpSave();" data-loading-text="暂存">暂存</button>
	            <button type="button" class="btn btn-default" onclick="submit(this);" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	    </div>
	</div>
    <div class="inner-title">
		新建行政报销
	</div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	<div  class="margin-group-bottom">
		<form id="expensesListWrapper" grouptitle="报销明细" class="margin-title-table">
			<table id="expensesList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				 <button type="button" class="btn btn-success" onclick="addExpenses();" id="b-add-expensesdtl">添加明细</button>
			</div>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>