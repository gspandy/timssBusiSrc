<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String warehouseId = request.getParameter("id")==null?"":String.valueOf(request.getParameter("id")); 
String mode=request.getParameter("mode")==null?"":String.valueOf(request.getParameter("mode"));
String forbidEdit=request.getParameter("forbidEdit")==null?"":String.valueOf(request.getParameter("forbidEdit"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" />
<title>仓库详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script>_useLoadingMask = true;</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
    var objs={};
    var originalActive;
	objs.warehouseId = '<%=warehouseId%>';
	objs.mode='<%=mode%>';
	objs.forbidEdit='<%=forbidEdit%>';
	objs.form = [
		{title : "仓库编号", id : "warehousecode",
			rules:{
				maxlength:30,
				remote:{
					url: basePath + "inventory/invwarehouse/isCodeExist.do",
					type:"post",
					data:{
						code : function(){return $("#f_warehousecode").val();},
						id : function(){return objs.warehouse?objs.warehouse.warehouseid:objs.blankWarehouse.warehouseid;}
					}
				}
			}
		},
    	{title : "仓库名称", id : "warehousename",rules : {	required : true,maxlength : 150}},
    	{title : "使用状态", id : "active",type : "combobox",
	        data : [
	            ["Y","启用"],
	            ["N","停用"]
	        ]
		},
	    {title : "仓库描述", id : "descriptions",rules : {maxlength : 500},
	    	type : "textarea",linebreak:true,wrapXsWidth:12,wrapMdWidth:8,height:48
	    }		    
	];
	objs.blankWarehouse={
		warehousecode:"",
		warehousename:"",
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
		if(objs.forbidEdit=='true'){//禁止编辑
			$(".btn-toolbar").hide();
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
			$("#pageTitle").html("仓库详情");
		}else if(mode=='create'){
			$("#autoform").iForm("setVal",objs.blankWarehouse);
			$("#autoform").ITC_Form("beginedit");
			$("#btnSave,#btnEdit,#btnDel").hide();
			$("#btnCreate").show();
			$("#pageTitle").html("新建仓库");
		}else if(mode=='edit'){
			$("#autoform").ITC_Form("beginedit");
			$("#btnEdit,#btnCreate").hide();
			$("#btnSave,#btnDel").show();
			$("#pageTitle").html("编辑仓库");
		}
		objs.mode=mode;
	}

	function loadData(){
		$.ajax({
			type : "POST",
			url: basePath+"inventory/invwarehouse/queryDetail.do",
			data: {"id":function(){return objs.warehouseId;}},
			dataType : "json",
			success : function(data) {
				objs.warehouse=data;
				$("#autoform").iForm("setVal",objs.warehouse);
				originalActive = objs.warehouse.active;
			}
		});
	}
	
	function confirmBeforeUpdate(){
		var params = $("#autoform").iForm("getVal");
		if(params.active=='N' && originalActive!=params.active){
			FW.confirm("确定停用？|请确认此仓库的物资已盘点或移库。停用后，将不能领用此仓库物资。",function(){
				update();
			})
		}else{
			update();
		}
	}
	
	function update(){
		if(!$("#autoform").valid()){
			FW.error("提交的内容有错误的地方，请修改后重试");
			return;
		}
		var params = $("#autoform").iForm("getVal");
		params.warehouseid=objs.warehouse.warehouseid;
		$.post(basePath + "inventory/invwarehouse/update.do",{
			"warehouse":JSON.stringify(params)
			},function(data){
			if(data.result == "ok"){
				FW.success("更新成功");
				originalActive = params.active;
				FW.itemTreeReflesh();
				objs.warehouse=data.warehouse;
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
			url: basePath+"inventory/invwarehouse/create.do",
			data: {"warehouse":FW.stringify(obj)},
			dataType : "json",
			success : function(data) {				
				if(data.status==1){
					FW.success("仓库创建成功");
					FW.itemTreeReflesh();
					objs.warehouse=data.warehouse;
					changeMode('view');
				}else{
					FW.error("仓库创建失败");
				}
			}
		});
	}

	function del(){
		Notice.confirm("确认删除|是否确定要删除仓库\""+objs.warehouse.warehousename+"\"？该操作无法撤销。",function(){
			$.post(basePath + "inventory/invwarehouse/delete.do",{"ids":objs.warehouse.warehouseid},function(data){
				if(data.failNum == "0"){
					FW.success("删除成功");
					FW.itemTreeReflesh();
					goBack();
				}else{
					FW.error("删除失败"+","+"请稍后重试或联系管理员");
				}
			},"json");
		});	
	}

	function goBack(){
		FW.itemTreeReflesh();
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
				<button class="btn-success btn" onclick="confirmBeforeUpdate()">保存</button>
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
		仓库信息
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>

</body>
</html>