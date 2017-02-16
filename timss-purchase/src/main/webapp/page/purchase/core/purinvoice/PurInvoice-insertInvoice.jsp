<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>新建发票</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>
<script src='${basePath}js/purchase/core/purinvoice/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/purchase/core/purinvoice/insertInvoice.js?ver=${iVersion}'></script>

<script>
//合同ID
var contractId = "${contractId}";
var contractNo = "${contractNo}";

var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "合同ID", id : "contractId",type:"hidden"},
				{title : "采购合同", id : "contractNo",rules : {required:true}},
				{title : "供应商Id", id : "supplierId",type:"hidden"},
				{title : "供应商", id : "supplier",type:"label"},
	  			{title : "商务网编号", id : "businessNo",type:"label"},
	  			{title : "发票号", id : "invoiceNo",rules : {required:true,remote:{
					url: basePath + 'purchase/purInvoice/queryCheckInvoiceNo.do',
					type: 'post',
					data: {
						'paramsMap' : function(){
							var params = {
									'invoiceNo': $('#f_invoiceNo').val(),
									'id': $('#f_id').val()
							};
							 return FW.stringify(params);
						}
						
					}
				}}},
	  			{title : "开票日期", id : "invoiceCreateDate",rules : {required:true},type:"date"},
	  			{title : "不含税金额   ", id : "noTaxSumPrice",type:"label",linebreak:true},
	  			{title : "税额", id : "tax",rules : {required:true},type:"label" },
	  			{title : "含税金额 ", id : "sumPrice",type:"label"}
	  		];
	  		
	var opts={
		validate:true,
		fixLabelWidth:true,
		labelFixWidth:150
	};
	
	$(document).ready(function() {
		$("#autoform").iForm("init",{"options":opts,"fields":fields});
		$("#autoform").iForm("setVal",{"tax":0, "noTaxSumPrice":0, "sumPrice" :0});
		
		//初始化物资
		setWuziApplyDatagrid();
		$("#detailTitle").iFold("init");
		
		//初始化合同IHINT
		var hintUrl = basePath + "purchase/purInvoice/queryHintContract.do";
		searchContractHint( "contractId", "contractNo", hintUrl );
		
		if( !isNull( contractId ) ){
			$("#autoform").iForm("setVal",{"contractNo":contractNo, "contractId":contractId});
			//通过合同ID查询发票基础表单需要的数据
			queryMainVoiceInfo( contractId );
			//加载物资列表
			getWuziDatagridData( contractId );
		}
		//添加物资
		$("#addDetail").click(function(){
			showDtlIframe( contractId );
		});
		
		//保存
		$("#saveButton").click(function(){
			if(!$("#autoform").valid() || !$("#wuziFormDatagrid").valid()  ){
				return;
			}
			
			 var rowDatas = $("#wuziDatagrid").datagrid('getRows');
			var len = rowDatas.length;
			/* if( len <= 0 ){
				FW.error( "请添加物资 ！");
				return ;
			} */
			for( var i = 0; i < len; i++ ){
				 $("#wuziDatagrid").datagrid( 'endEdit', i );
			}
			var rowDatas = $("#wuziDatagrid").datagrid('getRows');
			
			
			var formData = $("#autoform").iForm("getVal");

			var url = basePath + "purchase/purInvoice/insertOrUpdateInvoice.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  JSON.stringify( formData ),
					rowData : JSON.stringify( rowDatas )
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						//loadFormData( data.rowData );
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
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm">
	            <button type="button" class="btn btn-default priv" privilege="" onclick="closeTab()">关闭</button>
	            <button type="button" class="btn btn-default priv" privilege="" id="saveButton">保存</button>
	        </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		新建发票
	</div>
	<div id="formDiv">
		<form id="autoform"></form>
	</div>
	<!-- 物资明细 -->
	<div grouptitle="物资明细 " id="detailTitle">
		<div class="margin-title-table">
			<form id="wuziFormDatagrid">
			<table id="wuziDatagrid" style="" class="eu-datagrid"></table>
			</form>
		</div>
		<div class="btn-toolbar margin-foldable-button" role="toolbar" id="addDetailBtn_toolbar">
			<div privilege="F-ROLE-ADD" class="btn-group btn-group-xs" id="addDetailDiv">
				<button type="button" class="btn btn-success" id="addDetail">添加物资</button>
			</div>
		</div>
	</div>
</body>
</html>