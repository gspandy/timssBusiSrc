<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>工作票重新开工</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var latestWpic = '<%=request.getParameter("latestWpic")%>';
	$(document).ready(function() {
		var fields = [
			{id:"resTime",title : "重新开工时间",type:"datetime",dataType:"datetime",value:new Date(), rules : {required:true},wrapXsWidth:10,wrapMdWidth:10},
			{id:"resWpic",title : "工作负责人",type:"label",value:latestWpic, linebreak:true},
			{id:"resWl",title : "许可人",type:"label",value:userName, linebreak:true}
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
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 15px;margin-bottom: 10px;">
		<span>工作负责人已确认重新开工的工作票及其附近所列的安全措施完整、无变更，可以安全工作。</span>
	</div>
	<form id="bizForm" style="margin-left: 50px;margin-top: 20px;"></form>
</body>
</html>