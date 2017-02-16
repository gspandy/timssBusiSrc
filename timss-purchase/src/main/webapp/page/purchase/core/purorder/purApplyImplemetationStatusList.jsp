<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String sheetId = request.getParameter("sheetId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />

<script>
	var sheetId = '<%=sheetId%>';
	//初始化采购申请table
	var isSearchMode1 = false;
	
	//初始化执行js
	$(document).ready(function() {
		initDataGrid();
		FW.fixToolbar("#toolbar1");
	});
	
	function initDataGrid(){
		dataGrid = $("#table_apply").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "purchase/purorder/queryPurApplyImplemetationStatusList.do?sheetId="+sheetId,	//basePath为全局变量，自动获取的       
	        columns:[[ 
					{field:"itemcode",title:"物资编码",width:80,fixed:true},
					{field:"itemname",title:"物资名称",width:120},   
					{field:"cusmodel",title:"型号规格",width:120},
					{field:"isToBusiness",title:"经商务网",width:60,fixed:true},
					{field:"repliednum",title:"批准数量",width:60,fixed:true,align:'right'},
					{field:"itemnum",title:"采购数量",width:60,fixed:true,align:'right'},
					{field:"sheetno",title:"采购合同",width:135,fixed:true},
					{field:"applyPrice",title:"预算单价",width:60,fixed:true,align:'right'},
					{field:"orderPrice",title:"合同税后价",width:75,fixed:true,align:'right'},
					{field:"invNum",title:"已入库",width:50,fixed:true,align:'right'}
				]],
	       onLoadSuccess: function(data){
		        if(data && data.total==0){
		            $("#noSearchResult").show();
		            setTimeout(function(){ 
		    	 		$("#table_apply").datagrid("resize"); 
		    	 	},200);
		        }else{
		            $("#noSearchResult").hide();
		        }
	        },
	        onRenderFinish: function(data){
	        	setTimeout(function(){ 
	    	 		$("#table_apply").datagrid("resize"); 
	    	 	},200);
	        }
	    });
	}
	
	//刷新列表
	function refCurPage(){
	 	$("#table_apply").datagrid("reload");
	 	setTimeout(function(){ 
	 		$("#table_apply").datagrid("resize"); 
	 	},200);
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	    </div>
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager_1"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="table_apply" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
		
	<div id="bottomPager_1" style="width:100%;margin-top:6px"></div>
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
</body>
</html>