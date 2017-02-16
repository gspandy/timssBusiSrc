Date.prototype.format = function(format) {  
	var o = {  
	    "M+" : this.getMonth() + 1, //month   
	     "d+" : this.getDate(), //day   
	    "h+" : this.getHours(), //hour   
	    "m+" : this.getMinutes(), //minute   
	     "s+" : this.getSeconds(), //second   
	    "q+" : Math.floor((this.getMonth() + 3) / 3), //quarter   
	    "S" : this.getMilliseconds()  
	     //millisecond   
	    } ;
	    if (/(y+)/.test(format)) {  
	         format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));  
	     }  
	    
	    for ( var k in o) {  
	         if (new RegExp("(" + k + ")").test(format)) {  
	        format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));  
	         }  
	     }  
	    return format;  
} ; 
 //调用过程  
 function operation(value) {  

     var date = new Date(value);// 或者直接new Date();  

     return date.format("yyyy-MM-dd hh:mm:ss");  

 } 
 /**
 * 获取当前月的第一天
 */
function getCurrentMonthFirst(){
 var date=new Date();
 date.setDate(1);
 date = date.format("yyyy-MM-dd");
 date = stringToDate(date);
 return date;
}
/** 获取上个月第一天
 * @returns {Date}
 */
function getPreMonthFirst(){
	 var date=new Date();
	 var currentMonth=date.getMonth();
	 var preMonth=--currentMonth;
	 var preMonthFirstDay=new Date(date.getFullYear(),preMonth,1);
	 return preMonthFirstDay;
	} 
/** 获取上个月最后一天
 * @returns {Date}
 */
function getPreMonthEnd(){
	 var date=getPreMonthFirst();
	 var currentMonth=date.getMonth();
	 var nextMonth=++currentMonth;
	 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(nextMonthFirstDay.getTime()-oneDay);
	} 
/**
 * 获取当前月的最后一天
 */
function getCurrentMonthLast(){
 var date=new Date();
 var currentMonth=date.getMonth();
 var nextMonth=++currentMonth;
 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
 var oneDay=1000*60*60*24;
 return new Date(nextMonthFirstDay.getTime()-oneDay);
} 
/**
 * 获取当前下月的第一天
 */
function getCurrNextMonthFrist(){
 var date=new Date();
 var currentMonth=date.getMonth();
 var nextMonth=++currentMonth;
 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
 return nextMonthFirstDay; 
}

function stringToDate(dateStr){
    var d = new Date(dateStr.replace(/-/g,"/"));  
    return d;
}
/**
 * 获取某月的第一天
 */
function getMonthFirst(dateStr){
	return stringToDate(dateStr).setDate(1);
}
/**
 * 获取某天的下月的第一天
 */
function getNextMonthFrist(dateStr){
 var date=stringToDate(dateStr);
 var currentMonth=date.getMonth();
 var nextMonth=++currentMonth;
 var nextMonthFirstDay=new Date(date.getFullYear(),nextMonth,1);
 return nextMonthFirstDay; 
}
/** 获取某天的上个月第一天
 * @returns {Date}
 */
function getSomeDayPreMonthFirst(dateStr){
	var date=stringToDate(dateStr);
	 var currentMonth=date.getMonth();
	 var preMonth=--currentMonth;
	 var preMonthFirstDay=new Date(date.getFullYear(),preMonth,1);
	 var oneDay=1000*60*60*24;
	 return new Date(preMonthFirstDay);
	} 