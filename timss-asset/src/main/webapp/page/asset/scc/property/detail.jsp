<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String mode = request.getParameter("mode");
	String propertyId = request.getParameter("propertyId");
	String houseType = request.getParameter("houseType");
	
	Map<String,String> result = (HashMap<String,String>) request.getAttribute("result");
	String propertyBean=null;
	if(result!=null){
		propertyBean = (String)result.get("propertyBean");
	}
%>

<!DOCTYPE html>
<html>
<head>

<title>房产详细信息</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/asset/propertyDetail.js?ver=${iVersion}"></script>

<script type="text/javascript">
	$(document).ready(function() {		
		respond.update();
		
		Property.objs["mode"]="<%=mode%>";//模式有浏览、新建、编辑三种：view/create/edit
		Property.objs["propertyId"]="<%=propertyId%>";
		Property.objs["houseType"]="<%=houseType%>";
		Property.objs["propertyBean"]=<%=propertyBean%>;
		Property.init();		
	});
	
</script>
<style type="text/css">
.btn-garbage{
cursor:pointer;
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="Property.toBack()">返回</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn" onclick="Property.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="Property.updateProperty()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="Property.createProperty()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn" onclick="Property.toDelete()">删除</button>
			</div>	
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div id="assetSpare_info" grouptitle="备件信息">		
		<div id="assetSpareGrid" class="margin-title-table">
			<table id="assetSpare_table" class="eu-datagrid">
			</table>
		</div>
		<div id="spareBtn" class="btn-toolbar margin-foldable-button" role="toolbar" style='display:none;'>
			<div class="btn-group btn-group-xs">
				<button type="button" class="btn btn-success" id="btnAddSpare" onclick="Property.addSpare()">添加备件</button>
			</div>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="specs_info" grouptitle="技术规范" style='overflow: hidden;'>
		<form id="form_specs" class="margin-form-title margin-form-foldable">
		</form>
	</div>	
	<div class="margin-group"></div>
	
	<div id="workOrder_info" grouptitle="工单" style="overflow: hidden;">		
		<div id="workOrderGrid" class="margin-title-table">
			<table id="workOrder_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
	
	<div id="maintainPlan_info" grouptitle="维护计划" style="">		
		<div id="maintainPlanGrid" class="margin-title-table">
			<table id="maintainPlan_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
</body>
</html>
