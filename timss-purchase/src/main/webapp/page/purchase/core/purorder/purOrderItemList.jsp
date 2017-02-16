<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String includeitems = null!=request.getParameter( "includeitems" )?request.getParameter( "includeitems" ):"";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>申请采购物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var includeitems = '<%=includeitems%>';
	//获取列表中选中的id
	function getListIds(){
		var ids = "";
		var rowData = $("#table_orderitem").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择物资信息 ");
			return;
		}else{
			var arr = [];
			for(var i=0;i<rowData.length;i++){
				arr[i]=rowData[i].sheetId+"_"+rowData[i].itemid+"_"+rowData[i].warehouseid+"_"+rowData[i].invcateid;
			}
			ids = arr.join(",");
		}
		return ids;
	}
	//初始化item信息
	$(document).ready(function() {
        $("#btn_advlocal").click(function(){
		    if($(this).hasClass("active")){
		    	$("#btn_advlocal").removeClass("active");
		        $("#table_orderitem").iDatagrid("endSearch");
		    }
		    else{
		    	$("#btn_advlocal").addClass("active");
		       	$("#table_orderitem").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//初始化列表
		$("#table_orderitem").iDatagrid( "init",{
			idField:"tmpid",
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"/purchase/purorder/queryItemList.do?includeitems="+includeitems,
	        onLoadSuccess : function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_error").show();
	            }else{
	            	$("#toolbar_wrap").show();
	            	$("#grid1_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ 
	            	$("#table_orderitem").datagrid("resize"); 
	            },200);
	        }
	    });
	});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	    </div>
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager_1"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="table_orderitem" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'tmpid',width:10,checkbox:'true',fixed:true"></th>
					<th data-options="field:'sheetId',width:10,hidden:true,fixed:true"></th>
					<th data-options="field:'sheetno',width:120,fixed:true">申请单编号</th>
					<th data-options="field:'sheetName',width:200">申请单名称</th>
					<th data-options="field:'itemid',width:90,fixed:true">物资编码</th>
					<th data-options="field:'itemname',width:140">物资名称</th>
					<th data-options="field:'itemcus',width:90">型号规格</th>
					<th data-options="field:'itemnum',width:60,fixed:true,align:'right'">采购数量</th>
					<th data-options="field:'warehouseid',hidden:true">仓库id</th>
					<th data-options="field:'invcateid',hidden:true">物资分类id</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element" style='display:none'>
		                 <button type="button" class="btn btn-success" onclick="refreshGrid()">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>