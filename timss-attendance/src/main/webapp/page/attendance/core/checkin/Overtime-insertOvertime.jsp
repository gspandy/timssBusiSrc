<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
%>
<html>
<head>
<title>新建加班申请</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/attendance/overtime.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>

<script type="text/javascript">
	$(document).ready(function() {
		Overtime.objs.opts.sessId="<%=sessId%>";
		Overtime.objs.opts.valKey="<%=valKey%>";
		Overtime.objs.applicant={
			userId:'${userId}',
			userName:'${userName}',
			deptId:'${deptId}',
			deptName:'${deptName}',
			siteId:'${siteId}'
		};
		Overtime.objs.siteId="${siteId}";
		Overtime.init("add");
	});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeDiv">
	            <button type="button" class="btn btn-default priv" privilege="atd_ot_update_close" onclick="closeTab()">关闭</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
				<button id="saveButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save">暂存</button>
				<button id="commitButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save" >提交</button>
			</div>
	        <div id="unifyButtonDiv" class="btn-group btn-group-sm">		
				<button id="unifyButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save">统一加班时间</button>
			</div>
	        <div id="delBtnDiv" class="btn-group btn-group-sm">		
				<button id="deleteButton" type="button" class="btn btn-default priv" privilege="atd_ot_insert_delete" >删除</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm" id="flowDiv">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default atd_ot_update_flow">审批信息</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="inner-title">
		新建加班申请
	</div>
	<div>
		<form id="autoform"></form>
	</div>
	
	<!-- 加班明细 -->
	<div grouptitle="加班明细 " id="detailTitle">
		<div class="margin-title-table">
			<form id="overtimeDatagridForm">
			<table id="overtimeDatagrid" style="" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addDetailBtn_toolbar">
			<div privilege="F-ROLE-ADD" class="btn-group btn-group-xs" id="addDetailDiv">
				<button type="button" class="btn btn-success" id="addDetail">添加加班</button>
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