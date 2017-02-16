<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html  style="height: 99%;">
<head>
<title>历史票列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var ptwFilterType = <%=request.getParameter("ptwFilterType")%> ;
</script>

<script>
	var dataGrid = null;
	var isSearchMode = false;
	var isSearchLineShow = false;

	$(document).ready(function() {
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "ptw/ptwInfo/queryHisPtwList.do",	//basePath为全局变量，自动获取的
	        queryParams : {
			    ptwFilterType : ptwFilterType
			},
	        "columns":[[
				{field:'id',title:'id',width:20,fixed:true,hidden:true},
				{field:'wtNo',title:'编号',width:135,fixed:true, sortable: true},
				{field:'wtTypeName',title:'类型',width:80,fixed:true, sortable: true},
				{field:'eqName',title:'设备名称',width:150,fixed:true,sortable: true},
				{field:'workContent',title:'工作内容',width:150, sortable: true}
			]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap,#bottomPager").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	        	 if(data && data.total==0){
	            	 if( isSearchMode ){
		                	$("#noResult").show();
		                }else{
			            	$("#grid1_wrap,#toolbar_wrap").hide();
			                $("#grid1_empty").show();
			                $("#noResult").hide();
		                }
	            }else{
	            	$("#grid1_wrap,#toolbar_wrap").show();
	                $("#grid1_empty").hide();
	                $("#noResult").hide();
	            }
	           
	        },
	        onDblClickRow : function( rowIndex, rowData ){
	        	 var ptwId = rowData.id ;
				 var tabId = FW.getCurrentTabId();
				 var p = _parent().window.document.getElementById(tabId).contentWindow;
				 p.initDataFromPtw(ptwId);
				 _parent().$("#itcDlg").dialog("close");

		    }

	    });
		$("#btn_search").click(function(){
		    if(isSearchLineShow){
		    	isSearchLineShow=false;
		        $("#contentTb").iDatagrid("endSearch");
		    }else{
		    	isSearchLineShow=true;
		       	$("#contentTb").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(arg){
		       		isSearchMode = true;
					return {"search":JSON.stringify(arg)};
				}});
		    }
		});
	});
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_search" data-toggle="button" class="btn btn-default">查询</button>
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
			    <div style="font-size:14px">没有相关的历史票信息</div>
			</div>
		</div>
	</div>
</body>
</html>