<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>固定资产统计</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var siteid = '${siteid}';
	//初始化执行js
	$(document).ready(function() {
		var urlExport = fileExportPath + "preview?__report=report/TIMSS2_ITC_ASSET.rptdesign&__format=html&siteid="+siteid+"&__svg=true&__locale=zh_CN&__timezone=PRC&__masterpage=true&__rtl=false&__cubememsize=10&__emitterid=org.eclipse.birt.report.engine.emitter.html";
		$("#assetStatisticFrame").attr("src",urlExport);
		changeExportUrl();
	});
	function changeExportUrl(){
		var urlExport = fileExportPath + "preview?__report=report/TIMSS2_ITC_ASSET.rptdesign&__format=pdf&siteid="+siteid;
		var	title = "固定资产报表";
		FW.initPrintButton("#btn_export",urlExport,title,null);
	}
	function refreshIFrame(){
		var trs =$('#assetStatisticFrame').contents().find("[id='__bookmark_1'] tr");
		if(undefined != trs && null != trs)
		$(trs[2]).hide();
	}
</script>
</head>
<body style="height: 95%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_export">打印</button>
	        </div>
	    </div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:94%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="assetStatisticFrame" onload="refreshIFrame()"></iframe>
	</div>
</body>
</html>