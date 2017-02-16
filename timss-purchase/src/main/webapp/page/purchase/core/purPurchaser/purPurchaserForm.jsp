<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>买方资料表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<script>_useLoadingMask = true;</script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />

<script>

	var fields = [
          	{title : "id", id : "purchaserId", type: "hidden"},
          	{title : "买方名称", id : "purchaserName", wrapXsWidth:12,wrapMdWidth:8, rules : {required:true,maxChLength:60}},
              {title : "地址", id : "address", linebreak: true,wrapXsWidth:12,wrapMdWidth:8, rules : {required:true,maxChLength:100}},
              {title : "邮编", id : "zip", rules : {maxlength:20}},
              {title : "电话", id : "phone", linebreak: true, rules : {maxlength:40}},
              {title : "传真", id : "fax", rules : {maxlength:20}},
              {title : "开户行", id : "bank", linebreak: true, rules : {maxlength:100}},
              {title : "账号", id : "account", rules : {maxlength:20}},
              {title : "纳税识别号", id : "taxNo", rules : {maxlength:20}}];
	
	$(document).ready(function() {
		init();
	});
	
	function init(){
		$("#autoform").iForm("init",{"fields":fields,"options":{validate:true}});
		$.ajax({
			type : "POST",
			url: basePath+"purchase/purPurchaser/queryDetailBySiteId.do",
			dataType : "json",
			success : function(data) {
				if(typeof data.purchaserId != "undefined"){
					$("#btnCreate").hide();
					$("#btnSave").show();
					$("#autoform").iForm("setVal",data);
					$("#pageTitle").html("编辑买方合同资料");
				}
			}
		});
	}
	
	function update(){
		if(!$("#autoform").valid()){
			return;
		}
		var obj = $("#autoform").iForm("getVal");
		
		$.ajax({
			type : "POST",
			url: basePath+"purchase/purPurchaser/saveOrupdatePurPurchaser.do",
			data: {"purPurchaserBean":FW.stringify(obj)},
			dataType : "json",
			success : function(data) {				
				if(data.result=="success"){
					FW.success("保存成功");
					goBackList();
				}else{
					FW.error("保存失败");
				}
			}
		});
	}

	function create(){
		if(!$("#autoform").valid()){
			return;
		}
		var obj = $("#autoform").iForm("getVal");
		
		$.ajax({
			type : "POST",
			url: basePath+"purchase/purPurchaser/saveOrupdatePurPurchaser.do",
			data: {"purPurchaserBean":FW.stringify(obj)},
			dataType : "json",
			success : function(data) {				
				if(data.result=="success"){
					FW.success("保存成功");
					goBackList();
				}else{
					FW.error("保存失败");
				}
			}
		});
	}
	
	function goBackList(){
		FW.navigate(basePath+"purchase/purPurchaser/purPurchaserList.do");
	}
</script>
<style>
.btn-toolbar .btn-margin-fix{
	margin-left:0 !important;
	margin-right:7px !important;
}
</style>

</head>
<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnSave" style="display:none;">
				<button class="btn-success btn" onclick="update()">保存</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnCreate">
				<button class="btn-success btn" onclick="create()">保存</button>
			</div>
		</div>
	</div>
	
	<div class="inner-title" id="pageTitle">
		编辑买方合同资料
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>

	
</body>
</html>