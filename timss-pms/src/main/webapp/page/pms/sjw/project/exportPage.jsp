<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>导出合同清单预览</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var urlReport = null;
	var siteid = "SJW"; 
	var beginTime = '<%=request.getParameter("beginTime")%>'; 
	var endTime = '<%=request.getParameter("endTime")%>'; 
	var type = '<%=request.getParameter("type")%>'; 

	var urlHtml="";
	var urlExcel="";
	if(type=='signed'){
		urlHtml=fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_SIGN_001_html.rptdesign&siteid="+siteid+
		"&startTime="+beginTime+"&endTime="+endTime;  
		urlExcel= fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_SIGN_001.rptdesign&__format=xls&siteid="+siteid+
		"&startTime="+beginTime+"&endTime="+endTime; 
	}else if(type=='unpayed'){
		urlHtml=fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_UNPAYED_001_html.rptdesign&siteid="+siteid+
		"&startTime="+beginTime+"&endTime="+endTime;  
		urlExcel= fileExportPath+"preview?__report=report/TIMSS2_PMS_SJW_CONTRACT_UNPAYED_001.rptdesign&__format=xls&siteid="+siteid+
		"&startTime="+beginTime+"&endTime="+endTime; 
	}
	$(document).ready(function() {
		$("#exportFrame").attr("src",urlHtml);
		$("#exportData").bindDownload({
			url : urlExcel
		});
	});
	
	</script>
  </head>
  <body style="height:600px;min-width:850px" class="bbox">
	  <div id="toolbar1" class="btn-toolbar ">
	    <div class="btn-group btn-group-sm" style="margin-top:8px">
	     	<button type="button" class="btn btn-success" id="exportData">导出</button>
	     </div>
	  </div>
    <div style="margin-top:8px;width: 100%;height: 100%;">
    	<iframe frameborder="no" border="0" style="width:100%; height:100%;" id="exportFrame"></iframe>
    </div>
  </body>
</html>
