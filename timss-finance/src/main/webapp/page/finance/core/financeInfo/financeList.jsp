<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height: 99%;">
<head>
	<title>财务报销列表</title>
	<script type="text/javascript">
		_useLoadingMask = true;
	</script>
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<script type="text/javascript" src="${basePath}/js/finance/common/eventTab.js?ver=${iVersion}"></script>
	<script type="text/javascript" src="${basePath}/js/finance/financeList.js?ver=${iVersion}"></script>
	<style>
		.dropdown-menu{
			text-align:left;
		}
	</style>
	<script type="text/javascript">
		var selectMode="d"; 
		var searchType=1;
		var randomNbr="";
		var clkSchBtnFlag=0; //已点击"查询"按钮标志.0-未点击过查询 ,1-已点击过查询

		$(document).ready(function(){
			appendBtn();
			createdg();
		});
		
		$(window).resize(function(){
			$("#wiGrid").datagrid('resize');
		});	
	
	</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div id="grid1_show">
		<!-- 工具栏层 -->
		<div class="toolbar-with-pager bbox" >
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar" role="toolbar">
		        <div class="btn-group btn-group-sm">
		        	 <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		        	 	新建
		        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
		        	 </button>
		        	 <ul id="newButton1" class="dropdown-menu"></ul>
		        </div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" class="btn btn-default" onclick="search();">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		
		<!-- 表格层 -->
		<div id="grid1_wrap" style="width:100%">
		    <table id="wiGrid" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下分页器部分-->
		<div id="bottomPager" style="width:100%">
		</div>
	
		<!-- 错误信息-->
		<div class="row" id="grid1_error" style="display:none">
			无法从服务器获取数据，请检查网络是否正常
		</div>
		
		<!-- 点击查询按钮,结果为无数据 -->
		<div id="grid1_queryempty" style="width: 100%; height: 62%; margin: 10px;">
			<div style="height:100%;display:table;width:100%">
				<div style="display:table-cell;vertical-align:middle;text-align:center">
				    <div style="font-size:14px">没有找到符合条件的结果</div>
				    
				</div>
			</div>
		</div>
	</div>
	 <div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有报销信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	 <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		        	 	新建
		        	 	<span class="caret"></span> <!-- 添加"新建"文字右边的"下箭头" -->
		        	 </button>
		        	 <ul id="newButton2" class="dropdown-menu"></ul>
			           <!--  <li><a onclick="creFinOprPage('businessentertainment','only')">业务招待费报销</a></li> -->
		        </div>
			</div>
		</div>
	</div>
</body>
</html>