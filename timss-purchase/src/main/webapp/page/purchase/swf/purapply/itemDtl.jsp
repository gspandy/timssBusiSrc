<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>物资详情</title>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var result = ${result};
	$(document).ready(function(){
		var fields = [
			{title : "物资编号", id : "itemid",type : "label"},
			{title : "物资名称", id : "itemname",type : "label"},
			{title : "型号规格", id : "cusmodel",type : "label"},
			{title : "申请数量", id : "itemnum",type : "label"},
			{title : "批复数量", id : "repliednum",type : "label"},
			{title : "余量", id : "storenum",type : "label"},
			{title : "单位", id : "orderunitname",type : "label"},
			{title : "单价(元)", id : "averprice",type : "label"},
			{title : "小计(元)", id : "priceTotal",type : "label"},
			{title : "备注", id : "remark",type : "textarea",wrapXsWidth:12,wrapMdWidth:12,linebreak:true}
		];
		$("#form1").iForm("init",{"fields":fields});
		$("#form1").iForm("endEdit",["remark"]);
		$("#form1").iForm("setVal",result);
	});
</script>
</head>
<body>
<div style="margin-top:27px;"></div>
	<div class="bbox" id="toolbar_wrap" style="margin-left:30px;">
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
</div>
</body>
</html>