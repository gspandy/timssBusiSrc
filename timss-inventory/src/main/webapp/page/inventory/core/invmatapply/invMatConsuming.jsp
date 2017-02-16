<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imaid = request.getParameter("imaid") == null ? "":String.valueOf(request.getParameter("imaid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资发料单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script>
	var imaid = '<%=imaid%>';
	var counterBefore = 0;
	var counterAfter = 0;
	var flag = true;
	$(document).ready(function() {
		initConsumingList();
	});
	
</script>
<style type="text/css">.btn-garbage{ cursor:pointer;}</style>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatConsuming.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatConsumingList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatConsumingOper.js?ver=${iVersion}"></script>
</head>
<body>
	<div id="matapplydetail_list" grouptitle="物资明细">
		<div id="matapplydetailGrid" class="margin-title-table">
			<table id="matapplydetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
</body>
</html>