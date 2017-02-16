<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>工单标识列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>

	$(document).ready(function() {
		
		dataGrid = $("#woLabel_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "workorder/woParamsConf/woLabelListData.do",	     
	        singleSelect:true,
	        columns:[[ 
					{field:"labelCode",title:"编号",width:140,fixed:true,sortable:true}, 
					{field:"name",title:"标识",width:80,fixed:true},   
					{field:"weight",title:"权重",width:80,fixed:true},   
					{field:"remarks",title:"说明",width:150}
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
	        	openWoLabel(rowData);
			}
		});
		
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#woLabel_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#woLabel_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
	});
	/**打开工单标识详情*/
	function openWoLabel(rowData){
	var woLabelId = rowData.id;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = rowData.labelCode;
	   var opts = {
	        id : "openWoLabel"+rand,
	        name : "工单标识详情",
	        url : basePath+ "workorder/woParamsConf/openWoLabelPage.do?woLabelId="+woLabelId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refresh();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 
	}
	/** 新建一个工单类型 */
	function newWoLabel(){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var opts = {
	        id : "newWoLabel"+rand,
	        name : "新建标识",
	        url : basePath+ "workorder/woParamsConf/openWoLabelPage.do",
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
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="btn_jp_new" type="button" class="btn btn-success" onclick="newWoLabel()">新建</button>
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
		    <table id="woLabel_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">未查询到“工单标识”信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newWoLabel();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>