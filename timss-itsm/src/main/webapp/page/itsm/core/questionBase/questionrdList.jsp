<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>问题记录列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
<script>
var siteId = ItcMvcService.getUser().siteId;
var isSearchMode = false;
var selectFaultTypeId = "";
var selectFaultTypeName="";
var headSearchParams = {};	
function initDataGrid(){
	dataGrid = $("#questionrd_table").iDatagrid("init",{
		pageSize:pageSize,//pageSize为全局变量
	    singleSelect:true,
	    url: basePath + "itsm/questionrd/questionRdListData.do",	//basePath为全局变量，自动获取的       
	    columns:[[ 
			{field:"code",title:"单号",width:130,fixed:true,sortable:true},
			{field:"title",title:"标题",width:200,fixed:true,sortable:true},   
			{field:"resourceid",title:"来源",width:100,sortable:true,
				formatter: function(value,row,index){
					return FW.getEnumMap("ITSM_QUESTION_SOURCE")[value];
				},
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : FW.parseEnumData("ITSM_QUESTION_SOURCE",_enum)
					}
				}	
			}, 
			{field:"category",title:"分类",width:90,sortable:true},
			{field:"priorityid",title:"优先级",width:70,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return FW.getEnumMap("ITSM_URGENCY_DEGREE")[value];
				},
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : FW.parseEnumData("ITSM_URGENCY_DEGREE",_enum)
					}
				}	
			},
			{field:"createUserName",title:"创建人",width:80,fixed:true,sortable:true},
			//问题列表增加创建时间
			{field:"createdate",title:"创建时间",width:145,fixed:true,
				formatter: function(value,row,index){
					return FW.long2time(value);
				}
			},
			{field:"status",title:"状态",width:125,fixed:true,sortable:true,
				formatter: function(value,row,index){
					return FW.getEnumMap("ITSM_QUESTION_STATUS")[value]; 
				},
				"editor" : {
					"type":"combobox",
					"options" : {
						"data" : FW.parseEnumData("ITSM_QUESTION_STATUS",_enum)	
					}
				}
			}
		]],
	    onLoadSuccess: function(data){
	    	if(isSearchMode){
				if(data && data.total==0){
			    	$("#noSearchResult").show();
			    }else{
			        $("#noSearchResult").hide();
			 	}
			}else{
			    $("#noSearchResult").hide();
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
	    	toQuestionBaseInfoPage(rowData);
		}
	});
}
	
$(document).ready(function() {
	//添加左边类型树
	FW.addSideFrame({
		src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1",
		id:"itsmQuestionFaultTypeTree",
		conditions :[
					 {tab:"^itsm_root$",tree:"^itsm_root_itsm_question$"},
					 {tab:"^initNewQuestion.+"},
					 {tab:"^itsmQuestion.+"},
					 {tab:"^QU.+"}
		]
	}); 
	initDataGrid();
	//表头搜索相关的
	$("#btn_advSearch").click(function(){
		if($(this).hasClass("active")){
			$("#questionrd_table").iDatagrid("endSearch");
		}else{
		    $("#questionrd_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        isSearchMode = true;
				//强烈建议转为一个string传给后台，方便转Bean或hashMap
				return {"search":JSON.stringify(args)};
			}});
		}
	});
});
//根据树的选择，对问题信息进行查询
function queryByTree(){
	headSearchParams.selectTreeId = selectFaultTypeId;
    delete(_itc_grids["questionrd_table"]);
    var pager = $("#questionrd_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
/**左边树菜单选择触发事件--这个是在woParamsConf/faultTypeTree.jsp中调用的*/
function triggerFaultTypeTreeSelect(data){
	headSearchParams = {};
    $("#questionrd_table").iDatagrid("endSearch");
	var selectType = data.faultTypeCode;
	if(selectType == "SD"){
		selectFaultTypeId = data.id;
		selectFaultTypeName = data.text;
	}
	$("#questionrd_table").datagrid('options').queryParams.search = function(){
       return JSON.stringify(headSearchParams);
    };
    queryByTree();
}
//打开新建问题标签页
function newQuestion(){
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var categoryParams = JSON.stringify({"categoryId":selectFaultTypeId,"categoryName":selectFaultTypeName});
    var tabId = "initNewQuestion" + rand;
    var urlPath = basePath+ "itsm/questionrd/openNewQuestionPage.do?categoryParams="+encodeURIComponent(categoryParams);  
    var opts = {
        id : tabId,
        name : "新建问题",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};
//打开基本信息标签页
function toQuestionBaseInfoPage(rowData){
	var id = rowData.id;
	var status = rowData.status;
	  //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var opts = {
        id : "itsmQuestion" + rand,
        name : "问题详情",
        url : basePath+ "itsm/questionrd/todolistTOQuestionPage.do?id="+id,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
//刷新数据方法 被下面你的方法调用
function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["questionrd_table"]);
    var pager = $("#questionrd_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
//关闭标签页后刷新
function refreshAfterClose(){
	if(FW.get("QuestionlistDoNotRefresh")){
		FW.set("QuestionlistDoNotRefresh",false);
		return;
	}
	refresh();
}
</script>
</head>
<body style="height: 100%;min-width:850px" class="bbox list-page">

	<div id="mainContent">
		<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
		    <div id="toolbar1" class="btn-toolbar ">
		    	<div class="btn-group btn-group-sm">
					<button type="button" class="btn btn-success" onclick="newQuestion()">新建</button>
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
		    <table id="questionrd_table" pager="#pagination_1" class="eu-datagrid">
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
			    <div style="font-size:14px">没有问题信息</div>
			    <div class="btn-group btn-group-sm margin-element">
		        	<button type="button" class="btn btn-success" onclick="newQuestion();">新建</button>
			    </div>
			</div>
		</div>
	</div>
	﻿
</body>
</html>