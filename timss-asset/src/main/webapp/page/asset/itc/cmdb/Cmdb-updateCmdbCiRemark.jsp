<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>CI日志变更原因</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_dialogEmmbed=true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>


var fields = [
	  			{title : "变更原因", id : "remark1",linebreak:true}
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
	
	
	function valid(){
		return $("#statItemFrom").valid();
	}
	
	$(document).ready(function() {
		$("#statItemFrom").iForm("init",{"options":opts,"fields":fields});
	});
		
</script>

</head>
<body>
	<form id="statItemFrom" style="width:430px;margin-left:45px;margin-top:15px"></form>
</body>
</html>