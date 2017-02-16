<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String location = (String) request.getAttribute("location");
 %>
<!DOCTYPE html>
<html style='height:99%'>
<head>
<title>title</title>
<script type="text/javascript">
_useLoadingMask=true;
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script type="text/javascript" src="${basePath}js/asset/assetPriv.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/asset/initAssetTree.js?ver=${iVersion}"></script>
<script>
		var form=[[
			{field:'assetName',title:'资产名称',width:200,sortable:true,fixed:true},
			{field:'cuModel',title:'型号',width:100,sortable:true,fixed:true},
			{field:'manufacturer',title:'制造商',width:100,sortable:true,fixed:true},
			{field:'installDate',title:'安装日期',width:90,fixed:true,sortable:true,formatter: function(value,row,index){
				return value==null||value==""?"":new Date(value).format("yyyy-MM-dd");
			}},
			{field:'empty',title:'',width:100}
		]];
	
	$(document).ready(function(){
		FW.setTreeStat("fold");
		
		//勿删，接口示例
		/* var assetTreeRollBackFunc=function(attrs){
			window.parent.document.getElementById("assetTree").contentWindow.onSelectAssetTreeNode=function(node){
				
			};
		};
		var assetTreeRollbackFuncAttrs={
			result:'success'
		}; 
		
		FW.set("AssetTreeInitRollBackFunc",function(node,data,assetTreeRollbackFuncAttrs){
		
		});
		FW.set("AssetTreeInitRollBackFuncAttrs",assetTreeRollbackFuncAttrs);
		
		FW.set("AssetTreeLoadRollBackFunc",function(node,data,assetTreeRollbackFuncAttrs){
		
		});
		FW.set("AssetTreeLoadRollBackFuncAttrs",assetTreeRollbackFuncAttrs);*/
		
		initAssetTree();//初始化资产树
		
		dataGrid = $("#location_grid1").iDatagrid("init",{
			singleSelect:true,
			columns:form,
			pageSize:pageSize,
			fitColumns:true,
			url: basePath + "asset/location/getLocationListData.do",
			queryParams :{"location":"<%=location%>"},
			onLoadSuccess:function(data){
				//没有父节点时报错
				if("<%=location%>"=="null"){
					$("#grid1_wrap,.toolbar-with-pager,#grid1_empty").hide();
	                $("#grid1_error").show();
				}else{
					$("#grid1_error").hide();
					if(data && data.total==0){
		                $("#grid1_wrap,.toolbar-with-pager").hide();
		                $("#grid1_empty").show();
		            }else{
		            	$("#grid1_wrap,.toolbar-with-pager").show();
		                $("#grid1_empty").hide();
		            }
				}
			},
			onDblClickRow : function(rowIndex, rowData) {
				var id = rowData.assetId;
				//var assetTree=window.parent.frames["assetTree"].contentWindow.$('#asset_tree');
				var assetTree=window.parent.document.getElementById("assetTree").contentWindow.$('#asset_tree');				
				assetTree.tree('select',assetTree.tree('find', id).target);
			}
		});
		
		AssetPriv.init();
	});
	
	function createAsset(){
		FW.navigate(basePath+"asset/assetInfo/assetDetail.do?mode=create&assetId=<%=location%>");
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="toolbar-with-pager bbox">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn btn-success priv" privilege="VIRTUAL-NEW" onclick="createAsset()">新建</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="location_grid1" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success priv" privilege="VIRTUAL-NEW" onclick="createAsset()">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="grid1_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">错误：没有查询到父节点或根节点，请联系管理员解决</div>
			</div>
		</div>
	</div>
	<!--大表需要加下分页器-->
	<div id="bottomPager" style="width:100%">
	</div>
</body>
</html>
