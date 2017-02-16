<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<!DOCTYPE html>
<html style="height:99%">
<head>
<title>添加人员</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var editFlag = '<%=request.getParameter("editFlag")%>'; 
	var rowData = FW.get("rowWorkerData");  
</script>
<script type="text/javascript"> 
 	var fields = [
 			{title:"delteId",id:"delteId",type:"hidden"},
 			{title:"Id",id:"id",type:"hidden"},
 			{title:"姓名",id:"name",rules : {required:true}},
 			{title:"性别",id:"sex",type : "combobox",
 			    rules : {required:true},
 			    data: [["M","男",true],
				       ["F","女"]]
 			},
 			{title:"安规考试成绩",id:"score"},
 			{title:"持有特种作业证",id:"certificate", linebreak:true,
 				wrapXsWidth:12,wrapMdWidth:8,height:110,
		        rules : {maxChLength:170}
		    },
	        
	        {title:"PPE情况",id:"ppe",type : "textarea",linebreak:true, 
	        	rules : {maxChLength:170},
 				linebreak:true,
   		        wrapXsWidth:12,
   		        wrapMdWidth:8,
   		        height:100
   		     }	        
	        ];             
	
 	$(document).ready(function() {
		$("#workerForm").iForm("init",{"fields":fields,"options":{validate:true,fixLabelWidth:false,labelWidth:2}});
		if(rowData){
		 	$("#workerForm").iForm("setVal",rowData);
		 	if(editFlag == "false"){
		 		$("#workerForm").iForm("endEdit");
		 	}
		 	FW.set("rowWorkerData", null);
		}
	} ); 
	  	
</script>
</head>
	<body style="height: 100%;" class="bbox">
    	 	<form id="workerForm" class="autoform"></form>
	</body>
</html>