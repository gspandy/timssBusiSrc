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
			{title : "型号规格", id : "itemcus",type : "label"},
			{title : "采购申请编号", id : "sheetno",type : "label"},
			{title : "采购人", id : "createUserName",type : "label"},
			{title : "采购量", id : "itemnum",type : "label"},
			{title : "单位", id : "orderunitname",type : "label"},
			{title : "税前单价(元)", id : "averprice",type : "label"},
			{title : "税费(元)", id : "tax",type : "label"},
			{title : "税率(%)", id : "taxRate",type : "label"},
			{title : "税后单价(元)", id : "cost",type : "label"},
			{title : "小计(元)", id : "priceTotal",type : "label"},
			{title : "状态", id : "recievestatus"},
			{title : "备注", id : "remark",type : "textarea",wrapXsWidth:12,wrapMdWidth:12,linebreak:true}
		];
		$("#form1").iForm("init",{"fields":fields});
		$("#form1").iForm("endEdit",["recievestatus","remark"]);
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