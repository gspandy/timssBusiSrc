<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String propertyId = (String) request.getAttribute("propertyId");
 %>
<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>title</title>
<script type="text/javascript">
_useLoadingMask=true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/asset/propertyList.js?ver=${iVersion}"></script>

<script>
	var isInitTreeFold=false;
	
	$(document).ready(function(){
		Property.objs["propertyId"]="<%=propertyId%>";
		Property.init();
	});
	
</script>
<style type="text/css">
#btnBack,#btnCreate,#btnEdit{
	margin-left:0px;
	margin-right:7px;
}
</style>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="toolbar-with-pager bbox">
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm" id="btnBack" style="display:none">
				<button class="btn-default btn" onclick="Property.toBack()">返回</button>
			</div>
	        <div class="btn-group btn-group-sm" id="btnCreate">
		        <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
		      	 新建
		        <span class="caret"></span>
		        </button>
		        <ul class="dropdown-menu">
		            <li><a href="javascript:void(0)" onclick="Property.toCreate('house')">区域楼层</a></li>
		            <li><a href="javascript:void(0)" onclick="Property.toCreate('room')">房间</a></li>
		        </ul>
		    </div>
		    <div class="btn-group btn-group-sm" id="btnEdit" style="display:none">
				<button class="btn-default btn" onclick="Property.toEdit()">编辑</button>
			</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="propertyList" pager="#pagination" class="eu-datagrid">
	    </table>
	</div>
	<div id="noSearchResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">该房产下没有查询到相关房产</div>
			</div>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="grid_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		            <button type="button" class="btn btn-success dropdown-toggle" data-toggle="dropdown">
			      	 新建
			        <span class="caret"></span>
			        </button>
			        <ul class="dropdown-menu">
			            <li><a href="javascript:void(0)" onclick="Property.toCreate('house')">区域楼层</a></li>
			            <li><a href="javascript:void(0)" onclick="Property.toCreate('room')">房间</a></li>
			        </ul>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>