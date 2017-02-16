<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑结转信息</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/attendance/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/attendance/common/dataFormat.js?ver=${iVersion}'></script>

<script>


var fields = [
	  			{title : "ID", id : "id",linebreak:true,type: "hidden"},
	  			{title : "姓名", id : "userName",linebreak:true,hidden: true, type:"label"},
	  			{title : "结转年假天数", id : "annualRemain",rules : {required:true,number:true},linebreak:true},
	  			{title : "结转补休天数", id : "compensateRemain",rules : {required:true,number:true},linebreak:true},
	  			{title : "核减年假", id : "subAnualLeave",rules : {required:true,number:true},linebreak:true},
	  			{title : "备注", id : "remark2",linebreak:true},
	  			{title : "统一核减说明", id : "remark1",linebreak:true,type:"label"}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		xsWidth:11,
		mdWidth:11
	};
	

	function valid(){
		return $("#statItemFrom").valid();
	}
	
	//为空判断函数
	function isNull( arg1 ){
		return !arg1 && arg1!==0 && typeof arg1!=="boolean"?true:false;
	}
	
	function loadForm( rowData ){
		var data = {
				"id" : rowData.id,
				"userName" : rowData.userName + " ( " + rowData.userId + " )",
				"annualRemain" : AtdDataFormat.formatDoubleDays(rowData.annualRemain),
				"compensateRemain" : AtdDataFormat.formatDoubleDays(rowData.compensateRemain),
				"subAnualLeave" : AtdDataFormat.formatDoubleDays(rowData.subAnualLeave),
				"remark2" : rowData.remark2,
				"remark1" : rowData.remark1
			};
		
			$("#statItemFrom").iForm("setVal",data);
			
			if(  isNull( $.trim( rowData.remark1 ) ) ){
				$("#statItemFrom").iForm("hide",['remark1']);
			}
	}
	
	//初始化form
	function initForm( id ){
		var url =  basePath + "attendance/stat/queryStatById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success"){
					loadForm( data.rowData );
				}else{
					FW.error( "加载结转信息出错！" );
				}
			}
		});
	}
	
	$(document).ready(function() {
		$("#statItemFrom").iForm("init",{"options":opts,"fields":fields});
		
		var id = ${ id };
		
		initForm( id );
		
	});
		
</script>

</head>
<body>
	<form id="statItemFrom" style="width:430px;margin-left:45px;margin-top:15px"></form>
</body>
</html>