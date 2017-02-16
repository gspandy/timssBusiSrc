<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建工种</title>
<script>_useLoadingMask = true;</script>

<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/operation/common/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/operation/common/pageMode.js?ver=${iVersion}'></script>

<script>


var fields = [
				{title : "工种编码", id : "deptId",linebreak:true,type : "hidden"},
	  			{title : "名称", id : "name",rules : {required:true},linebreak:true},
	  			{title : "是否可用", id : "isActive",rules : {required:true},
	  				type : "radio",linebreak:true,
	  		        data : [
	  		            ['Y','是',true],
	  		            ['N','否']
	  		        ]
	  			}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	
	$(document).ready(function() {
		PageMode.objs.pageName="工种";
		PageMode.changeTitle("add");
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "operation/dept/insertDept.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data:{"formData":formData},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "新增工种成功 ！");
						//resetForm();
						closeTab();
					}else{
						FW.error( "新增工种失败 ！");
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
		新建工种
	</div>
	<form id="autoform"></form>
</body>
</html>