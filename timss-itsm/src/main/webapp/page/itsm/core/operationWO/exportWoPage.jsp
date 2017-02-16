<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
	<title>导出工单预览</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
	<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
	<!-- js引入的示例,basePath使用EL表达式获取 -->
	<script type="text/javascript">
	var _dialogEmmbed = true;
	var urlReport = null;
	var siteId = '<%=request.getParameter("siteId")%>'; 
	var beginTime = '<%=request.getParameter("beginTime")%>'; 
	var endTime = '<%=request.getParameter("endTime")%>'; 
	var enginnersId = '<%=request.getParameter("enginnersId")%>'; 
	var scValue = '<%=request.getParameter("scValue")%>'; 
	var sdList = '<%=request.getParameter("sdList")%>'; 
		
	$(document).ready(function() {
		var enginnersIdstr = "";
		if(enginnersId != null){
			engineersId = enginnersId.split(",");
			for(var i=0; i<engineersId.length; i++){
				enginnersIdstr +=  "'"+engineersId[i]+"',"
			}
			enginnersIdstr = enginnersIdstr.substring(0,enginnersIdstr.length-1)
			if(enginnersIdstr =="''"){
				enginnersIdstr="null";
			}
		}
		
		var scList = "";
		switch(scValue){
		case "all": scList = "null";break;
		case "request": scList = "'SCCONSULT','SCREQUEST','SCOTHER'";break;
		case "event": scList = "'SCFAULT'";break;
		default:break;
		}
		//fileExportPath = "http://10.0.17.164:8087/itc_report/";
		urlReport = fileExportPath+"preview?__report=report/TIMSS2_ITSM_ITWOEXPORT_html.rptdesign&siteId="+siteId+
										"&beginTime="+beginTime+"&endTime="+endTime+"&enginnersIdstr="+enginnersIdstr+"&sdList="+
										sdList+"&scList="+scList;  
		$("#exportWOFrame").attr("src",urlReport);
		
		//导出
		var exportUrl = fileExportPath+"preview?__report=report/TIMSS2_ITSM_ITWOEXPORT.rptdesign&__format=xls&siteId="+siteId+
										"&beginTime="+beginTime+"&endTime="+endTime+"&enginnersIdstr="+enginnersIdstr+"&sdList="+
										sdList+"&scList="+scList; 
		$("#exportWOData").bindDownload({
			url : exportUrl
		});
	});
	
	</script>
  </head>
  <body style="height:600px;min-width:850px" class="bbox">
    <div class="btn-group btn-group-sm" style="margin-top:8px">
     	<button type="button" class="btn btn-success" id="exportWOData">导出</button>
     </div>
    <div style="margin-top:8px;width: 100%;height: 100%;">
    	<iframe frameborder="no" border="0" style="width:100%; height:100%;" id="exportWOFrame"></iframe>
    </div>
    
  </body>
</html>
