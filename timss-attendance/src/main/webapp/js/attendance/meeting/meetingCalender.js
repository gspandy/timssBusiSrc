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

//初始化form
function initFormData(id ,self){
	var url = basePath + "attendance/meeting/queryMeetingById.do?id=" + id;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				var obj = data.meetingBean;
				var offset = self.offset();
				var scnWidth = $("body").width();
				var objWidth = $(self).width();
				var leftSize = offset.left;
				var rightSize = scnWidth - leftSize - objWidth;
				var direction = leftSize > rightSize ? "left" : "right";
				
				$("body").children(".popover").remove();
				var popContent = "<ul style='margin:0px;padding:4px 7px;list-style:none;line-height:18px;font-size:12px'>" +
					"<li style='color: #e3931f;font-size:14px;font-weight:bold;'>" + obj.meetingName + " 会议</li>" +
					"<li>" + new Date(obj.startDate).format("hh:mm") + " - " + new Date(obj.endDate).format("hh:mm")  + "</li>" +
					"<li>" + FW.getEnumMap("ATD_MT_NO")[obj.meetingNo] + " 会议室</li>" + 
					"<li>" + obj.userName + " 申请" ;
					if( obj.isLeader == 'Y'){
						popContent += ", 重要会议";
					}
					popContent += "</li>" + 
				"</ul>";						
				self.popover({
					"html":true,
					"title":null,
					"content":popContent,
					"trigger":"hover",
					"placement":direction,
					"container":"body"
				});
				self.popover("show");
			}
		}
	});
}

function calendarShow( ){
	//渲染日历
	$('#meetingCalendar').fullCalendar({
		header:{
              right: 'next today',
              center: 'title',
              left: 'prev'
        },
        buttonText :{
        	 prev: '上月',
        	 next: '下月',
        	 //prevYear: '去年',
        	 //nextYear: '明年',
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
        		var meetingNo = $("#meetingSelect").iCombo("getVal");
        		var url = basePath + "attendance/meeting/insertMeetingMenu.do?date=" + date.getTime() + "&meetingNo=" + meetingNo;
    			addTabFullcalendar( "addMeeting", "新建会议室预定", url,"attendance", "meetingCalendar");
        	}else{
        		//点击view 空白格跳转到响应的月份
        		$("#meetingCalendar").fullCalendar( "gotoDate", date.getFullYear(), date.getMonth() );	
        	}
		},
		eventClick:function( event, jsEvent, view ) {
			var url = basePath + "attendance/meeting/updateMeetingMenu.do?id=" + event.id;
			addTabFullcalendar( "detailMeeting" + event.id, "会议室预定详情", url,"attendance", "meetingCalendar");
		},
		eventMouseover : function( event, jsEvent, view ) {
			var self = $(this);
			initFormData( event.id, self);
		},
		events :  function(start,end, callback){
			//渲染本月事件
			//去到本月开始、结束
			var flagTime = start.dateAdd( 'd', 15);
			var year = flagTime.getFullYear();
			var month = flagTime.getMonth();
			var startTime = getMonthStartDate( year, month );
			var endTime = getMonthEndDate(year,month);
			
			var meetingNo = $("#meetingSelect").iCombo("getVal");
			//var meetingNo = "";//$("#meetingSelect").val();
			var url = basePath + "attendance/meeting/queryCalendarByDate.do?start=" + startTime + "&end=" + endTime + "&meetingNo=" + meetingNo;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				success : function(data) {
					//事件数组
					 var events =[];
					 if( data.result == 'success'){
						 events = data.eventsVos;
						 callback(events);
					 }
				}
			});
		}
	});
}