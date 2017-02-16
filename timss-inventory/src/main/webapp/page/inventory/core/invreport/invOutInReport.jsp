<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<title>采购申请列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<link href="${basePath}css/purchase/purchase.css" rel="stylesheet" type="text/css" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var siteId = '${siteId}';	
	var type = '${type}';
	var urlReport = null;
	var searchDateFrom = null;
	var searchDateTo = null; 
	var deptInput = null;
	var warehouseInput = null;
	
	//初始化按钮
	function initPackage(){
		$("#searchDateFrom").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		$("#searchDateTo").iDate("init",{datepickerOpts:{format:"yyyy-mm-dd",minView:2}});
		
		var newFrom = new Date().format("yyyy-MM")+"-01";
		$("#sDateFrom").val(newFrom);
		
		var newTo = FW.long2date(new Date().getTime());
		$("#sDateTo").val(newTo);
		
		//查询(单独申请)
		$("#btn_advlocal").click(function(){
			searchDateFrom = $("#sDateFrom").val();
			searchDateTo = $("#sDateTo").val();
			deptInput = $("#dept").val();
			warehouseInput = $("#warehouse").val();
			
			var fromlong = FW.time2long(FW.long2time(searchDateFrom));
			var tolong = FW.time2long(FW.long2time(searchDateTo));
			
			if(fromlong<=tolong){
				if("Out" == type){
					urlReport = fileExportPath+"preview?__report=report/TIMSS2_IMRLIST_001_html.rptdesign&__format=html&startDate="+searchDateFrom+"&endDate="+searchDateTo+"&siteid="+siteId+"&dept="+deptInput+"&warehouse="+warehouseInput;
				}else{
					urlReport = fileExportPath+"preview?__report=report/TIMSS2_IMTLIST_001_html.rptdesign&__format=html&startDate="+searchDateFrom+"&endDate="+searchDateTo+"&siteid="+siteId+"&dept="+deptInput+"&warehouse="+warehouseInput;
				}
				
				$("#grid_error").hide();
				$("#grid_wrap").show();
				$("#purApplyReportFrame").attr("src",urlReport);
			}else{
				FW.error("查询结束时间必须大于开始时间 ");
			}
			
		});
		var url = null;
		var title = null;
		if("Out" == type){
			url = fileExportPath+"preview?__report=report/TIMSS2_IMRLIST_001_pdf.rptdesign&__format=pdf&startDate="+$("#sDateFrom").val()+"&endDate="+$("#sDateTo").val()+"&siteid="+siteId+"&dept="+$("#dept").val()+"&warehouse="+$("#warehouse").val();
			title = "出库记录打印";
		}else{
			url = fileExportPath+"preview?__report=report/TIMSS2_IMTLIST_001_pdf.rptdesign&__format=pdf&startDate="+$("#sDateFrom").val()+"&endDate="+$("#sDateTo").val()+"&siteid="+siteId+"&dept="+$("#dept").val()+"&warehouse="+$("#warehouse").val();
			title = "入库记录打印";
		}
		FW.initPrintButton("#btn_print",url,title,null);
	} 
	
	//初始化执行js
	$(document).ready(function() {
		initPackage();
		//注册事件
		$("#searchDateFrom,#searchDateTo,#dept,#warehouse").blur(function(){
			changeDownloadUrl();
		}).on('changeDate',function(){
			changeDownloadUrl();
		});;
		
		if(null == urlReport){
			$("#grid_error").show();
			$("#grid_wrap").hide();
		}else{
			$("#grid_error").hide();
			$("#grid_wrap").show();
		}
		//打开页面就默认条件查询
		$("#btn_advlocal").click();
	});
	
	//更改下载地址
	function changeDownloadUrl(){
		var sDateFrom = $("#sDateFrom").val();
		var sDateTo = $("#sDateTo").val();
		var deptinput = $("#dept").val();
		var	warehouseinput = $("#warehouse").val();
		var url = null;
		var title = null;
		if("Out" == type){
			url = fileExportPath+"preview?__report=report/TIMSS2_IMRLIST_001_pdf.rptdesign&__format=pdf&startDate="+sDateFrom+"&endDate="+sDateTo+"&siteid="+siteId+"&dept="+deptinput+"&warehouse="+warehouseinput;
			title = "出库记录打印";
		}else{
			url = fileExportPath+"preview?__report=report/TIMSS2_IMTLIST_001_pdf.rptdesign&__format=pdf&startDate="+sDateFrom+"&endDate="+sDateTo+"&siteid="+siteId+"&dept="+deptinput+"&warehouse="+warehouseinput;
			title = "入库记录打印";
		}
		FW.initPrintButton("#btn_print",url,title,null);
	}
</script>
</head>
<body style="height: 100%;" class="bbox list-page">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <span class="ctrl-label pull-left">统计日期：</span>
		    <div class="pull-left" id="searchDateFrom" style="width: 130px;" style="margin-left:8px" input-id="sDateFrom"></div>
		    <span class="ctrl-label pull-left"  style="margin-left:8px">到：</span>
		    <div class="pull-left" id="searchDateTo" style="width: 130px;" input-id="sDateTo"></div>
	        
	        <span class="ctrl-label pull-left"  style="margin-left:8px">申请部门：</span>
	        <div class="input-group input-group-sm form-control-style">
			 	<input type="text" id="dept" icon="itcui_btn_mag" placeholder="请输入部门名称" style="width: 95%; float: left; border-width: 0px; outline: none; height: 24px; display: block;"/> 
			</div>
		    
		    <span class="ctrl-label pull-left"  style="margin-left:8px">仓库：</span>
		    <div class="input-group input-group-sm form-control-style" >
			 	<input type="text" id="warehouse" icon="itcui_btn_mag" placeholder="请输入仓库" style="width: 95%; float: left; border-width: 0px; outline: none; height: 24px; display: block;"/> 
			</div>
	        
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_advlocal">查询</button>
	        </div>
	        <div class="btn-group btn-group-sm" style="margin-left:7px;">
	            <button type="button" class="btn btn-default" id="btn_print">导出</button>
	        </div>
	    </div>
	</div>
	
	<div style="clear:both"></div>
	<div id="grid_wrap" style="width:100%;height:95%;">
	    <iframe frameborder="no" border="0" style="width:100%; height:100%;" id="purApplyReportFrame"></iframe>
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