<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>年度计划信息</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/plan/plan.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/plan/editPlan.js?ver=${iVersion}"></script>
<script>
var id=getUrlParam("id");
var notice=getUrlParam("notice");
var processInstId=getUrlParam("processInstId");


Priv.map("notice==1","cancel-notice-for-plan-change");
$(document).ready(function() {
	var form=$("#form1");
	
	
	$.post(basePath+'pms/plan/queryPlanById.do',{id:id},function(data){
		
		if(data && data.flag=='success'){
			var opt={
				data:data,
				form:$('#form1'),
				formFields:planFormFields
			};
			pmsPager.init(opt);
			
		}else{
			form.iForm("init",{"fields":planFormFields});
			FW.error(data.msg || "没有找到对应的年度计划信息");
		}
	});
});
    
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	   		<div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="tmpSave(this);" data-loading-text="提交" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="submit(this);" data-loading-text="提交" id="b-save">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="del(this);" data-loading-text="提交" id="b-del">删除</button>
	        </div>
	         <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-complex-privilege" onclick="changePlan(this);" id="pms-b-plan-change" >变更</button>
	        </div>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-complex-privilege"  id="pms-b-project-add"  onclick="addProject(this);" data-loading-text="提交" >添加项目</button>
	        </div>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default priv" privilege="cancel-notice-for-plan-change"  id="pms-b-cancel-notice"  onclick="cancelNotice(this);">取消待办提醒</button>
	        </div>
	        
	        
	    </div>
	</div>
	<div class="inner-title">
		年度计划详情
	</div>

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div class="priv" privilege="pms-b-project-query">
	<div id="projectListWrapper" class="margin-group-bottom">
		<div id="projectList" grouptitle="项目信息" class="margin-title-table">
			 <table id="projectTable" class="eu-datagrid">
		    </table>
		</div>
	</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>