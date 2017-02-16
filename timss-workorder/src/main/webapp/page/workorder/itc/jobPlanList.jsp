<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title>标准作业方案列表</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var isSearchMode = false;
	var selectFaultTypeId = "";
	var selectFaultTypeName="";
	var headSearchParams = {};
	
	$(document).ready(function() {
		
		 dataGrid = $("#jobPlan_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        url: basePath + "workorder/jobPlan/jobPlanListData.do",	//basePath为全局变量，自动获取的       
	        singleSelect:true,
	        columns:[[ 
					{field:"jobPlanCode",title:"编号",width:140,fixed:true,sortable:true}, 
					{field:"faultTypeName",title:"服务目录",width:80,fixed:true,sortable:true},   
					{field:"description",title:"名称",width:280},   
					{field:"remarks",title:"备注",width:150}
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
		            }else{
		            	$("#mainContent").show();
		                $("#grid1_empty").hide();
		            }
			    }
			    isSearchMode = false;
			    
	        },
	        onDblClickRow : function(rowIndex, rowData) {
				 openJobPlanInfo(rowData);
			}
	    });
	    
	     //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#jobPlan_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#jobPlan_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
				   isSearchMode = true;
				    return {"search":JSON.stringify(args)};
				}}); 
		    }
		});
		
		//控制权限
		Priv.apply();
	});
	
	function queryByTree(){
		headSearchParams.faultTypeId = selectFaultTypeId;
	    delete(_itc_grids["jobPlan_table"]);
	    var pager = $("#jobPlan_table").datagrid("getPager"); 
	    pager.pagination("select",1);
	}
	
	    
	/**左边树菜单选择触发事件*/
	function triggerFaultTypeTreeSelect(data){
		headSearchParams = {};
	    $("#jobPlan_table").iDatagrid("endSearch");
		selectFaultTypeId = data.id;
		selectFaultTypeName = data.text;
		$("#jobPlan_table").datagrid('options').queryParams.search = function(){
        		return JSON.stringify(headSearchParams);
       	};
		if(data.type == "root"){
	    	refresh();
	    }else{
	    	queryByTree();
	    }
	}
	/**打开标准作业方案详情*/
	function openJobPlanInfo(rowData){
		var jobPlanId = rowData.id;
		//新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = rowData.jobPlanCode;
	   var opts = {
	        id : "open"+rand,
	        name : "作业方案详情",
	        url : basePath+ "workorder/jobPlan/queryFullJPPage.do?jobPlanId="+jobPlanId,
	        tabOpt : {
	        	closeable : true,
	        	afterClose :"FW.deleteTab('$arg'); FW.activeTabById('equmaintain');FW.getFrame('equmaintain').refreshAfterClose();" 
	        }
	    }
	    _parent()._ITC.addTabWithTree(opts); 
	}
	/** 新建一个标准作业方案 */
	function newJobPlan(){
	 //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
	   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
	   var faultTypeParams = JSON.stringify({"faultTypeId":selectFaultTypeId,"faultTypeName":selectFaultTypeName});
	   var urlPath = basePath+ "workorder/jobPlan/openNewJPPage.do?faultTypeParams="+encodeURIComponent(faultTypeParams);
	   var opts = {
	        id : "newJP"+rand,
	        name : "新建标准作业方案",
	        url : urlPath,
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
	//$("#jobPlan_table").datagrid("reload").datagrid("resize");
	delete(_itc_grids["jobPlan_table"]);
    var pager = $("#jobPlan_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
function refreshAfterClose(){
	if(FW.get("JPlistDoNotRefresh")){
		FW.set("JPlistDoNotRefresh",false);
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
					<button id="btn_jp_new" type="button" class="btn btn-success priv" privilege="wo-jobPlan-oper" onclick="newJobPlan()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh();">刷新</button>
		        </div>
				<div class="btn-group btn-group-sm">
		            <button id="btn_advSearch" type="button" data-toggle="button" class="btn btn-default">查询</button>
		        </div>
		    </div>
		    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
		    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
		    </div>
		</div>
		<!--这里要清掉分页器的右浮动效果-->
		<div style="clear:both"></div>
		<div id="grid1_wrap" style="width:100%">
		    <table id="jobPlan_table" pager="#pagination_1" class="eu-datagrid">
		    </table>
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
			    <div style="font-size:14px">没有标准作业方案信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		             <button type="button" class="btn btn-success priv" privilege="wo-jobPlan-oper"  onclick="newJobPlan();">新建</button>
			    </div>
			</div>
		</div>
	</div>
</body>
</html>