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
<title>新建项目</title>

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var isNew = true;
var hasInitVal = false;
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/project.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/addProject.js?ver=${iVersion}"></script>
<script>
	var planId=getUrlParam('planId');
	var taskId=null;
	var defKey="pms_sjw_project";
	var selectBidCompName;
	$(document).ready(function() {
		var $form=$("#form1");
		if(!planId){
			var opt={
				form:$("#form1"),
				formFields:projectFormFields,
				initOther:initOther,
				wf_def_key:defKey
			};
			pmsPager.init(opt);
		}else{
			initFormWithPlanData(planId);
		}
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="tmpSave(this);"  data-loading-text="暂存">暂存</button>
	            <button type="button" class="btn btn-default" onclick="submit(this);"  data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" style="display:none;" onclick="createProjectCode();">生成项目编号</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	        
	       
	        
	    </div>
	</div>
	<div class="inner-title">
		新建项目
	</div>
	
	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div id="milestoneWrapper" class="margin-group-bottom" style="display:none;">
		<form id="milestoneList" grouptitle="里程碑信息" class="margin-title-table">
			 <table id="milestoneTable" class="eu-datagrid">
		    </table>
		</form>
		<div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addMilestone();" id="b-add-milestone">添加里程碑</button>
			</div>
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-default" onclick="saveMilestoneModel();" id="b-save-milestone-model">保存为模板</button>
			</div>
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-default" onclick="loadMilestoneModel();" id="b-load-milestone-model">载入模板</button>
			</div>
		</div>
	</div>
	<div id="workloadWrapper" class="margin-group-bottom" style="display:none;">
		<form id="workloadList" grouptitle="工作量信息" class="margin-title-table">
			 <table id="workloadTable" class="eu-datagrid">
		    </table>
		</form>
		<div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addWorkload();" id="b-add-workload">添加工作量记录</button>
			</div>
		</div>
	</div>
	<div id="outsourcingWrapper" class="margin-group-bottom" style="display:none;">
		<form id="outsourcingList" grouptitle="外购需求" class="margin-title-table">
			 <table id="outsourcingTable" class="eu-datagrid">
		    </table>
		</form>
		<div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-success" onclick="addOutsourcing();" id="b-add-outsourcing">添加外购需求</button>
			</div>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>