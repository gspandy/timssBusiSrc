<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" style="height:99%" xml:lang="en">

<head>
<title>定期工作计划列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}/js/operation/common/eventTab.js?ver=${iVersion}"></script>
<script src="${basePath}/js/operation/common/stationCommon.js?ver=${iVersion}"></script>
<script>
	var loginUserId = ItcMvcService.user.getUserId();
	var siteId = ItcMvcService.getUser().siteId;
    var isSearchMode=false;
	var queryType = "${queryType}"; 
	var stationId = "${stationId}"; 
	var dataGrid = null;
	$(document).ready(function() {
		var fields = [
			    {title : "工种", id:"deptId", type : "combobox"}
			];
		$("#stationForm").iForm('init',{"fields":fields,options:{labelFixWidth:50,mdWidth:12}});
		//工种
		stationOption( basePath, "f_deptId",successCallBack,null,changeStation,null);
			
		//表头搜索相关的
	    $("#btn_advSearch").click(function(){
		    if($(this).hasClass("active")){
		        $("#scheduleTaskGrid").iDatagrid("endSearch");
		    }
		    else{
		        $("#scheduleTaskGrid").iDatagrid("beginSearch",{"remoteSearch":true,"onParseArgs":function(args){
		        	isSearchMode=true;
				    //强烈建议转为一个string传给后台，方便转Bean或hashMap
				    return {"search":FW.stringify(args)};
				}});
		    }
		});
	});
	
	function refresh(){
		window.location.href=window.location.href;
	}
	function successCallBack(){
		$("#stationForm").iForm("setVal",{"deptId":stationId});
		initDatagrid();
	}
	function changeStation(){
		var stationData=$("#stationForm").iForm('getVal');
		stationId = stationData.deptId;
		initDatagrid()
		//dataGrid.datagrid('reload');
	}
	function initDatagrid(){
		//此处一定要给dataGrid赋值。dataGrid为全局变量，不设置会导致不能resize
		dataGrid = $("#scheduleTaskGrid").iDatagrid("init",{
		        pageSize:pageSize,//pageSize为全局变量，自动获取的
		        url: basePath + "operation/scheduleTask/scheduleTaskListData.do",	//basePath为全局变量，自动获取的       
		        queryParams: {"queryType":queryType,"stationId":function(){return stationId;}},
		        singleSelect:true,
		        nowrap : false,  //设置可以换行
		        columns : [ [
		         		    {field : 'id',hidden : true},
		         		    {field : 'code',title : '编号',width : 140,align : 'left',
		         		   	 	fixed : true,sortable:true}, 
		         		   	{field : 'type',title : '类型',width : 80,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter: function(value,row,index){
									return FW.getEnumMap("OPR_SCHEDULE_TYPE")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("OPR_SCHEDULE_TYPE",_enum)	
							        }
							    }
		         			},
		         			{field : 'content',title : '工作内容',width : 150,align : 'left'}, 
		         			{field : 'doStatus',title : '执行状态',width : 80,align : 'left',
		         				fixed : true,sortable:true,
		         				formatter: function(value,row,index){
									return FW.getEnumMap("OPR_SCHEDULE_DOSTATUS")[value]; 
								},
								"editor" : {
							        "type":"combobox",
							        "options" : {
							            "data" : FW.parseEnumData("OPR_SCHEDULE_DOSTATUS",_enum)	
							        }
							    }
		         			},
		         			{field : 'doTime',title : '执行时间',width : 100,fixed : true,
		         				formatter: function(value,row,index){
									return FW.long2time(value);
								}
		         			},
		         			{field : 'doUserNames',title : '执行人',width : 70,fixed : true},
		         			{field : 'remarks',title : '情况及备注',width : 150},
		         			{field : 'shiftName',title : '负责班次',width : 150,fixed : true},
		         			{field : 'dutyName',title : '值别',width : 60,fixed : true}
		         		] ],
		          onLoadSuccess: function(data){
		            //远程无数据需要隐藏整个表格和对应的工具条，然后在无数据信息中指引用户新建
				        if(data && data.total==0){
				            $("#noSearchResult").show();
				        }else{
				            $("#noSearchResult").hide();
				        }
				        if(queryType == "all"){  //如果是定期工作查询，则隐藏打印按钮
							$("#bt_print").hide();
							$("#btn_advSearch").show();
						}else if(queryType == "unfinished"){
							$("#btn_advSearch").hide();
							$("#scheduleTaskGrid").datagrid("hideColumn","remarks");
							$("#scheduleTaskGrid").datagrid("hideColumn","doUserNames");
							$("#scheduleTaskGrid").datagrid("hideColumn","doTime");
						}
						FW.fixToolbar("#toolbar1");
		        },
		        onDblClickRow:function(rowIndex, rowData){
		        	var newId="scheduleTask"+rowData['id'];
		    		var pageName="定期工作";
		    		var pageUrl=basePath + "operation/scheduleTask/openTaskPage.do?id="+rowData['id'];
		    		var oldId=FW.getCurrentTabId();
		    		addEventTab( newId, pageName, pageUrl, oldId );
		        },
	
		    });
	}
	/***
	*	获取当前时间加N天的日期字符串
	*/
	function showdate(n){  
		var uom = new Date(new Date()-0+n*86400000);  
		uom = uom.getFullYear() + "-" + (uom.getMonth()+1) + "-" + uom.getDate();  
		return uom;  
	}  
	/**
	 *	打印待执行工作
	*/
	function printToDoTask(){
		var printUrl = "http://timss.gdyd.com/";
		var tomorrow = showdate(1);
		var src = fileExportPath + "preview?__report=report/TIMSS2_OPR_SCHED_TODOTASK.rptdesign&__format=pdf"+
							"&siteId="+siteId+"&deptId="+stationId+"&tomorrow="+tomorrow+
							"&author="+loginUserId+"&url="+printUrl;
		var url = encodeURI(encodeURI(src));
		var title ="待执行工作";
		FW.dialog("init",{
			src: url,
			btnOpts:[{
		            "name" : "关闭",
		            "float" : "right",
		            "style" : "btn-default",
		            "onclick" : function(){
		                _parent().$("#itcDlg").dialog("close");
		             }
		        }],
			dlgOpts:{ width:800, height:650, closed:false, title:title, modal:true }
		});
	}
</script>
<style>
	#stationForm>.row>div{
		margin-top: 3px;
	}
</style>
</head>
<body style="height: 100%; min-width:850px" class="bbox list-page">
    <div id="main_content">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar1" class="btn-toolbar">
			<div style="width:160px;float:left;">
		    	 <form id="stationForm" class="autoform"></form>
			</div>
	    	 <div class="btn-group btn-group-sm">
	            <button type="button" id="btn_advSearch" data-toggle="button" class="btn btn-default">查询</button>
	        </div>
	    	<div class="btn-group btn-group-sm" style="float:left;" >
				<button id="bt_print" class="btn btn-default" onclick="printToDoTask()">打印</button>
			</div>
	    </div>
	    <!-- 上分页器部分 这里可以通过属性bottompager指定下分页器的DIV-->
	    <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager">        
	    </div>
	</div>
	
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="scheduleTaskGrid" pager="#pagination_1" class="eu-datagrid"></table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px">
	</div>
	</div>
	
	<!-- 表头搜索无数据时-->
	<div id="noSearchResult"  style="margin-top:20px;font-size:14px;vertical-align:middle;text-align:center" >
		没有找到符合条件的结果
	</div>
	
</body>
</html>