<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
	<title>新建缺陷优先级</title>
	<script>_useLoadingMask = true;</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}js/workorder/core/workOrderPriorityAdd.js?ver=${iVersion}"></script>
	<script>
		var woPriorityId = '<%=request.getParameter("woPriorityId")%>';
		var loginUserId = ItcMvcService.user.getUserId();
		var siteId = ItcMvcService.getUser().siteId;
		var currentSortNum;
	</script>
</head>
<body style="height: 100%;" class="bbox">
<div id="mainContent">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		<div id="toolbar" class="btn-toolbar ">
			<div class="btn-group btn-group-sm">
				<button id="btn_jp_new" type="button" class="btn btn-default" onclick="closeCurPage()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button id="btn_save" type="button" class="btn btn-success" onclick="savePriority()">保存</button>
				<button id="btn_edit" type="button" class="btn btn-default" onclick="editPriority()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm">
				<button id="btn_delete" type="button" class="btn btn-default" onclick="deletePriority()">删除</button>
			</div>
		</div>
	</div>
	<div>
		<form id="woPriorityForm" class="autoform"></form>
	</div>
</div>
</body>
</html>