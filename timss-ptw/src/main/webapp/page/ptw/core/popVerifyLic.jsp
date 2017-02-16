<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>许可验证信息</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/datagrid-groupview.js?ver=${iVersion}"></script>
<script>
	var isFireWt = <%=request.getParameter("isFireWt")%>;
	var userName = ItcMvcService.user.getUserName();
	var fireFields = [
			{id:"cfmGuardXf",title : "消防监护人",rules : {required:true,maxChLength:13},wrapXsWidth:5,wrapMdWidth:5},
			{id:"cfmGuardXfTime",title : "消防确认时间",type:"datetime",dataType:"datetime",rules : {required:true},wrapXsWidth:5,wrapMdWidth:5},
			{id:"issueName",title : "许可人",type:"label",value:userName,wrapXsWidth:5,wrapMdWidth:5},
			{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:5,wrapMdWidth:5}
		];
</script>
<script type="text/javascript" src="${basePath}js/ptw/core/popVerifyLic.js"></script>
<script>
function getFormData(){
	var formData = commonFormData();
	return {formData:formData,safeItems:safeItems};
}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div class="toolbar-with-pager">
		<div id="execComboDiv" style="float:right;">
	    	<div id="execComboWrap" style="float: right;display: none;">
	    		<select id="execCombo" style="width:120px;"></select>
	    	</div>
	    	<div id="checkDiv" style="float: right;width: 124px;">
			    <input type="checkbox" id="safeExecCheck">
			    <label for="safeExecCheck" style="font-size: 12px;">同一执行人</label>
			</div>
			
	    </div>
	</div>
	<table id="safeTable"></table>
	
	<form id="bizForm"></form>
</body>
</html>