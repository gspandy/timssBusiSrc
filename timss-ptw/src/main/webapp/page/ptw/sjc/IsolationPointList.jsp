<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>钥匙箱管理列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>
<script>
	var selectEquipId = "";
	
	$(document).ready(function() {
		initStdPointTree();
		
		dataGrid = $("#isolationPoint_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "ptw/ptwIsolationPoint/isolationPointListData.do",	   
	        singleSelect:true,
	        columns:[[ 
					{field:"assetName",title:"名称",width:200,fixed:true,sortable:true},
					{field:"cuModel",title:"型号",width:100,fixed:true,sortable:true},
					{field:"manufacturer",title:"制造商",width:100,sortable:true,fixed:true},
					{field:"installDate",title:"安装日期",width:90,fixed:true,sortable:true,formatter: function(value,row,index){
						return value==null||value==""?"":new Date(value).format("yyyy-MM-dd");
					}},
					{field:'empty',title:'',width:100}
				]],
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	           if(data && data.total==0){
	                $("#mainContent").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#mainContent").show();
	                $("#grid1_empty").hide();
	            } 
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	openIsolationPointPage(rowData.assetId);
			}
		});
		
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#isolationPoint_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#isolationPoint_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
		
	});
	
	/**左边树菜单选择触发事件*/
	function onTreeItemClick(data){
		headSearchParams = {};
	    $("#isolationPoint_table").iDatagrid("endSearch");
		selectEquipId = data.id;
		$("#isolationPoint_table").datagrid('options').queryParams.search = function(){
        		return JSON.stringify(headSearchParams);
       	};
		if(data.type == "root"){
	    	refresh();
	    }else{
	    	openIsolationPointPage(data.id);
	    }
	}
	
	function openIsolationPointPage(assetId){
		var isolationPointId = assetId;
		window.location.href = basePath+ "ptw/ptwIsolationPoint/openPtwIsolationPointPage.do?id="+isolationPointId;
	}
	
	function refresh(){
		headSearchParams = {};
		$("#mainContent").show();
		$("#grid1_empty").hide();
		$("#isolationPoint_table").datagrid("reload");
	}
	/**
	 * 关闭当前tab 页
	 */
	function closeCurPage(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">高级搜索</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="isolationPoint_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到“隔离点”信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success" onclick="refresh()">刷新</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>