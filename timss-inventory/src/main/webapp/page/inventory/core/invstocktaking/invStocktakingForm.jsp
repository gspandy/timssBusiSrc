<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>

<% 
	String istid = request.getParameter("istid") == null ? "":String.valueOf(request.getParameter("istid"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String classType = request.getAttribute("classType")==null?"":String.valueOf(request.getAttribute("classType"));
	//附件上传
	String sessId = request.getSession().getId();
	SecureUser operator = (SecureUser) session.getAttribute(Constant.secUser);
	String valKey = FileUploadUtil.getValidateStr(operator,FileUploadUtil.DEL_OWNED);
	
	String uploadFiles = request.getAttribute("uploadFiles")==null?"":String.valueOf(request.getAttribute("uploadFiles"));
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>物资盘点</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var istid = '<%=istid%>';
	var isEdit = '<%=isEdit%>';
	var defKey = '<%=defKey%>';
	var siteid = '<%=siteid%>';
	var taskId = '<%=taskId%>';
	var oper = '<%=oper%>';
	var processInstId = '<%=processInstId%>';
	var process = '<%=process%>';
	var processName = '<%=processName%>';
	var processStatus = null;
	var flag = true;
	var classType = '<%=classType%>';
	var proMsg = "";
	var uploadIds = "";
	var uploadFiles = '<%=uploadFiles%>';
	var sessId = '<%=sessId%>';
	var valKey = '<%=valKey%>';
	var switchFlag = ${switchFlag};
	var status = '${status}';
	var numVal = "";
	
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = null;
	/************************判断页面是否修改************************/
	
	var comboxArr = [];
	
	$(document).ready(function() {
		setProcessStatus();
		InvStocktakingPriv.init();
		InvStocktakingBtn.init();
		
		initPageProcess.init();
		FW.privilegeOperation("storecheck_listAllowEdit","stocktakingdetail_grid");
		FW.privilegeOperation("storecheck_btnAllowEdit",null);
		FW.privilegeOperation("storecheck_attachAllowEdit",null);
		
		FW.fixToolbar("#toolbar");
		addFormCloseEvent();
	});
</script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>

<script src="${basePath}js/inventory/core/invstocktaking/invStocktaking.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invstocktaking/invStocktakingForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invstocktaking/invStocktakingList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invstocktaking/invStocktakingBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invstocktaking/invStocktakingOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invstocktaking/invStocktakingProc.js?ver=${iVersion}"></script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar" role="toolbar">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>	    
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_edit" class="btn btn-default priv" privilege="storecheck_edit">编辑</button>
	        	<button type="button" id="btn_save" class="btn btn-default priv" privilege="storecheck_save">暂存</button>
	        	<button type="button" id="btn_delete" class="btn btn-default priv" privilege="storecheck_delete">作废</button>
	        	<button type="button" id="btn_submit" class="btn btn-default priv" privilege="storecheck_commit">审批</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_export" class="btn btn-default priv" privilege="storecheck_export">盘点表导出</button>
	        </div>
	        
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_processinfo" class="btn btn-default">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title" id="pageTitle"> 库存盘点详情 </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="stocktakingdetail_list" grouptitle="物资盘点明细">
		<div id="stocktakingdetailGrid" class="margin-title-table">
			<form id="stocktakingform">
				<table id="stocktakingdetail_grid" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar">
			<div class="btn-group btn-group-xs" id="btnGroup">
	        	<button type="button" class="btn btn-success" id="btn_add">选择盘点物资</button>
	    	</div>
    	</div>
    	<div class="margin-group"></div>
		<div grouptitle="附件"  id="uploadfileTitle">
			<div class="margin-title-table" id="uploadfile">
				<form id="uploadform" style=""></form>
			</div>
		</div>
	</div>
</body>
</html>