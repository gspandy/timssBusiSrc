<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String cateId = request.getParameter("cateId") == null ? "":String.valueOf(request.getParameter("cateId")); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资类型列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var cateId = '<%=cateId%>';
	//初始化列表
	function initList(){
		$("#categroy_grid").iDatagrid("init",{
			singleSelect:true,
			pageSize:pageSize,
			url: basePath+"inventory/invcategroy/queryCategroyLevelOne.do?cateId="+cateId,
			onLoadSuccess:function(data){
				 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid_wrap").hide();
	                $("#grid_error").show();
	                $("#toolbar_wrap").hide();
	            }else{
	            	$("#toolbar_wrap").show();
	            	$("#grid_wrap").show();
	                $("#grid_error").hide();
	            }
	            setTimeout(function(){ $("#categroy_grid").datagrid("resize"); },200);
			},
			onDblClickRow : function(rowIndex, rowData) {
			   window.location.href = basePath+"inventory/invcategroy/invcategroyForm.do?cateId=" + rowData.invcateid;
			}
		});
	}
	
	function addCategory(){
		window.location.href = basePath + "inventory/invcategroy/invcategroyForm.do?parentId=" + cateId;
	}
	
	$(document).ready(function() {
		initList();
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_new" data-toggle="button" class="btn btn-success" onclick="addCategory()">新建</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="categroy_grid" pager="#pagination" class="eu-datagrid">
	    	<thead>
				<tr>
					<th data-options="field:'invcateid',hidden:true,width:10,fixed:true">物资类型id</th>
					<th data-options="field:'invcatename',width:140,fixed:true">物资类型名称</th>
					<th data-options="field:'descriptions',width:140,fixed:true">物资类型描述</th>
					<th data-options="field:'status',width:56,formatter:function(value,row,index){if(row.status == 'ACTIVE'){ return '启用'}else{ return '停用'}}">启用状态</th>
				</tr>
			</thead>
	    </table>
	</div>
	
	<div id="bottomPager" style="width:100%">
	</div>

	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">该仓库下无分类数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="addCategory()">新建分类</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>