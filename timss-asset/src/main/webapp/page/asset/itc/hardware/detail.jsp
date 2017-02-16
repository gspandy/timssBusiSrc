<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%
	String mode = request.getParameter("mode");
	String hwLedgerId = request.getParameter("hwLedgerId");
	String hwLedgerType = request.getParameter("hwLedgerType");
	
	Map<String,String> result = (HashMap<String,String>) request.getAttribute("result");
	String hwLedgerBean=null;
	if(result!=null){
		hwLedgerBean = (String)result.get("hwLedgerBean");
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
<script type="text/javascript" src="${basePath}js/asset/hwLedgerDetail.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/searchHwLedger.js?ver=${iVersion}'></script>
<script type="text/javascript">
	$(document).ready(function() {		
		//respond.update();
		
		HwLedger.objs["mode"]="<%=mode%>";//模式有浏览、新建、编辑三种：view/create/edit
		HwLedger.objs["hwLedgerId"]="<%=hwLedgerId%>";
		HwLedger.objs["hwLedgerType"]="<%=hwLedgerType%>";
		HwLedger.objs["hwLedgerBean"]=<%=hwLedgerBean%>;
		HwLedgerPriv.init();
		HwLedger.init();	
		
		//查询
		$("#searchButtonDiv").click(function(){
			var hwId = "<%=hwLedgerId%>";
			var node =<%=hwLedgerBean%>;
			var nodeName = node.hwName;
			showDtlIframe( hwId, nodeName );
		});
	});
	
	function toShow(node){
		//如果是树节点为机房设备、存储设备、网络设备时，直接重载数据
		if(node.hwType=='HW_L_ROOM_EQPT'||node.hwType=='HW_L_NETWORK'||node.hwType=='HW_L_STORAGE'){
			HwLedger.getDetail(node.hwId,node.hwType);
		}else{//否则跳转到原来的处理逻辑			
			FW.navigate(basePath+"asset/assetMenu/queryHardwareMenu.do");
		}		
	}
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
.specialForms{
	display:none;
}
</style>
</head>

<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-group btn-group-sm" id="btnBack">
				<button class="btn-default btn" onclick="HwLedger.toBack()">返回</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn priv" privilege="AST_HWL_EDIT" onclick="HwLedger.toEdit()">编辑</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="HwLedger.updateHwLedger()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="HwLedger.createHwLedger()">保存</button>
			</div>
			<div class="btn-group btn-group-sm" id="btnDel">
				<button class="btn-default btn priv" privilege="AST_HWL_EDIT" onclick="HwLedger.toDelete()">删除</button>
			</div>	
		    <div class="btn-group btn-group-sm" id="searchButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="" id="searchButton">查询</button>
            </div>
		</div>
	</div>
	<!-- <div style="clear:both"></div> -->
	
	<div class="inner-title" id="pageTitle">
		此处写页标题
	</div>
	<form id="baseinfo_form" class="margin-form-title margin-form-foldable">

	</form>
	
	<div id="roomEqptForms" class="specialForms">
		<div id="re_hardwareInfo_div" grouptitle="硬件信息" class='formFold'>
			<form id="re_hardwareInfo_form" class="margin-form-title margin-form-foldable">
			</form>
		</div>
		<div class="margin-group"></div>
		<div id="re_configureInfo_div" grouptitle="配置信息" class='formFold'>
			<form id="re_configureInfo_form" class="margin-form-title margin-form-foldable">
			</form>
		</div>	
		<div class="margin-group"></div>
	</div>
	
	<div id="networkForms" class="specialForms">
		<div id="nw_deviceInfo_div" grouptitle="设备信息" class='formFold'>
			<form id="nw_deviceInfo_form" class="margin-form-title margin-form-foldable">
			</form>
		</div>	
		<div class="margin-group"></div>
	</div>
	
	<div id="storageForms" class="specialForms">
		<div id="s_hardwareInfo_div" grouptitle="硬件信息" class='formFold'>
			<form id="s_hardwareInfo_form" class="margin-form-title margin-form-foldable">
			</form>
		</div>	
		<div class="margin-group"></div>
		<div id="s_configureInfo_div" grouptitle="配置信息" class='formFold'>
			<form id="s_configureInfo_form" class="margin-form-title margin-form-foldable">
			</form>
		</div>	
		<div class="margin-group"></div>
	</div>
	
	<div id="serviceInfo_div" grouptitle="服务信息" class='formFold'>
		<form id="serviceInfo_form" class="margin-form-title margin-form-foldable">
		</form>
	</div>	
	<div class="margin-group"></div>
</body>
</html>
