<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>编辑行列规则</title>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>

<script>
	var fields = [
				{title : "ID", id : "id", type:"hidden"},
				{title : "行列规则编号", id : "num", rules : {required:true}, linebreak:true},
	  			{title : "行列规则名称", id : "name",rules : {required:true}, linebreak:true},
	  			{title : "值别总数", id : "dutyCount",rules : {required:true,digits:true}, linebreak:true},
	  			{title : "天次总数", id : "period",rules : {required:true,digits:true}, linebreak:true},
	  			{title : "替换日期(每月)", id : "changeLimit",rules:{digits:true,range:[1,28]},linebreak:true},
	  			{title : "轮询班次规则", id : "pollSequence", rules:{digits:true},linebreak:true},
	  			{title : "生效", id : "available",rules : {required:true},linebreak:true,
	                  type : "radio",
	                  data : [
	                      ["Y","是"],
	                      ["N","否"]
	                  ]	
	  			}
	  		];
	
	var opts={
			validate:true,
			fixLabelWidth:true
		};
	
	//初始化表单
	function initForm( rowData ){
		var data = {
				"id" : rowData.id,
				"num" : rowData.num,
				"name" : rowData.name,
				"dutyCount" : rowData.dutyCount,
				"period" : rowData.period,
				"changeLimit" : rowData.changeLimit==0 ? "" : rowData.changeLimit,
				"pollSequence" : rowData.pollSequence,
				"available": rowData.available,
				"siteId": rowData.siteId
			};
			$("#autoform").iForm("setVal",data);
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		var rowData = FW.get("operationRowData");//'${param.rowData}';
		//rowData = JSON.parse(decodeURI( rowData ) );
		initForm( rowData );
		$( "#updateButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/rules/updateRules.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if (data.result == "success") {
						FW.success("修改成功 ！");
					} else {
						FW.error("修改失败 ！");
					}
					
				}
			});
		});
		
		$( "#deleteButton" ).click(function(){
			FW.confirm("确定删除本条数据吗？",function(){
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/rules/deleteRules.do";
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					data:{"formData":formData},
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除成功 ！");
							closeTab();
						}else{
							FW.error( "删除失败 ！");
						}
					}
				});
			});
		});
		
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar">
	        <div class="btn-group-sm">
	            <button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
				<button type="button" class="btn btn-default" id="deleteButton">删除</button>
				<button type="button" class="btn btn-default" id="updateButton">保存</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<form id="autoform"></form>
</body>
</html>