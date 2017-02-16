<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>登塔申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<script>
</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var siteId = ItcMvcService.getUser().siteId;
var isSearchMode = false;
var headSearchParams = {};
var workflowId = "";

function initDataGrid(){
	dataGrid = $("#towerApply_table").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量
        singleSelect:true,
        url: basePath + "workorder/towerApply/towerApplyListdata.do",	//basePath为全局变量，自动获取的       
        columns:[[ 
				{field:"applyCode",title:"申请单号",width:140,fixed:true,sortable:true},
				{field:"applyCompany",title:"单位名称",width:280,sortable:true}, 
				{field:"towerPeople",title:"登塔人员",width:280}, 
				{field:"cabinRoof",title:"出机舱顶人员",width:280}, 
				{field:"applicant",title:"申请人",width:100,sortable:true}, 
				{field:"applyTime",title:"申请时间",width:150,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.long2time(value);
					}
				},  
				{field:"currHandlerName",title:"当前办理人",width:220,sortable:true}, 
				{field:"applyStatus",title:"状态",width:120,fixed:true,sortable:true,
					formatter: function(value,row,index){
						return FW.getEnumMap("WO_TOWERAPPLY_STATUS")[value]; 
					},
					"editor" : {
				        "type":"combobox",
				        "options" : {
				        	"data" : FW.parseEnumData("WO_TOWERAPPLY_STATUS",_enum)						        	
				        }
				    }} 
				
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
        	toTowerApplyInfoPage(rowData);
		}
    });
}
	
$(document).ready(function() {
		setTowerapplyButtonPriv();
		initDataGrid();  //初始化列表
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#towerApply_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#towerApply_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
	
		
});

//登塔申请权限
	function setTowerapplyButtonPriv(){
		Priv.apply();
		FW.fixRoundButtons("#toolbar1");	
	}



function newTowerApply(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var urlPath = basePath+ "workorder/towerApply/openNewTowerApplyPage.do";
    var opts = {
        id : "newTowerApply" + rand,
        name : "新建登塔申请",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
function  toTowerApplyInfoPage(rowData){
	var towerApplyId = rowData.id;
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = rowData.workapplyCode;
    var opts = {
        id : "towerApplyInfo" + rand,
        name : "登塔申请详情",
        url : basePath+ "workorder/towerApply/openTowerApplyInfoPage.do?id="+towerApplyId ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');if(!FW.get('ptwDoNotClose')){FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();}FW.set('ptwDoNotClose',false);"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}

function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	delete(_itc_grids["towerApply_table"]);
    var pager = $("#towerApply_table").datagrid("getPager"); 
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
					<button id="btn_newTowerApply" type="button"  class="btn btn-success priv" privilege="TOWERAPPLY_NEW" onclick="newTowerApply()">新建</button>
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
		    <table id="towerApply_table" pager="#pagination_1" class="eu-datagrid">
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
			    <div style="font-size:14px">没有登塔申请信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button id="btn_newTowerApply2" type="button" class="btn btn-success" onclick="newTowerApply();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>