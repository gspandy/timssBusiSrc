<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>新建参数项值</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>
//设置排序值
function setOrderType( ){
	var formData = $( "#autoform" ).iForm("getVal");
	
	var url = basePath + "asset/cmdbParams/queryOderTypeByType.do?ciType=" + formData.ciType + "&paramType=" + formData.paramType;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			$("#f_orderType").val( data.result );
		}
	});
}

var fields = [
				{title : "id", id : "id",type:"hidden"},
	  			{title : "CI类型", id : "ciType",rules : {required:true},linebreak:true, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_CI_TYPE",
					options:{
						allowEmpty:true,
						onChange : setOrderType
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}},
	  			{title : "参数项", id : "paramType",rules : {required:true},linebreak:true, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_PARAM_TYPE",
					options:{
						allowEmpty:true,
						onChange : setOrderType
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
				}},
	  			{title : "参数值", id : "paramVal",rules : {required:true},linebreak:true},
	  			{title : "排序", id : "orderType",rules : {required:true,digits:true},linebreak:true}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		//保存
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "asset/cmdbParams/insertCmdbParams.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  formData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						//resetForm();
						closeTab();
					}else{
						FW.error( "保存失败 ！");
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
		新建参数项
	</div>
	<form id="autoform"></form>
</body>
</html>