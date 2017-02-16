<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>关联主项目列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/asset/asset.js?ver=${iVersion}"></script>
<script>
	var isSearchLineShow=false;
	$(document).ready(function(){
		//查询
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		    	$("#btn_search").removeClass("active");
		        $("#item_grid").iDatagrid("endSearch");		        
		    }
		    else{
		    	isSearchLineShow=true;
		    	$("#btn_search").addClass("active");
		       	$("#item_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		$("#item_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			url: basePath+"asset/assetInfo/getInvItemList.do",
			queryParams: {"embbed":"1"},
			onLoadSuccess:function(data){
				if(data && data.total==0){
	                $("#item_grid,#toolbar_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#item_grid,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	            }
				setTimeout(function(){ $("#item_grid").datagrid("resize"); },200);
			}
		});
		
		function getItemMessage(){
		var itemMessage = "";
		var rowData = $("#item_grid").datagrid("getSelected");
		if( rowData == null || rowData == "" ){
			FW.error("请选择关联主项目 ");
			return;
		}else{
			info = rowData.itemCode+"||"+rowData.itemName;
		}
		return info;
	}
	})
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
    <div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="item_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'ck',width:10,checkbox:'true',fixed:true"></th>
					<th data-options="field:'itemId',width:90,sortable:true,fixed:true,hidden:true">物资ID</th>
					<th data-options="field:'itemCode',width:100,sortable:true,fixed:true">物资编号</th>
					<th data-options="field:'itemName',width:200,sortable:true,fixed:true">物资名称</th>
					<th data-options="field:'cusmodel',width:300">型号规格</th>
					<th data-options="field:'attr1',width:100,sortable:true,fixed:true">计量单位</th>
				</tr>
			</thead>
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
 </body>
</html>
