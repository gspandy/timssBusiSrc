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


//初始化meeting
function initMeeting(){
	$("#contentDiv").html("");
	var arr = FW.getEnumMap("ATD_MT_NO");
	for( i in arr ){
		initData(i, arr[i] );
		var html = '<div class="card" id= "card_' + i + '"> </div>';
		$("#contentDiv").append(html);
	}
}

//初始化DIV
function initData( meetingNo, meetingName ){
	var url = basePath + "attendance/meeting/queryMeetingByNo.do?meetingNo=" + meetingNo;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var beans = data.meetingBeans;
			initDivData( beans, meetingNo, meetingName );
		}
	});
}

//mouseover事件
function meetingMouseOver( id ){
	var self = $("#mLi_" + id );
	initFormData( id, self );
}

//会议详情
function meetingDetail( id ){
	var url = basePath + "attendance/meeting/updateMeetingMenu.do?id=" + id;
	addTabFullcalendar( "detailMeeting" + event.id, "会议室预定", url,"attendance", "meetingCalendar");
}



//获取星期几 date -- 时间 例如：new Date()
function getWeekDay( date ){
	var weekDay = ["星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"];
	return weekDay[date.getDay()];
}

//弹出框
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
					"<li style='color: #e3931f;font-size:14px;font-weight:bold;'>" + obj.meetingName + "</li>" +
					"<li>" + new Date(obj.startDate).format("MM月dd日") + " " + getWeekDay( new Date(obj.startDate) ) + " " 
							+ new Date(obj.startDate).format("hh:mm") + "-" + new Date(obj.endDate).format("hh:mm")  + "</li>" +
					/*"<li>" + FW.getEnumMap("ATD_MT_NO")[obj.meetingNo] + " 会议室</li>" + */
					"<li>" + obj.userName + " 申请" ;
					if( obj.isLeader == 'Y'){
						popContent += ", 重要会议";
					}
					if(obj.reason != null && obj.reason != '' && obj.reason != "null"){
						popContent += "<li>内容： " + obj.reason + "</li>" ;
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


//更多
function showAllData( meetingNo ){
	$("#more_" + meetingNo ).hide();
	var url = basePath + "attendance/meeting/queryMeetingByNo.do?meetingNo=" + meetingNo + "&more=more";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var beans = data.meetingBeans;
			var html = '';
			for( index in beans ){
				bean = beans[index];
				var startStr = new Date( bean.startDate ).Format("yyyy-MM-dd");
				var endStr = new Date( bean.endDate ).Format("yyyy-MM-dd");
				var todayStr = new Date().Format("yyyy-MM-dd");
				
				if( startStr == endStr && todayStr == endStr ){
					if( bean.isLeader == 'Y'){
						html += '<li class="today leader" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
					}else{
						html += '<li class="today" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
					}
					html += '今天 ' + new Date(bean.startDate).format("hh:mm") + " - "
					+ new Date(bean.endDate).format("hh:mm")  +	'<span>'+ bean.userName + '</span></li>';
				}else{
					if( bean.isLeader == 'Y'){
						html += '<li class="leader" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
					}else{
						html += '<li ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
					}
					html += new Date(bean.startDate).format("MM.dd hh:mm") + " - "
					+ new Date(bean.endDate).format("MM.dd hh:mm")  +	'<span>'+ bean.userName + '</span></li>';
				}
			}
			
			$("#ul_" + meetingNo ).html( html );
		}
	});
}

//格式化Str
function formatterStr( startStr, endStr, bean ){
	var todayStr = new Date().Format("yyyy-MM-dd");
	var tomorrowStr = new Date().dateAdd("d", 1).Format("yyyy-MM-dd");
	var afterTomorrowStr = new Date().dateAdd("d", 2).Format("yyyy-MM-dd");
	
	var htm = "";
	if( startStr == todayStr ){
		htm += '今天 ' + new Date(bean.startDate).format("hh:mm") + "-";
	}else if( startStr == tomorrowStr ){
		htm += '明天 ' + new Date(bean.startDate).format("hh:mm") + "-";
	}else if( startStr == afterTomorrowStr ){
		htm += '后天 ' + new Date(bean.startDate).format("hh:mm") + "-";
	}else{
		htm += new Date(bean.startDate).format("MM.dd hh:mm") + "-";
	}
	
	if( endStr == tomorrowStr  && startStr != endStr ){
		htm += '明天 ' + new Date(bean.endDate).format("hh:mm") +	'<span>'+ bean.userName + '</span></li>';
	}else if( endStr == afterTomorrowStr  && startStr != endStr ){
		htm += '后天 ' + new Date(bean.endDate).format("hh:mm") +	'<span>'+ bean.userName + '</span></li>';
	}else if( startStr == endStr ){
		htm += new Date(bean.endDate).format("hh:mm") +	'<span>'+ bean.userName + '</span></li>';
	}else{
		htm += new Date(bean.endDate).format("MM.dd hh:mm") +	'<span>'+ bean.userName + '</span></li>';
	}
	
	return htm;
}

//初始化DIV里面的数据
function initDivData(  beans, meetingNo, meetingName ){
	var html = '<div class="title-wrap">'+
					'<span class="title-text">' + meetingName + '会议室</span>'+
				'</div>'+
				'<div class="panel-group" id="card_' + meetingNo + '">'+
					'<div class="panel panel-default">'+
						'<div class="panel-collapse">'+
							'<ul class="cpanel-body conf-list" id="ul_' + meetingNo + '">' ;
								for( index in beans ){
									bean = beans[index];
									var startStr = new Date( bean.startDate ).Format("yyyy-MM-dd");
									var endStr = new Date( bean.endDate ).Format("yyyy-MM-dd");
									var todayStr = new Date().Format("yyyy-MM-dd");
									
									if( startStr == endStr && todayStr == endStr ){
										if( bean.isLeader == 'Y'){
											html += '<li class="today leader datagrid-cell datagrid-row " ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
										}else{
											html += '<li class="today datagrid-cell datagrid-row" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
										}
										html += '今天 ' + new Date(bean.startDate).format("hh:mm") + "-"
										+ new Date(bean.endDate).format("hh:mm")  +	'<span>'+ bean.userName + '</span></li>';
									}else{
										if( bean.isLeader == 'Y'){
											html += '<li class="leader datagrid-cell datagrid-row" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
										}else{
											html += '<li class="datagrid-cell datagrid-row" ondblclick="meetingDetail('+ bean.id +')" onmouseover="meetingMouseOver( ' + bean.id + ' )" id="mLi_'+ bean.id + '"><b></b>';
										}
										
										html += formatterStr( startStr, endStr, bean );
										
										/*html += new Date(bean.startDate).format("MM.dd hh:mm") + "-"
										+ new Date(bean.endDate).format("MM.dd hh:mm")  +	'<span>'+ bean.userName + '</span></li>';*/
									}
								}
								/*if( beans == null || beans.length == 0 ){
									html += '<li>无预定信息<span><b></b></span></li>';
								}*/
								
					html +=		'</ul>'+
						'</div>'+
					'</div>';
				/*	if( beans.length == 6 ){
						html +='<div class="panel panel-default" onclick="showAllData(\'' + meetingNo +'\')" id="more_' + meetingNo + '">'+
								'<div class="panel-heading">'+
								'<div class="panel-title">'+
									'<span>更多</span> <span class="arrow arrow-down"></span>'+
								'</div>'+
							'</div>'+
						'</div>';
					}*/
					
					html +='</div>';
		$("#card_" + meetingNo ).append( html );
}
