<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
	String sessId = request.getSession().getId();
	String siteId=operator.getCurrentSite();
%>
<html>
<head>
<title>加班申请信息</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/homepage/homepageService.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/commonWorkflow.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonBtnSecurity.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/attendance/overtime.js?ver=${iVersion}"></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>

<script>
	$(document).ready(function(){
		Overtime.objs.opts.sessId="<%=sessId%>";
		Overtime.objs.opts.valKey="<%=valKey%>";
		Overtime.objs.overtimeId="${param.id}";
		Overtime.objs.siteId="<%=siteId%>";
		Overtime.init("edit");
	});
</script>
</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar " >
	        <div class="btn-group btn-group-sm" id="closeDiv">
	            <button type="button" class="btn btn-default priv" privilege="atd_ot_update_close" onclick="closeTab()">关闭</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm" style="display: none;" id="approveButtonDiv">
				<button type="button" class="btn btn-default priv" privilege="atd_ot_update_approve" id="approveBtn">审批</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm" style="display: none;" id="editButtonDiv">
				<button id="saveButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save">暂存</button>
				<button id="commitButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save" >提交</button>
			</div>
	        <div id="unifyButtonDiv" class="btn-group btn-group-sm">	
				<button id="unifyButton" type="button" class="btn btn-default priv" privilege="atd_ot_update_save">统一加班时间</button>
			</div>
	        <div id="delBtnDiv" class="btn-group btn-group-sm">			
				<button id="deleteButton" type="button" class="btn btn-default priv" privilege="atd_ot_insert_delete" >删除</button>
				<button id="invalidButton" type="button" class="btn btn-default priv" privilege="atd_ot_insert_invalid" >作废</button>
	        </div>
	        
	        <div id="printDiv" class="btn-group btn-group-sm">
				<button type="button" class="btn btn-default" id="printBtn">打印</button>
			</div>
			
	        <div class="btn-group btn-group-sm" id="flowDiv">
	        	<button id="flowDiagramBtn" type="button" class="btn btn-default atd_ot_update_flow">审批信息</button>
	        </div>
	        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title" id="inner-title">
		加班申请详情
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
			<div privilege="atd_ot_update_save" class="btn-group btn-group-xs priv" id="addDetailDiv">
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