<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String pruordernoVal = request.getParameter("pruordernoVal") == null ? "":String.valueOf(request.getParameter("pruordernoVal")); 
	//String warehouseVal = request.getParameter("warehouseVal") == null ? "":String.valueOf(request.getParameter("warehouseVal"));
	String lotnoVal = request.getParameter("lotnoVal") == null ? "":String.valueOf(request.getParameter("lotnoVal"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>到货物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var pruordernoVal = '<%=pruordernoVal%>';
	//var warehouseVal = '<%--=warehouseVal--%>';
	var lotnoVal = '<%=lotnoVal%>';
	var isSearchLineShow=false;
	var columns = [[
		{field:'itemid',checkbox:'true',width:10,fixed:true},
		{field:'itemcode',title:'物资编号',width:90,sortable:true,fixed:true
		,formatter:function(value,row,index){
				return "<a onclick='FW.showItemInfo(\""+row.itemcode+"\",\""+row.warehouseid+"\");'>"+row.itemcode+"</a>";
			}
		},
		{field:'itemname',title:'物资名称',width:120,sortable:true,fixed:true},
		{field:'cusmodel',title:'物资型号',width:180,sortable:true},
		{field:'itemnum',title:'采购数量',width:60,sortable:true,fixed:true},
		{field:'stockqty',title:'库存余量',hidden:'true'},
		{field:'laststockqty',title:'已入库数量',width:80,sortable:true,fixed:true},
		{field:'unit1',title:'单位',width:50,sortable:true,fixed:true},
		{field:'price',title:'单位成本',hidden:'true'},
		{field:'binid',title:'货柜id',hidden:'true'},
		{field:'warehouseid',title:'仓库id',hidden:'true'},
		{field:'warehouse',title:'仓库',hidden:'true'},
		{field:'bin',title:'货柜',hidden:'true'},
		{field:'outterid',title:'外部关联id',hidden:'true'},
		{field:'purorderno',title:'采购编号',hidden:'true'}
	]];
	
	$(document).ready(function() {
		$("#arrival_grid").iDatagrid("init",{
			pageSize:pageSize,
			columns:columns,
			url: basePath+"inventory/invitem/queryArrivalItem.do",
			queryParams: {
							"pruordernoVal": pruordernoVal
						},
			onLoadSuccess:function(data){
				 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_wrap").hide();
	                $("#grid_empty").show();
	            }else{
	            	$("#grid_wrap").show();
	                $("#grid_empty").hide();
	            }
	            setTimeout(function(){ $("#arrival_grid").datagrid("resize"); },200);
			}
		});
		
		//查询
		 $("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#arrival_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#arrival_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
	});

	//获取列表中选中的id
	function getArrivalItem(){
		var dataArr = [];
		var rowData = $("#arrival_grid").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择采购合同信息 ");
			return;
		}else{
			for(var i=0;i<rowData.length;i++){
				rowData[i].bestockqty = rowData[i].itemnum - rowData[i].laststockqty;
				rowData[i].lotno = lotnoVal;
				rowData[i].remark = '';
				rowData[i].totalprice = parseFloat(rowData[i].price * rowData[i].bestockqty).toFixed(2);
				rowData[i].warehouse = rowData[i].warehouse;
				rowData[i].warehouseid = rowData[i].warehouseid;
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
	    <table id="arrival_grid" pager="#pagination" class="eu-datagrid"></table>
	</div>
	
	<!-- 无数据 -->
	<div class="row" id="grid_empty" style="display:none">查询到货数据不存在 </div>
</body>
</html>