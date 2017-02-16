<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//20170117 add by gucw 项目名称
	String warehouse = request.getAttribute("warehouse")==null?"":String.valueOf(request.getAttribute("warehouse"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
<script>
	//初始化采购申请table
	var isSearchMode1 = false;
	/************************项目名称相关信息*****************************/
	var warehouse = '<%=warehouse.replace("'","\\'").replace("\"","\\\"")%>';
	var warehouseArray ;
	var warehouseMap = {};
	if(""!=warehouse){
		warehouseArray = FW.parse(warehouse);		
		for(var w=0;w<warehouseArray.length;w++){
			warehouseMap[warehouseArray[w][0]] = warehouseArray[w][1];
		}
	}
	
	//初始化执行js
	$(document).ready(function() {
		ApplyBtn.initList();
		PurApplyPriv.init();
		initApplyTable();
		FW.fixToolbar("#toolbar1");
	});
	
	//刷新列表
	function refCurPage(){
	 	$("#table_apply").datagrid("reload");
	 	setTimeout(function(){ 
	 		$("#table_apply").datagrid("resize"); 
	 	},200);
	}
</script>
<script src="${basePath}js/purchase/core/purapply/purApply.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purapply/purApplyList.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/zjw/purapply/purApplyBtn.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" style="margin-left:0;margin-right:7px;">
	            <button type="button" class="btn btn-success priv" id="btn_new" privilege="applypurch_newSingle">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:0;margin-right:7px;">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	        <div class="input-group input-group-sm" style="width:160px;float:left;margin-left:7px;margin-top:1px">
		        <input type="text" id="item_search" icon="itcui_btn_mag" placeholder="请输入物资编号或名称" style="width:160px"/>     
		    </div>
	    </div>
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager_1"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="table_apply" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'sheetno',width:135,fixed:true,sortable:true">编号</th>
					<th data-options="field:'sheetname',width:3,sortable:true">名称</th>
					<th data-options="field:'projectAscription',width:1,sortable:true,
						formatter:function(val){
							return warehouseMap[val];
						},
				        editor: {
				        	'type':'combobox',
				            'options' : {
				            	'data' : warehouseArray
				            }
				        }	
					">项目名称</th>
					<th data-options="field:'createname',width:80,fixed:true">申请人</th>
					<th data-options="field:'orgname',width:90,fixed:true">申请部门</th>
					<th data-options="field:'createdate',width:105,fixed:true,sortable:true">申请日期</th>
					<th data-options="field:'dhdate',width:105,fixed:true,sortable:true">要求到货日期</th>
					<th data-options="field:'totalcost',width:90,fixed:true,sortable:true,align:'right'">总价(元)</th>
					<th data-options="field:'curHandler',width:80,fixed:true">办理人</th>
					<th data-options="field:'curLink',width:85,fixed:true">状态</th>
				</tr>
			</thead>
		</table>
		<div id="noSearchResult1" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>
	<div id="bottomPager_1" style="width:100%;margin-top:6px"></div>
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success btn-new">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>