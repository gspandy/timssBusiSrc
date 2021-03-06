<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String siteId = request.getAttribute("siteId")==null?"":String.valueOf(request.getAttribute("siteId"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script>
	var siteId = '<%=siteId%>';	
	var urlReport = null;
	var searchDateFrom = null;
	var searchDateTo = null;
	var searchYear = null;
	var browserType = ( navigator.userAgent.indexOf("Trident/7.0")>-1||navigator.userAgent.indexOf("IE")>-1||navigator.userAgent.indexOf("Edge")>-1)?"IE":"OTHER";
	function print(format,suffix){
		var sDateYear = $("#sDateYear").val();
		var sDateFrom = sDateYear+"-01-01";
		var sDateTo = sDateYear+"-12-31";
		var type = "SJW"==siteId?siteId:"ITC";
		var url = fileExportPath+"preview?__format="+format+"&__report=report/TIMSS2_"+type+"_PURGROUP_001_"+suffix+".rptdesign&startDate="+sDateFrom+"&endDate="+sDateTo+"&siteid="+siteId+"&__asattachment=true&browserType="+browserType;
		window.open(url);
	}
	//初始化按钮
	function initPackage(){
		$("#searchDateYear").iDate("init",{datepickerOpts:{format:"yyyy",minView:4,startView:4}});
		var newYear = new Date().format("yyyy");
		$("#sDateYear").val(newYear);
		//查询(单独申请)
		$("#btn_advlocal").click(function(){
			searchYear = $("#sDateYear").val();
			var selectYear = FW.time2long(FW.long2time(searchYear));
			var nowYear = FW.time2long(FW.long2time(new Date().format("yyyy")));
			if(selectYear<=nowYear){
				searchDateFrom = searchYear+"-01-01";
				searchDateTo = searchYear+"-12-31";
				var type = "SJW"==siteId?siteId:"ITC";
				urlReport = fileExportPath+"preview?__report=report/TIMSS2_"+type+"_PURGROUP_001_html.rptdesign&startDate="+ searchDateFrom +"&endDate="+ searchDateTo +"&siteid="+siteId;
				$("#grid_error").hide();
				$("#grid_wrap").show();
				$("#purGroupReportFrame").attr("src",urlReport);
			}else{
				FW.error("请选择今年或以往的年份进行查询！");
			}
		});
		changeDownloadUrl();
	} 
	
	//初始化执行js
	$(document).ready(function() {
		initPackage();
		$("#searchDateYear").blur(function(){
			changeDownloadUrl();
		}).on('changeDate',function(){
			changeDownloadUrl();
		});
		$("#btn_advlocal").click();
		if(null == urlReport){
			$("#grid_wrap").hide();
		}else{
			$("#grid_error").hide();
			$("#grid_wrap").show();
		}
	});
	
	
	//更改下载地址
	function changeDownloadUrl(){
		$("#btn_print1").click(function(){
			print("xls_spudsoft","xls");
		});
		$("#btn_print2").click(function(){
			print("pdf","pdf");
		});
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <span class="ctrl-label pull-left">统计年份：</span>
		    <div class="pull-left" id="searchDateYear" style="width: 130px;" style="margin-left:8px" input-id="sDateYear"></div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default dropdown-toggle" id="btn_print"  data-toggle="dropdown">
			                            &nbsp;导出&nbsp;
			        <span class="caret"></span>
			    </button>
			    <ul class="dropdown-menu">
			    	<li id="btn_print1"><a href="javascript:void(0)" >导出Excel</a></li>
			        <li id="btn_print2"><a href="javascript:void(0)" >导出PDF</a></li>
			    </ul>
	        </div>
	    </div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%;height:100%;" id="purGroupReportFrame"></iframe>
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