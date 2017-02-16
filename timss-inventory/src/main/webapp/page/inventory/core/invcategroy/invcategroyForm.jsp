<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String cateId = request.getParameter("cateId") == null ? "":String.valueOf(request.getParameter("cateId"));
	String parentId = request.getParameter("parentId") == null ? "":String.valueOf(request.getParameter("parentId")); 
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:99%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>主项目表单</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}page/asset/itcui_timssutil.js?ver=${iVersion}"></script>
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script>
	var cateId = '<%=cateId%>';
	var parentId = '<%=parentId%>';
	var new_form = [
	    	{title : "物资类型名称", id : "invcatename", rules : {required:true}},
	    	{title : "仓库", id : "warehousename",type:"label"},
	    	{title : "启用状态", id : "status",type:"radio",data:[["ACTIVE","是"],["NO","否"]], rules : {required:true}},
		    {title : "物资类型描述", id : "descriptions",type : "textarea",linebreak:true,height:48, wrapMdWidth:8},
		    {title : "父节点id", id : "parentid", type:"hidden"},
		    {title : "仓库id", id : "warehouseid",type:"hidden"}
		    
	];
	
	var edit_form = [
			{title : "物资类型id", id : "invcateid", type:"hidden"},
	    	{title : "物资类型名称", id : "invcatename", rules : {required:true}},
	    	{title : "仓库", id : "warehousename",type:"label"},
	    	{title : "启用状态", id : "status",type:"radio",data:[["ACTIVE","是"],["NO","否"]], rules : {required:true}},
		    {title : "物资类型描述", id : "descriptions",type : "textarea",linebreak:true,height:48, wrapMdWidth:8},
		    {title : "父节点id", id : "parentid", type:"hidden"},
		    {title : "仓库id", id : "warehouseid",type:"hidden"}
	];
	
	//编辑表单加载数据（通用方法）
	function initForm(inForm){
		$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
		//加载用户表单数据
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"inventory/invcategroy/queryInvCategroyDetail.do",
			data: {"cateId":cateId,"parentId":parentId },
			dataType : "json",
			success : function(data) {
				if("" == parentId){
					parentId = data.parentid;
				}
				var loaddata = {
					"invcateid" : cateId, 
					"invcatename" : data.invcatename,
					"descriptions" : data.descriptions, 
					"status" : data.status,
					"parentid" : parentId,
					"warehouseid" : data.warehouseid,
					"warehousename" : data.warehousename
					};
				$("#autoform").iForm("setVal",loaddata);
			}
		});
	}
	
	//初始化按钮
	function initBtn(){
		//保存
		$("#btn_save").click(function(){
			saveInfo();
		});
		
		//编辑
		$("#btn_edit").click(function(){
			changeState("edit");
		});
		
		$("#btn_back").click(function(){
			window.location.href = basePath + "inventory/invcategroy/invcategroyForm.do?cateId="+cateId+"&parentId="+parentId;
		});
	}
	
	//保存页面信息
	function saveInfo(){
		var formData =$("#autoform").ITC_Form("getdata");
		if(!$("#autoform").valid()){
			return ;
		}
		//禁用时发起请求，校验物资类型下是否有绑定
		if(formData.status == "NO"){
			$.ajax({
				type : "POST",
				async: false,
				url: basePath+"inventory/invcategroy/checkItemByInvcateid.do",
				data: {"formData":JSON.stringify(formData)},
				dataType : "json",
				success : function(data) {
					//返回值为false代表，物资类型下无物资，可禁用
					if( data.result == "false" ){
						saveInvCategroy(formData);
					}else{
						FW.error( data.itemCode + "主项目绑定了该分类，请先处理已绑定该分类的主项目");
						return ;
					}
				}
			});
		}
		else{
			saveInvCategroy(formData);
		}
	}
	
	$(document).ready(function() {
		initBtn();
		if("" != cateId){
			initForm(edit_form);
			changeState("read");
			
		}else{
			initForm(new_form);
			changeState("new");
		}
	});
	
	function changeState(state){
		if(state=="read"){
			$("#pageTitle").html("物资类型详情");
			$("#autoform").ITC_Form("readonly");
			$("#btn_edit").show();
			$("#btn_save").hide();
			$("#btn_back").hide();
		}else if(state=="edit"){
			$("#pageTitle").html("编辑物资类型");
			$("#btn_save").show();
			$("#btn_back").show();
			$("#btn_edit").hide();
			initForm(eval("edit_form"));
		}else if(state=="new"){
			$("#pageTitle").html("新建物资类型");
			$("#btn_edit").hide();
			$("#btn_back").hide();
		}
		FW.fixToolbar("#toolbar");
	}
	
	function saveInvCategroy(formData){
		$.ajax({
			type : "POST",
			async: false,
			url: basePath+"inventory/invcategroy/saveInvCategroy.do",
			data: {"formData":JSON.stringify(formData)},
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					cateId = data.id;
					parentId = data.parentId;
					FW.success( "保存成功 ！");
					FW.itemTreeReflesh();
					changeState("read");
				}else{
					FW.error( "保存失败");
				}
			}
		});
	}
</script>
</head>
<body style="height:99%">
	<div class="bbox toolbar-with-pager" id="toolbar_wrap">
		<!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar" id="myToolbar">
	        <div class="btn-group btn-group-sm">
				<button class="btn-default btn" id="btn_back">返回</button>
			</div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_save" class="btn btn-success">保存</button>
	        </div>
	        <div class="btn-group btn-group-sm">
	        	<button type="button" id="btn_edit" class="btn btn-default">编辑</button>
	        </div>
	    </div>
	</div>
	<div class="inner-title" id="pageTitle">
		物资类型
	</div>
	
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
</body>
</html>