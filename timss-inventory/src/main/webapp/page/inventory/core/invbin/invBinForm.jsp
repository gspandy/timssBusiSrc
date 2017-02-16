<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
String binId = request.getParameter("id")==null?"":String.valueOf(request.getParameter("id")); 
String mode=request.getParameter("mode")==null?"":String.valueOf(request.getParameter("mode"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<title>货柜详细信息</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<script>_useLoadingMask = true;</script>
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script>
    var objs={};
	objs.binId = '<%=binId%>';
	objs.mode='<%=mode%>';
	objs.form = [
    	{title : "货柜名称", id : "binname",rules : {	required : true,maxlength : 64,
				remote:{
					url: basePath + "inventory/invbin/isBinNameExist.do",
					type:"post",
					data:{
						binname : function(){return $("#f_binname").val();},
						warehouseid : function(){return $("#f_warehouseid").val()/* iCombo("getVal") */;},
						binid:function(){return objs.bin?objs.bin.binid:objs.blankBin.binid;}
					}
				}
		}},   
		{title : "仓库", id : "warehouseid",type : "combobox","options":{
			url: basePath+"inventory/invbin/queryWarehouseList.do",
	        onRemoteData:function(val){
	        	objs.warehouseList=val.warehouseList;
				var array = [];
				for(var i=0;i<objs.warehouseList.length;i++){
					array.push([objs.warehouseList[i].warehouseid,objs.warehouseList[i].warehousename]);
				}
				return array;
			},
			remoteLoadOn:'init',
			onChange:function(val){
				$("#autoform").valid();
			},
			initOnChange : false
		}},
		{title : "启用状态", id : "active",type : "radio",
	        data : [
	            ["Y","启用",true],
	            ["N","停用"]
	        ]
		}
	];
	objs.blankBin={
		binname:"",
		warehouseid:""
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
			$("#btnSave,#btnCreate").hide();
			$("#btnEdit").show();
			$("#pageTitle").html("货柜详情");
		}else if(mode=='create'){
			//$("#autoform").iForm("setVal",objs.blankBin);
			$("#autoform").ITC_Form("beginedit");
			$("#btnSave,#btnEdit").hide();
			$("#btnCreate").show();
			$("#pageTitle").html("新建货柜");
		}else if(mode=='edit'){
			$("#autoform").ITC_Form("beginedit");
			$("#btnEdit,#btnCreate").hide();
			$("#btnSave").show();
			$("#pageTitle").html("编辑货柜");
		}
		objs.mode=mode;
	}

	function loadData(){
		$.ajax({
			type : "POST",
			url: basePath+"inventory/invbin/queryDetail.do",
			data: {"id":function(){return objs.binId;}},
			dataType : "json",
			success : function(data) {
				objs.bin=data;
				$("#autoform").iForm("setVal",objs.bin);
			}
		});
	}
	
	function update(){
		if(!$("#autoform").valid()){
			FW.error("提交的内容有错误的地方，请修改后重试");
			return;
		}
		var params = $("#autoform").iForm("getVal");
		params.binid=objs.bin.binid;
		
		$.post(basePath + "inventory/invbin/update.do",{
				"bin":JSON.stringify(params)
			},function(data){
			if(data.result == "ok"){
				FW.success("更新成功");
				objs.bin=data.bin;
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
			url: basePath+"inventory/invbin/create.do",
			data: {"bin":FW.stringify(obj)},
			dataType : "json",
			success : function(data) {				
				if(data.status==1){
					FW.success("货柜创建成功");
					objs.bin=data.bin;
					changeMode('view');
				}else{
					FW.error("货柜创建失败");
				}
			}
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
			
		</div>
	</div>
	
	<div class="inner-title" id="pageTitle">
		货柜信息
	</div>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>

</body>
</html>