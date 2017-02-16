<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>添加上下联关系</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/asset/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/asset/commonForm.js?ver=${iVersion}'></script>

<script>
//up--上关联 || down -- 下关联
var type = "${ type }";
//ci id
var ciId = "${ ciId }";
//ci id 对应的bean
var bean = '${ bean }';

//上下联 iHint 通过名称模糊搜索
function searchCiHint( type ){
	var $firstPartyInput=$('#f_' + type + "CiName");
	
	var ciType = '';
	if( type == "up" ){
		ciType = $("#f_downCiId").val();
	}else if( type == "down" ){
		ciType = $("#f_upCiId").val();
	}
	
	var url = basePath + "asset/cmdbCi/queryHintCmdbRelationByName.do?ciId=" + ciId + "&ciType=" + ciType;
	var firstPartyInit = {
		datasource : url,
		clickEvent : function( id, name ) {
			var keyArr = id.split("##");
			
			if( type == "up" ){
				$("#autoform").iForm("setVal",{upCiId: keyArr[0], upCiName: name, upCiType : keyArr[1] });
			}else if( type == "down" ){
				$("#autoform").iForm("setVal",{downCiId: keyArr[0], downCiName: name, downCiType : keyArr[1] });
			}
		},maxItemCount : 100
	};
	
	$firstPartyInput.change(function(){
		var val = $.trim( $firstPartyInput.val() );
		if( val == "" ){
			if( type == "up" ){
				$("#autoform").iForm("setVal",{upCiId: "", upCiName: "", upCiType : "" });
			}else if( type == "down" ){
				$("#autoform").iForm("setVal",{downCiId: "", downCiName: "", downCiType : "" });
			}
		}
	});
	$firstPartyInput.iHint('init', firstPartyInit);
}

var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "上联CI", id : "upCiId",type:"hidden"},
				{title : "上联名称", id : "upCiName",rules : {required:true}},
	  			{title : "上联CI类型", id : "upCiType",rules : {required:true}, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_CI_TYPE",
					options:{
						allowEmpty:true
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}},
				{title : "下联CI", id : "downCiId",type:"hidden"},
				{title : "下联名称", id : "downCiName",rules : {required:true},linebreak:true},
	  			{title : "下联CI类型", id : "downCiType",rules : {required:true}, type : "combobox",
					dataType : "enum",
					enumCat : "CMDB_CI_TYPE",
					options:{
						allowEmpty:true
					},
					render : function(id){
						$("#" + id).iCombo("setTxt","");
					}}
	  		];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true
	};
	
	//初始化form数据
	function loadFormData(){
		if( bean != null && bean != "" ){
			bean = JSON.parse( bean );
		}
		
		$("#autoform").iForm("setVal",bean);
		$("#autoform").iForm("endEdit");
		if( type == "up" ){
			$(".inner-title").html( "上联详情" );
			//$("#autoform").iForm("endEdit",["downCiId", "downCiName", "downCiType"]);
		}else if( type == "down" ){
			$(".inner-title").html( "下联详情" );
			//$("#autoform").iForm("endEdit",["upCiId", "upCiName", "upCiType"]);
		}
		
		$("#closeButtonDiv,#editButtonDiv").show();
		$("#backButtonDiv,#saveButtonDiv,#deleteButtonDiv").hide();
		FW.fixRoundButtons("#toolbar");
	}
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		searchCiHint( "up" );
		searchCiHint( "down" );
		
		loadFormData();
		
		//保存
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "asset/cmdbCi/insertCmdbRealation.do";
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
						if( data.msg != null && data.msg != "" ){
							FW.error( "保存失败 ," + data.msg );
						}else{
							FW.error( "保存失败 " );
						}
					}
				}
			});
		});
		
		//点击编辑
		$("#editButton").click(function(){
			if( type == "up" ){
				$("#autoform").iForm("endEdit",["downCiId", "downCiName", "downCiType"]);
				$("#autoform").iForm("beginEdit",["upCiId", "upCiName", "upCiType"]);
			}else if( type == "down" ){
				$("#autoform").iForm("beginEdit",["downCiId", "downCiName", "downCiType"]);
				$("#autoform").iForm("endEdit",["upCiId", "upCiName", "upCiType"]);
			}
			
			$("#closeButtonDiv,#editButtonDiv").hide();
			$("#backButtonDiv,#saveButtonDiv,#deleteButtonDiv").show();
			FW.fixRoundButtons("#toolbar");
		});
		
		//删除
		$("#deleteButton").click(function(){
			var formData = $("#autoform").iForm("getVal");
			var url = basePath + "asset/cmdbCi/deleteCmdbRelationById.do?id=" + formData.id;
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
	});
		
</script>

</head>
<body>
<div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <div id="toolbar" class="btn-toolbar ">
	        <div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default" id="closeButton" onclick="closeTab()">关闭</button>
	        </div>
	        <div class="btn-group btn-group-sm" id="backButtonDiv">
	            <button type="button" class="btn btn-default" id="backButton" onclick="loadFormData()">返回</button>
	        </div>
	         <div class="btn-group btn-group-sm" id="editButtonDiv">
	            <button type="button" class="btn btn-default" id="editButton">编辑</button>
            </div>
	         <div class="btn-group btn-group-sm" id="saveButtonDiv">
	            <button type="button" class="btn btn-success" id="saveButton">保存</button>
            </div>
	         <div class="btn-group btn-group-sm" id="deleteButtonDiv">
	            <button type="button" class="btn btn-default" id="deleteButton">删除</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建上下关联关系
	</div>
	<form id="autoform"></form>
</body>
</html>