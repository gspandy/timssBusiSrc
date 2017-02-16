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

//ie兼容的文本转date的函数
function newDateAndTime(dateStr){
    var ds = dateStr.split(" ")[0].split("-");
    var ts = dateStr.split(" ")[1].split(":");
    var r = new Date();
    r.setFullYear(ds[0],ds[1] - 1, ds[2]);
    r.setHours(ts[0], ts[1], 0, 0);
    return r;
}
