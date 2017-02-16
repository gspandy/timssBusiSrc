<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<% 
	String iivData = request.getAttribute("iivData")==null?"":String.valueOf(request.getAttribute("iivData"));
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" style="width:100%;height:100%">
<head>
<link rel="shortcut icon" href="${basePath}favicon.ico" type="image/x-icon" /> 
<title>物资明细</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script type="text/javascript" src="${basePath}page/asset/itcui_timssutil.js?ver=${iVersion}"></script>
<script>
	var iivData = ${iivData};
	var form = [
			{title : "物资编码", id : "itemcode"},
	    	{title : "物资名称", id : "itemname"},
		    {title : "型号规格", id : "cusmodel"},
		    {title : "单位", id : "unit1"},
		    {title : "库存余量", id : "qtyStock",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else if(value===0){
		    		return value;
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "可用数量", id : "bestockqty",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else if(value===0){
		    		return value;
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "最后一次入库时间", id : "lateInDate",type:"datetime"},
		    {title : "最后一次入库数量", id : "lateInQty",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else if(value===0){
		    		return value;
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "最近入库价格(元)", id : "lateInPrice",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "平均采购价格(元)", id : "price",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "已通过申请数量", id : "stockqty",type:"label",formatter:function(value){
		    	if( value === "" || value === null ){
		    		return "--";
		    	}else if(value===0){
		    		return value;
		    	}else{
					return parseFloat(value).toFixed(2);
		    	}
			}},
		    {title : "所在仓库", id : "warehouse"},
		    {title : "所在货柜", id : "bin"},
		    {title : "物资类型", id : "cateName"},
		    {title : "生产厂商", id : "manufacturer"}
	];
	//编辑表单加载数据（通用方法）
	function initForm(){
		$("#autoform").iForm("init",{"fields":form,"options":{validate:true,labelFixWidth:140,xsWidth:4}});
		if( iivData.itemid ){
			$("#autoform").iForm("setVal",iivData);
		}else{
			$("#grid_error").show();
			$("#autoform").iForm("endEdit");
		}
	}
	
	
	$(document).ready(function() {
		initForm();
		$("#autoform").iForm("endEdit",['itemcode','itemname','cusmodel','unit1','lateInDate','warehouse','bin','cateName','manufacturer']);
	});
	
</script>
</head>
<body>
	<form id="autoform" class="margin-form-title margin-form-foldable autoform"></form>
	<div id="grid_error" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有选择物资明细信息</div>
			</div>
		</div>
	</div>
</body>
</html>