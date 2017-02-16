<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资领料详细列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchLineShow=false;
	var columns = [[
		{field:'itemid',checkbox:'true',width:10,fixed:true},
		{field:'itemcode',title:'物资编号',width:90,sortable:true,fixed:true
		,formatter:function(value,row,index){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
			}
		},
		{field:'itemname',title:'物资名称',width:110,sortable:true,fixed:true},
		{field:'cusmodel',title:'物资型号',width:110,sortable:true,fixed:true},
		{field:'stockqty',title:'库存余量',width:60,sortable:true,fixed:true,styler: function(value,row,index){
				if (value == 0){
					return 'color:red';
				}
			}},
		{field:'outqty',title:'已领料数量',hidden:'true',width:10,sortable:true,fixed:true},
		{field:'unit1',title:'单位',width:24,sortable:true},
		{field:'price',title:'单价(元)',width:70,sortable:true,fixed:true},
		{field:'precollarqty',title:'预领用',width:60,sortable:true,fixed:true,hidden:'true'},
		{field:'warehouseid',title:'仓库id',hidden:'true'}
	]];

	$(document).ready(function() {
		$("#invmatapplydetail_grid").iDatagrid("init",{
			columns:columns,
			pageSize:pageSize,
			url: basePath+"inventory/invmatapplydetail/queryConsumingList.do",
			onLoadSuccess:function(data){
				 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_wrap").hide();
	                $("#grid_empty").show();
	            }else{
	            	$("#grid_wrap").show();
	                $("#grid_empty").hide();
	            }
	            setTimeout(function(){ $("#invmatapplydetail_grid").datagrid("resize"); },200);
			}
		});
		
		//查询
         $("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#invmatapplydetail_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#invmatapplydetail_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
        
	});

	function getMatApplyDetail(){
		var dataArr = [];
		var rowData = $("#invmatapplydetail_grid").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择领料物资 ");
			return;
		}else{
			for(var i=0;i<rowData.length;i++){
				rowData[i].outqty = '0';
				rowData[i].qtyApply = '1';
				rowData[i].totalprice = rowData[i].price;
			}
		}
		return rowData;
	}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar1" class="btn-toolbar ">
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
	    <table id="invmatapplydetail_grid" pager="#pagination" class="eu-datagrid">
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid_empty" style="display:none">查询数据不存在 </div>
</body>
</html>