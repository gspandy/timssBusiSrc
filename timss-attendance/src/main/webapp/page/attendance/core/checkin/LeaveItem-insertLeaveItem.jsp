<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建请假单明细</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/formatterDate.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>


<script>
var flag = false;
var reason = "";

var fields = [
				{title : "请假类别", id : "category",linebreak:true,rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "ATD_LEI_CATEGORY",
					options:{
						allowEmpty:true,
						onChange : onChangeFileds
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}
				},
				{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",linebreak:true,options:{minView:1},dataType:"datetime"},
	  			{title : "结束时间", id : "endDate",rules : {required:true,greaterThan:"#f_startDate"},
	  				type : "datetime",linebreak:true,options:{minView:1},dataType:"datetime"
	  			},
	  			{title : "天数", id : "leaveDays",type:"label"/* ,rules : {required:true,number:true,min:0.001},linebreak:true, options:{onChange : onChangeFileds} */}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		xsWidth:11,
		mdWidth:11
	};
	

	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//日历onchange
	function calcDay(startTime,endTime,category){
		var url = basePath + "attendance/leave/queryDiffLeaveDay.do?startDate=" + startTime + "&endDate=" + endTime + "&category=" + category;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				var val = AtdDataFormat.formatDoubleDays(data.diffDay);
				$("#leaveItemFrom").iForm("setVal",{leaveDays:val});
				if( data.result == "success" ){
					flag = true;
				}else if( data.result == "fail" ){
					FW.error( data.reason );
					reason = data.reason;
					flag = false;
				}
			}
		});
		
	}
	
	//校验
	function valid(){
		if(!flag){
			FW.error(reason);
			return flag;
		}
		var data = $("#leaveItemFrom").iForm("getVal");
		var startDate = data.startDate;
		var category = data.category;
		var endDate = data.endDate;
		/* if( !isNull( startDate ) && !isNull( endDate ) ){
			calcDay( startDate, endDate,category );
		} */
		return $("#leaveItemFrom").valid()/*  & flag */;
	}
	
	//修改时间
	function onChangeFileds(){
		var data = $("#leaveItemFrom").iForm("getVal");
		var startDate = FW.long2time(data.startDate);
		var category = data.category;
		var endDate = FW.long2time(data.endDate);
		var leaveDays = data.leaveDays;
		if( !isNull( startDate ) && !isNull( endDate ) ){
			calcDay( startDate, endDate,category, leaveDays );
		}
	}
	
	$(document).ready(function() {
		$("#leaveItemFrom").iForm("init",{"options":opts,"fields":fields});
		
		var date = FW.time2long(new Date().Format("yyyy-MM-dd hh:00")) ;
		$("#leaveItemFrom").iForm("setVal",{startDate:date, endDate:date, leaveDays:0});
		
		$("#f_startDate,#f_endDate").change( function(){
			onChangeFileds();
		} );
	});
		
</script>

</head>
<body>
	<form id="leaveItemFrom" style="width:430px;margin-left:45px;margin-top:15px"></form>
</body>
</html>