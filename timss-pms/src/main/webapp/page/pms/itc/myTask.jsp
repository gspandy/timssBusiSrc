<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
 	String path = request.getContextPath();
 	
%>

<title>工作票列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>

	$(document).ready(function() {
    var userId; 
    TimssService.getUserInfo(function(Object){
		userId = Object.userId;
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: basePath + "workflow/process_inst/getTodos.do?userId="+userId,	//basePath为全局变量，自动获取的       
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
	        onDblClickRow:function(rowIndex, rowData){
	        	openAuditTab(rowData.taskId,rowData.processInstId);
	        },
	        columns:[[		
			 			/* {field:'ck',checkbox:true}, */
			 			{
			 				field:'taskName',title:'任务名称',width:50,align:'left'
			 			},
			 			{
			 				field:'taskDefKey',title:'任务key',width:50,align:'left'
	
			 			},
			 			{
			 				field:'owner',title:'owner',width:50,align:'left'
			 			}
			 			,
			 			{
			 				field:'taskId',title:'taskId',width:50,align:'left'
			 			}
			 			,
			 			{
			 				field:'processInstId',title:'processInstId',width:50,align:'left'
			 			}
			 		
			 		]]

	    });
	});
		
	});
	
function openAuditTab(taskId,processInstId){
	var date=new Date();
	var opts = {
	        id : "pmsEditCheckOutTab"+date,
	        name : "查看验收",
	        //这里最好不要直接访问jsp页面，以后分支版本会很麻烦
	        url : basePath+ "page/pms/itc/project/approveProject.do?taskId="+taskId+"&processInstId="+processInstId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : function(id){
	                FW.deleteTab(id),
	                //nav3是API选项卡的标识符
	                FW.activeTabById("pms");
			    }
	        }
	    };
	    _parent()._ITC.addTabWithTree(opts); 
}
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="refresh();">刷新</button>
	        </div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="test_grid1" pager="#pagination_1">
	        
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
	    没有xxxxxxx数据，单击这里创建新的....
	</div>
</body>
</html>