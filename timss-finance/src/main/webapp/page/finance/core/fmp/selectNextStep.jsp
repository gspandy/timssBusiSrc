<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html  PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>选择下一步操作</title>
<script src="${basePath}js/core/fma/common/common.js?ver=${iVersion}"></script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var msg = [
		{"title":"是否提交总经理审批","ch1":"是","ch2":"否"}
	];
	var index ="${index}";
	$(document).ready(function(){
		var fields = [
			{
				title : msg[index]["title"], 
				id : "flag",
				type : "radio",
				data : [
					['Y',msg[index]["ch1"],true],
					['N',msg[index]["ch2"]]
				],
				wrapXsWidth:12,
		        wrapMdWidth:8
		    }
		];
		$("#form1").iForm("init",{"fields":fields, "options":{fixLabelWidth:false,labelWidth:6}});
	});
</script>
</head>
<body>
	<div style="margin-top:27px;"></div>
	<div class="bbox" id="toolbar_wrap" style="margin-left:30px;">
	<form id="form1"  class="margin-form-title margin-form-foldable">
	</form>
</div>
</body>
</html>