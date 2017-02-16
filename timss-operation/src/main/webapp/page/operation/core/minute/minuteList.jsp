<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>生产碰头会列表</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script>
var keywords='';
var isSearchMode = false;
function initDataGrid(){
	dataGrid = $("#minute_table").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量
        singleSelect:true,
        url: basePath + "operation/minute/minuteList.do",	//basePath为全局变量，自动获取的       
        queryParams:{
        	"search" : function(){
        		return keywords;
        	}
        },
        columns:[[ 
					{field:'issueDate',title:'会议日期',width:120,fixed:true,sortable:true,
						formatter:function(val){
							return FW.long2date(val);
						}	
					},
					{field:'presider',title:'主持人',width:120,fixed:true},
					{field:'minute',title:'摘要',width:20,
						formatter:function(val){
							return val.replaceAll("BRBR"," ").substring(0,50);
						}
					}
				]],
		onLoadError: function(){
		    $("#grid1_error").show();
		    $("#grid1_wrap").hide();
		   	$("#grid1_empty").hide();
		   	$("#bottomPager").hide();
		    $("#noSearchResult").hide();
		},

       onLoadSuccess: function(data){
        	if(isSearchMode){
		        //搜索时的无数据信息
		        if(data && data.total==0){
		            $("#noSearchResult").show();
		        }
		        else{
		            $("#noSearchResult").hide();
		        }
		    } 
		    else{
		    	$("#noSearchResult").hide();
		    	 //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
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
        	var url = basePath + "operation/minute/minuteForm.do?type=view&id=" + rowData.id;
	    	addTabWithTree( "minuteDetail"+rowData.id, "生产碰头会详情", url, "opmm", "minute_table" );
		}
    });
}
	
$(document).ready(function() {
		//权限
		Priv.map("privMapping.minute_new","minute_new");
		Priv.apply();
		initDataGrid();  //初始化列表
		$("#search").ITCUI_Input();
		$("#search").keypress(function(e) {
		    if(e.which == 13) {
		        search();
		    }
		});
		$("#search").next(".itcui_btn_mag").click(function(){
			search();
		});	

});

function search(){
	keywords = $("#search").val();
	isSearchMode = true;
	initDataGrid();
}


function newMinute(){
	var url = "${basePath}page/operation/core/minute/meetingMinute.jsp?type=create";
	addTabWithTree( "addMinute", "新建生产碰头会", url, "opmm", "minute_table");
}

function refresh(){
	keywords='';
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	delete(_itc_grids["minute_table"]);
    var pager = $("#minute_table").datagrid("getPager"); 
    pager.pagination("select",1);
}


</script>
</head>
<body style="height: 100%;" class="bbox list-page">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button id="minute_btn_add" type="button"  class="btn btn-success priv" privilege="minute_new" onclick="newMinute();">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button id="btn_refresh" type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
		        <div class="input-group input-group-sm" style="width:150px;float:left;margin-left:7px;margin-top:1px">
			        <input type="text" id="search" icon="itcui_btn_mag" placeholder="搜索会议内容" style="width:150px"/>     
			    </div>
	  
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="minute_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
		</div>
		
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px">
		</div>
	</div>
	
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
			没有找到符合条件的结果
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有生产碰头会数据</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success  priv" privilege="minute_new" onclick="newMinute();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	<!-- 错误信息-->
	<div class="row" id="grid1_error" style="display:none">
	    无法从服务器获取数据，请检查网络是否正常
	</div>
	﻿
</body>
</html>