<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>下一步操作</title>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var msg = [{"title":"是否交由相关部门会签","ch1":"是","ch2":"否"},
	           {"title":"下一步操作","ch1":"直接录入结算信息","ch2":"提交合同审批流程"},
	           {"title":"公司副总审批会签","ch1":"是","ch2":"否"},
	           {"title":"是否交由董事长","ch1":"是","ch2":"否"}
	];
	var index =getUrlParam('index');
	$(document).ready(function(){
		var fields = [
			{
				title : msg[index]["title"], 
				id : "needCounterSign",
				type : "radio",
				data : [
					['Y',msg[index]["ch1"],true],
					['N',msg[index]["ch2"]]
				],
				wrapXsWidth:12,
		        wrapMdWidth:12,
		        labelFixWidth:3==index?150:(0==index?180:200)
		    }
		];
		$("#form1").iForm("init",{"fields":fields});
	});
</script>
</head>
<body>
<div style="margin-top:27px;"></div>
	<div class="bbox" id="toolbar_wrap" style="margin-left:30px;">
	<form id="form1"  class="margin-form-title margin-form-foldable">
	
	</form>
</div>
</body>
</html>