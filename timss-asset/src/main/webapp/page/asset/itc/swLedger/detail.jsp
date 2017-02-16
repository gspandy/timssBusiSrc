<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String mode = request.getParameter("mode");
	String swLedgerId = request.getParameter("swLedgerId");
	
	Map<String,String> result = (HashMap<String,String>) request.getAttribute("result");
	String swLedgerBean=null;
	if(result!=null){
		swLedgerBean = (String)result.get("swLedgerBean");
	}
%>

<!DOCTYPE html>
<html>
<head>

<title>软件台账详细信息</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/asset/swLedgerDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script type="text/javascript">
	$(document).ready(function() {		
		//respond.update();
		
		SwLedger.objs["mode"]="<%=mode%>";//模式有浏览、新建、编辑三种：view/create/edit
		SwLedger.objs["swLedgerId"]="<%=swLedgerId%>";
		SwLedger.objs["swLedgerBean"]=<%=swLedgerBean%>;
		SwLedgerPriv.init();
		SwLedger.init();		
	});
	
</script>
<style type="text/css">
.btn-garbage{
cursor:pointer;
}
html{
	height:95%
}
body{
	height:100%
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="SwLedger.toClose()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCancel">
				<button class="btn-default btn" onclick="SwLedger.toBack()">取消</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="SwLedger.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="SwLedger.updateSwLedger()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="SwLedger.createSwLedger()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="SwLedger.toDelete()">删除</button>
			</div>	
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div id="appsList_info" grouptitle="应用信息" style="overflow: hidden;">		
		<div id="appsListGrid" class="margin-title-table">
			<table id="appsList_table" class="eu-datagrid">
			</table>
		</div>
		<div id="appBtn" class="btn-toolbar margin-foldable-button" role="toolbar" style='display:none;'>
			<div class="btn-group btn-group-xs">
				<button type="button" class="btn btn-success" id="addAppBtn" onclick="SwLedger.addApp()">添加应用</button>
			</div>
		</div>
	</div>
	<div class="margin-group"></div>
</body>
</html>
