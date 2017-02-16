<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>工单列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/workorder/common/workOrder.js?ver=${iVersion}"></script>
<script>
	var siteId = ItcMvcService.getUser().siteId;
	var isSearchMode = false;
	var rowFilterFlag =0 ; //默认不进行 行过滤（前台未选中）
	var unEndFilterFlag =0 ; //默认不进行 行过滤（前台未选中）
	var selectEquipId = "";
	var selectEquipName="";
	var selectEquipCode = "";
	var headSearchParams = {};

	/**
	 * 将后台得到的data数据格式转换为ui组件能够识别的格式
	 * @param data 从后台得到的数据
	 */
	function convertEnumToJSONType(data){
		var result={};
		var res,crr;
		for(var i in data){
			crr=data[i];
	        result[crr['code']]=crr['label'];
		}
		return result;
	}
	
	function initDataGrid(){
		dataGrid = $("#workorder_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        url: basePath + "workorder/workorder/workorderListdata.do",	//basePath为全局变量，自动获取的       
	        queryParams: {rowFilterFlag:function(){return rowFilterFlag;},unEndFilterFlag:function(){return unEndFilterFlag;}},
	        columns:[[ 
					{field:"workOrderCode",title:"编号",width:110,fixed:true,sortable:true},
					{field:"woSpecCode",title:"专业",width:85,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("WO_SPEC")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						        	"data" : FW.parseEnumData("WO_SPEC",_enum)						        	
						        }
						    }
					},   
					{field:"description",title:"名称",width:280}, 
					{field:"createuser",title:"报障人",width:70,fixed:true},
					{field:"createdate",title:"报障时间",width:130,fixed:true,sortable:true,
						formatter: function(value,row,index){
							//时间转date的string，还有long2date(value)方法
							return FW.long2time(value);
						}
					},   
					{field:"urgentDegreeCode",title:"紧急程度",width:60,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("WO_URGENCY_DEGREE")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						        	"data" : FW.parseEnumData("WO_URGENCY_DEGREE",_enum)
						        }
						    }
					},
					{field:"currHandlerUser",title:"办理人",width:70,fixed:true,sortable:true}, 
					{field:"currStatus",title:"状态",width:130,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("WO_STATUS")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("WO_STATUS",_enum)	
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
		                if(rowFilterFlag != 0 ||unEndFilterFlag != 0){
		            		$("#noSearchResult").show();
		            	}else{
		            		$("#mainContent").hide();
		                	$("#grid1_empty").show();
		            	}
		            }else{
		            	$("#mainContent").show();
		                $("#grid1_empty").hide();
		            }
			    }
			    isSearchMode = false;
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	switch(rowData.currStatus){
	        		case "draft":;
	        		case "newWo":;
	        		case "monitorAudit":;
	        		case "assistantAudit":;
	        		case "plantLeaderAudit":;
	        		case "assistantToTeamleader":toWoBaseInfoPage(rowData);break;
	        		case "woPlan":;
	        		case "inPTWing":;
	        		case "endWOReport":;
	        		case "woAcceptance":;
	        		case "woFiling":;
	        		case "woObsolete":toWoPlanPage(rowData);break;
	        	}
			}
	    });
	}
	
$(document).ready(function() {
		FW.createAssetTree({multiSelect:false,forbidEdit:true}); //左边设备树
		//initPage();
		initDataGrid();  //初始化列表
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#workorder_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#workorder_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
	
		$("#set_showAllWo,#set_showUnEndWo").iCheck({
		    checkboxClass: "icheckbox_flat-blue",
		    radioClass: "iradio_flat-blue",
		});
		$("#set_showAllWo").on("ifChanged", function(event){
			$("#set_showAllWo").on("ifChecked", function(event){
			  	rowFilterFlag = 1;
			});
			$("#set_showAllWo").on("ifUnchecked", function(event){
			  	rowFilterFlag = 0;
			});
			refresh();
		});
		$("#set_showUnEndWo").on("ifChanged", function(event){
			$("#set_showUnEndWo").on("ifChecked", function(event){
			  	unEndFilterFlag = 1;
			});
			$("#set_showUnEndWo").on("ifUnchecked", function(event){
			  	unEndFilterFlag = 0;
			});
			refresh();
		});
});

function queryByTree(){
	headSearchParams.selectTreeId = selectEquipId;
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["workorder_table"]);
    var pager = $("#workorder_table").datagrid("getPager"); 
    pager.pagination("select",1);
    
}
	
/**左边树菜单选择触发事件*/
function passAssetSelect(data){
	
	headSearchParams = {};
    $("#workorder_table").iDatagrid("endSearch");
	selectEquipId = data.id;
	selectEquipName = data.text;
	selectEquipCode = data.assetCode;
	$("#workorder_table").datagrid('options').queryParams.search = function(){
       		return JSON.stringify(headSearchParams);
      	};
  /*   if(data.type == "root"){
    	refresh();
    }else{ */
    	queryByTree();
   /*  } */
}

function newWO(){
	//FW.setTreeStat("fold");
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var equipParams = JSON.stringify({"equipId":selectEquipId,"equipName":selectEquipName,"equipCode":selectEquipCode});
    var urlPath = basePath+ "workorder/workorder/openNewWOPage.do?woStatus=newWO&equipParams="+encodeURIComponent(equipParams);
   var opts = {
        id : "newWO" + rand,
        name : "工单",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
function  toWoPlanPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = rowData.workOrderCode;
   var opts = {
        id : "woPlanInfo" + rand,
        name : "工单",
        url : basePath+ "workorder/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg');if(!FW.get('ptwDoNotClose')){FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();}FW.set('ptwDoNotClose',false);"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
function toWoBaseInfoPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
   var rand = rowData.workOrderCode;
   var opts = {
        id : "woInfo" + rand,
        name : "工单",
        url : basePath+ "workorder/workorder/openNewWOPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
function refresh(){
	//window.location.href=window.location.href;
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
//	$("#workorder_table").datagrid("reload").datagrid("resize");
	delete(_itc_grids["workorder_table"]);
    var pager = $("#workorder_table").datagrid("getPager"); 
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
					<button type="button" class="btn btn-success" onclick="newWO()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		       		<!-- <button type="button" id="btn_test" data-toggle="button" class="btn btn-default">测试</button> -->
		        </div>
		        <div class="btn-group btn-group-sm">
		        	<input type="checkbox" id="set_showAllWo"></input>
					<label for="set_default" class="pure-label">仅显示与我相关的工单</label>
					<input type="checkbox" id="set_showUnEndWo"></input>
					<label for="set_default" class="pure-label">仅显示处理中的工单</label>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="workorder_table" pager="#pagination_1" class="eu-datagrid">
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
			    <div style="font-size:14px">没有工单信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success" onclick="newWO();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>