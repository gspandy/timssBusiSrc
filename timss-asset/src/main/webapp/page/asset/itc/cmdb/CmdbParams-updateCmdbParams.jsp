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
<script src='${basePath}js/asset/cmdbPrivCommon.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>

//初始化数据
function initFormData( cId ){
	var url = basePath + "asset/cmdbParams/queryCmdbParamsById.do?id=" + cId;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success"){
				$(".inner-title").html("参数项详情");
				$( "#autoform" ).iForm("setVal", data.bean );
				$( "#autoform" ).iForm("endEdit");
				$("#backButtonDiv, #deleteButtonDiv, #saveButtonDiv").hide();
				$("#closeButtonDiv, #editButtonDiv").show();
				FW.fixRoundButtons("#toolbar");
				//权限
				setParamsPriv();
			}else{
				FW.error("初始化数据错误！");
			}
		}
	});
}


var fields = [
				{title : "id", id : "id",type:"hidden"},
	  			{title : "CI类型", id : "ciType",rules : {required:true},linebreak:true, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_CI_TYPE",
					options:{
						allowEmpty:true
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}},
	  			{title : "参数项", id : "paramType",rules : {required:true},linebreak:true, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_PARAM_TYPE",
					options:{
						allowEmpty:true
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
		
		var id = '${id}';
		initFormData( id );
		
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
		
		//编辑
		$("#editButton").click(function(){
			$(".inner-title").html("编辑参数项");
			$( "#autoform" ).iForm("beginEdit",["orderType", "paramVal"]);
			$("#backButtonDiv, #deleteButtonDiv, #saveButtonDiv").show();
			$("#closeButtonDiv, #editButtonDiv").hide();
			FW.fixRoundButtons("#toolbar");
		});
		
		//删除
		$("#deleteButton").click(function(){
			var url = basePath + "asset/cmdbParams/deleteCmdbParamsById.do?id=" + id;
			FW.confirm("确定删除本条数据吗？该操作无法恢复。", function() {
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "删除成功 ！");
							//resetForm();
							closeTab();
						}else{
							FW.error( "删除失败 ！");
						}
					}
				});
			});
			
		});
		
		//返回
		$("#backButton").click(function(){
			initFormData( id );
		});
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_PARAMS_CLOSE" id="closeButton" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="backButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_PARAMS_BACK" id="backButton">取消</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="editButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_PARAMS_EDIT" id="editButton">编辑</button>
	        </div>
	         <div class="btn-group btn-group-sm" id="saveButtonDiv">
	            <button type="button" class="btn btn-success priv" privilege="CMDB_PARAMS_SAVE" id="saveButton">保存</button>
            </div>
	         <div class="btn-group btn-group-sm" id="deleteButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="CMDB_PARAMS_DELETE" id="deleteButton">删除</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		参数项详情
	</div>
	<form id="autoform"></form>
</body>
</html>