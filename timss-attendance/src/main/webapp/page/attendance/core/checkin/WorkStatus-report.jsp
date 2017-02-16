<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>考勤异常明细</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/formatterDate.js?ver=${iVersion}'></script>

<script>
	var startYear='${startYear}';
	var startMonth='${startMonth}';
	var endYear='';
	var endMonth='';
	var yearSelectData=[];
	var monthSelectData =[];
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	//校验
	function valid(){
		return $("#reportFrom").valid();
	}
	
	function getYear(){
		var year=endYear;
		yearSelectData=[[year,year+"年",true]];
		while(year>startYear){
			year--;
			yearSelectData.push([year,year+"年"]);
		}
	}
	function setMonth(year){
		monthSelectData=[];
		var m=year==startYear?startMonth:1;
		var n=year==endYear?endMonth:12;
		for(var i=n;i>=m;i--){
			monthSelectData.push([i,i+"月"]);
		}
		$("#f_month" ).iCombo("init", {
			data : monthSelectData
		});	
	}
	
	$(document).ready(function() {
		var now=new Date();
		endYear=parseInt(now.getFullYear());
		endMonth = parseInt(now.getMonth())+1;
		getYear();
		
		var fields = [
			{title:"值别",id:"dutyName"},
			{title:"部门",id:"deptName"},
			{title : "年份", id : "year",rules : {required:true},
				type : "combobox",
				data: yearSelectData,
				options:{
					onChange:function(val){
						setMonth(val);
					}
				}
			},
			{title : "月份", id : "month",rules : {required:true},
				type : "combobox",
				data: []
			}
	 	];	
	 	var opts={
			validate:true,
			fixLabelWidth:true,
			xsWidth:11,
			mdWidth:11
		};
		$("#reportFrom").iForm("init",{"options":opts,"fields":fields});
	});
		
</script>

</head>
<body>
	<form id="reportFrom" style="width:300px;margin-top:15px"></form>
</body>
</html>