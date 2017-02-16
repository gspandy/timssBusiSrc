<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% String sessId = request.getSession().getId(); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>资产台账数据一键初始化</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var sessId = '<%=sessId%>';
	var searchFlag = false;
	
	function upload(){
		$("#file_upload").uploadify("upload");
	}
	
	$(document).ready(function() {
		//列表
		$("#astODUpload_list").iFold("init");
		initList();
		//上传组件
		$("#file_upload").uploadify({
			"swf": "${basePath}js/uploadify/uploadify.swf",
			"uploader": "${basePath}asset/astorganizedata/uploadExcel.do;jsessionid="+sessId,
	        "multi": false,
	        "auto": true,
	        "fileObjName": "excelFiles",
	        "fileExt":"*.xls",
	        "buttonText": "点击选择导入数据",
	        "successTimeout":300,
	        "rollover":true,
	        "onInit":function(){
	        	$("#SWFUpload_0").css("width","100%");
	        	$("#file_upload-button").css({"line-height":"1.5","height":"20px","width":"100%"});
	        },
	        "onUploadSuccess":function(file,data,response){
	            FW.success("数据上传成功");
	            searchFlag = true;
	            initList();
	        }
		});
		
		$("#moduleDownload").bindDownload({
			url : "${basePath}page/asset/core/astorganizedata/ast_init_import.xls"
		});
	});
</script>
<script src="${basePath}js/astorganizedata/astOrganizeDataUploadList.js?ver=${iVersion}"></script>
</head>
<body>

	<div class="inner-title" id="pageTitle"> ${orgName}资产台账数据一键导入
		<span style="font-size: 12px;text-decoration: underline;" id="moduleDownload">（导入模板下载）</span> 
	</div>
	<div id="pagination_1" class="toolbar-pager" bottompager="#bottomPager_1" style="margin-top: 5px;"></div>
	
	
	<!-- 上传数据列表   -->
	<div style="clear:both"></div>
	<div id="astODUploadGrid" class="margin-title-table">
		<table id="astODUpload_grid" class="eu-datagrid" pager="#pagination_1"></table>
	</div>
	<div id="bottomPager_1" style="width:100%"></div>
	
	
	
	<div style="line-height: 35px;">
		<div class="input-group input-group-sm" style="float:left;margin-top: 5px;margin-right: 10px;">
			<form id="form1" method="POST" enctype="multipart/form-data">
				<input type="input" id="file_upload" style="float: left"/>
			</form>
		</div>
	</div>
</body>
</html>