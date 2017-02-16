<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>公司查询列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	//获取列表中选中的id
	function getCompanyIds(){
		var info = "";
		var rowData = $("#table_company").datagrid("getSelections");
		if( rowData == null || rowData == "" ){
			FW.error("请选择供应商信息 ");
			return;
		}else{
			var arr = [];
			for(var i=0;i<rowData.length;i++){
				arr[i]=rowData[i].code+"_"+rowData[i].name;
			}
			info = arr.join(",");
		}
		return info;
	}
	
	//初始化item信息
	$(document).ready(function() {
       $("#btn_advlocal").click(function(){
		    if($(this).hasClass("active")){
		    	$("#btn_advlocal").removeClass("active");
		        $("#table_company").iDatagrid("endSearch");
		    }
		    else{
		    	$("#btn_advlocal").addClass("active");
		       	$("#table_company").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
		
		//初始化列表
		$("#table_company").iDatagrid( "init",{
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"purchase/purvendor/queryCompanyList.do",
	        singleSelect:true,
	        onLoadSuccess : function(data){
	        	var total = data.rows.length; 
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(total == 0){
	                $("#grid1_wrap,#toolbar_wrap").hide();
	                $("#grid_error").show();
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ 
	            	$("#table_company").datagrid("resize"); 
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
	    <table id="table_company" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'name',width:300,fixed:true">公司名称</th>
					<th data-options="field:'spiCellphone',width:150,fixed:true">联系电话</th>
					<th data-options="field:'spiName',width:120,fixed:true">联系人</th>
					<th data-options="field:'type',width:160,fixed:true">类型</th>
					<th data-options="field:'blank',width:160"></th>
				</tr>
			</thead>
		</table>
	</div>

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