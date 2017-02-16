<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String sheetno = request.getParameter("sheetno") == null ? "":String.valueOf(request.getParameter("sheetno"));
	String imaid = request.getParameter("imaid") == null ? "":String.valueOf(request.getParameter("imaid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyItemDetail.js?ver=${iVersion}"></script>
<script>
	var imaid = '<%=imaid%>';//领料单ID
	var sheetno = '<%=sheetno%>';//领料单单号
	var imadId = '${imadId}';
	var rowData = ${rowData};
	rowData.sheetno = sheetno;
	var recipientsDetailListData = ${recipientsDetailListData};
	
	$(document).ready(function() {
		initForm();
	});
	
</script>
</head>
<body>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	
	<div id="recipientsDetailListDiv" grouptitle="发料记录">
		<div id="matapplyoutGrid" class="margin-title-table">
			<table id="recipientsDetailListTable" class="eu-datagrid"></table>
		</div>
	</div>

</body>
</html>