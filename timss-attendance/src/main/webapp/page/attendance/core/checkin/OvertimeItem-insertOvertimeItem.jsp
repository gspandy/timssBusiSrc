<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建加班单明细</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/formatterDate.js?ver=${iVersion}'></script>

<script>
var isToUnify = "${isToUnify}";

var fields = (isToUnify=="1")?
	[
		{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType: "datetime"},
		{title : "结束时间", id : "endDate",rules : {required:true,greaterThan:"#f_startDate"},type : "datetime",dataType: "datetime"},
 		{title : "合计工时", id : "planOverHours",rules : {required:true,number:true,min:0.001},linebreak:true}
 	]:
	[
		{title : "加班人员ID", id : "userId",type:"hidden"},
		{title : "部门ID", id : "deptId",type:"hidden"},
		{title : "加班人员", id : "userName",linebreak:true,rules : {required:true},
			render : function(id){
				$('#' + id).attr('placeholder','请输入姓名或账号').initHintPersonList();
				$('#' + id).initHintPersonList({
					clickEvent: function(id, name) {
						var info = id.split('_');
						$("#overtimeItemFrom").iForm("setVal",{userId:info[0],deptId:info[1],userName:name});
					}
				});	
			}
		},
		{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType: "datetime"},
		{title : "结束时间", id : "endDate",rules : {required:true,greaterThan:"#f_startDate"},type : "datetime",dataType: "datetime"},
 		{title : "合计工时", id : "planOverHours",rules : {required:true,number:true,min:0.001},linebreak:true},
 		{title : "备注", id : "remarks",type:"textarea"}
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
	
	function setHourTime( startDate, endDate ){
		//startDate = startDate.replace(/-/g,"/");
		//endDate = endDate.replace(/-/g,"/");
		var date1 = new Date( startDate );
		var date2 = new Date( endDate );
		var s1 = date1.getTime();
		var s2 = date2.getTime();
		var total = (s2 - s1)/1000;
		var hour = parseFloat( total / ( 60 * 60 ) ).toFixed(3);
		$("#overtimeItemFrom").iForm("setVal",{planOverHours:hour});
	}
	
	//校验
	function valid(){
		return $("#overtimeItemFrom").valid();
	}
	
	$(document).ready(function() {
		$("#overtimeItemFrom").iForm("init",{"options":opts,"fields":fields});
		
		$("#f_startDate,#f_endDate").change( function(){
			var data = $("#overtimeItemFrom").iForm("getVal");
			var startDate = data.startDate;
			var endDate = data.endDate;
			if( !isNull( startDate ) && !isNull( endDate ) ){
				setHourTime( startDate, endDate );
			}
		} );
	});
		
</script>

</head>
<body>
	<form id="overtimeItemFrom" style="width:430px;margin-left:45px;margin-top:15px"></form>
</body>
</html>