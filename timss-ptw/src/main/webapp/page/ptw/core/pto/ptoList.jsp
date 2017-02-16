<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>标准操作票记录列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var newBtnShowFlag =  ${newBtnShowFlag};
	var isNeedFlow =  ${isNeedFlow};
</script>
<script type="text/javascript" src="${basePath}js/pto/core/ptoList.js?ver=${iVersion}"></script>
<script>
var siteId = ItcMvcService.getUser().siteId;
var isSearchMode = false;
var selectAssetId = "";
var selectAssetName="";
var headSearchParams = {};
var subGrid;
var currentData = null;

	
$(document).ready(function() {
	//添加左边类型树
	FW.addSideFrame({
		src:basePath+"page/ptw/core/tree.jsp?jumpMode=func",
		id:"assetTree",
		conditions :[
			{tab:"^ptw$",tree:"^ptw_sptolist$"},
			{tab:"^ptw$",tree:"^ptw_pto_ptolist$"},
			{tab:"^PTO.+"},
			{tab:"^initNewSpto.+"},
			{tab:"^ptwSpto.+"},
			{tab:"^ptwPto.+"},
			{tab:"^newPto.+"}
		]
	});
	initDataGrid();
	if(newBtnShowFlag){  //控制新建按钮的显示与隐藏
		$("#btn_newPto").show();
		$("#btn_newPto2").show();
	}else{
		$("#btn_newPto").hide();
		$("#btn_newPto2").hide();
	}
	//表头搜索相关的
	$("#btn_advSearch").click(function(){
		if($(this).hasClass("active")){
			$("#ptolist_table").iDatagrid("endSearch");
		}else{
		    $("#ptolist_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        isSearchMode = true;
				//强烈建议转为一个string传给后台，方便转Bean或hashMap
				return {"search":JSON.stringify(args)};
			}});
		}
	});
});

</script>
<style type="text/css">
.afooter {
  position: fixed;
  bottom: 5px;
  height: 60px;
  box-shadow: 2px 2px 2px #888888;
  border: 1px solid #C0C0C0;
  background: #fff;
  width: 100%;
}
</style>

</head>
<body style="height: 100%;min-width:850px" class="bbox list-page">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button type="button" id="btn_newPto" class="btn btn-success" onclick="newPto()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">     
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="ptolist_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px">
		</div>
		<div style="height: 70px;"></div>
		<!-- 扩展信息 -->
		<div id="subGridDiv" class="afooter">
			 <table id="subPtwGrid" class="eu-datagrid">
		    </table>
		</div>
		
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有操作票信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	<button type="button" class="btn btn-success" id="btn_newPto2" onclick="newPto();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>