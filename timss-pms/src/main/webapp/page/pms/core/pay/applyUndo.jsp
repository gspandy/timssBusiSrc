<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
%>
<head>
<title>填写退票说明</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script>
	var redoFormField=[
					   {id:"undoRemark",title:"备注",type:"textarea",wrapXsWidth:12,wrapMdWidth:8,height:64,fixLabelWidth:true,labelFixWidth:80}
    ];
	$(document).ready(function() {
		$("#form1").iForm("init",{fields:redoFormField});
		$("#form1").iForm("beginEdit");
	});
	

</script>
</head>
<body>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
</body>
</html>