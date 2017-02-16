<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String id = request.getParameter("id")==null?"":String.valueOf(request.getParameter("id"));
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
%>
<!DOCTYPE html>
<html>
<head>
<title>新建生产碰头会</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>
	var objs ={};
	objs.type = '<%=type%>';
	objs.id = '<%=id%>';
	$(document).ready(function() {
		MinutePriv.init();
		init();
	});
</script>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src="${basePath}js/operation/minute/minuteForm.js?ver=${iVersion}"></script>
</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	         <div class="btn-margin-fix btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" onclick="changeMode('edit')" privilege="minute_edit">编辑</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnSave">
				<button class="btn-success btn" onclick="update()">保存</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnCreate">
				<button class="btn-success btn" onclick="create()">保存</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" onclick="del()" privilege="minute_del">删除</button>
			</div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="pageTitle">
		生产碰头会详情
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
</body>
</html>