<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>定期工作</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/common/eventTab.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/scheduleTask/scheduleTask.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/common/stationCommon.js?ver=${iVersion}"></script>
<script>
	var id="${id}";
	var loginUserId = ItcMvcService.user.getUserId();
	var sameStationIdFlag = false;
	//页面初始化
	$(document).ready(function() {
		$("#title_content").iFold("init");
		$("#title_content").iFold("show");
		
		$("#contentForm").iForm('init',{"fields":contentFormFields,options:{validate:true}});
		
		if(id && id != "" && id != null){
			initPageData(id);
		}else{
			$("#btn_edit").hide();
			$("#btn_delete").hide();
			$("#contentForm").iForm("hide",["code"]);
		}
		//控制权限
		Priv.apply();
		FW.fixToolbar("#toolbar1");
	});
	
	function refresh(){
		window.location.href=window.location.href;
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	     	<div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTabUnMsg();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_do" privilege ="OPR_NOTE_SAVE" class="btn btn-success" onclick="showDoDialog();">执行</button>
	            <button type="button" id="btn_undo"  privilege ="OPR_NOTE_SAVE" class="btn btn-default" onclick="showUnDoDialog();">不执行</button>
	            <button type="button" id="btn_change"  privilege ="OPR_NOTE_SAVE" class="btn btn-default"  onclick="showChangeDialog();">转其他班次</button>
	        </div>
	    </div>
	</div>
	
	<div class="inner-title">
		<span id="inPageTitle">定期工作详情</span>
	</div>
   
    <div id="title_content" grouptitle="工作内容">
   	 	<form id="contentForm" class="autoform"></form>
   	</div>
   	<div id="title_doInfo" grouptitle="执行情况">
   	 	<form id="doInfoForm" class="autoform"></form>
   	</div>
   	<div id="title_changeInfo" grouptitle="流转记录">
	   	<div class="margin-title-table">
	   	 	<form id="changeInfodatagrform"><table id="changeInfoGrid" class="eu-datagrid"></table></form>
   	 	</div>
   	</div>
</body>
</html>