<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>供应商列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var adv_btn = null;
	$(document).ready(function() {
		//高级查询
		$("#btn_advlocal").click(function(){
			    if($(this).hasClass("active")){
			    	$("#btn_advlocal").removeClass("active");
			        $("#table_vendor").iDatagrid("endSearch");
			    }
			    else{
			    	$("#btn_advlocal").addClass("active");
			       	$("#table_vendor").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
						return {"search":FW.stringify(arg)};
					}});
			    }
		});
		//新建
		$("#btn_new").click(function(){
			var url = basePath+ "/purchase/purvendor/purVendorForm.do?type=new";
		    var prefix = new Date().getTime() + "" + Math.floor(Math.abs(Math.random()*100));
		    FW.addTabWithTree({
		        id : "newVendorForm" + prefix,
		        url : url,
		        name : "新建供应商",
		        tabOpt : {
		            closeable : true,
		            afterClose : function(id){
		                FW.deleteTab(id),
		                FW.activeTabById("purchasing");
		            }
		        }
		    });
		});
		
		//初始化列表
		dataGrid = $("#table_vendor").iDatagrid(
		"init",{
        	pageSize:pageSize,//默认每页显示的数目 只能从服务器取得
	        url: basePath+"/purchase/purvendor/queryPurVendor.do",
	        onLoadSuccess : function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                $("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        }
	    });
		dataGrid.iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
			return {"search":FW.stringify(arg)};
		}});
	});
</script>
</head>
<body class="list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager"></div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="table_vendor" pager="#pagination_1" class="eu-datagrid">
			<thead>
				<tr>
					<th data-options="field:'companyNo',width:20,checkbox:'true',fixed:true">公司编码</th>
					<th data-options="field:'name',width:300,fixed:true">公司名称</th>
					<th data-options="field:'type',width:120,fixed:true">公司类型</th>
					<th data-options="field:'contact',width:80,fixed:true">联系人</th>

				</tr>
			</thead>
		</table>
	</div>
	
	<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">无法从服务器获取数据，请检查网络是否正常</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">查询数据不存在</div>
</body>
</html>