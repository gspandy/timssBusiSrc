<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<head>
<title>维护计划列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	var selectFaultTypeId = "";
	var selectFaultTypeName="";
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
	        url: basePath + "itsm/maintainPlan/maintainPlanListdata.do",	//basePath为全局变量，自动获取的       
	        columns:[[ 
					{field:"maintainPlanCode",title:"编号",width:150,fixed:true,sortable:true},
					{field:"faultTypeName",title:"故障类型",width:85,fixed:true,sortable:true}, 
					{field:"description",title:"名称",width:280},   
					/* {field:"equipName",title:"维护设备",width:150}, */
					{field:"workTeamName",title:"负责组",width:80}
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
			   		 $("#noSearchResult").hide();
			    	//远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
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
	            isSearchMode = false;
	        },
	        onDblClickRow : function(rowIndex, rowData) {
				openMTPInfo(rowData);
			}
	    });
	}
	
	$(document).ready(function() {
		 initDataGrid();
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
				    args.faultTypeId=selectFaultTypeId;
				    return {"search":JSON.stringify(args)};
				}});
		    }
		});
		//控制权限
		Priv.apply();
	});
	function queryByTree(){
		headSearchParams.faultTypeId = selectFaultTypeId;
	    delete(_itc_grids["maintainPlan_table"]);
	    var pager = $("#maintainPlan_table").datagrid("getPager"); 
	    pager.pagination("select",1);
	}
	
	    
	/**左边树菜单选择触发事件*/
	function triggerFaultTypeTreeSelect(data){
		headSearchParams = {};
	    $("#maintainPlan_table").iDatagrid("endSearch");
		selectFaultTypeId = data.id;
		selectFaultTypeName = data.text;
		$("#maintainPlan_table").datagrid('options').queryParams.search = function(){
        		return JSON.stringify(headSearchParams);
       	};
		if(data.type == "root"){
	    	refresh();
	    }else{
	    	queryByTree();
	    }
	}
	/**打开标准作业方案详情*/
	function openMTPInfo(rawData){
		var maintainPlanId = rawData.id;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = rawData.maintainPlanCode;
	   var faultTypeParams = JSON.stringify({"faultTypeId":selectFaultTypeId,"faultTypeName":selectFaultTypeName});
	   
	   var urlPath = basePath+ "itsm/maintainPlan/queryFullMTPPage.do?maintainPlanId="+maintainPlanId+
	        		"&faultTypeParams="+encodeURIComponent(faultTypeParams);
	   
	   var opts = {
	        id : "openItsmMTP"+rand,
	        name : "维护计划详情",
	        url : urlPath,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
	        }
	    }
	    
	    _parent()._ITC.addTabWithTree(opts); 
	}
	function newMTP(){
		//FW.setTreeStat("fold");
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var faultTypeParams = JSON.stringify({"faultTypeId":selectFaultTypeId,"faultTypeName":selectFaultTypeName});
	   
	   var urlPath = basePath+ "itsm/maintainPlan/openNewMTPPage.do?faultTypeParams="+encodeURIComponent(faultTypeParams);
	   var opts = {
	        id : "newItsmMTP" + rand,
	        name : "新建维护计划",
	        url : urlPath,
	        tabOpt : {
	        	closeable : true,
	        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 

	}
function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	//$("#maintainPlan_table").datagrid("reload").datagrid("resize");
	delete(_itc_grids["maintainPlan_table"]);
    var pager = $("#maintainPlan_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
 function refreshAfterClose(){
	if(FW.get("MTPlistDoNotRefresh")){
		FW.set("MTPlistDoNotRefresh",false);
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
					<button id="btn_mtp_new" type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper" onclick="newMTP()">新建</button>
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
		                 <button type="button" class="btn btn-success priv" privilege="wo-maitainPlan-oper"  onclick="newMTP();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>