<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String imrsid = request.getParameter("imrsid") == null ? "":String.valueOf(request.getParameter("imrsid"));
	String pruorderno = request.getParameter("pruorderno") == null ? "":String.valueOf(request.getParameter("pruorderno")); 
	String imtid = request.getParameter("imtid") == null ? "":String.valueOf(request.getParameter("imtid"));
	String returnReason = request.getParameter("returnReason") == null ? "":String.valueOf(request.getParameter("returnReason"));
	String openType = request.getParameter("openType") == null ? "":String.valueOf(request.getParameter("openType"));
	String siteid = request.getAttribute("siteid")==null?"":String.valueOf(request.getAttribute("siteid"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>物资退货表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/homepage/homepageService.js?ver=${iVersion}"></script>
<script>
	var imtid = '<%=imtid%>';
	var imrsid = '<%=imrsid%>';
	var pruorderno = '<%=pruorderno%>'
	var returnReason = '<%=returnReason%>';
	var siteid = '<%=siteid%>';
	var openType = '<%=openType%>';
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
		InvMatReturnBtn.init();
		
		initList();
		//页面初始化配置
		
		var dateTime = $("#f_createdate").val();
		if("" == dateTime){
			$("#f_createdate").val(FW.long2date(new Date()));
		}
		FW.fixToolbar("#toolbar");
	});
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturns.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturnsForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturnsList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturnsOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturnsBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatreturns/invMatReturnsProc.js?ver=${iVersion}"></script>
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
	        	<button type="button" id="btn_save" class="btn btn-default" >提交</button>
	        </div>	        
	    </div>
	</div>
	<div class="inner-title" id="pageTitle"> 物资退货单详情 </div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
		
	<div id="matreturnsdetail_list" grouptitle="退货列表">
		<div id="matreturnsdetailGrid" class="margin-title-table">
			<table id="matreturnsdetail_grid" class="eu-datagrid"></table>
		</div>
	</div>
</body>
</html>