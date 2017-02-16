<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>增加任务</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var latestWpic = '<%=request.getParameter("latestWpic")%>';
	$(document).ready(function() {
		var fields = [
			{id:"remarkWorkContent",title : "工作内容",type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:12,rules : {required:true}},
			{id:"remarkWpic",title : "工作负责人",type:"label",value:latestWpic, linebreak:true},
			{id:"remarkWl",title : "许可人",type:"label",value:userName, linebreak:true},
			{id:"remarkSignTime",title : "许可时间",type:"datetime",dataType:"datetime",value:new Date(), rules : {required:true},wrapXsWidth:8,wrapMdWidth:8}
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
	<form id="bizForm" style="margin-top: 20px;"></form>
</body>
</html>