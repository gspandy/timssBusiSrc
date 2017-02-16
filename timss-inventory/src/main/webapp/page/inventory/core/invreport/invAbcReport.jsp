<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>库存ABC页面</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var siteId = '${siteId}';	
	
	//初始化按钮
	function initPackage(){
		var urlReport = fileExportPath+ "preview?__format=pdf&__report=report/TIMSS2_ABC_001_pdf.rptdesign&siteid="+siteId;
		//var url = fileExportPath + "preview?__format=xlsx&__report=report/TIMSS2_ABC_001_xlsx.rptdesign&siteid="+siteId;
		//查询(单独申请)
		//$("#btn_advlocal").click(function(){
			$("#grid_error").hide();
			$("#grid_wrap").show();
			$("#AbcReportFrame").attr("src",urlReport);
		//});
		
		//FW.initPrintButton("#btn_print",url,"库存ABC打印",null);
		//FW.fixToolbar("#toolbar");
	} 
	
	//初始化执行js
	$(document).ready(function() {
		initPackage();
		
		if(null == urlReport){
			$("#grid_error").show();
			$("#grid_wrap").hide();
		}else{
			$("#grid_error").hide();
			$("#grid_wrap").show();
		}
	});
	
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<!--<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar">
	        
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	         <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_print">导出</button>
	        </div> 
	    </div>
	</div>-->
	
	<div class="inner-title" id="pageTitle"> 库存ABC查询 </div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="AbcReportFrame"></iframe>
	</div>
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有查询到相关数据</div>
			</div>
		</div>
	</div>
</body>
</html>