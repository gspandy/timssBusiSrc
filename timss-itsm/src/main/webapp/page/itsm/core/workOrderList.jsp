<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="height:99%">
<title>维护计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/itsm/common/workOrder.js?ver=${iVersion}"></script>
<script type="text/javascript" src="${basePath}js/itsm/common/itsmConstant.js?ver=${iVersion}"></script>
<script>
var siteId = ItcMvcService.getUser().siteId;
var isSearchMode = false;
var rowFilterFlag = 0 ; //默认不进行 行过滤（前台未选中）
var unEndFilterFlag = 0; //默认不进行未完工顾虑（前台未选中）
	var selectFaultTypeId = "";
	var selectFaultTypeName="";
	var headSearchParams = {};
	var isITCSer = '<%=request.getParameter("isITCSer")%>';   //客服标识
	var isEngineer = '<%=request.getParameter("isEngineer")%>';  //维护工程师标识
	isITCSer = isEngineer=="true"?true:false;
	isEngineer = isEngineer=="true"?true:false;
	
	function initDataGrid(){
		dataGrid = $("#workorder_table").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量
	        singleSelect:true,
	        url: basePath + "itsm/workorder/workorderListdata.do",	//basePath为全局变量，自动获取的       
	        queryParams: {rowFilterFlag:function(){return rowFilterFlag;},unEndFilterFlag:function(){return unEndFilterFlag;}},
	        columns:[[ 
					{field:"workOrderCode",title:"编号",width:140,fixed:true,sortable:true},
					{field:"faultTypeName",title:"服务目录",width:95,fixed:true,sortable:true},   
					{field:"woName",title:"名称",width:100,sortable:true}, 
					{field:"customerName",title:"客户",width:70,fixed:true,sortable:true},
					{field:"createuser",title:"报障人",width:70,fixed:true,sortable:true},
					{field:"createdate",title:"报障时间",width:150,fixed:true,sortable:true,
						formatter: function(value,row,index){
							return FW.long2time(value);
						}
					},   
					{field:"priorityName",title:"服务级别",width:60,fixed:true,sortable:true},
					{field:"currHandlerUser",title:"办理人",width:70,fixed:true,sortable:true}, 
					{field:"currStatus",title:"状态",width:100,fixed:true,sortable:true,
						formatter: function(value,row,index){
								return FW.getEnumMap("ITSM_STATUS")[value]; 
							},
							"editor" : {
						        "type":"combobox",
						        "options" : {
						            "data" : FW.parseEnumData("ITSM_STATUS",_enum)	
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
	        	 	case itsmWoStatus.DRAFT:toWoBaseInfoPage(rowData);break;
	        		case itsmWoStatus.NEWWO:toWoBaseInfoPage(rowData);break;
	        		case itsmWoStatus.APPLICANTAUDIT:
	        		case itsmWoStatus.COMAUDIT:
	        		case itsmWoStatus.CENTERAUDIT:
	        		case itsmWoStatus.GROUPAUDIT:toWoInintBaseInfoPage(rowData);break;
	        		case itsmWoStatus.SENDWO:toWoBaseInfoPage(rowData);break;
	        		case itsmWoStatus.WORKPLAN:
	        		case itsmWoStatus.CHIEFAUDIT:
	        		case itsmWoStatus.DELAYAUDIT:
	        		case itsmWoStatus.ENDREPORT:
	        		case itsmWoStatus.APPLICANTCONFIRM:
	        		case itsmWoStatus.FEEBACK:
	        		case itsmWoStatus.FILING:
	        		case itsmWoStatus.OBSOLETE:toWoPlanPage(rowData);break; 
	        		default:break;
	        	}
			}
	    });
	}
	
$(document).ready(function() {
		/* isCusSer(); */
		//添加左边类型树
		FW.addSideFrame({
			src:basePath+"page/itsm/core/woParamsConf/faultTypeTree.jsp?embbed=1&treeType=SD",
			id:"itsmFaultTypeTree",
			conditions :[{tab:"^itsm_root$",tree:"^itsm_root_itsm_wo$"},
						 {tab:"^itsm_root$",tree:"^itsm_root_itsm_mtp$"},
						 {tab:"^itsm_root$",tree:"^itsm_root_itsm_proj$"},
						 {tab:"^itsm_root$",tree:"^itsm_root_itsm_woFaultTypeConf$"},
						 {tab:"^itsm_root$",tree:"^itsm_root_itsm_normKLBase$"},
						 {tab:"^openItsmKnowledge.+"},
						 {tab:"^IT.+"},
						 {tab:"^newItsm.+"},
						 {tab:"^initNewItsm.+"},
						 {tab:"^itsmInfo.+"},
						 {tab:"^newItsmMTP.+"},
						 {tab:"^openItsmMTP.+"},
						 {tab:"^newItsmJP.+"},
						 {tab:"^openItsmJP.+"},
						 {tab:"^itsmPlanInfo.+"},
						 {tab:"^newItsmFaultType.+"},
						 {tab:"^openItsmFaultType.+"}
						]
						
		}); 
		//initPage();
		initDataGrid();
		if(siteId != "ITC"){
			$("#btn_exportWo").hide();
			FW.fixRoundButtons("#toolbar1");
			FW.setTreeStat("fold");
		}
		  //表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#workorder_table").iDatagrid("endSearch");
		    }
		    else{
		        $("#workorder_table").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode = true;
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
	headSearchParams.selectTreeId = selectFaultTypeId;
    delete(_itc_grids["workorder_table"]);
    var pager = $("#workorder_table").datagrid("getPager"); 
    pager.pagination("select",1);
}
	
/**左边树菜单选择触发事件*/
function triggerFaultTypeTreeSelect(data){
	headSearchParams = {};
    $("#workorder_table").iDatagrid("endSearch");
	var selectType = data.faultTypeCode;
	if(selectType == "SD"){
		selectFaultTypeId = data.id;
		selectFaultTypeName = data.text;
	}
	$("#workorder_table").datagrid('options').queryParams.search = function(){
       	//	console.log("datagrid查询时" +JSON.stringify(headSearchParams));
       		return JSON.stringify(headSearchParams);
      	};
	/* if(data.type == "root"){
    	refresh();
    }else{ */
    	queryByTree();
   /*  } */
}

function newWO(){
	//FW.setTreeStat("fold");
    //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
    var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    var faultTypeParams = JSON.stringify({"faultTypeId":selectFaultTypeId,"faultTypeName":selectFaultTypeName});
    var tabId = "initNewItsm" + rand;
    var urlPath = basePath+ "itsm/workorder/openInitNewWOPage.do?woStatus=newWO&faultTypeParams="+encodeURIComponent(faultTypeParams);  
   if(isEngineer||isITCSer){
   		tabId = "newItsm" + rand;
   		urlPath = basePath+ "itsm/workorder/openNewWOPage.do?woStatus=newWO&faultTypeParams="+encodeURIComponent(faultTypeParams);  
   } 
  
   var opts = {
        id : tabId,
        name : "工单",
        url : urlPath,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    };
    _parent()._ITC.addTabWithTree(opts); 

};


function  toWoPlanPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
	   //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand =  rowData.workOrderCode;
   var opts = {
        id : "itsmPlanInfo" + rand,
        name : "工单",
        url : basePath+ "itsm/workorder/openWOPlanInfoPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
function toWoBaseInfoPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
	var customerSiteid = rowData.siteid;
	var jumpUrl = basePath+ "itsm/workorder/openNewWOPage.do" ;
	if(customerSiteid!="ITC"&&woStatus=="newWO"){
		jumpUrl = basePath+ "itsm/workorder/openInitNewWOPage.do" ;
	}
	  //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
   var opts = {
        id : "itsmInfo" + rand,
        name : "工单",
        url : jumpUrl+"?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}
function toWoInintBaseInfoPage(rowData){
	var woId = rowData.id;
	var woStatus = rowData.currStatus;
	  //新建一个与时间相关的随机数 防止多次点击新建选项卡时ID重复
   var rand = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
   var opts = {
        id : "itsmInfo" + rand,
        name : "工单",
        url : basePath+ "itsm/workorder/openInitNewWOPage.do?woId="+woId+"&woStatus="+woStatus ,
        tabOpt : {
        	closeable : true,
        	afterClose : "FW.deleteTab('$arg'); FW.activeTabById('itsm_root');FW.getFrame('itsm_root').refreshAfterClose();"
        }
    }
    _parent()._ITC.addTabWithTree(opts); 
}


function showExportDialog(){
    var src = basePath + "page/itsm/core/operationWO/exportWoCondition.jsp?siteId="+siteId;
    var btnOpts = [{"name" : "关闭",
		            "onclick" : function(){
		                return true;
		               }
		        	},
		           {
		            "name" : "确定",
		            "style" : "btn-success",
		            "onclick" : function(){
		                //itcDlgContent是对话框默认iframe的id
		                var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		                var searchDateFrom = p.$("#exportCondition").iForm("getVal");
		                
						var beginTime = FW.long2date(searchDateFrom.fromTime);
						var endTime = FW.long2date(searchDateFrom.toTime); 
						var sdList = searchDateFrom.serDirectorysId;
						
						var timeStepLength = searchDateFrom.toTime - searchDateFrom.fromTime;
						if(timeStepLength>92*24*60*60*1000){
							FW.error("导出跨度不能大于3个月");
							return ;
						}
						if(sdList == ''){
							sdList = "null";
						}
						var scValue = searchDateFrom.serCharacter;
						var scList = "";
						switch(scValue){
						case "all": scList = "null";break;
						case "event": scList = "'SCCONSULT','SCREQUEST','SCOTHER'";break;
						case "request": scList = "'SCFAULT'";break;
						default:break;
						}
						var engineersId = searchDateFrom.engineers.split(",");
						var enginnersIdstr = "";
						for(var i=0; i<engineersId.length; i++){
							enginnersIdstr +=  "'"+engineersId[i]+"',"
						}
						enginnersIdstr = enginnersIdstr.substring(0,enginnersIdstr.length-1)
						if(enginnersIdstr =="''"){
							enginnersIdstr="null";
						}
						
						 if(searchDateFrom.fromTime<=searchDateFrom.toTime){ 
							var url = basePath+"page/itsm/core/operationWO/exportWoPage.jsp?siteId="+siteId+
										"&beginTime="+beginTime+"&endTime="+endTime+"&enginnersId="+engineersId+"&sdList="+sdList+
										"&scValue="+scValue; 
							//window.open (url);
							_parent().$("#itcDlg").dialog("close");
							var dlgOpts2 = {width : 1000,height:600, title:"导出工单数据",};
							var btnOpts2 = [{"name" : "关闭",
								            "onclick" : function(){
								                return true;
								               }
								        	}];
    						FW.dialog("init",{"src":url,"dlgOpts":dlgOpts2,"btnOpts":btnOpts2,idSuffix:"ExportWo"}); 
						}else{
							FW.error("查询结束时间必须大于开始时间 ");
						} 
		            }
		           }];
    var dlgOpts = {width : 400,height:300, title:"导出工单数据",};
    FW.dialog("init",{"src":src,"dlgOpts":dlgOpts,"btnOpts":btnOpts}); 
}

function refresh(){
	headSearchParams = {};
	$("#mainContent").show();
	$("#grid1_empty").hide();
	/* $("#workorder_table").datagrid("reload").datagrid("resize"); */
	//这里的mytable要换成datagrid对应的id
    delete(_itc_grids["workorder_table"]);
    var pager = $("#workorder_table").datagrid("getPager"); 
    pager.pagination("select",1);
}

function refreshAfterClose(){
	if(FW.get("WOlistDoNotRefresh")){
		FW.set("WOlistDoNotRefresh",false);
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
					<button type="button" class="btn btn-success" onclick="newWO()">新建</button>
				</div>
		        <div class="btn-group btn-group-sm">
		        	<button type="button" class="btn btn-default" onclick="refresh()">刷新</button>
		        </div>
		        <div class="btn-group btn-group-sm">
		            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
		        	<button type="button" id="btn_exportWo"  class="btn btn-default"  onclick="showExportDialog()" >导出</button>
		        </div>
		        <div style="margin-left:200px;margin-top:5px;">
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