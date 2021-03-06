<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>质检日志列表</title>
<script type="text/javascript">
_useLoadingMask=true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/operation/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/operation/common/pageList.js?ver=${iVersion}"></script>
<script>
	$(document).ready(function(){
		PageList.init({
			datagrid:{
				id:"qualityTestList",//required
				detailPage:{
					url:basePath+"operation/qualityTest/detailPage.do?mode=view&qtId=",//required
					createUrl:basePath+"operation/qualityTest/detailPage.do?mode=create",//required
					idPrefix:"qualityTest",//详情页面id前缀，后面加_详情项id
					namePrefix:"质检日志"//详情页面名称前缀，后面加详情项名称
				},
				params:{
					idField:"qtId",//required
					nameField:"",//required
					columns:[[
						{field:'qtDate',title:'质检日期',width:110,sortable:true,fixed:true,formatter:function(val){
							return FW.long2date(val);
						}},
						{field:'userName',title:'记录人',width:70,sortable:true,fixed:true},
						{field:'content',title:'质检情况',width:200,sortable:true},
						{field:'createdate',title:'记录时间',width:160,sortable:true,fixed:true,formatter:function(val){
							return FW.long2time(val);
						}}
					]],//required
					url:basePath+"operation/qualityTest/getList.do"//required
				}
			}
		});
	});
	
</script>
<style type="text/css">
</style>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="toolbar-with-pager bbox">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="btnCreate">
		        <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="PageList.toCreate()">新建</button>
		    </div>
			<div class="btn-group btn-group-sm" id="btnSearch"> 
	        	<button onclick="PageList.toShowSearchLine()" class="btn btn-default">查询</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%">
	    <table id="qualityTestList" pager="#pagination" class="eu-datagrid">
	    </table>
	</div>
	<div id="noSearchResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关质检日志</div>
			</div>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="grid_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		           <button class="btn-success btn priv" privilege="VIRTUAL-NEW" onclick="PageList.toCreate()">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>