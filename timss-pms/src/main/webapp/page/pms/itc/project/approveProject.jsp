<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%
 	String path = request.getContextPath();
 	
 	String taskId = request.getParameter("taskId");
 	String processInstId = request.getParameter("processInstId");
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<title>工作票列表</title>
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/project/project.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/itc/project/approveProject.js?ver=${iVersion}"></script>
<script>
    var taskId = "<%=taskId%>";
    var processInstId = "<%=processInstId%>";
    var defKey="pms_itc_project";
    var modifiable=null;

	$(document).ready(function() {
		var form=$("#form1");
		
		$.post(basePath+'pms/project/queryProcessInstIdById.do',{id:processInstId,taskId:taskId},function(data){
			if(data && data.flag=='success'){
				var opt={
					form:$("#form1"),
					formFields:projectFormFields,
					data:data
				};
				pmsPager.init(opt);
			
			}else{
				FW.error(data.msg || "没有找到对应的项目立项信息");
			}
		});
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default">关闭</button>
	        </div>
	       	<div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="showWorkflow();">审批信息</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="shenpi();">审批</button>
	        </div>

	    </div>
	</div>
	<div class="inner-title">
		立项详情
	</div>
	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>