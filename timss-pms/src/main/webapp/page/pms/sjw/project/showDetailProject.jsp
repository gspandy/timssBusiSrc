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
<title>工作票列表</title>
<script>_useLoadingMask = true;</script><script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/project.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/project/detailProject.js?ver=${iVersion}"></script>

<script>
	
    var id=getUrlParam("id");
    var processInstId=getUrlParam("processInstId");
    var defKey="pms_itc_project";
	$(document).ready(function() {
		var form=$("#form1");
		$.post(basePath+'pms/project/queryProjectDetailById.do',{id:id,processInstId:processInstId},function(data){
			if(data && data.flag=='success'){
				var opt={
					form:form,
					data:data,
					formFields:projectFormFields
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
	        <div class="btn-group btn-group-sm pms-readOnly">
	            <button type="button" class="btn btn-default "  onclick="closeTab();">关闭</button>
	        </div>
	        
	   
			<div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();">审批信息</button>
	        </div>
		</div>
	</div>
	<div class="inner-title">
		项目详情
	</div>

	<form id="form1" class="margin-form-title margin-form-foldable"></form>
	
	<div id="bidListWrapper" class="margin-group-bottom">
		
		<div id="bidList" grouptitle="招标信息" class="margin-title-table">
			 <table id="test_grid1" class="eu-datagrid">
		        
		    </table>
		</div>
	</div>
	<div id="contractListWrapper" class="margin-group-bottom">
		<div id="contractList" grouptitle="合同信息" class="margin-title-table">
			 <table id="contractTable" class="eu-datagrid">
		    </table>
		</div>
	</div>
	<div id="checkoutListWrapper" class="margin-group-bottom">
		<div id="checkoutList" grouptitle="验收信息" class="margin-title-table">
			 <table id="checkoutTable" class="eu-datagrid">
		    </table>
		</div>
	</div>
	<div id="payListWrapper" class="margin-group-bottom">
		<div id="payList" grouptitle="结算信息" class="margin-title-table">
			 <table id="payTable" class="eu-datagrid">
		    </table>
		</div>
	</div>
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>