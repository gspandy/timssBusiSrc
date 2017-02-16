<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html style="min-height: 99%;">
<head>
<title>标准树</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src='${basePath}js/ptw/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/commonForm.js?ver=${iVersion}'></script>
<script src='${basePath}js/ptw/sjc/updateStdTree.js?ver=${iVersion}'></script>
<script type="text/javascript" src="${basePath}js/ptw/commonPtwTree.js?ver=${iVersion}"></script>

<script>
//本节点id
var currentId = '';
//本节点名字
var currentName = '';
//父节点ID
var parentId = '';
//是否是根节点
var rootFlag = false;
//第一次加载
var firstFlag = false;

//接受标准树
function toShow( node ){
	if( node.id != null && node.id != '' ){
		loadForm( node.id );
	}
}

//load form
function loadForm( id ){
	var url = basePath + "ptw/ptwStandardTree/queryStandardTreeById.do?id=" +  id;
	$.ajax({
		url : url,
		type : 'post',
		dataType : "json",
		success : function(data) {
			if( data.result == "success" ){
				setForm( data.ptwStdTreeVo );
			}else{
				FW.error( "加载失败 ！");
			}
		}
	});
}

//init form
function initRootForm(  ){
	var flag = window.parent.document.getElementById("ptwStandardTree").contentWindow.$;
	if( flag != null ) {
		var tree = getTree();
		if( tree.length != null && tree.length > 0 ){
			var node = tree.tree('getSelected');
			loadForm( node.id );
		}
	}
	else{
		var url = basePath + "ptw/ptwStandardTree/queryPtwStdRootIdBySite.do";
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					setForm( data.ptwStdTreeVo );
				}else{
					FW.error( "加载失败 ！");
				}
			}
		});
	}
	
	firstFlag = true;
}

//设置form表单数据
function setForm( formData ){
	currentName = formData.name;
	currentId = formData.id;
	parentId = formData.parentId;
	//是否是根节点
	rootFlag = parentId == 0 ? true : false;
	
	var data = {
			"id" : formData.id,
			"name" : formData.name,
			"parentId" : formData.parentId,
			"parentName" : formData.parentName,
			"code" : formData.code,
			"equipmentId" : formData.equipmentId,
			"equipmentName" : formData.equipmentName,
			"remark" : formData.remark
	};
	
	$("#autoform").iForm("setVal", data);
	$("#autoform").iForm("endEdit");
	$("#editBtn").show();
	$("#createBtn").show();
	$("#backBtn").hide();
	$("#saveButton").hide();
	$("#deleteButton").hide();
	//权限控制
	privButton();
	FW.fixToolbar("#toolbar");
}

var fields = [
              {title : "id", id : "id" , type: "hidden"},
				{title : "名称", id : "name" ,rules : {required:true/* ,remote:{
					url: basePath + "ptw/ptwStandardTree/queryCheckStdTreeName.do",
					type: "post",
					data: {
						"paramsMap" : function(){
							var params = {
									"name": $("#f_name").val(),
							};
							 return FW.stringify(params);
						}
						
					}
				} */},
              	 messages:{remote:"名称已存在，请输入其他名称"}
				},
	  			{title : "编码", id : "code",rules : {required:true}},
	  			{title : "父节点", id : "parentId", type: "hidden"},
	  			{title : "父节点", id : "parentName"},
	  			{title : "设备树", id : "equipmentId", type: "hidden"},
	  			{title : "设备树", id : "equipmentName",rules : {required:true},
					render:function(id){
		            	 var ipt = $("#" + id);
		            	 ipt.removeClass("form-control").attr("icon","itcui_btn_mag");
		            	 ipt.ITCUI_Input();
		            	 ipt.next(".itcui_input_icon").on("click",function(){
		                     var src = basePath + "ptw/ptwStandardTree/queryDeviceTreePage.do";
		                     var dlgOpts = {
		                         width : 400,
		                         height:500,
		                         closed : false,
		                         title:"选择设备树",
		                         modal:true
		                     };
		                     var btnOpts = [{
		             			"name" : "取消",
		             			"float" : "right",
		             			"style" : "btn-default",
		             			"onclick" : function() {
		             				return true;
		             			}
		             		},{
		             			"name" : "确定",
		             			"float" : "right",
		             			"style" : "btn-success",
		             			"onclick" : function() {				
		             				var conWin = _parent().window.document.getElementById("itcDlgContent").contentWindow;
		             				var node = conWin.getSelectedNode();
		             				$("#f_equipmentId").val( node.id );
		             				$("#" + id ).val( node.text );
		             				return true;
		             			}
		             		} ];
		                     Notice.dialog(src,dlgOpts,btnOpts);
		                 });
		             }},
	  			{title : "备注", id : "remark",type:"textarea", linebreak:true,
	  				wrapXsWidth:12,
	  				wrapMdWidth:8,
	  				height : 50
	  			}
              ];
	  		
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	//权限按钮控制
	function privButton(){
		//新建
		Priv.map("privMapping.PTW_STD_NEW","PTW_STD_NEW");
		//编辑
		Priv.map("privMapping.PTW_STD_EDIT","PTW_STD_EDIT");
		Priv.apply();
	}
	
	$(document).ready(function() {
		//树初始化
		initStandardTree();
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		
		//初始化根节点
		initRootForm();
		$( "#saveButton" ).click(function(){
			if(!$("#autoform").valid()){
				return;
			}
			var formData = getFormData( "autoform" );
			var url = basePath + "ptw/ptwStandardTree/insertOrUpdateStandard.do" ;
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data : {
					formData : formData
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						setForm( data.ptwStdTreeVo );
						updateTreeNode( data.ptwStdTreeVo );
						//closeTab();
					}else{
						if( data.reason != null ){
							FW.error( data.reason );
						}else {
							FW.error( "保存失败 ！");
						}
					}
				}
			});
		});
		
		//新建
		$("#createBtn").click(function(){
			var url = basePath + "ptw/ptwStandardTree/insertStdTreePage.do?parentId="
					+ currentId + "&parentName=" + currentName;
			addTabWithTreeNoReload( "addStandardTree", "新建标准树节点", url,"ptw");
		});
		
		//点击编辑
		$("#editBtn").click(function(){
			$("#autoform").iForm("beginEdit" ,['name','code','equipmentName','remark'] );
			$("#editBtn").hide();
			$("#createBtn").hide();
			$("#backBtn").show();
			$("#saveButton").show();
			$("#deleteButton").show();
			FW.fixToolbar("#toolbar");
		});
		
		//点击返回
		$("#backBtn").click(function(){
			$("#autoform").iForm("endEdit",['name','code','equipmentName','remark']);
			$("#editBtn").show();
			$("#createBtn").show();
			$("#backBtn").hide();
			$("#saveButton").hide();
			$("#deleteButton").hide();
			FW.fixToolbar("#toolbar");
		});
		
		//点击删除
		$("#deleteButton").click(function(){
			if(rootFlag){
				//根节点不可删除
				FW.error("根节点不可删除");
				return;
			}
			FW.confirm("确定删除" + currentName + "节点及其子节点吗？该操作无法恢复。", function() {
				var url = basePath + "ptw/ptwStandardTree/deleteStandardTree.do?id=" + currentId;
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success("删除成功！");
				        	deleteTreeNode( parentId );
						}else{
							FW.error( "保存失败 ！");
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
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="backBtn">返回</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-success priv only-hide" privilege="PTW_STD_NEW" id="createBtn">新建</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv only-hide" privilege="PTW_STD_EDIT" id="editBtn">编辑</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="saveButton">保存</button>
            </div>
	        <div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default" id="deleteButton">删除</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		标准树信息
	</div>
	<form id="autoform"></form>
</body>
</html>