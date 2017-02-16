<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建上级通知</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>

<script>
var fields = [
	  			{title : "领导", id : "leaderUserName",rules : {required:true},
	  				render: function(id){
						$("#" + id).iHint("init",{
							datasource :"${basePath}" + "/user?method=hint",
							highlight : true,
							clickEvent : function(id,name){
								$("#f_leaderUserName").val(name);
								var leaderUserId = id.split("_")[0];
								$("#f_leaderUserId").val(leaderUserId);
							}
						});
					}
	  			},
	  			{title : "领导Id", id : "leaderUserId",rules : {required:true}, type : "hidden"},
	  			{
	  				title : "通知内容", 
	  				id : "content", 
	  				rules : {required:true},
	  				linebreak : true,
	  				type : "textarea",
	  				wrapXsWidth : 8,
	  				wrapMdWidth : 8,
	  				height : 50
	  			},
	  			/* {title : "接话人", id : "executeUserName",
	  			render: function(id){
					$("#" + id).iHint("init",{
						datasource :"${basePath}" + "/user?method=hint",
						highlight : true,
						clickEvent : function(id,name){
							$("#f_executeUserName").val(name);
							var executeUserId = id.split("_")[0];
							$("#f_executeUser").val(executeUserId);
						}
					});
				}
	  			},*/
	  			{title : "接话人Id", id : "executeUserId",rules : {required:true},type : "hidden" }
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
				"abbName" : "",
				"startTime" : "",
				"longTime" : "",
				"sortType" : "",
			};
			$("#autoform").iForm("setVal",data);
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		$("#f_executeUserId").val(0);
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
	      	}
			if($("#f_leaderUserId").val()==""){
				FW.error( "您输入的领导信息有误");
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/leaderNotice/insertLeaderNotice.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData : formData 
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增上级通知成功 ！");
						closeTab();
					}else{
						FW.error( "新增上级通知失败 ！");
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
	       		<button type="button" class="btn btn-default" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建上级通知
	</div>
	<form id="autoform"></form>
</body>
</html>