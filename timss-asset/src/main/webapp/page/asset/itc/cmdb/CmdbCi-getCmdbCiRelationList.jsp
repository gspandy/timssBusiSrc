<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>关联关系列表</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>

<script>
//第一次打开页面
var modelFlag = false;
var ciId = "${ciId}";

	$(document).ready(function() {
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "asset/cmdbCi/queryCiRelation.do?ciId=" + ciId,	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'upCiName',title:'上联CI名称',width:250,fixed:true, sortable: true},
				{field:'upCiId',title:'上联CI',width:250,fixed:true, sortable: true,hidden:true},
				{field:'upCiType',title:'上联CI类型',width:180,fixed:true, sortable: true,formatter:function(val){
					return FW.getEnumMap("CMDB_CI_TYPE")[val];
				},editor:{
					type : "combobox",
					options: {
						data : FW.parseEnumData("CMDB_CI_TYPE",_enum)
					}
				}},
				{field:'downCiName',title:'下联CI名称',width:250,fixed:true, sortable: true},
				{field:'downCiId',title:'下联CI',width:250,fixed:true, sortable: true,hidden:true},
				{field:'downCiType',title:'下联CI类型',width:180,fixed:true, sortable: true,formatter:function(val){
					return FW.getEnumMap("CMDB_CI_TYPE")[val];
				},editor:{
					type : "combobox",
					options: {
						data : FW.parseEnumData("CMDB_CI_TYPE",_enum)
					}
				}},
				{field:'name',title:'名称',width:250,sortable: true,formatter:function(val, row, index ){
					return row.upCiName + "-" + FW.getEnumMap("CMDB_CI_TYPE")[row.upCiType] 
					+ "-" + row.downCiName + "-" + FW.getEnumMap("CMDB_CI_TYPE")[row.downCiType];
				}}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	                if( modelFlag ){
	                	$("#noResult").show();
	                }else{
	                	$("#grid1_wrap").hide();
		                $("#grid1_empty,#toolbar_wrap").show();
		                $("#noResult").hide();
	                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	            
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}/asset/cmdbCi/updateCmdbRelationMenu.do?id=" + rowData.id + "&ciId=" + ciId ;
		    	addTabWithTreeDataGrid( "updateCmdbRelation"+rowData.id, "关联关系", url, "downUpDownCmdbRelation" + ciId, "contentTb" );
		    }

	    });
		
		//添加上联
		$("#upRelationBtn").click(function(){
			var url ="${basePath}asset/cmdbCi/insertCmdbRelationMenu.do?type=up&ciId=" + ciId;
			addTabWithTreeDataGrid( "addUpCmdbRelation", "上联关系", url,"downUpDownCmdbRelation" + ciId, "contentTb");
		});
		
		//添加下联
		$("#downRelationBtn").click(function(){
			var url ="${basePath}asset/cmdbCi/insertCmdbRelationMenu.do?type=down&ciId=" + ciId;
			addTabWithTreeDataGrid( "addDownCmdbRelation", "下联关系", url,"downUpDownCmdbRelation" + ciId, "contentTb");
		});
		
		//打开示意图
		$("#showRelationBtn").click(function(){
			var url ="${basePath}page/asset/itc/cmdb/CmdbCi-showCmdbRelationImg.jsp";
			addTabWithTreeDataGrid( "showRelationBtn", "示意图-关联关系", url,"downUpDownCmdbRelation" + ciId, "contentTb");
		});
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			modelFlag = true;
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				},noSearchColumns:{7:true}});
			}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="upRelationBtnDiv">
	           <button type="button" class="btn btn-default" id="upRelationBtn">添加上联</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="downRelationBtnDiv">
	           <button type="button" class="btn btn-default" id="downRelationBtn">添加下联</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="showRelationBtnDiv">
	           <button type="button" class="btn btn-default" id="showRelationBtn">示意图</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="btn_advlocalDiv">
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">查询</button>
	    	</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	
	<div id="noResult" style="display:none;width:100%;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px"> 没有找到符合条件的结果</div>
			    
			</div>
		</div>
	</div>
	
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有关联关系信息</div>
			</div>
		</div>
	</div>
</body>
</html>