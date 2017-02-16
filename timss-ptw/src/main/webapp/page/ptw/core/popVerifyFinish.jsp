<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>结束工作票验证</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var latestWpic = '<%=request.getParameter("latestWpic")%>';
	$(document).ready(function() {
		if(latestWpic == "null"){
			latestWpic = "";
		}
		
		var fields = [
			{id:"finWpic",title : "工作负责人",type:"label",value:latestWpic},
			{id:"finWl",title : "结束许可人",type:"label",value:userName, linebreak:true},
			{id:"finTime",title : "实际完工时间",type:"datetime",dataType:"datetime",value:new Date(), rules : {required:true}, linebreak:true,wrapXsWidth:10,wrapMdWidth:10},
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
		<span>全部工作班人员已清楚工作结束，并撤离工作区域；<br/> 现场已清理完毕，已对运行值班员作必要的技术交底。</span>
	</div>
	<form id="bizForm" style="margin-left: 20px;"></form>
</body>
</html>