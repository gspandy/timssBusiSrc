<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String mode = request.getParameter("mode");
	String hwModelId = request.getParameter("hwModelId");
	
	Map<String,String> result = (HashMap<String,String>) request.getAttribute("result");
	String hwModelBean=null;
	if(result!=null){
		hwModelBean = (String)result.get("hwModelBean");
	}
%>

<!DOCTYPE html>
<html>
<head>

<title>硬件类型详细信息</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/asset/hwModelDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script type="text/javascript">
	$(document).ready(function() {		
		//respond.update();
		
		HwModel.objs["mode"]="<%=mode%>";//模式有浏览、新建、编辑三种：view/create/edit
		HwModel.objs["hwModelId"]="<%=hwModelId%>";
		HwModel.objs["hwModelBean"]=<%=hwModelBean%>;
		HwModelPriv.init();
		HwModel.init();		
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
				<button class="btn-default btn" onclick="HwModel.toClose()">关闭</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCancel">
				<button class="btn-default btn" onclick="HwModel.toBack()">取消</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="HwModel.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="HwModel.updateHwModel()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="HwModel.createHwModel()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="HwModel.toDelete()">删除</button>
			</div>	
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="form_baseinfo" class="margin-form-title margin-form-foldable">

	</form>
	
	<div id="hwLedgerList_info" grouptitle="使用它的硬件台账" style="overflow: hidden;">		
		<div id="hwLedgerListGrid" class="margin-title-table">
			<table id="hwLedgerList_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
</body>
</html>
