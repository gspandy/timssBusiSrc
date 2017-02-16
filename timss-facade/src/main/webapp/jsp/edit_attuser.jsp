<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
   
<%
	String g = (String)request.getAttribute("g");
	String mode = request.getParameter("mode");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<jsp:include page="/page/sysconf/auth_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}${resBase}js/servletjs/edit_attuser.js?ver=${iVersion}"></script>



<title>编辑用户</title>
<script>
	var g = <%=g%> || {roles:{},orgs:{},groups:{}};
	var mode = "<%=mode%>";	
</script>
<style>
	
</style>
</head>
<body style="margin-top:5px" class="bbox">
	<div class="toolbar-with-pager btn-toolbar">
		<div class="btn-group btn-group-sm">
			<button class="btn-default btn" onclick="goBack()">返回</button>
		</div>
		<div class="btn-group btn-group-sm" id="btnEdit">
			<button class="btn-default btn" onclick="beignEdit()">编辑</button>
		</div>
		<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
			<button class="btn-default btn" onclick="save();">保存</button>
		</div>

	</div>
	<div class="inner-title" id="page_title">人员详情</div>
	<div style="clear:both"></div>
	<div id="base_info" style="margin-top:10px">
		<form id="form_baseinfo">
		</form>
	</div>	
</body>
</html>