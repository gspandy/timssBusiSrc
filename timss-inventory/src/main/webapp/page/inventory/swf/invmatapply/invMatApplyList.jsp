<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资领料列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script>
	var isSearchLineShow=false;
	var isSearchMode = false;
	$(document).ready(function() {
		InvMatApplyBtn.initList();
		//初始化页面按钮虚拟权限
		InvMatApplyPriv.init();
		initMatApplyList();
		
		FW.fixToolbar("#toolbar1");
	});

	function refCurPage(){
	 	$("#invmatapply_grid").datagrid("reload");
	}
</script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invmatapply/invMatApplyProc.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_new" class="btn btn-success priv"  privilege="materiReQ_new">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	        <div class="input-group input-group-sm" style="width:160px;float:left;margin-left:7px;margin-top:1px">
		        <input type="text" id="quickSearchInput" icon="itcui_btn_mag" placeholder="请输入物资编号或名称" style="width:160px"/>     
		    </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="invmatapply_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'imaid',hidden:true"></th>
					<th data-options="field:'sheetno',width:140,fixed:true,sortable:true">编号</th>
					<th data-options="field:'sheetname',width:80,sortable:true">名称</th>
					<th data-options="field:'createuser',width:70,fixed:true">申请人</th>
					<th data-options="field:'dept',width:90,fixed:true">申请部门</th>
					<th data-options="field:'createdate',width:100,fixed:true,sortable:true,formatter: function(value,row,index){return FW.long2date(value);}">申请日期</th>
					<th data-options="field:'price',width:90,fixed:true,sortable:true,align:'right'">成本(元)</th>
					<th data-options="field:'status',width:140,fixed:true">状态</th>
				</tr>
			</thead>
	    </table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element" style=''>
		                 <button type="button" class="btn btn-success btn_new">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>