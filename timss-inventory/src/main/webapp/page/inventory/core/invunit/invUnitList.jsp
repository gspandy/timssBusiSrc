<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>计量单位列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/inventory/inventory.css" rel="stylesheet" type="text/css" />
<script>
	//var treeUrl = null;
	var isSearchLineShow=false;
	var isSearchMode = false;
	$(document).ready(function() {
		InvUnitBtn.initList();
		InvUnitPriv.init();
		initList();
		$("#btn_enable").attr("disabled",true);
		$("#btn_disable").attr("disabled",true);
		FW.fixToolbar("#toolbar1");
	});
	
	function refCurPage(){
	 	$("#unit_grid").datagrid("reload");
	}
</script>
<script src="${basePath}js/inventory/core/invunit/invUnitBtn.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invunit/invUnitList.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invunit/invUnitOper.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/core/invunit/invUnitProc.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_new" class="btn btn-success priv" onclick="createUnit()" privilege="unitList_new">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm" style='display:none'>
	        	<button type="button" id="btn_enable" class="btn btn-default priv" privilege="unitList_enable">启用</button>
	        </div>
	        <div class="btn-group btn-group-sm" style='display:none'>
	        	<button type="button" id="btn_disable" class="btn btn-default priv" privilege="unitList_disable">停用</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>	        
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="unit_grid" pager="#pagination_1" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'unitname',width:150,sortable:true,fixed:true">单位名称</th>
					<th data-options="field:'descriptions',width:150,fixed:true">单位描述</th>
					<th data-options="field:'active',width:70,sortable:true,
						formatter:function(value,row,index){
							if(row.active == 'Y'){ 
								return '启用'
							}else{ 
								return '停用'
							}
						}">状态</th>
				</tr>
			</thead>
	    </table>
	    <div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success priv" onclick="createUnit()" privilege="unitList_new">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>