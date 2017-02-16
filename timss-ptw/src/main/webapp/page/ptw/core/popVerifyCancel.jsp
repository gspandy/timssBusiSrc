<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>作废信息验证</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var userName = ItcMvcService.user.getUserName();
	var isStdWt = <%=request.getParameter("isStdWt")%>;
	$(document).ready(function() {
		if(isStdWt){
			$("#confirm_info").html("确认作废标准票？");
		}
		var fields = [
			{id:"cancelName",title : "作废人",type:"label",value:userName},
			{id:"password",title : "密码",dataType:"password", linebreak:true,rules : {required:true},wrapXsWidth:10,wrapMdWidth:10}
		];	
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	function getFormData(){
		if(! $("#bizForm").valid()){
			return null;
		}
		return $("#bizForm").iForm("getVal");
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 15px;margin-bottom: 10px;">
		<span id="confirm_info">确认作废工作票？</span>
	</div>
	<form id="bizForm"></form>
</body>
</html>