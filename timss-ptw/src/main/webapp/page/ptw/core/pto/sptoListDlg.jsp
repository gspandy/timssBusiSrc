<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>标准操作票记录列表对话框</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>
<%--
_useLoadingMask = true;
--%>
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/pto/core/sptoListDlg.js?ver=${iVersion}"></script>
<script>
var siteId = ItcMvcService.getUser().siteId;
var isSearchMode = false;
var headSearchParams = {};	
	
$(document).ready(function() {
	initDataGrid();
	//表头搜索相关的
	$("#btn_advSearch").click(function(){
		if($(this).hasClass("active")){
			$("#sptord_table").iDatagrid("endSearch");
		}else{
		    $("#sptord_table").iDatagrid("beginSearch",{noSearchColumns:{4:true},"remoteSearch":true,"onParseArgs":function(args){
		        isSearchMode = true;
		        args["_status"]="passed";
				return {"search":JSON.stringify(args)};
			}});
		}
	});
});
</script>
</head>
<body style="height: 100%;" class="bbox">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">     
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="sptord_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px">
		</div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有标准操作票信息</div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>