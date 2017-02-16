<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String sessId = request.getSession().getId();
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String site=operator.getCurrentSite().toLowerCase();
%>

<!DOCTYPE html>
<html>
<head>

<title>资产详细信息</title>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script type="text/javascript">
_useLoadingMask=true;
</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/asset/asset.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/initAssetTree.js?ver=${iVersion}"></script>

<script type="text/javascript">
	var uploadIds = "";
	var pageCode = "<%=site%>_core_assetDetail";
	var formId = "form_baseinfo";
	
	$(document).ready(function() {		
		respond.update();
		$.extend(Asset.objs,{
			bean:{assetId:"${assetId}"},
			mode:"${mode}",//模式有浏览、新建、编辑三种：view/create/edit
			sessId:"<%=sessId%>",
			valKey:"<%=valKey%>"
		});
		Asset.init("${param.paramsMap}");	
	});

	function goBack() {
		if(Asset.objs["bean"].assetId==Asset.objs["assetTreeRootNode"].id){//根节点则退回列表
			window.parent.document.getElementById("assetTree").contentWindow.jumpTo(
			//window.parent.frames["assetTree"].contentWindow.jumpTo(
				basePath+"asset/location/locationList.do?location="+Asset.objs["bean"].assetId
			);
		}else{
			Asset.showDetail(null,true);
		}
	}	
	
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
				<button class="btn-default btn" onclick="goBack()">关闭</button>
			</div>
		
<!-- 	<div class="btn-group btn-group-sm" id="btnNew">
        <button class="btn btn-default dropdown-toggle" data-toggle="dropdown">
        新建
        <span class="caret"></span>
        </button>
        <ul class="dropdown-menu">
            <li><a href="javascript:void(0);" onclick="Asset.changeMode('create','child')" id="btnNew-child">子资产</a></li>
            <li><a href="javascript:void(0);" onclick="Asset.changeMode('create','brother')" id="btnNew-brother">兄弟资产</a></li>
            <li><a href="javascript:void(0);" onclick="Asset.changeMode('create','parent')" id="btnNew-parent">父资产</a></li>  
        </ul>
    </div>-->  
   		
   			<div class="btn-group btn-group-sm" id="btnNew" style="margin-left:0">
				<button class="btn-default btn priv" privilege="VIRTUAL-NEW" onclick="Asset.changeMode('create','child')">新建子资产</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCopy">
				<button class="btn-default btn priv" privilege="VIRTUAL-NEW" onclick="Asset.changeMode('create','copy')">复制</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="VIRTUAL-EDIT" onclick="Asset.changeMode('edit')">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="Asset.updateAsset()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="Asset.createAsset()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="VIRTUAL-DELETE" onclick="Asset.delAsset()">删除</button>
			</div>	
			<div class="btn-group btn-group-sm" id="btnConf">
				<button class="btn-default btn priv" privilege="VIRTUAL-CONFIG" onclick="FW.openDDBox()">配置表单</button>
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
				<button type="button" class="btn btn-success" id="btnAddSpare" onclick="Asset.addSpare()">添加备件</button>
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
	
	<div id="woQx_info" grouptitle="缺陷记录" style="">		
		<div id="woQxGrid" class="margin-title-table">
			<table id="woQx_table" class="eu-datagrid">
			</table>
		</div>
	</div>
	<div class="margin-group"></div>
</body>
</html>
