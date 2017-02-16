<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>审核信息验证</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var ptwStatus = <%=request.getParameter("ptwStatus")%>;
	ptwStatus = ptwStatus ? ptwStatus : "";
	var userName = ItcMvcService.user.getUserName();
	$(document).ready(function() {
		var issueName = "审核人";
		if(ptwStatus==320){
			issueName = "审批人";
		}
		var fields = [
			{id:"issueName",title : issueName,type:"label",value:userName},
			{id:"password",title : "密码",dataType:"password", linebreak:true,rules : {required:true},wrapXsWidth:10,wrapMdWidth:10,render:function(id){
				$("#" + id).keydown(function(e){
					var ev = e || window.event;
					if(e.keyCode == 13){
						e.preventDefault();
					}
				});
			}}
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
		<span>以上安全措施已正确完备，可执行。</span>
	</div>
	<form id="bizForm"></form>
</body>
</html>