<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>点检日志列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/patrolBtnSecurity.js?ver=${iVersion}'></script>

<script>

	var keywords = "";
	$(document).ready(function() {
		//权限
		setPatrolPriv();
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "operation/patrol/queryAllPatrolList.do",	//basePath为全局变量，自动获取的       
	        queryParams : {
	        	"search" : function(){
	        		return keywords;
	        	}
	        },
	        //add by yangk 点检情况换成富文本
	        loadFilter: function(data){
		 		var rows = data.rows;
				for(var i=0; i<rows.length; i++){
					rows[i].patrolSituation = FW.unspecialchars(rows[i].patrolSituation.replace(/<.*?>/g, ""));
		   		}
		    	return data;
		 	},
	        "columns":[[
				{field:'patrolId',title:'ID',fixed:true,hidden:true},
				{field:'checkDate',title:'点检日期',width:120,fixed:true,
					formatter:function(val){
						return FW.long2date(val);
					}	
				},
				{field:'speciality',title:'专业',width:120,fixed:true,
					formatter:function(val){
						return FW.getEnumMap("OPR_SPECIALITY")[val];
					}
				},
				{field:'patrolSituation',title:'点检情况',width:20},
				{field:'createUserName',title:'填写人',width:60,fixed:true},
				{field:'approveUserName',title:'审批人',width:60,fixed:true},
				{field:'status',title:'状态',width:120,fixed:true,
					formatter:function(val){
						return FW.getEnumMap("OPR_PATROL_STATUS")[val];
					}
				}
			]],
			onLoadError: function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap").hide();
			   	$("#grid1_empty").hide();
			    $("#grid1_error").show();
			},
	        onLoadSuccess: function(data){
	            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
	            if(data && data.total==0){
	            	$("#grid1_wrap").hide();
	                $("#grid1_empty").show();
			    	$("#grid1_error").hide();
	            }else{
	            	$("#grid1_wrap").show();
	                $("#grid1_empty").hide();
	                $("#grid1_error").hide();
	            }
	        },
	        onDblClickRow : function( rowIndex, rowData ){
		    	var url = "${basePath}operation/patrol/updatePatrolPage.do?patrolId=" + rowData.patrolId;
		    	addTabWithTree( "patrolDetail"+rowData.patrolId, "点检日志", url, "opmm", "contentTb" );
		    }
	    });
		
		//新建日志按钮
		$("#patrol_btn_add").click(function(){
			var url ="${basePath}operation/patrol/insertPatrolPage.do";
			addTabWithTree( "addPatrol", "点检日志", url, "opmm", "contentTb");
		});
		
		
		//所有条件高级搜索
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
		
		//部分条件搜索
		$("#search").iInput("init");
		$("#search").keypress(function(e) {
		    if(e.which == 13) {
		    	search();
		    }
		});
		$("#search").next(".itcui_btn_mag").click(function(){
			search();
		});
	});
	
	//搜索
	function search(){
		keywords = $("#search").val();
		delete(_itc_grids["contentTb"]);
		var pager = $("#contentTb").datagrid("getPager"); 
		pager.pagination("select",1);
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success priv" privilege="OPR_PATROL_ADD" id="patrol_btn_add" style="display: none;">新建</button>
	        </div>
	       	<div class="input-group input-group-sm bbox" style="width: 150px;float: left;margin-left: 8px;">
				<input type="text" placeholder="搜索填写人或点检情况" id="search" icon="itcui_btn_mag"/>     
			</div>	         
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="contentTb" pager="#pagination_1"  class="eu-datagrid">
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
	    没有点检日志数据
	</div>
</body>
</html>