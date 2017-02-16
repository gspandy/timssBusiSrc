<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
String payId = request.getParameter("payId")==null||"".equals(request.getParameter("payId"))?String.valueOf(request.getAttribute("payId")):String.valueOf(request.getParameter("payId"));
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>发送ERP</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var payId = '<%=payId%>';
var _dialogEmmbed = true;
	var erpField=[
   {id:"payId",type:"hidden",data:payId},
   {
		id : "invNum",
		title : "发票编号",
		wrapXsWidth : 10,
		wrapMdWidth : 10,
		linebreak:true,
		rules : {
			required : true,
			maxChLength : 50
		}
	}, {
		id : "invDesc",
		title : "发票描述",
		linebreak:true,
		wrapXsWidth : 10,
		wrapMdWidth : 10,
		type:"textarea",
		rule : {
		    required : true,
			maxChLength : 200
		}
	}

	];
	$(document).ready(function() {
		$("#form1").iForm("init", {
			fields : erpField
		});
		$("#form1").iForm("setVal", {
			"payId":payId
		});
	});

</script>
</head>
<body>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
</body>
</html>