<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>缺陷统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var siteId = '${siteId}';	
	var urlReport = null;
	var woYear = null;
	var woMonth = null; 
	var woRange = null;
	var dataGrid = null;
	var woWindStationEnumList = _enum.WO_WIND_STATION;
	//fileExportPath = "http://10.0.17.153:8080/itc_report/";
	
	//初始化执行js
	$(document).ready(function() {
		var myDate = new Date();
		var currentYear = myDate.getFullYear();    //获取完整的年份
		var currentMonth = myDate.getMonth()+1; //获取完整月份
		if(currentMonth<10){
			currentMonth = "0"+currentMonth;
		}
		initPackage(currentYear,currentMonth);
		initTable(currentYear,currentMonth,"all");
		if(null == urlReport){
			$("#noResult").show();
			$("#grid_wrap").hide();
		}else{
			$("#noResult").hide();
			$("#grid_wrap").show();
		}
	});
	//初始化按钮
	function initPackage(currentYear,currentMonth){
		var data = "['-1','全部'],";
		for ( var i = 2014; i <= currentYear; i++) {
			data += "['"+ i +"' , '" + i + "年'],";
		}
		if(data!=""){
			data = "[" + data.substring(0, data.length-1) + "]";
		}
		//年份
		$("#woYear").iCombo("init",{
			data:eval(data),
			onChange:function(){
				changeExportUrl();
			}
		});	
		$("#woYear").iCombo("setVal",currentYear);
		//月份
		$("#woMonth").iCombo("init",{
			data:[
				["-1","全部"],
			    ["01","1月"],
			    ["02","2月"],
			    ["03","3月"],
			    ["04","4月"],
			    ["05","5月"],
			    ["06","6月"],
			    ["07","7月"],
			    ["08","8月"],
			    ["09","9月"],
			    ["10","10月"],
			    ["11","11月"],
			    ["12","12月"]
			],
			onChange:function(){
				changeExportUrl();
			}
		});			
		$("#woMonth").iCombo("setVal",currentMonth);
		//风场
		var woWindStationIcomboData = "['all','全部'],";
		for ( var i = 0; i < woWindStationEnumList.length; i++) {
			woWindStationIcomboData += "['"+ woWindStationEnumList[i].code +"' , '" + woWindStationEnumList[i].label + "'],";
		}
		if(woWindStationIcomboData!=""){
			woWindStationIcomboData = "[" + woWindStationIcomboData.substring(0, woWindStationIcomboData.length-1) + "]";
		}
		$("#woWindStation").iCombo("init",{
			data:eval(woWindStationIcomboData),
			onChange:function(){
				changeExportUrl();
			}
		});			
		$("#woWindStation").iCombo("setVal","all");
		//查询
		$("#btn_query").click(function(){
			woYear = $("#woYear").iCombo("getVal");
			woMonth = $("#woMonth").iCombo("getVal");
			var woWindStation = $("#woWindStation").iCombo("getVal");
			dataGrid.datagrid('load',{
		        	woYear: woYear,
		        	woMonth: woMonth,
		        	woWindStation:woWindStation
			});
			if(typeof(_itc_grids)!="undefined"){
				delete(_itc_grids["test_grid1"]);
            }
		});
		$(".itc_object_wrap").css("margin-top","2px");
		//queryByBirt(woYear,woMonth);
		changeExportUrl();
	} 
	function queryByBirt(woYear,woMonth){
		var urlReport = fileExportPath + "preview?__report=report/TIMSS2_WOBUGREPORT_001_html.rptdesign"
				+ "&woYear="+woYear+"&woMonth="+woMonth+"&woWindStation="+woWindStation+"&siteid="+siteId;							
		$("#noResult").hide();
		$("#grid_wrap").show();
		$("#woReportFrame").attr("src",urlReport);
	}
	//初始化表格
	function initTable(currentYear,currentMonth,woWindStation){
		dataGrid = $("#test_grid1").iDatagrid("init",{
	        pageSize:pageSize,//pageSize为全局变量，自动获取的
	        url: basePath + "/workorder/workorder/woBugReportList.do",	//basePath为全局变量，自动获取的       
	        queryParams: {
	        	woYear: currentYear,
	        	woMonth: currentMonth,
	        	woWindStation:woWindStation
	    	},
	        singleSelect:true,
	        onLoadSuccess: function(data){
	            if(data && data.total==0){
	            	$('#noResult').show();
	            	$("#grid1_wrap").hide();
	            }else{
	            	$('#noResult').hide();
	            	$("#grid1_wrap").show();
	            }
	        },
	        columns : [ [{
	         				field : 'workOrder',
	         				title : '工单编号',
	         				width : 170,
	         				fixed : true,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'equipName',
	         				title : '设备',
	         				width : 50,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'woSepcCodeName',
	         				title : '分类',
	         				width : 50,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'description',
	         				title : '名称',
	         				width : 80,
	         				align : 'left',
	         				sortable:true
	         			}, {
	         				field : 'beginTime',
	         				title : '开始时间',
	         				width : 120,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	          					return FW.long2time(value);
	          				},
	         				sortable:true
	         			}, {
	         				field : 'endTime',
	         				title : '结束时间',
	         				width : 120,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	          					return FW.long2time(value);
	          				},
	         				sortable:true
	         			}, {
	         				field : 'currWindSpeed',
	         				title : '风速(m/s)',
	         				width : 70,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	         					if(!isNaN(value)){
	         						value = new Number(value);
	         						value = value.toFixed(2);
	         					}
	         					return value;
	          				},
	         				sortable:true
	         			}, {
	         				field : 'usedTime',
	         				title : '累积故障时间(h)',
	         				width : 110,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	         					if(!isNaN(value)){
	         						value = new Number(value);
	         						value = value.toFixed(2);
	         					}
	         					return value;
	          				},
	         				sortable:true
	         			}, {
	         				field : 'loseElecticPower',
	         				title : '损失电量(kWh)',
	         				width : 110,
	         				align : 'left',
	         				fixed : true,
	         				formatter: function(value,row,index){
	         					if(!isNaN(value)){
	         						value = new Number(value);
	         						value = value.toFixed(2);
	         					}
	         					return value;
	          				},
	         				sortable:true
	         			}, {
	         				field : 'createUser',
	         				title : '记录人',
	         				width : 60,
	         				align : 'left',
	         				fixed : true,
	         				sortable:true
	         			}, {
	         				field : 'defectSolveUser',
	         				title : '消缺人',
	         				width : 60,
	         				align : 'left',
	         				fixed : true,
	         				sortable:true
	         			}, {
	         				field : 'faultConfirmUser',
	         				title : '确认人',
	         				width : 60,
	         				align : 'left',
	         				fixed : true,
	         				sortable:true
	         			} ] ]

	    });
	}
	//更改下载地址
	function changeExportUrl(){
		woYear = $("#woYear").iCombo("getVal");
		woMonth = $("#woMonth").iCombo("getVal");
		var woWindStation = $("#woWindStation").iCombo("getVal");
		urlExport = fileExportPath + "preview?__report=report/TIMSS2_WOBUGREPORT_001_pdf.rptdesign&__format=pdf"
				+ "&woYear="+woYear+"&woMonth="+woMonth+"&woWindStation="+woWindStation+"&siteid="+siteId;							
		var	title = "缺陷统计报表";
		FW.initPrintButton("#btn_export",urlExport,title,null);
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<div id="toolbar1" class="btn-toolbar ">
			<span class="ctrl-label pull-left" style="margin-left:8px">年份：</span>
	    	<select id="woYear" style="width:100px;float:left">
			</select>
		    <span class="ctrl-label pull-left"  style="margin-left:8px;">月份：</span>
	    	<select id="woMonth" style="width:100px;float:left">
			</select>
			<span class="ctrl-label pull-left"  style="margin-left:8px;">风场：</span>
	    	<select id="woWindStation" style="width:100px;float:left">
			</select>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_query">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_export">打印</button>
	        </div>
	        <div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager"></div>
		</div>
	</div>
	<div style="clear:both"></div>
	<div id="grid1_wrap" style="width:100%">
	    <table id="test_grid1" pager="#pagination_1" class="eu-datagrid">
	    </table>
	</div>
	<!-- 下页器部分-->
	<div id="bottomPager" style="width:100%;margin-top:6px"></div>
	<div id="noResult" style="display:none;width:100%;height:62%;margin:10px;">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有找到符合条件的结果</div>
			</div>
		</div>
	</div>
	<%-- 
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="woReportFrame"></iframe>
	</div>
	<div style="clear:both"></div>
	--%>
</body>
</html>