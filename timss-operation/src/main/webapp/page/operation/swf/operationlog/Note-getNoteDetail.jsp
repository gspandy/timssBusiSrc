<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>运行记事详情</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<style type="text/css">
#defined_msk{
	position: absolute;
    left: 0;
    z-index: 2333;
    background-color: #FFF;
    display: table;
    vertical-align: middle;
}
</style>

<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/formatterDate.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/operationlog/noteDetail.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/operationlog/notePriv.js?ver=${iVersion}'></script>

<script>
//工种
var jobsId ='${jobsId}';
//班次
var shiftId ='${shiftId}';
//岗位
var stationId ='${stationId}';
//交接班ID
var handoverId = '${ handoverId }';
//值别ID
var dutyId = '${ dutyId }';
//查询日期
var dateStr = '${dateStr}';
//站点
var siteId = '${siteId}';
//运行方式分组信息
var teams = '${teams}';

var noteDatagridHideColumns=["type"];

$(function(){
	$("#baseformDiv").iFold("init");
	$("#noteTbDiv").iFold("init");
	$("#modeContentDiv").iFold("init");
	$("#modeContentDiv").prev("div").append($("#teamSelectDiv").detach());
	dynamicForm( jobsId, stationId, handoverId );
	//初始化数据
	setBaseformData();
	//运行记事
	setNoteHistory();
	//加载运行方式
	//loadModeContentDataByDutyJobsHandover( dutyId, jobsId, handoverId );
	teamOption( jobsId, teams, dutyId, handoverId);
	setNotePriv();
	
	$("#printButton").click(function(){
		var userId = Priv.secUser.userId;
		var reportName = "TIMSS2_SWF_OPRHISLOG_002"
		if("SWF"!= siteId){
			reportName = "TIMSS2_OPRHISLOG_002"
		}
		var url= fileExportPath+"preview?__format=pdf&__report=report/"+reportName+".rptdesign&jobsId="+jobsId
		+"&stationId="+stationId
		+"&shiftId="+shiftId
		+"&date="+dateStr
		+"&siteId="+siteId
		+"&author="+userId
		+"&__isnull=url";
		FW.dialog("init",{
			src: url,
			btnOpts:[
					{
					    "name" : "关闭",
					    "float" : "right",
					    "style" : "btn-default",
					    "onclick" : function(){
					        _parent().$("#itcDlg").dialog("close");
					    }
					}
		        ],
			dlgOpts:{ width:800, height:650, closed:false, title:"运行记事打印", modal:true }
		});
	});
});
		
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	    	<div id="btn_wo_operDiv" class="atd_btn_pri atd_le_insert_save btn-group btn-group-sm">
	        	<button id="btn_close" type="button" class="btn btn-default" onclick="closeTab();">关闭</button>
	        </div>	
	        <div class="btn-group btn-group-sm">	
	        	<button type="button" class="btn btn-default priv only-hide" privilege="OPR_NOTE_PRINT" id="printButton">打印</button>
			</div>
	    </div>
	</div>
	
	<div id="baseformDiv" grouptitle="基本信息 ">
		<form  id="baseform" class="autoform" ></form>
	</div>
	<div id="noteTbDiv" grouptitle="运行记事">
		<table id="noteTb" pager="#pagination_1" class="eu-datagrid"></table>
	</div>
	<!-- 无数据 -->
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有运行记事信息</div>
			</div>
		</div>
	</div>
	
	 <div id="teamSelectDiv" style="float: right;">
       	<span class="pull-left ctrl-label" style="line-height:28px;margin-right:5px">分组：</span>
		<select id="teamSelect" style="width: 150px; float: left;" >
		</select>
	</div>
	<div id="modeContentDiv" grouptitle="运行方式">
		<div id="defined_msk"><span style="vertical-align:middle;"><span class="loading-juhua"></span></span></div>
		<div id="modeFormDiv">
			<form id="modeContentForm"></form>
		</div>
	</div>
	<!-- 无数据 -->
	<div id="mode_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有运行方式数据</div>
			</div>
		</div>
	</div>
</body>
</html>