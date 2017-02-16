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
<title>物资领料单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>

<script>
	var taskId = '<%=taskId%>';			//任务id
	var codes = '<%=codes%>';			//任务id
	var defKey='<%=defKey%>';
	var imaid = '<%=imaid%>';
	var process = '<%=process%>';		//环节名称
	var processName = '<%=processName%>';//环节名称
	var processInstId = '<%=processInstId%>';
	var classType = '<%=classType%>';
	var isEdit='<%=isEdit%>';			//当前用户是否编辑
	var siteid = '<%=siteid%>';
	var oper = '<%=oper%>';
	var hasRpts = '<%=hasRpts%>';
	
	var processStatus = null;
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = "matapplydetail_grid";
	/************************判断页面是否修改************************/
	
	var flag = true;
	var proMsg = "";
	var warehouseArr = null;
	var buttonType = "save";
	var nextStatus = '';//下一个流程状态 审批流程确定后

	$(document).ready(function() {
		//转换流程状态别名
		setProcessStatus();
		//初始化页面按钮
		InvMatApplyBtn.init();
		//初始化页面按钮虚拟权限
		InvMatApplyPriv.init();
		//初始化页面控制
		initPageProcess.init();
		
		FW.privilegeOperation("materiReQ_listAllowEdit","matapplydetail_grid");
		FW.privilegeOperation("materiReQ_btnAllowEdit",null);
		
		FW.fixToolbar("#toolbar");
		//repositionTab();
		
		//领料暂存按钮 先全部隐藏
		//$("#btn_save").hide();
	});
	
</script>
<script src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
<script src="${basePath}js/workorder/common/workOrder.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyProc.js?ver=${iVersion}"></script>

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
	        	<button type="button" id="btn_save" class="btn btn-default priv" privilege="materiReQ_save">暂存</button>
	        	<button type="button" id="btn_delete" class="btn btn-default priv" privilege="materiReQ_delete">作废</button>
	        	<button type="button" id="btn_edit" class="btn btn-default priv" privilege="materiReQ_edit">编辑</button>
	        	<button type="button" id="btn_submit" class="btn btn-default priv" privilege="materiReQ_commit">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_send" class="btn btn-default priv" privilege="materiReQ_grant">通知领料</button>
	        	<button type="button" id="btn_stopsend" class="btn btn-default priv" privilege="materiReQ_stopsend">终止领料</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_refund" class="btn btn-default priv" privilege="materiReQ_refund">退库</button>
	        </div>	        
	        <!-- timss2.1版本前使用 -->
	        <div class="btn-group btn-group-sm" id="showGroupBtn">
	        	<button type="button" id="btn_print" class="btn btn-default priv" privilege="materiReQ_export">打印</button>
	        </div> 
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_processinfo" class="btn btn-default">审批信息</button>
	        </div>
	        

	        
	    </div>
	</div>
	<div class="inner-title" id="pageTitle"> 物资领料单详情  </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matapplydetail_list" grouptitle="物资明细">
		<div id="matapplydetailGrid" class="margin-title-table">
			<table id="matapplydetail_grid" class="eu-datagrid"></table>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar">
			<div class="btn-group btn-group-xs">
		        <button type="button" class="btn btn-success" id="btn_add">添加物资</button>
		    </div>
		</div>
	</div>
	
	<div id="matapplyout_list" grouptitle="领料出库明细">
		<div id="matapplyoutGrid" class="margin-title-table">
			<table id="matapplyout_grid" class="eu-datagrid"></table>
		</div>
	</div>
	<div id="matrefund_list" grouptitle="退库明细">
		<div id="matrefundGrid" class="margin-title-table">
			<table id="matrefund_grid" class="eu-datagrid"></table>
		</div>
	</div>
	
</body>
</html>