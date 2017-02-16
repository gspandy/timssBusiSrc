<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html style="height:99%">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var flwId = '<%=request.getParameter("flwId")%>';  
	var cmtId = '<%=request.getParameter("cmtId")%>';
</script>

<script type="text/javascript"> 
 	
 	var filds = [
 			{title:"id",id:"cmtId",type:"hidden"},
 			{title:"处理人",id:"userName"},
 			{title:"处理时间",id:"creationTime",type:"datetime"},
	        {title:"分析与处理",id:"commentInfo",
		        type : "textarea",
		        linebreak:true,
		        wrapXsWidth:12,
		        wrapMdWidth:8,
		        height:110
		     }];             
	        
 	$(document).ready(function() {
 		$.post( basePath + "itsm/complainRecords/queryHandleInfo.do",{"flwId":flwId,"cmtId":cmtId},
 		  	function(result){ 
 		  		$("#anlyAndTreaForm").iForm("init",{"fields":filds,"options":{xsWidth:12,
					mdWidth:8,labelFixWidth:80}});//处理记录的表单
 		     	var data=JSON.parse(result.processRd);
	      	 	$("#anlyAndTreaForm").iForm("setVal",data[0]);
	      	 	$("#anlyAndTreaForm").iForm("endEdit");
 		  	},"json");
	}); 
</script>
</head>
	<body style="height: 100%;" class="bbox">
		<div id="formDiv" title="分析与处理">
    	 	<form id="anlyAndTreaForm" class="autoform"></form>
    	</div>
	</body>
</html>