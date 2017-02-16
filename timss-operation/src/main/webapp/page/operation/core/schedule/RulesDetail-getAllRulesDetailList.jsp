<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>排班规则列表</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<%-- <script type="text/javascript" src="<%=basePath %><%=resBase %>js/common/addTab.js?ver=${iVersion}"></script> --%>
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/map.js?ver=${iVersion}'></script> 	

<script>
//岗位Json 需要引入map.js
var stationMap = new Map();

//岗位map
function stationJson(){
	var url = basePath + "operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var stationList = data.result;
			for( var index in stationList ){
				stationMap.put( stationList[index].roleId, stationList[index].name );
			}
		}
	});
}

//formatter 单元格
function formatterData( val ){
	return val != null ? stationMap.get( val ) : "" ;
}

//搜索
function search(){
	keywords = $("#search").val();
	delete(_itc_grids["contentTb"]);
	var pager = $("#contentTb").datagrid("getPager"); 
	pager.pagination("select",1);
}

	var keywords = "";
	$(document).ready(function() {
		stationJson();
		
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#contentTb").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        singleSelect :true,
	        url: basePath + "operation/rulesDetail/queryAllRulesDetailList.do",	//basePath为全局变量，自动获取的    
	        queryParams : {
	        	"search" : function(){
	        		return keywords;
	        	}
	        },
	        "columns":[[
						{field:'uuid',title:'UUID', hidden:true},
						{field:'name',title:'排班规则名称',width:120,fixed:true},
						{field:'dutyString',title:'值别',width:380,fixed:true},
						{field:'dutyCount',title:'值别总数',width:65,fixed:true},
						{field:'shiftString',title:'班次',width:380,fixed:true},
						{field:'shiftCount',title:'班次总数',width:65,fixed:true},
						{field:'stationId',title:'工种',width:180,
							formatter: function(val,row){
								return formatterData(val);
							}	
						},{field:'rulesId',title:'行列规则Id', hidden:true}
						]],
			"onLoadError": function(){
				//加载错误的提示 可以根据需要添加
				$("#grid1_wrap").hide();
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
		    	var url = "${basePath}page/operation/core/schedule/RulesDetail-updateRulesDetail.jsp";
		    	FW.set("operationRowData",rowData);
		    	addTabWithTree( "rulesDetail"+rowData.uuid, "排班规则", url,"opmm", "contentTb" );
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
				}});
			}
		});
		
		/* //生成日历
		$("#createRulesDetail").click(function(){
			var rowData = $("#contentTb").datagrid("getSelected");
			//如果没有勾选数据，单选
			if( rowData == null || rowData == "" ){
				FW.error( "请勾选一条排班规则详细 ！" );
				return;
			}
			var pageUrl = "${basePath}page/operation/core/schedule/ScheduleDetail-insertScheduleDetail.jsp";
			FW.set("operationRowData",rowData);
			addTabWithTree("rulesDetail" + rowData.id , "生成排班日历", pageUrl);
		}); */
		
		//新建
		$("#createBtn").click(function(){
			var pageUrl = "${basePath}page/operation/core/schedule/RulesDetail-insertRulesDetail.jsp";
			//var date = new Date().getTime();
			addTabWithTree( "addRules", "排班规则", pageUrl,"opmm", "contentTb");
		});
		
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
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group-sm" style="float: left;margin-top: 2px!important;">
	           <button type="button" class="btn btn-success" id="createBtn">新建</button>
	           <!-- <button type="button" class="btn btn-default" data-toggle="button" id="createRulesDetail">生成排班日历</button>
	           <button type="button" class="btn btn-default" data-toggle="button" id="btn_advlocal">高级搜索</button> -->
	        </div>
	       <div class="input-group input-group-sm bbox" style="width: 150px;float: left;margin-left: 8px;">
				<input type="text" placeholder="排班规则名称" id="search" icon="itcui_btn_mag"/>     
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
	    没有数据
	</div>
</body>
</html>