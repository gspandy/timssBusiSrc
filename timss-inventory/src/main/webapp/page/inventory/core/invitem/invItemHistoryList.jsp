<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String opentype = request.getParameter("opentype") == null ? "":String.valueOf(request.getParameter("opentype")); 
	String embbed = request.getParameter("embbed") == null ? "":String.valueOf(request.getParameter("embbed"));
	String isCkShow = request.getParameter("isCkShow")==null?"":String.valueOf(request.getParameter("isCkShow"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script>_useLoadingMask = true;</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />

<script>
	var opentype = '<%=opentype%>';
	var embbed = '<%=embbed%>';
	var isSearchLineShow=false;
	var isCkShow='<%=isCkShow%>';
	var isSearchMode = false;
	
	$(document).ready(function() {
		InvItemHistoryBtn.init();
		initItemGrid();
		
		$("#item_grid").datagrid("hideColumn","sparecode");
		$("#item_grid").datagrid("hideColumn","manufacturer");
		
		FW.fixToolbar("#toolbar1");
	});
</script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invItemHistory.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invItemHistoryList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invitem/invItemHistoryBtn.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div class="btn-toolbar" id="toolbar1">
	        <div class="btn-group btn-group-sm" id="historyDiv">
	        	<button type="button" id="toinv" class="btn btn-success">转为物资库存</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>	        
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="item_grid" pager="#pagination" class="eu-datagrid"></table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>

	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>

	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>