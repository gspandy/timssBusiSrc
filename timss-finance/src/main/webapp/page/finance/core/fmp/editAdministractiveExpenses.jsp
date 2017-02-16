<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html>
<html style="height: 99%;">
<head>
<%
	//valKey是上传组件删除时的key，sessionid是上传组件初始化时的属性
	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<title>行政报销信息</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script type="text/javascript" src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fma/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fma/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fmp/administractiveExpenses.js?ver=${iVersion}"></script>
<script src="${basePath}js/finance/core/fmp/editAdministractiveExpenses.js?ver=${iVersion}"></script>
<script>
	var defKey = "${defKey}";
	//这里fmpId不是pay表的主键，而是mainId
	var fmpId = '${fmpId}';
	var userId = "${userId}";
	var userName = "${userName}";
	$(document).ready(function() {
		initFmpForm(fmpId);
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default fin-readOnly priv" privilege="fin-close-readOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="fin-close-edit" onclick="closeTab();">关闭</button>
	            <button type="button" class="btn btn-default priv" privilege="fin-tmpSave" onclick="tmpSave();" data-loading-text="暂存">暂存</button>
	            <button type="button" class="btn btn-default priv" privilege="fin-submit" onclick="submit(this);" data-loading-text="提交">提交</button>
	            <button type="button" class="btn btn-default priv" onclick="approve();" privilege="fin-approve" id="fin-approve">审批</button>
	            <button type="button" class="btn btn-default priv" onclick="approveRvk();" privilege="fin-approve-rvk" id="fin-approve-rvk">审批</button>
	            <button type="button" class="btn btn-default priv" onclick="approveSubmitPaperMaterial();" privilege="fin-approve-submitPaperMaterial" id="fin-approve-submitPaperMaterial">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default fin-gray-button priv" privilege="fin-del" onclick="del(fmpId);" data-loading-text="删除" id="fin-del">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default priv" onclick="nullify();" privilege="fin-workflow-del" id="fin-workflow-del">作废</button>
	        </div>
	        <%--
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default priv"  id="fin-print">打印</button>
	        </div>
	        --%>
	        <div class="btn-group btn-group-sm">
		        <button type="button" class="btn btn-default dropdown-toggle priv" privilege="fin-print" data-toggle="dropdown">
		        	打印
		        <span class="caret"></span> 
		        </button>
		        <ul class="dropdown-menu">
		        	<li><a id="fin-print">报销单</a></li>
			        <li><a id="fin-print-apply">申请单</a></li>
			    </ul>
		    </div>
		    
		    <div class="btn-group btn-group-sm " >
	            <button type="button" class="btn btn-default" id="pms-b-fma-view">查看申请单</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default priv" privilege="fin-workflow-show" onclick="showWorkflow();"  id="fin-wfinfo">审批信息</button>
	        </div>
	        <%-- 行政报销审批-结束 --%>
	    </div>
	</div>
	<div class="inner-title">
		<span id="inPageTitle">行政报销详情</span>
	</div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	<div  class="margin-group-bottom">
		<form id="expensesListWrapper" grouptitle="报销明细" class="margin-title-table">
			<table id="expensesList" class="eu-datagrid"></table>
		</form>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				 <button type="button" class="btn btn-success" onclick="addExpenses();" id="b-add-expensesdtl">添加明细</button>
			</div>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>