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
<script>_useLoadingMask = true;</script><jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
var session="<%=sessionid%>";
var valKey = "<%=valKey%>";
</script>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/bid/bid.js?ver=${iVersion}"></script>
<script src="${basePath}js/pms/sjw/bid/editBid.js?ver=${iVersion}"></script>
<script>
	var id=getUrlParam("id");
	
	$(document).ready(function() {
		var $form=$("#form1");
		$.post(basePath+"pms/bid/queryBidByBidId.do?bidId="+id,{},function(data){
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
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-readOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" class="btn btn-default pms-editOnly"  onclick="closeTab();">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button" onclick="temSave();" id="b-tmp-save">暂存</button>
	            <button type="button" class="btn btn-default pms-button" onclick="submit();" id="b-save">提交</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button"
	             onclick="addBidMethod();" id="b-add-bidmethod">添加评标方法</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button"
	             onclick="addBidResult();" id="b-add-bidresult">添加招标结果</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default pms-button"
	             onclick="addNewContract();" id="b-add-contract">添加合同</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title">
		招标详情
	</div>
	<form id="form1" class="margin-form-title margin-form-foldable"></form>

	<div id="bidMethodWrapper" grouptitle="评标信息">
		<form id="form2" class="margin-form-title margin-form-foldable"></form>
	</div>
	
	<div id="bidResultWrapper" grouptitle="招标结果">
		<form id="form3" class="margin-form-title margin-form-foldable"></form>
	</div>
	
	<div id="supplierListWrapper" grouptitle="招标单位" class="margin-group-bottom">
	    <div  class="margin-title-table">
			<table id="supplierList" class="eu-datagrid"></table>
		</div>
        <div class="btn-toolbar margin-foldable-button">
			<div class="btn-group btn-group-xs">
				  <button type="button" class="btn btn-success pms-button" onclick="addSupplier();"
				  id="b-add-supplier" >添加招标单位</button>
			</div>
		</div>
	</div>
	<div id="bidAttachFormWrapper" grouptitle="招标附件">
		<form id="bidAttachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
	<div id="bidMethodAttachFormWrapper" grouptitle="评标附件">
		<form id="bidMethodAttachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
	<div id="bidResultAttachFormWrapper" grouptitle="招标结果附件">
		<form id="bidResultAttachForm" class="margin-form-title margin-form-foldable"></form>
	</div>
</body>
</html>