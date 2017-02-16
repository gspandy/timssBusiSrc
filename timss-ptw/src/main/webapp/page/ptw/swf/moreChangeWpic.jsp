<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>工作负责人变更</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var chaOldWpic = '<%=request.getParameter("chaOldWpic")%>';
	$(document).ready(function() {
		var fields = [
			{id:"chaOldWpic",title : "原工作负责人",type:"label",value:chaOldWpic},
			{id:"chaNewWpic",title : "变更后工作负责人",rules : {required:true}, linebreak:true,wrapXsWidth:10,wrapMdWidth:10},
			{id:"chaWl",title : "许可人",type:"label",value:userName, linebreak:true},
			{id:"chaSignTime",title : "变更时间",type:"datetime",dataType:"datetime", rules : {required:true},value:new Date(), linebreak:true,wrapXsWidth:10,wrapMdWidth:10}
		];	
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	function getFormData(){
		if(! $("#bizForm").valid()){
			return null;
		}
		var formData = $("#bizForm").iForm("getVal");
		return formData;
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<form id="bizForm" style="margin-left: 50px;margin-top: 20px;"></form>
</body>
</html>