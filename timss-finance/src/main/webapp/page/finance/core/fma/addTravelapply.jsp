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
<title>新增出差申请</title>
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
<script src="${basePath}/js/finance/core/fma/ftravelapply.js?ver=${iVersion}"></script>
<script>
	var pager={};
	var g = {users:{}};
	var id="${id}";
	var siteId = ItcMvcService.getUser().siteId;
	var defKey="finance_"+siteId.toLowerCase()+"_travelapply";  //流程ID前缀
	var applyUserId='${applyuserid}';
	var applyName='${applyname}';
	var deptname='${deptname}';
	var deptid='${deptid}';
	var processInstId= null;
	var taskId = null;
	var loginUserId = ItcMvcService.user.getUserId();
	var currHandUser = "";
	//页面初始化
	$(document).ready(function() {
		var $form=$("#form1");
		$form.iForm('init',{"fields":ftravelFormFields,options:{validate:true}});
		$form.iForm("hide","needZJL");
		$form.iForm("hide","isTravel");
		$form.iForm("setVal",{"subject":"clf"});
		$form.iForm("hide","subject");
		
		if(id && id != "" && id != null){
			initPageData(id);
		}else{
			//附件处理
			initAttachForm([],$('#attachForm'),$('#attachFormWrapper'),false);
			$("#btn_audit").hide();
			$("#btn_delete").hide();
			$("#btn_obsolete").hide();
			$("#btn_newTravelFin").hide();
			$("#fin-print").hide();
		}
		//处理申请人和申请部门框
		initApplyUserAndDept($form);
		FW.fixToolbar("#toolbar1");
	});
	function refreshFinancePage(){
		//$("#fincostTable").datagrid('reload');
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
	            <button type="button" id="btn_newTravelFin" class="btn btn-default dropdown-toggle" data-toggle="dropdown" >办理
	            	<span class="caret"></span> <!-- 添加文字右边的"下箭头" -->
	            </button>
	            <ul class="dropdown-menu">
					<li><a onclick="newTravelFinance()">新增差旅费报销</a></li>
					<li id="btn_cancel"><a onclick="cancelTravel()">取消出差</a></li>
				</ul>
	        </div>
	         <div class="btn-group btn-group-sm">
	            <button type="button"  class="btn btn-default" id="fin-print">打印</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_flowInfo" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	        
	    </div>
	</div>
	<div class="inner-title">
		<span id="inPageTitle">新建出差申请</span>
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