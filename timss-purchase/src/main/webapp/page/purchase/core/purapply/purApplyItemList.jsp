<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购物资列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	//获取列表中选中的id
	function getListIds(){
		var ids = "";
		var rowData = $("#table_applyitem").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择物资信息 ");
			return;
		}else{
			var arr = [];
			for(var i=0;i<rowData.length;i++){
				arr[i]=rowData[i].itemid;
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
		        $("#table_applyitem").iDatagrid("endSearch");
		    }
		    else{
		    	$("#btn_advlocal").addClass("active");
		       	$("#table_applyitem").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//初始化列表
		$("#table_applyitem").iDatagrid( "init",{
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"/purchase/purapply/queryItemList.do",
	        onLoadSuccess : function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ 
	            	$("#table_applyitem").datagrid("resize"); 
	            },200);
	        }
	    });
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
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
	    <table id="table_applyitem" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'ck',width:10,checkbox:'true',fixed:true"></th>
					<th data-options="field:'itemid',width:120,fixed:true">物资编码</th>
					<th data-options="field:'itemname',width:180">物资名称</th>
					<th data-options="field:'cusmodel',width:200">型号规格</th>
					<th data-options="field:'storenum',width:60,fixed:true,align:'right'">库存余量</th>
				</tr>
			</thead>
		</table>
	</div>

	<div id="bottomPager_1" style="width:100%;margin-top:6px"></div>
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element" style="display:none;">
		                 <button type="button" class="btn btn-success btn_new">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>