<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<title>下一步操作</title>
<script src="${basePath}js/pms/common/common.js?ver=${iVersion}"></script>
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
	var msg = [{"title":"是否交由相关部门会签","ch1":"交由相关部门会签","ch2":"交由法务会签","ch3":"经营部经理审批"}
	];
	var index =getUrlParam('index');
	$(document).ready(function(){
		var fields = [
			{
				title : "下一步操作",
				id : "needCounterSign",
				type : "radio",
				data : [
					['N',msg[index]["ch3"],true],
					['Y',msg[index]["ch1"]],
					['S',msg[index]["ch2"]]
					
				],
				wrapXsWidth:10,
		        wrapMdWidth:10
		        //labelFixWidth:3==index?150:(0==index?180:200)
		    }
		];
		$("#form1").iForm("init",{"fields":fields});
	});
</script>
</head>
<body>
	<div class="bbox" id="toolbar_wrap" >
	<form id="form1"  class="margin-form-title margin-form-foldable">
	
	</form>
</div>
</body>
</html>