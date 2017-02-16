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
<title>定期工作计划</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/common/eventTab.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/scheduleTask/scheduleTaskPlan.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/common/stationCommon.js?ver=${iVersion}"></script>
<script>
	var id="${id}";
	var loginUserId = ItcMvcService.user.getUserId();
	var shiftIds = "";
	//页面初始化
	$(document).ready(function() {
		FW.createAssetTree({multiSelect:false,forbidEdit:true}); //左边设备树
		//控制权限
		Priv.apply();
		
		$("#title_content").iFold("init");
		$("#title_content").iFold("show");
		$("#title_cycle").iFold("init");
		$("#title_cycle").iFold("show");
		$("#title_workteam").iFold("init");
		$("#title_workteam").iFold("show");
		
		var $contentForm=$("#contentForm");
		var $cycleForm=$("#cycleForm");
		var $workteamForm=$("#workteamForm");
		
		$contentForm.iForm('init',{"fields":contentFormFields,options:{validate:true}});
		$cycleForm.iForm('init',{"fields":cycleFormFields,options:{validate:true}});
		$workteamForm.iForm('init',{"fields":workteamFormFields,options:{validate:true}});
		//日期选择框
		getDateNumberOfMonth();
		//星期选择框
		getWeekNumberOfWeek();
		//工种
		stationOption( basePath, "f_deptId",initPage,null,changeShift,null);
		
	});
	function initPage(){
		if(id && id != "" && id != null){
			initPageData(id);
		}else{
			$("#btn_edit").hide();
			$("#btn_delete").hide();
			$("#contentForm").iForm("hide",["code"]);
		}
		FW.fixToolbar("#toolbar1");
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
	        	<button type="button" id="btn_edit"  privilege="OPR_SCHEDTASKPLAN" class="btn btn-default priv" onclick="edit();">编辑</button>
	            <button type="button" id="btn_save"  privilege="OPR_SCHEDTASKPLAN" class="btn btn-success priv" onclick="save();">保存</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_delete"  privilege="OPR_SCHEDTASKPLAN" class="btn btn-default priv"  onclick="del();">删除</button>
	        </div>
	    </div>
	</div>
	
	<div class="inner-title">
		<span id="inPageTitle">新建定期工作计划</span>
	</div>
   
    <div id="title_content" grouptitle="工作内容">
   	 	<form id="contentForm" class="autoform"></form>
   	</div>
   	<div id="title_cycle" grouptitle="工作周期">
   	 	<form id="cycleForm" class="autoform"></form>
   	</div>
   	<div id="title_workteam" grouptitle="负责班次">
   	 	<form id="workteamForm" class="autoform"></form>
   	</div>
</body>
</html>