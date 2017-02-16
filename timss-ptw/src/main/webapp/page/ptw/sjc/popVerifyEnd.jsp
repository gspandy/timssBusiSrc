<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>终结验证信息</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/datagrid-groupview.js?ver=${iVersion}"></script>
<script>
	var _dialogEmmbed = true;
	
	var userName = ItcMvcService.user.getUserName();
	$(document).ready(function() {
		var fields = [
			{id:"endWl",title : "终结许可人",type:"label",value:userName},
			{id:"endTime",title : "终结时间",type:"datetime",dataType:"datetime",value:new Date(), rules : {required:true}, linebreak:true,wrapXsWidth:10,wrapMdWidth:10},
			{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:10,wrapMdWidth:10}
		];	
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	function getFormData(){
		return {formData:$("#bizForm").iForm("getVal"),endJdxNum:"",endJdxNo:""};
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 10px;margin-bottom: 10px;">
		<span>本工作票上安全措施及补充安全措施已全部解除，警示牌已收回。</span>
	</div>
	<form id="bizForm"></form>
</body>
</html>