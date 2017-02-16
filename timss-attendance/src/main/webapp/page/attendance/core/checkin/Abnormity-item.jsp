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
var isToUnify = "${isToUnify}";

var fields = (isToUnify=="1")?
	[
		{title : "异常类型", id : "category",rules : {required:true},
			type : "hidden",
			dataType : "enum",
			enumCat : "ATD_AB_CATEGORY",
			options:{
				allowEmpty:true
			},
			render : function(id){
				$("#" + id).iCombo("setTxt","");
			}
		},
		{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType: "datetime"},
		{title : "结束时间", id : "endDate",rules : {required:true,greaterThan:"#f_startDate"},type : "datetime",dataType: "datetime"}
 	]:
	[
		{title : "异常人员ID", id : "userId",type:"hidden"},
		{title : "部门ID", id : "deptId",type:"hidden"},
		{title : "异常人员", id : "userName",linebreak:true,rules : {required:true},
			render : function(id){
				$('#' + id).attr('placeholder','请输入姓名或账号').initHintPersonList();
				$('#' + id).initHintPersonList({
					clickEvent: function(id, name) {
						var info = id.split('_');
						$("#abnormityItemFrom").iForm("setVal",{userId:info[0],deptId:info[1],userName:name});
					}
				});	
			}
		},
		{title : "异常类型", id : "category",rules : {required:true},
			type : "hidden",
			dataType : "enum",
			enumCat : "ATD_AB_CATEGORY",
			options:{
				allowEmpty:true
			},
			render : function(id){
				$("#" + id).iCombo("setTxt","");
			}
		},
		{title : "开始时间", id : "startDate",rules : {required:true}, type : "datetime",dataType: "datetime"},
		{title : "结束时间", id : "endDate",rules : {required:true,greaterThan:"#f_startDate"},type : "datetime",dataType: "datetime"},
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
	
	//校验
	function valid(){
		return $("#abnormityItemFrom").valid();
	}
	
	$(document).ready(function() {
		$("#abnormityItemFrom").iForm("init",{"options":opts,"fields":fields});
	});
		
</script>

</head>
<body>
	<form id="abnormityItemFrom" style="width:430px;margin-left:45px;margin-top:15px"></form>
</body>
</html>