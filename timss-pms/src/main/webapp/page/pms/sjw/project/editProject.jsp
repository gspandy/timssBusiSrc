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
<title>立项信息</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var loginUserId = ItcMvcService.user.getUserId();
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var isNew = false;
var hasInitVal = false;
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/project.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/editProject.js?ver=${iVersion}"></script>

<script>
	
    var id=getUrlParam("id");
    var processInstId=getUrlParam("processInstId");
    var defKey="pms_sjw_project";
	$(document).ready(function() {
		var form=$("#form1");
		$.post(basePath+'pms/project/queryProjectById.do',{id:id,processInstId:processInstId},function(data){
			if(data && data.flag=='success'){
				var opt={
					form:form,
					data:data,
					formFields:projectFormFields
				};
				pmsPager.init(opt);
			}else{
				FW.error(data.msg || "没有找到对应的项目立项信息");
			}
		});
		
		
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-button" onclick="tmpSave();" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button" onclick="submit(this);" id="b-save" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button" onclick="del();"  id="b-del">删除</button>
	        </div>
	         <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default pms-button" onclick="delWorkflow();" id="b-workflow-del">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default" onclick="shenpi();" style="display:none;" id="b-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-success" id="b-create-project-code" style="display:none;"  onclick="createProjectCode();">生成项目编号</button>
	        </div>
	        <div class="btn-group btn-group-sm "   >
	            <button type="button" class="btn btn-default pms-complex-privilege" id="pms-milestone-edit-actualtime" onclick="editMilestoneActualtime();" style="display:none;">录入实施进度</button>
	        </div>
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default  pms-complex-privilege" onclick="changeMilestone();" id="pms-milestone-change" style="display:none;">变更里程碑信息</button>
	        </div>
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default" id="pms-project-print">打印</button>
	        </div>
			<div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default pms-complex-privilege" onclick="openContractTab();" id="pms-project-complex-add">新建合同</button>
	        </div>
			<div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();">审批信息</button>
	        </div>
		</div>
	</div>
	<div class="inner-title">
		立项详情
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
				 <button type="button" class="btn btn-default" onclick="saveMilestoneModel();" style="display:none;" id="b-save-milestone-model">保存为模板</button>
			</div>
			<div class="btn-group btn-group-xs" >
				 <button type="button" class="btn btn-default" onclick="loadMilestoneModel();" style="display:none;" id="b-load-milestone-model">载入模板</button>
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
	
	<div id="milestoneChangeWrapper" class="margin-group-bottom" style="display:none;">
		<form id="milestoneChangeList" grouptitle="里程碑变更历史" class="margin-title-table">
			 <table id="milestoneChangeTable" class="eu-datagrid">
		    </table>
		</form>
		
	</div>
	<div class="priv" privilege="pms-b-br-query">
	<div id="bidListWrapper" class="margin-group-bottom">
		
		<div id="bidList" grouptitle="招标信息" class="margin-title-table">
			 <table id="test_grid1" class="eu-datagrid">
		        
		    </table>
		</div>
	</div>
	</div>
	<div class="priv" privilege="pms-b-contract-query">
	<div id="contractListWrapper" class="margin-group-bottom">
		<div id="contractList" grouptitle="合同信息" class="margin-title-table">
			<table id="contractTable" class="eu-datagrid">
		    </table>
		</div>
	</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>