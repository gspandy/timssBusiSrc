<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>开工申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workorder/common/workOrder.js?ver=${iVersion}"></script>
<script>
	var siteId = ItcMvcService.getUser().siteId;
	var isSearchMode = false;
	var headSearchParams = {};
	var workflowId = "";

	function initDataGrid(){
		dataGrid = $("#workapply_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        url: basePath + "workorder/workapply/workapplyListdata.do",	//basePath为全局变量，自动获取的       
	        queryParams:{
	        	search : function(){
	        		//console.log("datagrid查询时" +JSON.stringify(headSearchParams));
	        		return JSON.stringify(headSearchParams);
	        	}
	        },
	        columns:[[ 
					{field:"workapplyCode",title:"申请单号",width:140,fixed:true,sortable:true},
					{field:"name",title:"工程名称",width:280}, 
					{field:"workCom",title:"施工单位",width:180}, 
					{field:"startTime",title:"开工日期",width:120,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.long2date(value);
						}
					}, 
					{field:"endTime",title:"计划竣工日期",width:120,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.long2date(value);
						}
					},  
					{field:"applicantName",title:"申请人",width:80,sortable:true}, 
					
					{field:"currHandlerName",title:"当前办理人",width:130,sortable:true}, 
					{field:"applyStatus",title:"状态",width:100,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("WO_WOAPPLY_STATUS")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						        	"data" : FW.parseEnumData("WO_WOAPPLY_STATUS",_enum)						        	
						        }
						    }
					} 
					
				]],
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
	        	toWorkapplyInfoPage(rowData);
			}
	    });
	}
	
$(document).ready(function() {
		setWoapplyButtonPriv();
		initDataGrid();  //初始化列表
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		    	headSearchParams = {};
		        $("#workapply_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#workapply_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
		        	headSearchParams = args;
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
	
		
});

	//开工申请权限
	function setWoapplyButtonPriv(){
		Priv.apply();
		FW.fixRoundButtons("#toolbar");	
	}


function newWorkapply(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var urlPath = basePath+ "workorder/workapply/openNewWoapplyPage.do";
   var opts = {
        id : "newWoapply" + rand,
        name : "新建开工申请",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
function  toWorkapplyInfoPage(rowData){
	var woapplyId = rowData.id;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = rowData.workapplyCode;
   var opts = {
        id : "woPlanInfo" + rand,
        name : "工单申请详情",
        url : basePath+ "workorder/workapply/openNewWoapplyPage.do?woapplyId="+woapplyId ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');if(!FW.get('ptwDoNotClose')){FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();}FW.set('ptwDoNotClose',false);"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}

function refresh(){
	//window.location.href=window.location.href;
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
//	$("#workapply_table").datagrid("reload").datagrid("resize");
	delete(_itc_grids["workapply_table"]);
    var pager = $("#workapply_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
function refreshAfterClose(){
	if(FW.get("eqWOlistDoNotRefresh")){
		FW.set("eqWOlistDoNotRefresh",false);
		return;
	}
	refresh();
}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button type="button" id="btn_newWoapply"  class="btn btn-success priv" privilege="WOAPPLY_NEW" onclick="newWorkapply()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="workapply_table" pager="#pagination_1" class="eu-datagrid">
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
			    <div style="font-size:14px">没有工单申请信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" id="btn_newWoapply2" class="btn btn-success" onclick="newWorkapply();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>