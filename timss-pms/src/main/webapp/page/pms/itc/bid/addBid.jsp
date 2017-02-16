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

<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/bid/bid.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/bid/addBid.js?ver=${iVersion}"></script>
<script>
	var projectId=getUrlParam("projectId");
	
	$(document).ready(function() {
		if(!projectId){
			initForm();
		
		}else{
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
	            <button type="button" class="btn btn-default" onclick="temSave();">暂存</button>
	            <button type="button" class="btn btn-default" onclick="submit();">提交</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		新建招标
	</div>
	<!-- 两个表单显示成一个表单的样式 -->
	<form id="projectForm" class="margin-form-title"></form>
	<form id="form1" class="margin-form-foldable"></form>
	
	<div id="supplierListWrapper" grouptitle="招标单位" class="margin-group-bottom">
	    <div  class="margin-title-table">
			<table id="supplierList" class="eu-datagrid"></table>
		</div>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				  <button type="button" class="btn btn-success" onclick="addSupplier();" id="b-add-supplier">添加招标单位</button>
			</div>
		</div>
	</div>
	<div id="bidAttachFormWrapper" grouptitle="招标附件">
		<form id="bidAttachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>