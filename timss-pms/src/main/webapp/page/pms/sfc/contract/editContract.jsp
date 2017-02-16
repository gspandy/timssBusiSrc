<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@page import="com.yudean.itc.util.Constant"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<%
 	SecureUser operator = (SecureUser)session.getAttribute(Constant.secUser);
 	String valKey = FileUploadUtil.getValidateStr(operator, FileUploadUtil.DEL_OWNED);
 	String sessionid=session.getId();
%>
<head>
<title>合同信息</title>

<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/contract/contract.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/contract/editContract.js?ver=${iVersion}"></script>
<script>
	var defKey="pms_sfc_contractapp";
	var contractId=getUrlParam('contractId');
	var changeContractStatus=getUrlParam("changeContractStatus");
	var flowId=getUrlParam("processInstId");
	var userId = ${userId};
	
	Priv.map("hasPmsFlowVoidPrip()","pms-flow-void");
    
    function hasPmsFlowVoidPrip(){
    	var data=pmsPager.opt.data.data;
    	if(data.status!='approving'){
    		return false;
    	}else{
    		if(pmsPager.hasPri(pmsPager.opt,"voidFlow")){
    			return true;
    		}
    	}
    }
	pmsPager.editContractFirstInit = true;
	pmsPager.editContractFirstInitCC = true;
	$(document).ready(function() {
			initContractForm(contractId);
	});
	
	
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm ">
	            <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab(this);">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="tmpSaveContract(this);" data-loading-text="暂存" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="submitContract(this);" data-loading-text="提交" id="b-save">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button pms-gray-button" onclick="delContract(this);" data-loading-text="删除" id="b-del">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default" onclick="shenpi();" style="display:none;"  id="b-approve">审批</button>
	        </div>
	       
	        
	        
	        
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default priv" privilege="pms-flow-void" onclick="zuofei();" id="b-pms-void-flow">作废</button>
	        </div>
	        
	       
	       
			
	        <div class="btn-group btn-group-sm"  >
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();" id="pms-contractApp-wfinfo">审批信息</button>
	        </div>
	        
			
	        
	        
			
	    </div>
	</div>
	<div class="inner-title">
		合同详情
	</div>
	<form id="form1"  class="margin-form-title margin-form-foldable"></form>
	
	
	
	<div id="attachFormWrapper" grouptitle="附件">
		<form id="attachForm"  class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>