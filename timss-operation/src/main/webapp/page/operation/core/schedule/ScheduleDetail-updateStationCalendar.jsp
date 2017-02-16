<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>更新日历</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath }/js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath }/js/operation/common/map.js?ver=${iVersion}'></script>
<script>

//对Date的扩展，将 Date 转化为指定格式的String
//月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
//年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
//例子： 
//(new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
//(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
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

//岗位Json 需要引入map.js
var stationMap = new Map();

//岗位map
function stationJson(){
	var url = basePath + "operation/duty/queryStationInfoBySitId.do";
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			var stationList = data.result;
			for( var index in stationList ){
				stationMap.put( stationList[index].roleId, stationList[index].name );
			}
			createAndInitForm();
		}
	});
}

//formatter 单元格
function formatterData( val ){
	return val != null ? stationMap.get( val ) : "" ;
}

//根据stationId动态获取班次
var shifts=new Array();
shifts=[  
        ["0","暂无数据"],
       ];

var fields = [
				{title : "日期", id : "dateTime",type:"label"}, 
				{title : "工种", id : "stationId",type:"label",
					formatter: function(val,row){
						return formatterData(val);
					}
				}
				/* {title : "值别", id : "dutyName",type:"label",linebreak:true},
	  			{title : "班次", id : "shiftId",rules : {required:true},
	                  type : "combobox",
	                  data : shifts
	  			},
				{title : "值别ID", id : "dutyId",type:"hidden"},  */
	  		];
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:60,
	};
	
	//初始化表单
	function initForm(resultData){
		var dateTime = resultData[0].dateTime;
		var stationId = resultData[0].stationId;
		var data = '{"dateTime" : "'+ dateTime + '",'
				+ '"stationId" : "'+ stationId + '",';
			
		for(var index in resultData){
			data = data + '"dutyId_' + resultData[index].dutyId + '" : "' +resultData[index].shiftId + '",';
		}
		data = data.substring(0, data.length-1);
		data += "}";
		data=JSON.parse(data);
		$("#autoform").iForm("setVal",data);
		var canEdit = '<%=request.getParameter("canEdit") %>';
		if(canEdit == 0){
			$("#autoform").iForm("endEdit");
		}
	}
	
	function validAndGetForm(){
		if(!$("#autoform").valid()){
		        return null;
		    }
		return getFormData("autoform");
	}
	
	function createAndInitForm(){
		var stationId = '<%=request.getParameter("stationId") %>';
		var date = '<%=request.getParameter("date") %>';
		
		var url = basePath + "operation/scheduleDetail/getCalendarByDS.do?date=" + date + "&stationId=" + stationId;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				var scheduleDetail = data.scheduleDetail;
				var shiftResult = data.shiftResult;
				//var dutyResult=data.dutyResult;
				if( scheduleDetail.length > 0 && shiftResult.length > 0){
					var d=new Date(scheduleDetail[0].dateTime);
					scheduleDetail[0].dateTime=d.Format("yyyy-MM-dd");
					shifts=[];
					for(var index in shiftResult){
						shifts.push([shiftResult[index].id,shiftResult[index].name]);
					}
					 for(var index in scheduleDetail){
						fields.push({
							 title : scheduleDetail[index].dutyName, 
						   		id : 'dutyId_'+scheduleDetail[index].dutyId, 
			                  type : "combobox",
			                  data : shifts,
			                  linebreak:index % 2 == 0 ? true : false
			  			});
					 }
					 //console.log(fields);
					$("#autoform").iForm("init",{"options":opts,"fields":fields});
					initForm(scheduleDetail);
				}else{
					FW.error( "日历数据异常 ");
					$("body").html("日历数据异常");
				}
			}
		});
	}
	
	$(document).ready(function() {
		//岗位Json
		stationJson();
	});
		
</script>

</head>
<body>
	<form id="autoform"></form>
</body>
</html>