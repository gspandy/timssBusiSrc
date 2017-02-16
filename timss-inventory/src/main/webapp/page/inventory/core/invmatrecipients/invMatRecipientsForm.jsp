<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<% 
	String imrid = request.getParameter("imrid") == null ? "":String.valueOf(request.getParameter("imrid"));
	String embed = request.getParameter("embed") == null ? "":String.valueOf(request.getParameter("embed"));
	String imaid = request.getAttribute("imaid")==null?"":String.valueOf(request.getAttribute("imaid"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId")); 
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	//String isWarehouse = request.getAttribute("isWarehouse")==null?"":String.valueOf(request.getAttribute("isWarehouse"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资发料表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}/js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>

<script>
	var taskId = '<%=taskId%>';			//任务id
	var siteid = '<%=siteid%>';
	var defKey='<%=defKey%>';
	var imrid = '<%=imrid%>';
	var imaid = '<%=imaid%>';
	var embed = '<%=embed%>';
	var process = '<%=process%>';		//环节名称
	var processName = '<%=processName%>';//环节名称
	var processInstId = '<%=processInstId%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var oper = '<%=oper%>';
	var imaCreateUserId = '${imaCreateUserId}';
	var imaCreateUserName = '${imaCreateUserName}';
	var deliveryDate;//发料日期
	var form = null;
	
	$(document).ready(function() {
		InvMatRecipientsPriv.init();
		InvMatRecipientsBtn.init();
		
		IMRLoadingForm.init();
		
		initList();
		
		FW.fixToolbar("#toolbar");
		FW.privilegeOperation("materiReQ_btnAllowEdit",null);
	});
</script>
<style type="text/css">.btn-garbage{ cursor:pointer;}</style>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrecipients/invMatRecipientsProc.js?ver=${iVersion}"></script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>	    
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_submit" class="btn btn-default priv" privilege="materiReQ_applyend">发料</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_borrow" class="btn btn-default priv" onclick="openBorrowWindow()" privilege="materiReQ_borrow">登记资产领用</button>
	        </div>	        
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default">打印</button>
	        </div>	        
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">
		物资发料单详情
	</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matrecdetail_list" grouptitle="物资明细">
		<div id="matrecdetailGrid" class="margin-title-table">
			<table id="matrecdetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
	
</body>
</html>