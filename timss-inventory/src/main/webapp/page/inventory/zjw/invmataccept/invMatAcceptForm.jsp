<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imaid = request.getParameter("imaid") == null ? "":String.valueOf(request.getParameter("imaid"));
	String codes = request.getParameter("codes") == null ? "":String.valueOf(request.getParameter("codes"));
	String taskId = request.getAttribute("taskId")==null?"":String.valueOf(request.getAttribute("taskId")); 
	String defKey = request.getAttribute("defKey")==null?"":String.valueOf(request.getAttribute("defKey"));
	String process = request.getAttribute("process")==null?"":String.valueOf(request.getAttribute("process"));
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
	String processName = request.getAttribute("processName")==null?"":String.valueOf(request.getAttribute("processName"));
	String processInstId = request.getAttribute("processInstId")==null?"":String.valueOf(request.getAttribute("processInstId"));
	String isEdit = request.getAttribute("isEdit")==null?"":String.valueOf(request.getAttribute("isEdit"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	String hasRpts = request.getAttribute("hasRpts")==null?"":String.valueOf(request.getAttribute("hasRpts"));
	String classType = request.getAttribute("classType")==null?"":String.valueOf(request.getAttribute("classType"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>物资验收表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>

<script>
	var data=${data};
    var defKey="inventory_"+data.siteid+"_accept";
	// 20151127 JIRA ==> TIM221
	var curProcess = data.curProcess;//当前环节id
	var invMatAcceptInfo = data.invMatAccept;
	$(document).ready(function() {
		//查询
		InvAcceptBtn.initForm(data);
		InvAcceptPriv.initFormBefore(data);
		InvAcceptForm.init(data);
		InvAcceptDetailList.init(data);
		
		if((typeof invMatAcceptInfo === 'undefined')||(invMatAcceptInfo.inacId ==null || invMatAcceptInfo.inacId=="")){
			$("#pageTitle").html("新建物资验收单");
		}
		//InvAcceptPriv.initFormAfter(data);
		FW.fixToolbar("#toolbar");
		
	});
	
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/zjw/invmataccept/invAcceptBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/zjw/invmataccept/invAcceptProc.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/zjw/invmataccept/invAcceptDetailList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/zjw/invmataccept/invAcceptForm.js?ver=${iVersion}"></script>

<style type="text/css">.btn-garbage{ cursor:pointer;}</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>

	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_void" class="btn btn-default" >作废</button>
	        	<button type="button" id="btn_delete" class="btn btn-default" style="display:none" >删除</button>
	        	<button type="button" id="btn_submit" class="btn btn-default" >提交</button>
	        	<button type="button" id="btn_approve" class="btn btn-default " >审批</button>
	        </div>
	        	        
	        <!-- timss2.1版本前使用 -->
	        <div class="btn-group btn-group-sm" id="showGroupBtn">
	        	<button type="button" id="btn_print" class="btn btn-default" >打印</button>
	        </div> 
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_processinfo" class="btn btn-default">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">物资验收单详情</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matacceptdetailGrid">
		<form id="matacceptdetail_list" class="margin-title-table" grouptitle="物资明细">
			<table id="matapplydetail_grid" class="eu-datagrid"></table>
		</form>
		<!-- <div class="btn-toolbar margin-foldable-button" role="toolbar">
			<div class="btn-group btn-group-xs">
		        <button type="button" class="btn btn-success" id="btn_add">添加物资</button>
		    </div>
		</div> -->
	</div>
</body>
</html>