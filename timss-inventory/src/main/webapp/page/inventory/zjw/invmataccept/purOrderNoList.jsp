<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>采购合同号列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchLineShow=false;
	$(document).ready(function() {
		$("#purorder_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			url: basePath+"inventory/invmataccept/queryPurOrderList.do",
			onLoadSuccess:function(data){
				 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ $("#purorder_grid").datagrid("resize"); },200);
			}
		});
		
		//查询
		 $("#btn_search").click(function(){
		    if(isSearchLineShow){
			    isSearchLineShow=false;
		        $("#purorder_grid").iDatagrid("endSearch");
		    }
		    else{
		    	isSearchLineShow=true;
		       	$("#purorder_grid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	});

	//获取列表中选中的id
	function getPurOrderNos(){
		var info = "";
		var rowData = $("#purorder_grid").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择采购合同信息 ");
			return;
		}else{
			var arr = [];
			for(var i=0;i<rowData.length;i++){
				arr[i]=rowData[i].sheetno+"||"+rowData[i].sheetname;
			}
			info = arr.join(",");
		}
		return info;
	}
</script>
</head>
<body class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
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
	    <table id="purorder_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'ck',width:10,checkbox:'true',fixed:true"></th>
					<th data-options="field:'sheetno',width:140,fixed:true">采购合同编号</th>
					<th data-options="field:'sheetname',width:170">采购合同名称</th>
					<th data-options="field:'companyname',width:140">供应商信息</th>
				</tr>
			</thead>
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%;margin-top: 10px;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
</body>
</html>