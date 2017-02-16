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
<title>新增业务招待费申请</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script type="text/javascript" src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/common.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/common/selectPersonWithStructure.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}/js/finance/core/fma/businessentertainmentapply.js?ver=${iVersion}"></script>
<script>
	var pager={};
	var g = {users:{}};
	var id="${id}";
	var defKey="finance_itc_businessentertainment";
	var applyUserId='${applyuserid}';
	var applyName='${applyname}';
	var deptname='${deptname}';
	var deptid='${deptid}';
	var processInstId= null;
	var taskId = null;
	var loginUserId = ItcMvcService.user.getUserId();
	var currHandUser = "";
	var currStatus = null;
	var oldBudget = null;
	//页面初始化
	$(document).ready(function() {
		var $form=$("#form1");
		$form.iForm('init',{"fields":businessentertainFormFields,options:{validate:true}});
		$form.iForm("setVal",{"subject":"ywzdf"})
		$form.iForm("hide","subject");
		
		if(id && id != "" && id != null){
			initPageData(id);
		}else{
			//附件处理
			initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
			$form.iForm("hide",["budget","needZJL"]);
			$("#btn_audit").hide();
			$("#btn_delete").hide();
			$("#btn_obsolete").hide();
			$("#btn_newbusinessFin").hide();
			$("#fin-print").hide();
		}
		//处理申请人和申请部门框
		initApplyUserAndDept($form);
		FW.fixToolbar("#toolbar1");
	});
	function refreshFinancePage(){
		$("#fincostTable").datagrid('reload');
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
	            <button type="button" id="btn_save" class="btn btn-default" onclick="tmpSave();">暂存</button>
	            <button type="button" id="btn_commit" class="btn btn-default" onclick="submit(this);">提交</button>
	            <button type="button" id="btn_audit" class="btn btn-default "  onclick="shenpi();">审批</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_delete" class="btn btn-default"  onclick="del();">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_obsolete" class="btn btn-default"  onclick="zuofei();">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_newbusinessFin" class="btn btn-default"  onclick="newbusinessFinance();">新增业务招待费报销</button>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="fin-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_flowInfo" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		<span id="inPageTitle">新建业务招待费申请</span>
	</div>
   
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	 <!-- 费用报销单层 -->
	<div grouptitle="相关费用报销单" id="fincostTitle">
		<div class="margin-title-table">
			<table id="fincostTable" style="" class="eu-datagrid"></table>
		</div>
	</div>
		
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>