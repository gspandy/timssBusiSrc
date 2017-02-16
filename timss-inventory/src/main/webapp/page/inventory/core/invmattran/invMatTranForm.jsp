<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imtid = request.getParameter("imtid") == null ? "":String.valueOf(request.getParameter("imtid"));
	String openType = request.getParameter("openType") == null ? "":String.valueOf(request.getParameter("openType"));
	String type = request.getParameter("type") == null ? "":String.valueOf(request.getParameter("type"));
	String oper = request.getAttribute("oper")==null?"":String.valueOf(request.getAttribute("oper"));
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
	String imtData = request.getAttribute("imtData")==null?"":String.valueOf(request.getAttribute("imtData"));
	String imtListData = request.getAttribute("iivListData")==null?"":String.valueOf(request.getAttribute("iivListData"));
	String processInitId = request.getAttribute("processInitId")==null?"":String.valueOf(request.getAttribute("processInitId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>物资接收单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script>
	var imtid = '<%=imtid%>';
	var type = '<%=type%>';
	var oper = '<%=oper%>';
	var siteid = '<%=siteid%>';
	var imtData = '<%=imtData%>';
	var imtListData = '<%=imtListData%>';
	var processInitId = '<%=processInitId%>';
	var openType = '<%=openType%>';
	var isEdit = false;
	var tmpid=1;
	var flagCounter = 0;
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = null;
	/************************判断页面是否修改************************/
	
	$(document).ready(function() {
		//按钮初始化
		InvMatTranBtn.init();
		//按钮虚拟权限初始化
		InvMatTranPriv.init();
		
		initList();
		//页面初始化配置
		initPageProcess.init();
		
		var dateTime = $("#f_createdate").val();
		if("" == dateTime){
			$("#f_createdate").val(FW.long2date(new Date()));
		}
		FW.fixToolbar("#toolbar");
		addFormCloseEvent();
		
	});
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTran.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTranForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTranList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTranOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTranBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmattran/invMatTranProc.js?ver=${iVersion}"></script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar" role="toolbar">
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_close" class="btn btn-default">关闭</button>
	        </div>	    
	    	<div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_edit" class="btn btn-default priv" style="display:none" privilege="storeinQ_edit">编辑</button>
	        	<!-- <button type="button" id="btn_record" class="btn btn-default priv" privilege="storeinQ_commit">暂存</button> -->
	        	<button type="button" id="btn_save" class="btn btn-default priv" privilege="storeinQ_commit">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_returns" class="btn btn-default priv" privilege="storeinQ_returns">退货</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_asset" class="btn btn-default priv" privilege="storeinQ_asset">资产化</button>
	        </div>	
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_print" class="btn btn-default priv" privilege="storeinQ_export">打印</button>
	        </div>	        
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">物资接收单详情 </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="mattrandetail_list" grouptitle="物资列表">
		<div id="mattrandetailGrid" class="margin-title-table">
			<table id="mattrandetail_grid" class="eu-datagrid"></table>
		</div>
		<!-- <div class="btn-toolbar margin-foldable-button" role="toolbar">
			<div class="btn-group btn-group-xs" id="btnGroup">
	        	<button type="button" class="btn btn-success" id="btn-add">添加到货物资</button>
	    	</div>
    	</div> -->
	</div>
	
	<div id="mattranasset_list" grouptitle="资产化记录">
		<div id="mattranassetapplyGrid" class="margin-title-table">
			<table id="mattranassetapply_grid" class="eu-datagrid"></table>
		</div>
	</div>	
	
	<div id="matreturndetail_list" grouptitle="退货列表">
		<div id="matreturndetailGrid" class="margin-title-table">
			<table id="matreturndetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
</body>
</html>