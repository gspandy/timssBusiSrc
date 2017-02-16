<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>培训申请列表</title>
<script type="text/javascript">
_useLoadingMask=true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/attendance/common/addTab.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/attendance/common/pageList.js?ver=${iVersion}"></script>
<script>
	$(document).ready(function(){
		PageList.init({
			datagrid:{
				id:"trainingList",//required
				detailPage:{
					url:basePath+"attendance/training/detailPage.do?mode=view&trainingId=",//required
					createUrl:basePath+"attendance/training/detailPage.do?mode=create",//required
					idPrefix:"training",//详情页面id前缀，后面加_详情项id
					namePrefix:"培训申请"//详情页面名称前缀，后面加详情项名称
				},
				params:{
					idField:"trainingId",//required
					nameField:"",//required
					columns:[[
						{field:'trainingNo',title:'申请单号',width:140,sortable:true,fixed:true},
						{field:'userName',title:'申请人',width:70,sortable:true,fixed:true},
						{field:'deptName',title:'申请部门',width:100,sortable:true,fixed:true},
						{field:'createdate',title:'申请日期',width:100,sortable:true,fixed:true,formatter:function(val){
							return FW.long2date(val);
						}},
						{field:'startDate',title:'培训开始日期',width:100,sortable:true,fixed:true,formatter:function(val){
							return FW.long2date(val);
						}},
						{field:'endDate',title:'培训结束日期',width:100,sortable:true,fixed:true,formatter:function(val){
							return FW.long2date(val);
						}},
						{field:'content',title:'培训内容',width:70},
						{field:'address',title:'培训地点',width:70,fixed:true},
						{field:'status',title:'状态',width:140,sortable:true,fixed:true}
					]],//required
					url:basePath+"attendance/training/getList.do"//required
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
	    <table id="trainingList" pager="#pagination" class="eu-datagrid">
	    </table>
	</div>
	<div id="noSearchResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关培训申请</div>
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