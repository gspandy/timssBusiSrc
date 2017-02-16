<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	//获取List页面传送过来的参数，由于没有使用el表达式，所以只能通过java代码获取
	String type = request.getParameter("type")==null?"":String.valueOf(request.getParameter("type"));
	String companyNo = request.getParameter("companyNo")==null?"":String.valueOf(request.getParameter("companyNo"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>供应商表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script>
	var currFormStatus = null;
	var initFormStatus = null;
	var initListStatus = null;
	var currListStatus = null;
	var saveFlag = false;
	var formName = "autoform";
	var listName = null;
	
	var cNo = '<%=companyNo%>';
	
	$(document).ready(function() {
		VendorBtn.init();
		PurVendorPriv.init();
	
		//判断用户是新建操作还是查看操作
		if('edit' == '<%=type%>'){
			editForm(edit_field);
			$("#autoform").ITC_Form("readonly");
			$("#btn-submit").hide();
			$("#pageTitle").html("供应商");
		}else{
			$("#autoform").ITC_Form({validate:true,fixLabelWidth:true},new_field);
			$("#btn-edit").hide();
			$("#pageTitle").html("新建供应商");
		}
		FW.fixToolbar("#toolbar_wrap1");
		addFormCloseEvent();
	});
	
	function closeTab(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
</script>
<script src="${basePath}js/purchase/core/purvendor/purVendor.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purvendor/purVendorForm.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/core/purvendor/purVendorBtn.js?ver=${iVersion}"></script>
<style type="text/css">.btn-garbage{cursor:pointer;}</style>
</head>
<body>
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	<div class="btn-toolbar" role="toolbar" id="toolbar_wrap1">
        <div class="btn-group btn-group-sm" style="margin-left:0;margin-right:7px;">
	        <button type="button" class="btn btn-default" id="btn-close">关闭</button>
	        <button type="button" class="btn btn-default priv" id="btn-submit" privilege="companyinfo_commitApply">提交</button>
	        <button type="button" class="btn btn-default priv" id="btn-edit" privilege="companyinfo_modify">编辑</button>
	    </div>
	</div>
	</div>
	<div class="inner-title" id="pageTitle">
		供应商
	</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	
</body>
</html>