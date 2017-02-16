<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>运行方式列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>

<script>

	$(document).ready(function() {
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "operation/mode/queryAllModeList.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'deptId',title:'工种Id',width:180,fixed:true,hidden:true},
				{field:'deptName',title:'工种',width:180,fixed:true, sortable: true},
				{field:'jobId',title:'岗位ID',width:180,fixed:true,hidden:true},
				{field:'jobName',title:'岗位',width:180,fixed:true, sortable: true},
				{field:'checkPoint',title:'运行方式检查点',width:180, sortable: true, formatter: function(value,row,index){
					if (value > 0){
						return value + " 个";
					} else {
						return "<span style='color:red;'>未设置</span>";
					}
				}}
				]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap,#toolbar_wrap").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#grid1_wrap").hide();
	                $("#grid1_empty").show();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}operation/mode/insertOrUpdateModePage.do?jobId=" + rowData.jobId;
		    	
		    	addTabWithTree( "ModeDetail"+rowData.jobId, "运行方式设置详情"+rowData.jobId, url,"opmm", "contentTb" );
		    }

	    });
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				},noSearchColumns:{4:true}});
			}
		});
		
	});
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
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
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	<!-- 无数据 -->
	<div class="row" id="grid1_empty" style="display:none">
	    没有工种数据
	</div>
</body>
</html>