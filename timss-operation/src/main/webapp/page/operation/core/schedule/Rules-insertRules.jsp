<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建行列规则</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>

<script>
	var fields = [
				{title : "行列规则编号", id : "num", rules : {required:true}},
	  			{title : "行列规则名称", id : "name",rules : {required:true}, linebreak:true},
	  			{title : "值别总数", id : "dutyCount",rules : {required:true,digits:true}, linebreak:true},
	  			{title : "天次总数", id : "period",rules : {required:true,digits:true}, linebreak:true},
	  			{title : "替换日期(每月)", id : "changeLimit", rules : {digits:true,range:[1,28]}, linebreak:true},
	  			{title : "轮询班次规则", id : "pollSequence",rules : {digits:true}, linebreak:true},
	  		];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
	
	//重置插入表单
	function resetForm(){
		var data = {
				"num" : "",
				"name" : "",
				"dutyCount" : "",
				"period" : "",
				"changeLimit" : "",
				"pollSequence" : "",
				"available":"Y",
				"siteId":"1"
			};
			$("#autoform").iForm("setVal",data);
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/rules/insertRules.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增成功 ！");
						resetForm();
					}else{
						FW.error( "新增失败 ！");
					}
				}
			});
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">添加</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<form id="autoform"></form>
</body>
</html>