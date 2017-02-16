<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>许可验证信息</title>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}js/ptw/datagrid-groupview.js?ver=${iVersion}"></script>
<script>
	var currFrame = FW.getFrame(FW.getCurrentTabId());
	var _dialogEmmbed = true;
	var userName = ItcMvcService.user.getUserName();
	var isFireWt = <%=request.getParameter("isFireWt")%>;
	$(document).ready(function() {
		
		var fields = [
			{id:"issueName",title : "许可人",type:"label",value:userName,wrapXsWidth:10,wrapMdWidth:10},
			{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:10,wrapMdWidth:10}
		];
		if(isFireWt){
			fields = [
				{id:"cfmGuardXfNo",title : "消防监护人",type:"combobox",rules : {required:true},
					options:{allowSearch:true,remoteLoadOn:"init",url:basePath+ "ptw/ptwInfo/queryPtwUsersByGroup.do?role=PTW_fire_mgr"},wrapXsWidth:10,wrapMdWidth:10},
				{id:"cfmGuardXfTime",title : "消防确认时间",type:"datetime",dataType:"datetime",rules : {required:true},wrapXsWidth:10,wrapMdWidth:10},
				{id:"issueName",title : "许可人",type:"label",value:userName,wrapXsWidth:10,wrapMdWidth:10},
				{id:"password",title : "密码",dataType:"password", rules : {required:true},wrapXsWidth:10,wrapMdWidth:10}
			];
		}
		$("#bizForm").iForm("init",{"fields" : fields,"options":{validate:true}});
	});
	
	
	
	function getFormData(){
		var formData = $("#bizForm").iForm("getVal");
		if(isFireWt){
			formData.cfmGuardXf = $("#f_cfmGuardXfNo").iCombo('getTxt');
		}
		return {formData:formData};
	}
</script>
</head>
<body style="height: 100%;" class="bbox">
	<div style="text-align: center;font-size: 14px;font-weight: bold;margin-top: 15px;margin-bottom: 10px;">
		<span>以上隔离措施以全部执行，与我厂的安全规程相一致。与此相关的工作可以安全进行，并且警示牌已经挂在了所有隔离点上。</span>
	</div>
	<form id="bizForm"></form>
</body>
</html>