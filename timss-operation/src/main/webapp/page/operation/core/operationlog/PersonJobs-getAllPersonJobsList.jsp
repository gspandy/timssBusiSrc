<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title></title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<%-- <script type="text/javascript" src="<%=basePath %><%=resBase %>js/common/addTab.js?ver=${iVersion}"></script> --%>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>

<script>

	$(document).ready(function() {
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "operation/personJobs/queryAllPersonJobsList.do",	//basePath为全局变量，自动获取的       
	        "columns":[[
				{field:'stationId',title:'工种编码',width:180,fixed:true,hidden: true},
				{field:'stationName',title:'工种',width:180,fixed:true},
				{field:'dutyId',title:'值别ID',width:180,fixed:true,hidden: true},
				{field:'dutyName',title:'值别',width:180,fixed:true},
				{field:'jobsId',title:'岗位Id',width:20,hidden: true},
				{field:'jobsName',title:'岗位',width:20/* ,hidden: true */}
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
	        	$.ajax({
					url : "${basePath}operation/personJobs/prepareToQueryOrgsRelatedToUsers.do",
					type : 'post',
					dataType : "json",
					data :{
						"formData" : JSON.stringify(rowData) 
					},
					success : function(data) {
						if( data.result == "success" ){
							var url = "${basePath}operation/personJobs/queryOrgsRelatedToUsers.do";
		    				addTabWithTree( "personJobs"+rowData.dutyId, "人员值别详情"+rowData.dutyId, url,"opmm", "contentTb" );
						}else{
							FW.error( "操作失败 ！");
						}
					}
				}); 
		    	
		    }

	    });
		
		$("#btn_add").click(function(){
			var url ="${basePath}page/operation/core/operationlog/PersonJobs-insertPersonJobs.jsp";
			addTabWithTree( "addPersonJobs", "新建人员值别", url );
		});
		
		
		//高级搜索
		$("#btn_advlocal").click(function() {
			if($(this).hasClass("active")){
				$("#contentTb").ITCUI_GridSearch("end");
			}
			else{
				$("#contentTb").ITCUI_GridSearch("init",{"remoteSearch":true,"onParseArgs":function(arg){
					return {"search":JSON.stringify(arg)};
				}});
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
	           <button type="button" class="btn btn-success" id="btn_add">新建</button>
           </div>
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
	    没有人员管理数据
	</div>
</body>
</html>