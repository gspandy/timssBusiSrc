<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>合理化建议列表</title>
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
				id:"rationalizationList",//required
				detailPage:{
					url:basePath+"attendance/Rationalization/detailPage.do?mode=view&rationalId=",	//required
					createUrl:basePath+"attendance/Rationalization/detailPage.do?mode=create",		//required
					idPrefix:"rationalization",															//详情页面id前缀，后面加_详情项id
					namePrefix:"合理化建议"																//详情页面名称前缀，后面加详情项名称
				},
				params:{
					idField:"rationalId",//required
					nameField:"",//required
					/* 合理化建议列表字段 */
					columns:[[
						{field:'rationalNo',title:'编号',width:150,sortable:true,fixed:true},
						{field:'deptName',title:'部门',width:80,sortable:true,fixed:true},
						{field:'recomName',title:'建议人',width:160,fixed:true},
						{field:'userName',title:'申请人',width:60,sortable:true,fixed:true},
						{field:'rationalType',title:'专业分类',width:90,fixed:true,
							'editor':{
								 "type" : "combobox",
		 						 "options" : {
		 					    	"data" : FW.parseEnumData("ATD_RATION_TYPE",_enum)
		 				     	}
		 					}	
						},
						{field:'projectName',title:'项目名称',width:230},
						{field:'handleName',title:'处理人',width:170,fixed:true},
						{field:'status',title:'状态',width:100,sortable:true}
					]],//required
					url:basePath+"attendance/Rationalization/getList.do"//required
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
	    <table id="rationalizationList" pager="#pagination" class="eu-datagrid">
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