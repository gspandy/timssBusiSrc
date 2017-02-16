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
<title>招投标信息</title>
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/workflow/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/common/workflow.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/bid/bidResult.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sfc/bid/editBidResult.js?ver=${iVersion}"></script>
<script>
var loginUserId = ItcMvcService.user.getUserId();
	var id=getUrlParam("id");
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
	$(document).ready(function() {
		var $form=$("#form1");
		$.post(basePath+"pms/bid/queryBidResultById.do?id="+id,{},function(data){
			if(data && data.flag=="success"){
				var opt={
					form:$form,
					data:data,
					formFields:bidFormFields
				};
				pmsPager.init(opt);
			
			}else{
				FW.error(data.msg || "没有找到对应的招标信息,请尝试重新打开页面");
			}
		});
		
	});
</script>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar1" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm  ">
	        <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm  ">
	            <button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm  ">
	            <button type="button" class="btn btn-default pms-button" onclick="tmpSave();" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button" onclick="submit(this);" id="b-save" data-loading-text="提交">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button" onclick="del();"  id="b-del">删除</button>
	        </div>
	        <div class="btn-group btn-group-sm" >
	            <button type="button" class="btn btn-default" onclick="shenpi();" style="display:none;"  id="b-approve">审批</button>
	        </div>
	        <div class="btn-group btn-group-sm"   >
	            <button type="button" class="btn btn-default priv" privilege="pms-flow-void" onclick="zuofei();" id="b-pms-void-flow">作废</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="viewWorkFlow();">审批信息</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title">
		招标详情
	</div>
	<form id="form1" class="margin-form-title margin-form-foldable"></form>

	
	<div id="bidAttachFormWrapper" grouptitle="招标附件">
		<form id="bidAttachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
	
</body>
</html>