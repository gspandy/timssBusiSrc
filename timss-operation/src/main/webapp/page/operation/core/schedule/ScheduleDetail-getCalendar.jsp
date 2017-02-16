<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>排班查询</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<!-- fullcalendar  -->
<link href='${basePath}js/operation/fullcalendar/fullcalendar.css' rel='stylesheet' />
<link href='${basePath}js/operation/fullcalendar/fullcalendar.print.css' rel='stylesheet' media='print' />
<script src='${basePath}js/operation/fullcalendar/jquery-ui.custom.min.js'></script>
<script src='${basePath}js/operation/fullcalendar/fullcalendar.min.js'></script>
<script type="text/javascript" src="${basePath}js/operation/schedule/scheduleSearch.js?ver=${iVersion}"></script>
<script src='${basePath}js/operation/common/stationCommon.js?ver=${iVersion}'></script>

<style type="text/css">
#content {
	width: 98%;
	margin: 8px auto;
	min-width:792px;
}
 .datagrid-row {
height: 23px;
}

.datagrid-header{
height:18px !important;
}

table{
font-size: 12px;
text-align: center;
}
.thead-tr{
/* background-color: #e9e9e9; */
}
table tr td
{
	padding-left: 0px;
	/* border-color:#DDDDDD; */
}
table tr th
{
	padding-left: 0px;
}

.fc-today{
	background-color: yellow;
}

.fc-header-title h2 {
	font-size: 16px;
	font-weight: bolder;
}
.fc-button{
	height: 28px;
}
.fc-event{
	cursor:pointer
}
</style>

<script>
	var currentView="stationView";

	//填充值别select 角色建好加上角色
	function dutyOption( stationId ){
		
		var url = basePath + "operation/duty/queryDutyByStationId.do?stationId=" + stationId ;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.total > 0 ){
					var dutyList = data.rows;
					var dutyArr = [];
					for( var index in dutyList ){
						dutyArr.push( [dutyList[index].id,dutyList[index].name]);
					}
					/* $("#dutySelect").iCombo("loadData",dutyArr); */
					$("#dutySelect").iCombo("init", {
						data : dutyArr,
						"onChange" : function(val) {
							$("#personCalendar").html("");
							personShow();
						},
						initOnChange : false
					});
					//AJAX 请求值别返回后才能调用personShow,因为personShow需要值别数据
					//personShow();
				}
			}
		});
	}

	$(function() {
		//岗位
		stationOption(basePath,null,function(stationList){
			$("#stationSelect").iCombo("init", {
				data : stationList,
				"onChange" : function(val) {
					$("#calendar").html("");
					groupShow();
				}
			});

			$("#stationForDuty").iCombo("init", {
				data : stationList,
				"onChange" : function(val) {
					dutyOption(val);
				}
			});

			$("#stationForYear").iCombo("init", {
				data : stationList,
				"onChange" : function(val) {
					//年份改变时，请求新的数据重新渲染年视图
					var yearVal = $("#yearSelect").iCombo("getVal");
					if (yearVal != null && yearVal != "") {
						createYearView();
					}
				},
				initOnChange : false
			});
		});

		//年份select从当前年份前后多给3年，一共7年可选
		var yearSelectdata = [];
		var currentYear = parseInt(new Date().getFullYear());
		for (var i = 0; i < 9; i++) {
			if (i == 4) {
				yearSelectdata.push([ currentYear, currentYear, true ]);
			} else {
				yearSelectdata
						.push([ currentYear + 4 - i, currentYear + 4 - i ]);
			}
		}
		$("#yearSelect").iCombo("init", {
			data : yearSelectdata,
			"onChange" : function(val) {
				//年份改变时，请求新的数据重新渲染年视图
				var stationForYearVal = $("#stationForYear").iCombo("getVal");
				if (stationForYearVal != null && stationForYearVal != "") {
					createYearView();
				}
			},
			initOnChange : false
		});
		
		$("#btn_refreshSchedulePerson").click(function(){
			FW.confirm("刷新值班人员会清空当年的排班的值班人员并从值别人员中重建，清空的数据不可恢复，请确认",function(){
				$( "#btn_refreshSchedulePerson" ).attr("disabled",true);
				var url = basePath + "operation/scheduleDetail/refreshSchedulePerson.do";
				$.ajax({
					url : url,
					dataType : "json",
					data:{siteId:Priv.secUser.siteId,isWithHistory:true},
					success : function(data) {
						if (data.result == "success") {
							FW.success("刷新值班人员成功 ！");
						} else {
							FW.error("刷新值班人员失败 ！");
						}
						$( "#btn_refreshSchedulePerson" ).attr("disabled",false);
					}
				});
			});
		});
	});

	$(window).resize(function() {
		if (currentView == "yearView") {
			//console.log("reDraw year view");
			//只改变窗口大小，不用调createYearView，因为不用去请求数据
			drawYearView();
		}
	});
</script>

</head>
<body>
	<div id="content">
		<div class="toolbar toolbar-with-pager" style="margin-bottom: 8px;">

			<div class="btn-group btn-group-sm bbox" data-toggle="buttons">
				<label class="btn btn-default active" onclick="groupShow()">
				<input type="radio">工种视图
				</label>
				<label class="btn btn-default" onclick="personShow()" id="personBtn">
				<input type="radio">值别视图
				</label>
				<!-- <label class="btn btn-default" onclick="yearShow()">
				<input type="radio">全年视图
				</label> -->
				
			</div>
			<div class="atd_btn_pri opr_calendar_refresh_person btn-group btn-group-sm" style="display:none;">
	           <button type="button" class="btn btn-default" id="btn_refreshSchedulePerson">刷新值班人员</button>
	    	</div>
			<div id="stationDiv" style="float: right; height: 28px;display: none;">
				<select id="stationSelect" style="width: 150px;">
				</select>
			</div>
			<div id="dutyDiv" style="float: right; height: 28px;">
				<select id="dutySelect" style="width: 150px; float: left;" >
				</select>
			</div>
			<!-- 联动岗位视图，这个div联动值别 dutyDiv -->
			<div id="stationForDutyDiv" style="float: right; height: 28px;margin-right:5px;">
				<select id="stationForDuty" style="width: 150px; float: left;" >
				</select>
			</div>
		
			<div id="stationForYearDiv" style="float: right; height: 28px;display: none;">
				<select id="stationForYear" style="width: 150px; float: left;" >
				</select>
			</div>
			
			<div id="yearDiv" style="float: right; height: 28px;display: none;">
				<select id="yearSelect" style="width: 150px; float: left;" >
				</select>
			</div>
			
		</div>

		<div id='personCalendar' style="display:none;"></div>
		<div id='calendar' style="display: none;"></div>
		<div id='yearViewNoData' style="display: none;"></div>
		<div id="yearCalendar" style="display: none;">
		<!-- 前六个月日历 -->
		<div style="float: left;">
		<table id="zhibie1">
		<thead>
		<tr class="thead-tr">
		</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
		</div>
		<div style="float: left;">
		<table id="first">
			<thead>
				<tr class="thead-tr">
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		</div>
		<div style="clear:both;height: 2px;"></div><!-- 隐形分割线 -->
		<!-- 后六个月日历 -->
		<div style="float: left;clear: both;">
		<!-- <table class="easyui-datagrid" style="" id="zhibie2">
		</table> -->
		<table id="zhibie2">
		<thead>
		<tr class="thead-tr">
		</tr>
		</thead>
		<tbody>
		</tbody>
		</table>
		</div>
		<div style="float: left;">
		<table id="second">
			<thead>
				<tr class="thead-tr">
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		</div>
		</div><!-- yearCalendar -->
	</div>
</body>
</html>