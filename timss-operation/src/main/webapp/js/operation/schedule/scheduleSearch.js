// 对Date的扩展，将 Date 转化为指定格式的String
// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
// 例子： 
// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
Date.prototype.Format = function (fmt) { //author: meizz 
    var o = {
        "M+": this.getMonth() + 1, //月份 
        "d+": this.getDate(), //日 
        "h+": this.getHours(), //小时 
        "m+": this.getMinutes(), //分 
        "s+": this.getSeconds(), //秒 
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
        "S": this.getMilliseconds() //毫秒 
    };
    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    for (var k in o)
    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    return fmt;
};

/* 得到日期年月日等加数字后的日期 */   
Date.prototype.dateAdd = function(interval,number)   
{   
    var d = this;   
    var k={'y':'FullYear', 'q':'Month', 'm':'Month', 'w':'Date', 'd':'Date', 'h':'Hours', 'n':'Minutes', 's':'Seconds', 'ms':'MilliSeconds'};   
    var n={'q':3, 'w':7};   
    eval('d.set'+k[interval]+'(d.get'+k[interval]+'()+'+((n[interval]||1)*number)+')');   
    return d;   
} ;

/**
*日期比较
*参数为字符串，日期格式为yyyy-MM-dd
*返回true表示d1晚于d2
*/
function compareDate(d1,d2){
	var d1Arr = d1.split('-');
	var d2Arr = d2.split('-');
	d1 = new Date(d1Arr[0],d1Arr[1]-1,d1Arr[2]);
	d2 = new Date(d2Arr[0],d2Arr[1]-1,d2Arr[2]);
	if(d1.getTime() >= d2.getTime()){
		return true;
	}else{
		return false;
	}
}

//获得某月的天数
function getMonthDays(year, month ) {
	var monthStartDate = new Date(year, month, 1);
	var monthEndDate = new Date(year, month + 1, 1);
	var days = (monthEndDate - monthStartDate) / (1000 * 60 * 60 * 24);
	return days;
}
	
//获得本月的开始日期
function getMonthStartDate( year, month ) {
	var monthStartDate = new Date(year, month, 1);
	return monthStartDate.Format("yyyy-MM-dd");
}

//获得本月的结束日期
function getMonthEndDate(year,month) {
	var monthEndDate = new Date(year, month, getMonthDays(year,month));
	return monthEndDate.Format("yyyy-MM-dd");
}

//两个日期相差天数
function daysBetweenDates(early,late){
	return parseInt(Math.abs(late-early)/1000/60/60/24);
}

// 岗位日历
function groupShow(){
	$("#stationForDutyDiv").hide();
	$("#dutyDiv").hide();
	$("#personCalendar").hide();
	
	$("#yearCalendar").hide();
	$("#yearDiv").hide();
	$("#stationForYearDiv").hide();
	$("#yearViewNoData").hide();
	
	$("#stationDiv").show();
	$("#calendar").show().html("");
	currentView="stationView";
	
	if( $("#stationSelect").find("option").length == 1 ){
		$("#stationDiv").hide();
	}
	
	//渲染岗位日历
	$('#calendar').fullCalendar({
		header:{
              right: 'prev,next today',
              center: 'title',
              left: 'prevYear,nextYear'
        },
        buttonText :{
        	 prev: '上月',
        	 next: '下月',
        	 prevYear: '去年',
        	 nextYear: '明年',
        	 today : '今天'
        },
        titleFormat : {
        	month: 'yyyy年MM月'
        },
        firstDay : 1,
        height : '600',
        dayNames : ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
        dayNamesShort : ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
		editable: false,
		dayClick : function( date, allDay, jsEvent, view ) {
        	var start = view.start;
        	//view 对当前visible cell操作的时候
        	if( start.getMonth() == date.getMonth() ){	
        		//stationCalendarDayClicked(date);
        	}else{
        		//点击view 空白格跳转到响应的月份
        		$("#calendar").fullCalendar( "gotoDate", date.getFullYear(), date.getMonth() );	
        	}
		},
		eventClick:function( event, jsEvent, view ) {
			showDutyShift(event.start,event.dutyShift);
			/*var pri=_parent().privMapping;
			if( pri["opr_cal_update"] == 1 ){
				stationCalendarDayClicked(event.start);
			}*/
		},
		events :  function(start,end, callback){
			//渲染本月事件
			//去到本月开始、结束
			var flagTime = start.dateAdd( 'd', 15);
			var year = flagTime.getFullYear();
			var month = flagTime.getMonth();
			var startTime = getMonthStartDate( year, month );
			var endTime = getMonthEndDate(year,month);
			
			var stationId = $("#stationSelect").iCombo("getVal");
			var url = basePath + "operation/scheduleDetail/getDutyCalendarByStationId.do?stationId=" + stationId + "&start=" + startTime + "&end=" + endTime;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					var result = data.result;
					 if( result != null ){
						 callback(result);
					 }
				}
			});
		}
	});
}

//值别视图
function personShow(){
	$("#calendar").hide();
	$("#stationDiv").hide();
	
	$("#yearCalendar").hide();
	$("#yearDiv").hide();
	$("#stationForYearDiv").hide();
	$("#yearViewNoData").hide();
	
	$("#personCalendar").show().html("");
	$("#dutyDiv").show();
	$("#stationForDutyDiv").show();
	currentView="dutyView";
	
	if( $("#stationForDuty").find("option").length == 1 ){
		$("#stationForDutyDiv").hide();
	}
	
	$('#personCalendar').fullCalendar({
		header:{
              right: 'prev,next today',
              center: 'title',
              left: 'prevYear,nextYear'
        },
        buttonText :{
        	 prev: '上月',
        	 next: '下月',
        	 prevYear: '去年',
        	 nextYear: '明年',
        	 today : '今天'
        },
        titleFormat : {
        	month: 'yyyy年MM月'
        },
        firstDay : 1,
        height : '500',
        dayNames : ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
        dayNamesShort : ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
		editable: false,
		dayClick : function( date, allDay, jsEvent, view ) {
        	var start = view.start;
        	//view 对当前visible cell操作的时候
        	if( start.getMonth() == date.getMonth() ){
        		//dutyCalendarDayClicked(date);
        	}else{
        		//点击view 空白格跳转到响应的月份
        		$("#personCalendar").fullCalendar( "gotoDate", date.getFullYear(), date.getMonth() );	
        	}
		},
		eventClick:function( event, jsEvent, view ) {
			showDutyShift(event.start,event.dutyShift);
			/*var pri=_parent().privMapping;
			if( pri["opr_cal_update"] == 1 ){
				dutyCalendarDayClicked(event.start);
			}*/
		},
		events :  function(start,end, callback){
			//渲染事件Events
			//去到本月开始、结束
			var flagTime = start.dateAdd( 'd',15);
			var year = flagTime.getFullYear();
			var month = flagTime.getMonth();
			var startTime = getMonthStartDate( year, month );
			var endTime = getMonthEndDate(year,month);
			//岗位
			var stationId = $("#stationForDuty").iCombo("getVal");
			//值别
			var dutyId = $("#dutySelect").iCombo("getVal");
			var url = basePath + "operation/scheduleDetail/getDutyCalendarByDutyId.do?dutyId="+ dutyId +"&stationId=" + stationId + "&start=" + startTime + "&end=" + endTime;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					var result = data.result;
					 if( result != null ){
						 //重新渲染事件
						 callback(result);
					 }
				}
			});
		}
	});
}

/**
 * 展示值别班次信息
 * @param dutyShift
 */
function showDutyShift(date,dutyShift){
	initDialog(date,dutyShift.stationId,dutyShift.dutyId,"dutyShift",dutyShift.id);
}

/*
 * 单击值别视图的日历格子的处理函数
 */
function dutyCalendarDayClicked(date){
	//岗位
	var stationId = $("#stationForDuty").iCombo("getVal");
	//值别
	var dutyId = $("#dutySelect").iCombo("getVal");
	initDialog(date,stationId,dutyId,"duty");
}

/*
 * 单击岗位视图的日历格子的处理函数
 */
function stationCalendarDayClicked(date){
	//岗位
	var stationId = $("#stationSelect").iCombo("getVal");
	initDialog(date,stationId,0,"station");
}

//初始化弹出修改日历对话框，包括岗位视图和值别视图，用flag区分
function initDialog(date,stationId,dutyId,flag,scheduleId){
	var today = new Date();
	var todayString = today.getFullYear()+'-'+(today.getMonth()+1)+'-'+today.getDate();
	var dateTime = date.Format("yyyy-MM-dd hh:mm:ss");
	var dateYMD = date.Format("yyyy-MM-dd");
	var btnOpts = [{
		name:"关闭",
		onclick:function(){
		   return true;
		}
	}];
	var url = basePath + "operation/handover/isExistHandoverByDSD.do?dutyId="+ dutyId +"&stationId=" + stationId + "&date="+dateYMD;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var canEdit = 0;
			if(Priv.hasPrivilege("opr_cal_update")){
				btnOpts[0].name="取消";
				btnOpts.push(
					{name:"保存",style:"btn-success",onclick:function(){
						//saveCalendarChange(flag);
						saveSchedulePersons(scheduleId);
					}}
				);
				canEdit = 1;
			}
			//flag == "dutyShift"
			var src = basePath +"/page/operation/core/schedule/ScheduleDetail-updateDutyShift.jsp?date="+dateTime+"&stationId="+stationId+"&dutyId="+dutyId+"&canEdit="+canEdit;
			var dlgWidth = 450;
			var dlgHeight = 480;
			if(flag == "station"){
				src = basePath +"/page/operation/core/schedule/ScheduleDetail-updateStationCalendar.jsp?date="+dateTime+"&stationId="+stationId+"&canEdit="+canEdit;
				dlgWidth = 450;
				dlgHeight = 450;
			}else if(flag == "duty"){
				src = basePath +"/page/operation/core/schedule/ScheduleDetail-updateDutyCalendar.jsp?date="+dateTime+"&stationId="+stationId+"&dutyId="+dutyId+"&canEdit="+canEdit;
				dlgWidth = 280;
				dlgHeight = 360;
			}
			FW.dialog("init",{
			    src : src,
			    dlgOpts : {
			    	title:canEdit==1?"修改值班人员":"排班详情",
			    	width:dlgWidth,
			    	height:dlgHeight
			    },
			    btnOpts : btnOpts
			});
		}
	});
}
function saveSchedulePersons(scheduleId){
	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	var d=p.getDataForSubmit();
	$.post(
		basePath+"operation/scheduleDetail/updateSchedulePerson.do",{
			nowScheduleId:scheduleId,
			userIdStr:FW.stringify(d),
			updateType:"schedule"
		},
		function(data){
			if( data.result == "success" ){
				FW.success( "值班人员修改成功 ！");
				_parent().$("#itcDlg").dialog("close");
			}else{
				FW.error( "值班人员修改失败 ！");
			}
		},
		"json"
	);
}
/**
 * 修改日历的保存
 * @param type
 */
function saveCalendarChange(type){
	var p = _parent().window.document.getElementById("itcDlgContent").contentWindow;
	var formData=p.validAndGetForm();
	var url = basePath + "operation/scheduleDetail/updateDutyCalendar.do";
	if(type=="duty"){
		url = basePath + "operation/scheduleDetail/updateDutyCalendar.do";
	}else if(type=="station"){
		url = basePath + "operation/scheduleDetail/updateStationCalendar.do";
	}
	
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		data:{"formData":formData},
		success : function(data) {
			if (data.result == "success") {
				FW.success("修改成功 ！");
				personShow(basePath);
			} else {
				FW.error("修改失败 ！");
			}
			_parent().$("#itcDlg").dialog("close");
		}
	});
}

/*
 * 创建全年的日历空格子,其中flag表示上半年还是下半年
*/
function createYearSchedule(flag,minWidth){	
	var monthFormat={
			"1":"一月",
			"2":"二月",
			"3":"三月",
			"4":"四月",
			"5":"五月",
			"6":"六月",
			"7":"七月",
			"8":"八月",
			"9":"九月",
			"10":"十月",
			"11":"十一月",
			"12":"十二月"
	};
	
	var start = 7;
	if(flag == "first"){
		start = 1;
	}
	
	//根据每月的列数设置表头跨列
	$("#" + flag + " thead tr").html("");
	for(var i = 0;i < 6;i++){
		$("#" + flag + " thead tr").append("<th colspan='"+yearCalendarColNum+"'>"+monthFormat[start+i]+"</th>");
	}
	
	//渲染生成日历空格子
	var i, j;
	var html = "";
	for (j = 0; j < daysCount; j++) {
		var temp1 = j + 1;
		html = html + "<tr style='' id="+flag+"_"+temp1+">";
		for (i = 0; i < 6 * yearCalendarColNum; i++) {
			var temp2 = i + 1;
			html += "<td style='text-align:center;height:17px;min-width:"+minWidth+"px;border:1px solid;border-color:#DDDDDD;'" 
					+" id="+flag+"_"+temp1+"_"+temp2+"></td>";
		}
		html += "</tr>";
	}
	$("#" + flag + " tbody").append(html);
	
	//注册mouseover、mouseout、click事件
	$("#" + flag + " tbody tr").mouseover(function(){
		//console.log(clickedRow+"and "+this.rowIndex);
		if(!clickedRow||this.rowIndex!=clickedRow.rowIndex){
			if($(this).parents("table")[0].id=="first"){
				$("#zhibie1_"+this.rowIndex+"_1").mouseover();
			}else{
				$("#zhibie2_"+this.rowIndex+"_1").mouseover();
			}
			$(this).css("background-color","#DDDDDD");
			
		}
	});
	
	$("#"+flag+" tbody tr").mouseout(function(){
		//console.log(clickedRow+"and "+this.rowIndex);
		if(!clickedRow||this.rowIndex!=clickedRow.rowIndex){
			if($(this).parents("table")[0].id=="first"){
				$("#zhibie1_"+this.rowIndex+"_1").mouseout();
			}else{
				$("#zhibie2_"+this.rowIndex+"_1").mouseout();
			}
			$(this).css("background-color","white");
		}
	});
	
	$("#"+flag+" tbody tr").click(function(){
		//console.log($(this).parents("table")[0].id);
		if($(this).parents("table")[0].id=="first"){
			$("#zhibie1_"+this.rowIndex+"_1").click();
		}else{
			$("#zhibie2_"+this.rowIndex+"_1").click();
		}
	});
}

/* 根据设定的起始位置，将2014年填入格子内
 * 格子ID：flag_row_col
*/
function fillIn(startDate,startRow,endDate){
		var startDateSplit = startDate.split("-");
		var year = parseInt(startDateSplit[0]);
		var currentMonth = parseInt(startDateSplit[1]);
		var currentDay = parseInt(startDateSplit[2]);
		
		var endDateSplit = endDate.split("-");
		var endMonth = parseInt(endDateSplit[1]);
		var endDay = parseInt(endDateSplit[2]);
		
		//计算开始和结束日期之间的总天数
		var totalDays = 1 + daysBetweenDates(new Date(year, currentMonth - 1, currentDay), new Date(year, endMonth - 1, endDay));
		
		//待填充格子的行号
		var rowIndex = startRow;
		//待填充格子的列号，通过起始月份和每月列数计算得出
		var colIndex = (currentMonth - 1) * yearCalendarColNum + 1;
		//修正开始列
		if(colIndex > 6*yearCalendarColNum){
			colIndex -= 6*yearCalendarColNum;
		}
		
		//当前月份最大天数，用于判断是否要移动填充位置
		var currentMonthMaxDay;
		var today=new Date();
		for (var dCount = 0; dCount < totalDays; dCount++) {
			var currentDate = new Date(year, currentMonth - 1, currentDay);
			//console.log("正在填充："+currentDate);
			//获取当前月份最大天数
			currentMonthMaxDay = getLastDay(year,currentMonth);
			
			flag = "first";
			if(currentMonth > 6 ){
				flag = "second";
			}
			//构造要填入的单元格的ID
			var fillId="#"+flag+"_" + rowIndex + "_" + colIndex;
			//console.log("fillId==="+fillId);
			if (today.getMonth() == currentDate.getMonth()&&
				today.getDate()== currentDate.getDate()) {
				$(fillId).css("background-color","yellow");
			}
			$(fillId).html(currentDay);
			if (currentDate.getDay() == 0) {
				$(fillId).css("color", "red");
			}
			if (currentDate.getDay() == 6) {
				$(fillId).css("color", "green");
			}
			rowIndex++;
			if (rowIndex > daysCount) {
				rowIndex = 1;//超过一个周期就从第一行开始
				colIndex++;//同时列数+1
			}
			
			currentDay++;
			//如果下一天大于当前月最大天数，则月份+1
			if(currentDay > currentMonthMaxDay){
				currentDay = 1;
				currentMonth++;
				//月份增加时，如果rowIndex不等于1，则列数+1
				if (rowIndex != 1) {
					colIndex++;
				}
				//列数+1后，如果不是某个月的第一列，则要进行调整
				if(colIndex | yearCalendarColNum != 1){
					colIndex = (currentMonth - 1) * yearCalendarColNum + 1;
				}
			}
			//如果列数超过总列数，则说明是下半年，要减去6*yearCalendarColNum
			if(colIndex > 6*yearCalendarColNum){
				colIndex -= 6*yearCalendarColNum;
			}
		}//for循环
 }

/*
 * 创建左边的值别表格
 */
var clickedRow;
var clickedFlag;
function createZhibie(id,minWidth) {
	var i, j;
	var html = "";
	//TODO 动态表头，与sortType相关
	var headers=["A","B","C","D","E","F","G","H","I"];
	
	//渲染表格表头
	for(i = 0; i < dutyCount; i++){
		html = html + "<th>" + headers[i] + "</th>";
	}
	$("#" + id + " thead tr").append(html);
	
	//渲染表格内容
	html = "";
	for (j = 0; j < daysCount; j++) {
		var temp1 = j + 1;
		html = html + "<tr id="+id+"_"+temp1+">";
		for (i = 0; i < dutyCount; i++) {
			var temp2 = i + 1;
			html += "<td style='border:1px solid;border-color:#DDDDDD;min-width:"+minWidth+"px;'" 
					+" id='"+id+"_"+temp1+"_"+temp2+"'>"
					+formatShift(rulesDetailData[j]["field"+temp2])+"</td>";
		}
		html += "</tr>";
	}
	$("#"+id+" tbody").append(html);
	//给左侧规则表格注册鼠标事件
	addEvents(id);
}

//给左侧规则表格注册鼠标事件
function addEvents(id){
	$("#"+id+" tbody").click(function(e){
		if(clickedRow){
			$(clickedRow).css("background-color","white");
			$("#"+clickedFlag+"_"+clickedRow.rowIndex).css("background-color","white");
		}
		clickedRow=$("#"+e.target.id).parent()[0];
		$(clickedRow).css("background-color","#FFFFD5");
		if($(this).parent()[0].id=="zhibie1"){
			clickedFlag="first";
		}else {
			clickedFlag="second";
		}
		$("#"+clickedFlag+"_"+clickedRow.rowIndex).css("background-color","#FFFFD5");
	});
	
	$("#"+id+" tbody tr").mouseover(function(){
		//console.log(clickedRow+"and "+this.rowIndex);
		if(!clickedRow||this.rowIndex!=clickedRow.rowIndex){
			if($(this).parents("table")[0].id=="zhibie1"){
				$("#first_"+this.rowIndex).css("background-color","#DDDDDD");
			}else{
				$("#second_"+this.rowIndex).css("background-color","#DDDDDD");
			}
			$(this).css("background-color","#DDDDDD");
		}
	});
	
	$("#"+id+" tbody tr").mouseout(function(){
		//console.log(clickedRow+"and "+this.rowIndex);
		if(!clickedRow||this.rowIndex!=clickedRow.rowIndex){
			if($(this).parents("table")[0].id=="zhibie1"){
				$("#first_"+this.rowIndex).css("background-color","white");
			}else{
				$("#second_"+this.rowIndex).css("background-color","white");
			}
			$(this).css("background-color","white");
		}
	});
}

//加载并渲染年视图
function yearShow(){
	$("#calendar").hide();
	$("#personCalendar").hide();
	$("#stationForDutyDiv").hide();
	$("#dutyDiv").hide();
	$("#stationDiv").hide();
	
	$("#yearCalendar").show();
	$("#yearDiv").show();
	$("#stationForYearDiv").show();
	currentView="yearView";
	
	if( $("#stationForYear").find("option").length == 1 ){
		$("#stationForYearDiv").hide();
	}
	
	createYearView();
}
/**
 * 变量名称：yearViewData
 * 年视图基本信息
 * 包括success，startDate，endDate，uuid，startRow
 */
var yearViewData;
//天次总数
var daysCount;
//值别总数
var dutyCount;
//排班规则详情数据
var rulesDetailData;

//值别列表，用于formatter
var dutyList;
//班次列表，用于formatter
var shiftList;

//格式化班次，通过ID获取别名
function formatShift(val){
	return val > 0 ? shiftList[val] : "";
}

//创建年视图的总入口，包括2次ajax请求和将数据填充到表格
function createYearView(){
		var stationId=$("#stationForYear").iCombo("getVal");
		var year=$("#yearSelect").iCombo("getVal");
		var url = basePath + "operation/scheduleDetail/queryYearCalendar.do?year=" + year + "&stationId=" + stationId;
		//获取年视图相关参数，其中success用于判断是否有年视图
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				yearViewData = data;
				 if(data.success == 1){
					 dutyList={};
					 shiftList={};
					 for ( var index in data.dutyList) {
						dutyList[data.dutyList[index].id]=data.dutyList[index].abbName;
					 }
					 for ( var index in data.shiftList) {
						 shiftList[data.shiftList[index].id]=data.shiftList[index].abbName;
					 }
					 var url = basePath + "operation/rulesDetail/queryRulesDetailByUuid.do?uuid="+data.uuid;
					 //判断有年视图的前提下，去获取排班规则情况
					 $.ajax({
							url : url,
							type : 'post',
							dataType : "json",
							success : function(data) {
								 if (data.total > 0) {
									 rulesDetailData = data.rows;
									 daysCount = data.total;
									 dutyCount = Object.keys(data.rows[0]).length - 1;
									 //console.log("天次总数：" + daysCount);
									 //console.log("值别总数：" + dutyCount);
									 drawYearView();
									 $("#yearCalendar").show();
									 $("#yearViewNoData").hide();
								 } else {
									 FW.error("获取排班规则数据失败");
								 }
						   }
					 });
				} else {
					//FW.error("所选年份和岗位下没有全年排班");
					$("#yearCalendar").hide();
					var stationName=$("#stationForYear").iCombo("getTxt");
					$("#yearViewNoData").show().html(year+"年“"+stationName+"”没有全年排班");
				}
			}
		});
}

var yearCalendarColNum;
//在数据请求都到位的前提下，调用此函数画出年视图表格
function drawYearView(){
	if(yearViewData.success != 1){
		return;
	}
	
	$("#zhibie1 tbody").html("");
	$("#zhibie2 tbody").html("");
	$("#zhibie1 thead tr").html("");
	$("#zhibie2 thead tr").html("");
	$("#first tbody").html("");
	$("#second tbody").html("");
	
	 // 获取内容页面宽度
	 var clientWidth = $("#content").width();
	 
	 // 计算每个值别格子和日历格子的宽度
	 if((( 31 + daysCount - 1 )  / daysCount) % 1 > 0){
		 yearCalendarColNum = parseInt( ( 31 + daysCount - 1 )  / daysCount ) + 1 ;
	 }else{
		 yearCalendarColNum = parseInt( ( 31 + daysCount - 1 )  / daysCount ) ; 
	 } 
	 //console.log("每个月多少列：" + yearCalendarColNum);
	 var zbWidth = parseInt(clientWidth * 0.84 / (yearCalendarColNum * 6 + dutyCount)) + 1;
	 var scheduleWidth = parseInt(clientWidth * 0.84 / (yearCalendarColNum * 6 + dutyCount));
	 if (zbWidth < 23) {
		zbWidth = 23;
	 }
	 if (scheduleWidth < 21) {
		scheduleWidth = 21;
	 }
	 //console.log("左侧格子宽度："+zbWidth);
	 //console.log("右侧格子宽度："+scheduleWidth);
	 
	 createZhibie("zhibie1", zbWidth);
	 createZhibie("zhibie2", zbWidth);
	 createYearSchedule("first", scheduleWidth);
	 createYearSchedule("second", scheduleWidth);
	 /*var yearSearchDivML=clientWidth-600;
	 $("#yearSearchDiv").css("margin-left",yearSearchDivML+"px");*/
	 // fillIn(startRow,startCol,flag,year,totalDays)
	 fillIn(yearViewData.fristDateStr,yearViewData.startFlag,yearViewData.lastDateStr);	
}

/*
 *获得当前月份的最大天数
*/
function getLastDay(p_year, p_month) {
	var new_year = p_year; // 取当前的年份
	var new_month = p_month++;// 取下一个月的第一天，方便计算（最后一天不固定）
	if (p_month > 12) // 如果当前大于12月，则年份转到下一年
	{
		new_month -= 12; // 月份减
		new_year++; // 年份增
	}
	var new_date = new Date(new_year, new_month, 1); // 取当年当月中的第一天
	return (new Date(new_date.getTime() - 1000 * 60 * 60 * 24)).getDate();//获取当月最后一天日期        
}