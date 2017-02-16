<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String unitId = request.getParameter("id")==null?"":String.valueOf(request.getParameter("id")); 
String mode=request.getParameter("mode")==null?"":String.valueOf(request.getParameter("mode"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>计量单位详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script>_useLoadingMask = true;</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
    var objs={};
	objs.unitId = '<%=unitId%>';
	objs.mode='<%=mode%>';
	
	if(objs.mode!='create'){
		objs.form = [
		         	{title : "单位名称", id : "unitname",rules : {	required : true,maxlength : 48,
		     				remote:{"url": basePath + "inventory/invunit/isCodeExist.do",
		     					type:"post",
		     					data:{
		     						code : function(){return $("#f_unitcode").val();},
		     						id : function(){return objs.unit.unitid;}
		     					}
		     				}
		     		}},
		         	{title : "使用状态", id : "active",type : "combobox",
		     	        data : [
		     	            ["Y","启用"],
		     	            ["N","停用"]
		     	        ]
		     		},
		     	    {title : "单位描述", id : "descriptions",rules : {maxlength : 50},type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height:48}		    
		     	];
	}else{
		objs.form = [
		         	{title : "单位名称", id : "unitname",rules : {	required : true,maxlength : 48}},
		         	{title : "使用状态", id : "active",type : "combobox",
		     	        data : [
		     	            ["Y","启用"],
		     	            ["N","停用"]
		     	        ]
		     		},
		     	    {title : "单位描述", id : "descriptions",rules : {maxlength : 50},type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height:48}		    
		     	];
	}
	
	objs.blankUnit={
		unitname:"",
		active:"Y",
		descriptions:""
	};
	
	//编辑表单加载数据（通用方法）
	function init(){
		$("#autoform").iForm("init",{"fields":objs.form,"options":{validate:true}});
		changeMode(objs.mode);
		if(objs.mode!='create'){
			loadData();
		}
	}
	
	$(document).ready(function() {
		init();
	});

	function changeMode(mode){
		if(!mode){
			mode='view';
		}
		if(mode=='view'){
			$("#autoform").ITC_Form("readonly");
			$("#btnSave,#btnCreate,#btnDel").hide();
			$("#btnEdit").show();
			$("#pageTitle").html("计量单位详情");
		}else if(mode=='create'){
			$("#autoform").iForm("setVal",objs.blankUnit);
			$("#autoform").ITC_Form("beginedit");
			$("#btnSave,#btnEdit,#btnDel").hide();
			$("#btnCreate").show();
			$("#pageTitle").html("新建计量单位");
		}else if(mode=='edit'){
			$("#autoform").ITC_Form("beginedit");
			$("#btnEdit,#btnCreate").hide();
			$("#btnSave,#btnDel").show();
			$("#pageTitle").html("计量单位详情");
		}
		objs.mode=mode;
	}

	function loadData(){
		$.ajax({
			type : "POST",
			url: basePath+"inventory/invunit/queryDetail.do",
			data: {"id":function(){return objs.unitId;}},
			dataType : "json",
			success : function(data) {
				objs.unit=data;
				$("#autoform").iForm("setVal",objs.unit);
			}
		});
	}

	function update(){
		if(!$("#autoform").valid()){
			FW.error("提交的内容有错误的地方，请修改后重试");
			return;
		}
		var params = $("#autoform").iForm("getVal");
		params.unitid=objs.unit.unitid;
		
		$.post(basePath + "inventory/invunit/update.do",{
				"unit":JSON.stringify(params)
			},function(data){
			if(data.result == "ok"){
				FW.success("更新成功");
				objs.unit=data.unit;
				changeMode('view');
			}else{
				FW.error("更新失败");
			}
		},"json");
	}

	function create(){
		if(!$("#autoform").valid()){
			FW.error("提交的内容有错误的地方，请修改后重试");
			return;
		}
		var obj = $("#autoform").iForm("getVal");
		
		$.ajax({
			type : "POST",
			url: basePath+"inventory/invunit/create.do",
			data: {"unit":FW.stringify(obj)},
			dataType : "json",
			success : function(data) {				
				if(data.status==1){
					FW.success("计量单位创建成功");
					objs.unit=data.unit;
					changeMode('view');
				}else{
					FW.error("计量单位创建失败");
				}
			}
		});
	}

	function del(){
		Notice.confirm("确认删除|是否确定要删除计量单位\""+objs.unit.unitname+"\"？该操作无法撤销。",function(){
			$.post(basePath + "inventory/invunit/delete.do",{"ids":objs.unit.unitid},function(data){
				if(data.failNum == "0"){
					FW.success("删除成功");
					goBack();
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		});	
	}

	function goBack(){
		FW.deleteTabById(FW.getCurrentTabId());
	}
	
</script>
<style>
.btn-toolbar .btn-margin-fix{
	margin-left:0 !important;
	margin-right:7px !important;
}
</style>
</head>
<body>
	<div class="toolbar-with-pager bbox">
		<div class="btn-toolbar" role="toolbar">
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnBack" style="">
				<button class="btn-default btn" onclick="goBack()">关闭</button>
			</div>
   			<div class="btn-margin-fix btn-group btn-group-sm" id="btnNew" style="display:none;">
				<button class="btn-success btn" onclick="changeMode('create')">新建</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnEdit">
				<button class="btn-default btn" onclick="changeMode('edit')">编辑</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnSave" style="display:none">
				<button class="btn-success btn" onclick="update()">保存</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnCreate" style="display:none">
				<button class="btn-success btn" onclick="create()">保存</button>
			</div>
			<div class="btn-margin-fix btn-group btn-group-sm" id="btnDel" style="display:none">
				<button class="btn-default btn" onclick="del()">删除</button>
			</div>	
		</div>
	</div>
	
	<div class="inner-title" id="pageTitle">
		计量单位信息
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>

</body>
</html>