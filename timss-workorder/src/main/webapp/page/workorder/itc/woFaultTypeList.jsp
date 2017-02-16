<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>工单类型列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	var ftRootId = "";  //根ID
	var selectFTypeId = "";  //选中的服务目录ID
	var selectFTypeName = "";  //选中的服务目录名
	var selectFTypeParentId = "";  //选中的服务目录的父ID
	$(document).ready(function() {
		//列表初始化 
		dataGrid = $("#faultType_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "workorder/woParamsConf/faultTypeListData.do",	     
	        singleSelect:true,
	        columns:[[ 
					{field:"faultTypeCode",title:"编码",width:100,fixed:true,sortable:true}, 
					{field:"name",title:"名字",width:150,fixed:true}, 
					{field:"defaultScore",title:"排序码",width:200},
					{field:"keywords",title:"关键字",width:150}, 
				]],
	        onLoadSuccess: function(data){
	        	 ftRootId = data.rootId;
	        	 if(isSearchMode){
			        //搜索时的无数据信息
			        if(data && data.total==0){
			            $("#noSearchResult").show();
			        }
			        else{
			            $("#noSearchResult").hide();
			        }
			    }else{
			   		$("#noSearchResult").hide();
			   		if(data && data.total==0){
		                $("#mainContent").hide();
		                $("#grid1_empty").show();
		            }else{
		            	$("#mainContent").show();
		                $("#grid1_empty").hide();
		            }
			   	
			   	}
	            isSearchMode = false;
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	openFaultTypeInfo(rowData);
			}
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#faultType_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#faultType_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    isSearchMode = true;
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
	});
	
	
	function queryByTree(){
		headSearchParams.id = selectFTypeId;
	    delete(_itc_grids["faultType_table"]);
	    var pager = $("#faultType_table").datagrid("getPager"); 
	    pager.pagination("select",1);
	}
		
	/**左边故障类型树选择触发事件*/
	function triggerFaultTypeTreeSelect(data){
		headSearchParams = {};
	    $("#faultType_table").iDatagrid("endSearch");
		selectFTypeId = data.id;
		selectFTypeName = data.text;
		selectFTypeCode = data.faultTypeCode;
		selectFTypeParentId = data.parentId;
		$("#faultType_table").datagrid('options').queryParams.faultTypeId = function(){
	       		//return JSON.stringify(headSearchParams);
	       		return selectFTypeId;
	      	};
		if(data.type == "root"){
	    	refresh();
	    }else{
	    	queryByTree();
	    }
	}
	

	/**打开故障类型详情*/
	function openFaultTypeInfo(rowData){
		var faultTypeId = rowData.id;
	   var rand = rowData.faultTypeCode;
	   var opts = {
	        id : "openWoFaultType"+rand,
	        name : "工单类型详情",
	        url : basePath+ "workorder/woParamsConf/openFaultTypePage.do?ftRootId="+ftRootId+"&faultTypeId="+faultTypeId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 
	}
	/** 新建一个故障类型 */
	function newFaultType(){
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "newWoFaultType"+rand,
	        name : "新建工单类型",
	        url : basePath+ "workorder/woParamsConf/openFaultTypePage.do?ftRootId="+ftRootId+"&selectFTypeId="+
	        				selectFTypeId+"&selectFTypeName="+selectFTypeName+"&selectFTypeParentId="+selectFTypeParentId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 

	}
	
	 
function refresh(){
	window.location.href=window.location.href;
}
/**
 * 关闭当前tab 页
 */
function closeCurPage(){
	FW.deleteTabById(FW.getCurrentTabId());
}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="btn_jp_new" type="button" class="btn btn-success" onclick="newFaultType()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="faultType_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有服务目录信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newFaultType();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>