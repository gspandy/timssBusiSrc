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
<title>工作票列表</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/plan/plan.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/plan/addPlan.js?ver=${iVersion}"></script>
<script>
	
	
	$(document).ready(function() {
		var options={
			formFields:planFormFields,
			form:$('#form1')
		};
		pmsPager.init(options);
		
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
	            <button type="button" class="btn btn-default pms-gray-button" data-loading-text="暂存" onclick="temSave(this);">暂存</button>
	            <button type="button" class="btn btn-default pms-gray-button" data-loading-text="提交" onclick="submit(this);">提交</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		新建年度计划
	</div>

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>