<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imrsid = request.getParameter("imrsid") == null ? "":String.valueOf(request.getParameter("imrsid"));
	String refundReason = request.getParameter("refundReason") == null ? "":String.valueOf(request.getParameter("refundReason"));
	String embed = request.getParameter("embed") == null ? "":String.valueOf(request.getParameter("embed"));
	String sheetno = request.getParameter("sheetno") == null ? "":String.valueOf(request.getParameter("sheetno"));
	String imaid = request.getParameter("imaid") == null ? "":String.valueOf(request.getParameter("imaid"));
	String hasRpts = request.getAttribute("hasRpts")==null?"":String.valueOf(request.getAttribute("hasRpts"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>物资退库表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>

<script>
	var imaid = '<%=imaid%>';
	var imrsid = '<%=imrsid%>';  //退库退货单id
	var hasRpts = '<%=hasRpts%>';
	var embed ='<%=embed%>';
	var sheetno ='<%=sheetno%>';  //退库单sheetno
	var refundReason = '<%=refundReason%>'; //退库原因
	
	/************************判断页面是否修改************************/
	var initFormStatus = null;
	var currFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = "matrefunddetail_grid";
	/************************判断页面是否修改************************/
	
	var flag = true;

	$(document).ready(function() {
		InvMatRefundBtn.init();
		if(imrsid==null || imrsid==""){
			$("#pageTitle").html("新建物资退库单");
		}
		// 新建生成领料单
		initList();
		FW.fixToolbar("#toolbar");
	});
	
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrefund/invMatRefund.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrefund/invMatRefundForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrefund/invMatRefundList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrefund/invMatRefundOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatrefund/invMatRefundBtn.js?ver=${iVersion}"></script>

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
	        	<button type="button" id="btn_submit" class="btn btn-default priv" privilege="materiReQ_commit">提交</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title" id="pageTitle"> 物资退库单详情  </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matrefunddetail_list" grouptitle="物资明细">
		<div id="matrefunddetailGrid" class="margin-title-table">
			<table id="matrefunddetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
	
</body>
</html>