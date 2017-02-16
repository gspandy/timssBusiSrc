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

<script type="text/javascript"> 
	var editFlag = '<%=request.getParameter("editFlag")%>';  
	var rowData = FW.get("rowRiskData");  
 	var fields = [
 			{title:"delteId",id:"delteId",type:"hidden"},
 			{title:"Id",id:"id",type:"hidden"},
 			{title:"可承受风险",id:"bearFlag",type : "combobox",linebreak:true,
 			 rules : {required:true},
 			 data : [["true","是"],
				     ["false","否"]]
			},
 			{title:"风险点",id:"riskPoint",rules : {required:true,maxChLength:200},
 				wrapXsWidth:12,wrapMdWidth:8,height:110
 			},
 			{title:"主要风险源",id:"riskSource",rules : {required:true,maxChLength:100},
 				wrapXsWidth:12,wrapMdWidth:8,height:110
 			}, 			
 			{title:"所采取的控制措施",id:"safeItem", type : "textarea",
 				rules : {required:true,maxChLength:340},
 				linebreak:true,
   		        wrapXsWidth:12,
   		        wrapMdWidth:8,
   		        height:150
 			},
	        {title:"备注",id:"remarks",type : "textarea",rules:{maxChLength:170},
	        	linebreak:true,
   		        wrapXsWidth:12,
   		        wrapMdWidth:8,
   		        height:50
	        }
	        ];             
	   
	
	
 	$(document).ready(function() {
		$("#riskForm").iForm("init",{"fields":fields,"options":{validate:true,fixLabelWidth:false,labelWidth:2}});
		if(rowData){
		 	$("#riskForm").iForm("setVal",rowData);
		 	if(editFlag == "false"){
		 		$("#riskForm").iForm("endEdit");
		 	}
		 	FW.set("rowRiskData", null);
		}
	} ); 
	  	
</script>
</head>
	<body style="height: 100%;" class="bbox">
    	 	<form id="riskForm" class="autoform"></form>
	</body>
</html>