<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String pvData = request.getAttribute("pvData")==null?"":String.valueOf(request.getAttribute("pvData"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>物资明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}page/asset/itcui_timssutil.js?ver=${iVersion}"></script>
<script>
	var pvData = '<%=pvData%>';
	var form = [
			{title : "名称", id : "name",breakAll:true},
	    	{title : "电话", id : "spiCellphone"},
		    {title : "公司类型", id : "type"},
		    {title : "详细地址", id : "address",wrapXsWidth :8}
	];
	//编辑表单加载数据（通用方法）
	function initForm(){
		$("#autoform").iForm("init",{"fields":form,"options":{validate:true,labelFixWidth:140,xsWidth:4}});
		if("" != pvData){
			var loaddata = JSON.parse(pvData);
			$("#autoform").iForm("setVal",loaddata);
		}else{
			$("#grid_error").show();
		}
	}
	
	
	$(document).ready(function() {
		initForm();
		$("#autoform").ITC_Form("readonly");
	});
	
</script>
</head>
<body>
	<form id="autoform" class="margin-form-title autoform"></form>
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有选择供应商信息</div>
			</div>
		</div>
	</div>
</body>
</html>