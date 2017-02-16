<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购合同列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>
	//初始化
	var isSearchMode = false;
	$(document).ready(function() {
		OrderListBtn.init();
		PurOrderPriv.init();		
	    initListList();
	    FW.fixToolbar("#toolbar1");
	});
	
</script>
<script type="text/javascript" src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/purchase/core/purorder/purOrder.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/purchase/core/purorder/purOrderBtn.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/purchase/core/purorder/purOrderList.js?ver=${iVersion}"></script>
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" style='margin-left:0;margin-right:7px'>
	            <button type="button" class="btn btn-success priv" id="btn_new" privilege="exampurch_new">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm" style='margin-left:0;'>
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	        <div class="input-group input-group-sm" style="width:160px;float:left;margin-left:7px;margin-top:1px">
		        <input type="text" id="item_search" icon="itcui_btn_mag" placeholder="请输入物资编号或名称" style="width:160px"/>     
		    </div>
	    </div>
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="table_order" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'sheetno',width:135,fixed:true,sortable:true">流水号</th>
					<th data-options="field:'spNo',width:65,fixed:true,sortable:true,formatter:function(val){return null!=val?val.replace('YD/ZSD-WS-',''):''}">合同编号</th>
					<th data-options="field:'sheetname',width:1,sortable:true">名称</th>
					<th data-options="field:'supComName',width:240,fixed:true,sortable:true,formatter:function(val){return val.replace('有限','').replace('市','').replace('公司','').replace('集团','').replace('股份','')}">供应商</th>
					<th data-options="field:'applysheetno',width:155,fixed:true,sortable:true">申请单号</th>
					<th data-options="field:'username',width:64,fixed:true">经办人</th>
					<th data-options="field:'createdate',width:105,fixed:true,sortable:true">创建日期</th>
					<th data-options="field:'totalPrice',width:90,fixed:true,sortable:true,align:'right'">总价(元)</th>
					<th data-options="field:'curHandler',width:64,fixed:true">办理人</th>
					<th data-options="field:'statusName',width:85,fixed:true">状态</th>
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
			    <div class="btn-group btn-group-sm margin-element">
		        	<button type="button" class="btn btn-success btn_new">新建</button>
			    </div>
			</div>
		</div>
	</div>

	<div id="bottomPager" style="width:100%"></div>
</body>
</html>