<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<!DOCTYPE html>
<html>
<head>
<title>考勤异常申请</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/attendance/abnormity.js?ver=${iVersion}"></script>

<script>
	$(document).ready(function() {
		Abnormity.objs.opts.sessId="<%=sessId%>";
		Abnormity.objs.opts.valKey="<%=valKey%>";
		Abnormity.objs.currentUser={
			userId:"${userId}",
			userName:"${userName}",
			deptId:"${deptId}",
			deptName:"${deptName}",
			siteId:"${siteId}"
		};
		Abnormity.objs.abnormityId="${id}";
		Abnormity.init("${mode}");
	});
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeDiv">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="display: none;" id="approveButtonDiv">
				<button type="button" class="btn btn-default priv" privilege="atd_ab_update_approve" id="approveBtn">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
				<button id="saveButton" type="button" class="btn btn-default priv" privilege="atd_ab_update_commit">暂存</button>
				
				<!-- <div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
						提交<span class="caret"></span>
			        </button>
			        <ul class="dropdown-menu">
			            <li><a id="commitButton" class="priv" privilege="atd_ab_update_commit">审批</a></li>
			            <li><a id="commitButtonBeian" class="priv" privilege="atd_ab_update_commit">备案</a></li>
			        </ul>
		        </div> -->
				<button id="commitButton" type="button" class="btn btn-default priv" privilege="atd_ab_update_commit" >提交审批</button>
				<button id="commitButtonBeian" type="button" class="btn btn-default priv" privilege="atd_ab_update_commit" >提交备案</button>
			</div>
	        <div id="unifyButtonDiv" class="btn-group btn-group-sm">	
				<button id="unifyButton" type="button" class="btn btn-default priv" privilege="atd_ab_update_commit">统一异常时间</button>
			</div>
	        <div id="delBtnDiv" class="btn-group btn-group-sm">	
				<button id="deleteButton" type="button" class="btn btn-default priv" privilege="atd_ab_update_delete" >删除</button>
				<button id="invalidButton" type="button" class="btn btn-default priv" privilege="atd_ab_update_invalid" >作废</button>
	        </div>
	        <div id="printDiv" class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" id="printBtn">打印</button>
			</div>
	        <div class="btn-group btn-group-sm" id="flowDiv">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default">审批信息</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="inner-title">
		考勤异常详情
	</div>
	<form id="autoform"></form>
	
	<!-- 异常明细 -->
	<div grouptitle="异常明细 " id="detailTitle">
		<div class="margin-title-table">
			<form id="abnormityDatagridForm">
			<table id="abnormityDatagrid" style="" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addDetailBtn_toolbar">
			<div privilege="F-ROLE-ADD" class="btn-group btn-group-xs" id="addDetailDiv">
				<button type="button" class="btn btn-success" id="addDetail">添加异常</button>
			</div>
		</div>
	</div>
	
	<!-- 附件 -->
	<div id="fileDiv" grouptitle="附件">
	    <form id="fileForm" style="width:100%">
	    </form>
	</div>
</body>
</html>