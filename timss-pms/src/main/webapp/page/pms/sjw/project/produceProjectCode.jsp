<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	String path = request.getContextPath();
 	
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>立项信息</title>

<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
var _dialogEmmbed = true;
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/project/project.js?ver=${iVersion}"></script>


<script>
	
    var projectCodeFormFields=[{
			title : "项目性质",
			id:"type",
			type : "combobox",
			dataType:"enum",
			enumCat:"PMS_PCODE_TYPE",
			linebreak:true,
	        rules: {
	            required: true
	        }
		},
		{
			title : "年度",
			id : "year",
			type : "combobox",
	        data:yearFields,
	        value:getCurrentYear(),
	        linebreak:true,
	        rules: {
                required: true
	        }
			
		}
	];
 
	$(document).ready(function() {
		var form=$("#form1");
		var opt={
				form:form,
				
				formFields:projectCodeFormFields
			};
		pmsPager.init(opt);
		
	});
	
	function valid(){
		return $("#form1").valid();
	}
	function getData(){
		return $('#form1').iForm("getVal");
	}
</script>
</head>
<body>
	

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	
	
</body>
</html>