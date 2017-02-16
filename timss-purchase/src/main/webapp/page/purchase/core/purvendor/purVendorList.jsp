<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>供应商列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />  
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
<script>
	var adv_btn = null;
	var isSearchMode = false;
	$(document).ready(function() {
		VendorListBtn.init();
		PurVendorPriv.init();
		FW.fixToolbar("#toolbar1");
		//初始化列表
		dataGrid = $("#table_vendor").iDatagrid("init",{
			singleSelect:true,
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"/purchase/purvendor/queryPurVendor.do",
	        onLoadSuccess : function(data){
	        	if(isSearchMode){
	            	 if(data && data.total==0){
	            		 $("#noSearchResult").show();
	            	 }
	            	 else{
	            		 $("#noSearchResult").hide();
	            	 }
	            }
	        	else{
		            if(data && data.total==0){
		                $("#grid_wrap,#toolbar_wrap").hide();
		                $("#grid_error").show();
		            }else{
		            	$("#grid_wrap,#toolbar_wrap").show();
		                $("#grid_error").hide();
		            }
		            $("#noSearchResult").hide();
	        	}
	        	setTimeout(function(){ 
	        		$("#table_vendor").datagrid("resize"); 
	        	},200);
	        	isSearchMode = false;
	        },
	        onDblClickRow : function(rowIndex, rowData) {
				var url = basePath+ "/purchase/purvendor/purVendorForm.do?type=edit&companyNo="+rowData.companyNo;
		    	var prefix = rowData.companyNo;
			    FW.addTabWithTree({
			        id : "editVendorForm" + prefix,
			        url : url,
			        name : "供应商信息",
			        tabOpt : {
			            closeable : true,
			            afterClose : "FW.deleteTab('$arg');FW.activeTabById('purchasing');FW.getFrame(FW.getCurrentTabId()).refCurPage();"
			        }
			    });
			}
	    });
	});
	
	
	function refCurPage(){
	 	$("#table_vendor").datagrid("reload");
	 	setTimeout(function(){ 
	 		$("#table_vendor").datagrid("resize"); 
	 	},200);
	} 
</script>
<script src="${basePath}js/purchase/core/purvendor/purVendor.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purvendor/purVendorBtn.js?ver=${iVersion}"></script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success priv" id="btn_new" privilege="companyinfo_new">新建</button>	            
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn btn-default" id="btn_advlocal">查询</button> 
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="table_vendor" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'name',width:420,sortable:true">公司名称</th>
					<th data-options="field:'type',width:80,fixed:true,sortable:true">公司类型</th>
					<th data-options="field:'contact',width:70,fixed:true">联系人</th>
					<th data-options="field:'tel',width:140,fixed:true">电话</th>
					<th data-options="field:'fax',width:140">传真</th>  
				</tr>
			</thead>
		</table>
		<div id="noSearchResult" style="width: 100%;display:none">
			<span>没有找到符合条件的结果</span>
		</div>
	</div>
	
	<!-- 无数据 -->
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success btn_new">新建</button>
			    </div>
			</div>
		</div>
	</div>

	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>