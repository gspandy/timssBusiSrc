<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>维护计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workorder/common/workOrder.js?ver=${iVersion}"></script>
<script>
	var siteId = ItcMvcService.getUser().siteId;
	var isSearchMode = false;
	var selectEquipId = "";
	var selectEquipName="";
	var selectEquipCode = "";
	var headSearchParams = {};

	function initDataGrid(){
		dataGrid = $("#woQx_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        url: basePath + "workorder/woQx/woQxListdata.do",	//basePath为全局变量，自动获取的       
	        columns:[[ 
					{field:"defectCode",title:"缺陷编号",width:110,fixed:true,sortable:true},
					{field:"monthCode",title:"月度编号",width:70,fixed:true,sortable:true},
					{field:"equipName",title:"设备名称",width:110}, 
					{field:"defectDes",title:"缺陷情况",width:110},
					{field:"defectTime",title:"缺陷时间",width:110,fixed:true,
						formatter: function(value,row,index){
							return FW.long2time(value);
						}
					},   
					{field:"onDutyUserName",title:"值班人员",width:70,fixed:true},
					{field:"runningUserName",title:"运行人员",width:70,fixed:true},
					{field:"instructionsUserName",title:"批示领导",width:70,fixed:true} 
					
				]],
	       onLoadSuccess: function(data){
	         	if(data && data.total==0){
	            		$("#mainContent").hide();
	            		$("#noSearchResult").hide()
	                	$("#grid1_empty").show();
	            }else{
	            	$("#mainContent").show();
	            	$("#noSearchResult").hide()
	                $("#grid1_empty").hide();
	            }
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	woQxInfoPage(rowData);
	        }
	    })
	}
	
$(document).ready(function() {
		FW.createAssetTree({multiSelect:false,forbidEdit:true}); //左边设备树
		initDataGrid();  //初始化列表
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#woQx_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#woQx_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
});

function queryByTree(){
	headSearchParams.selectTreeId = selectEquipId;
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["woQx_table"]);
    var pager = $("#woQx_table").datagrid("getPager"); 
    pager.pagination("select",1);
    
}
	
/**左边树菜单选择触发事件*/
function passAssetSelect(data){
	
	headSearchParams = {};
    $("#woQx_table").iDatagrid("endSearch");
	selectEquipId = data.id;
	selectEquipName = data.text;
	selectEquipCode = data.assetCode;
	$("#woQx_table").datagrid('options').queryParams.search = function(){
       		return JSON.stringify(headSearchParams);
      	};
    	queryByTree();
}

function newQx(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var equipParams = JSON.stringify({"equipId":selectEquipId,"equipName":selectEquipName,"equipCode":selectEquipCode});
    var urlPath = basePath+ "workorder/woQx/newQxPage.do";
   var opts = {
        id : "newQx" + rand,
        name : "新建缺陷",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
function  woQxInfoPage(rowData){
	var woQxId = rowData.id;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = rowData.defectCode;
   var opts = {
        id : "newQx" + rand,
        name : "缺陷详情",
        url : basePath+ "workorder/woQx/newQxPage.do?woQxId="+woQxId,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}

function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	delete(_itc_grids["woQx_table"]);
    var pager = $("#woQx_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
function refreshAfterClose(){
	if(FW.get("eqwoQxlistDoNotRefresh")){
		FW.set("eqwoQxlistDoNotRefresh",false);
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
					<button type="button" class="btn btn-success" onclick="newQx()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
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
		    <table id="woQx_table" pager="#pagination_1" class="eu-datagrid">
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
			    <div style="font-size:14px">没有缺陷记录</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newQx();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>