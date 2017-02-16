<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<head>
<title>维护计划列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	var selectEquipId = "";
	var selectEquipName="";
	var selectEquipCode = "";
	var headSearchParams = {};
	
	function initDataGrid(){
		 dataGrid = $("#maintainPlan_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        queryParams:{
	        	search : function(){
	        		//console.log("datagrid查询时" +JSON.stringify(headSearchParams));
	        		return JSON.stringify(headSearchParams);
	        	}
	        },
	        url: basePath + "workorder/maintainPlan/maintainPlanListdata.do",	//basePath为全局变量，自动获取的       
	        columns:[[ 
					{field:"maintainPlanFrom",title:"分类",width:100,fixed:true,sortable:true,
							formatter: function(value,row,index){
								return FW.getEnumMap("WO_MAINTAINPLAN_FROM")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("WO_MAINTAINPLAN_FROM",_enum)
						        }
						    }
					}, 
					{field:"maintainPlanCode",title:"编号",width:120,fixed:true,sortable:true},
					{field:"specialtyId",title:"专业",width:85,fixed:true,sortable:true,
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
					{field:"equipName",title:"维护设备",width:150}
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
			    }else{
			    //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
		            $("#noSearchResult").hide();
		            if(data && data.total==0){
		                $("#mainContent").hide();
		                $("#grid1_empty").show();
		                headSearchParams = {};
		                $("#maintainPlan_table").iDatagrid("endSearch");
		            }else{
		            	$("#mainContent").show();
		                $("#grid1_empty").hide();
		            }
			    }
	            
	        },
	        onDblClickRow : function(rowIndex, rowData) {
	        	selectEquipId = rowData.id;
				selectEquipName=rowData.equipName;
				openMTPInfo(rowData);
			}
	    });
	}
	
	$(document).ready(function() {
		FW.createAssetTree({multiSelect:false}); //左边设备树
		// initPage();
		 initDataGrid();  //初始化列表
		 //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		    	headSearchParams = {};
		        $("#maintainPlan_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#maintainPlan_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	//人为改变参数
		        	//args["extraParam"] = "WT10001";
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    isSearchMode = true;
				    headSearchParams = args;
				    args.equipId=selectEquipId;
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
		//控制权限
		Priv.apply();
	});
	function queryByTree(){
		headSearchParams.equipId = selectEquipId;
		//这里的mytable要换成datagrid对应的id
	    delete(_itc_grids["maintainPlan_table"]);
	    var pager = $("#maintainPlan_table").datagrid("getPager"); 
	    
	    pager.pagination("select",1);
	    
	}
	
	    
	/**左边树菜单选择触发事件*/
	function passAssetSelect(data){
		headSearchParams = {};
	    $("#maintainPlan_table").iDatagrid("endSearch");
		selectEquipId = data.id;
		selectEquipName = data.text;
		selectEquipCode = data.assetCode;
		$("#maintainPlan_table").datagrid('options').queryParams.search = function(){
        		//console.log("datagrid查询时" +JSON.stringify(headSearchParams));
        		return JSON.stringify(headSearchParams);
       	};
		if(data.type == "root"){
	    	refresh();
	    }else{
	    	queryByTree();
	    }
	}
	/**打开标准作业方案详情*/
	function openMTPInfo(rowData){
		var maintainPlanId = rowData.id;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = rowData.maintainPlanCode;
	   var equipParams = JSON.stringify({"equipId":selectEquipId,"equipName":selectEquipName,"equipCode":selectEquipCode});
	   
	   var urlPath = basePath+ "workorder/maintainPlan/queryFullMTPPage.do?maintainPlanId="+maintainPlanId+
	        		"&equipParams="+encodeURIComponent(equipParams);
	   
	   var opts = {
	        id : "openMTP"+rand,
	        name : "维护计划详情",
	        url : urlPath,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();"
	        }
	    }
	    
	    _parent()._ITC.addTabWithTree(opts); 
	}
	function newMTP(){
		//FW.setTreeStat("fold");
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var equipParams = JSON.stringify({"equipId":selectEquipId,"equipName":selectEquipName,"equipCode":selectEquipCode});
	   
	   var urlPath = basePath+ "workorder/maintainPlan/openNewMTPPage.do?equipParams="+encodeURIComponent(equipParams);
	   var opts = {
	        id : "newMTP" + rand,
	        name : "新建维护计划",
	        url : urlPath,
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
	$("#maintainPlan_table").datagrid("reload").datagrid("resize");
}
function refreshAfterClose(){
	if(FW.get("eqMTPlistDoNotRefresh")){
		FW.set("eqMTPlistDoNotRefresh",false);
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
					<button id="btn_mtp_new" type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper"  onclick="newMTP()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager"  bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="maintainPlan_table" pager="#pagination_1" class="eu-datagrid"></table>
		</div>
		
		<!-- 下页器部分-->
		<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	</div>
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有维护计划信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		                 <button type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper" onclick="newMTP();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>