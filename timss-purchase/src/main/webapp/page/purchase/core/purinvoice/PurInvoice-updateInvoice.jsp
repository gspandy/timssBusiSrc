<%@page import="com.yudean.itc.util.FileUploadUtil"%>
<%@page import="com.yudean.itc.util.Constant"%>
<%@page import="com.yudean.itc.dto.sec.SecureUser"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<title>发票详细</title>
<script>_useLoadingMask = true;</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<!-- 由于在本地web工程中，这个路径不存在，会编译异常 -->
<jsp:include page="/page/mvc/mvc_include.jsp" flush="false" />
<script src="${basePath}js/inventory/common/inventory.js?ver=${iVersion}"></script>
<script src="${basePath}js/purchase/common/purchase.js?ver=${iVersion}"></script>

<script src='${basePath}js/purchase/core/purinvoice/addTab.js?ver=${iVersion}'></script>
<script src='${basePath}js/purchase/core/purinvoice/privInvoice.js?ver=${iVersion}'></script>
<script src='${basePath}js/purchase/core/purinvoice/insertInvoice.js?ver=${iVersion}'></script>
<script src='${basePath}js/purchase/core/purinvoice/updateInvoice.js?ver=${iVersion}'></script>

<style>
	#readonly_f_contractNo{
		color:#428bca;
	}
	#readonly_f_contractNo:hover{
		text-decoration:underline;
		cursor:pointer;
	}
</style>
<script>
//合同ID
var contractId = "";
//Id
var id = '${id}';

var fields = [
				{title : "id", id : "id",type:"hidden"},
				{title : "编号", id : "sheetNo",type:"hidden"},
				{title : "合同ID", id : "contractId",type:"hidden"},
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
				{title : "状态", id : "status",rules : {required:true},
					type : "combobox",
					dataType : "enum",
					enumCat : "PUR_INVOICE_STATUS"
				},
				{title : "采购合同", id : "contractNo",rules : {required:true}},
				{title : "供应商Id", id : "supplierId",type:"hidden"},
				{title : "供应商", id : "supplier",type:"label"},
	  			{title : "商务网编号", id : "businessNo",type:"label"},
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
		$("#autoform").on("click", function(e){
			var target = $(e.target);
			if(target.attr("id") == "readonly_f_contractNo"){
				trunToContract();
			}
		});
		
		//初始化合同IHINT
		var hintUrl = basePath + "purchase/purInvoice/queryHintContract.do";
		searchContractHint( "contractId", "contractNo", hintUrl );
		
		//初始化物资
		setWuziApplyDatagrid();
		$("#detailTitle").iFold("init");
		
		initFormDataById( id );
		initDatagridById( id );
		
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
			rowDatas = $("#wuziDatagrid").datagrid('getRows');
			
			$("#autoform").iForm("endEdit");
			var formData = $("#autoform").iForm("getVal");

			var url = basePath + "purchase/purInvoice/insertOrUpdateInvoice.do";
			$.ajax({
				url : url,
				type : 'post',
				dataType : "json",
				data :{
					formData :  JSON.stringify( formData ),
					rowData : FW.stringify( rowDatas )
				},
				success : function(data) {
					if( data.result == "success" ){
						FW.success( "保存成功 ！");
						initFormDataById( id );
						initDatagridById( id );
					}else{
						FW.error( "保存失败 ！");
					}
				}
			});
		});
		
		//删除
		$("#delButton").click(function(){
			var url = basePath + "purchase/purInvoice/deleteInvoiceById.do?id=" + id;
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
		
		//报账
		$("#repoButton").click(function(){
			var rowDatas = $("#wuziDatagrid").datagrid('getRows');
			var len = rowDatas.length;
			if( len <= 0 ){
				FW.error( "请先添加物资 ！");
				return ;
			}
			var url = basePath + "purchase/purInvoice/updateInvoiceStatus.do?id=" + id;
			FW.confirm("入账后不可撤销，是否执行该操作？", function() {
				$.ajax({
					url : url,
					type : 'post',
					dataType : "json",
					success : function(data) {
						if( data.result == "success" ){
							FW.success( "报账成功 ！");
							initFormDataById( id );
						}else{
							FW.error( "报账失败 ！");
						}
					}
				});
			});
		});
		
		//编辑
		$("#editButton").click(function(){
			updatePageBeginEdit();
			$("#closeButtonDiv, #editButtonDiv, #repoButtonDiv").hide();
			$("#backButtonDiv, #saveButtonDiv, #delButtonDiv").show();
			$("#grid1_empty").hide();
			FW.fixRoundButtons("#toolbar");
		});
		
		//返回
		$("#backButton").click(function(){
			/* updatePageEndEdit();
			$("#closeButtonDiv, #editButtonDiv, #repoButtonDiv").show();
			$("#backButtonDiv, #saveButtonDiv, #delButtonDiv").hide();
			var rowDatas = $("#wuziDatagrid").datagrid('getRows');
			var len = rowDatas.length;
			if( len <= 0 ){
				$("#grid1_empty").show();
			}
			FW.fixRoundButtons("#toolbar"); */
			initFormDataById( id );
			initDatagridById( id );
		});
		
		
	});
	
</script>

</head>
<body>
	 <div class="bbox toolbar-with-pager" id="toolbar_wrap">
	    <!-- 这里可以在分页器的同排渲染一个按钮工具栏出来 在下面的toolbar1中 -->
	    <div id="toolbar" class="btn-toolbar ">
	    	<div class="btn-group btn-group-sm" id="closeButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_CLOSE" id="closeButton" onclick="closeTab()">关闭</button>
	        </div>
	    	<div class="btn-group btn-group-sm" id="backButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_BACK" id="backButton">返回</button>
	        </div>
	         <div class="btn-group btn-group-sm" id="saveButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_SAVE" id="saveButton">保存</button>
            </div>
	         <div class="btn-group btn-group-sm" id="editButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_EDIT" id="editButton">编辑</button>
            </div>
	         <div class="btn-group btn-group-sm" id="delButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_DEL" id="delButton">删除</button>
            </div>
	         <div class="btn-group btn-group-sm" id="repoButtonDiv">
	            <button type="button" class="btn btn-default priv" privilege="PUR_INVOICE_REPO" id="repoButton">报账</button>
            </div>
	    </div>
	</div>
	<!--这里要清掉分页器的右浮动效果-->
	<div style="clear:both"></div>
	<div class="inner-title">
		发票详情
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
				<button type="button" class="btn btn-success" id="addDetail"">添加物资</button>
			</div>
		</div>
	</div>
	<div id="grid1_empty" style="display:none;width:100%;height:62%">
		<div style="height:100%;display:table;width:100%">
			<div style="display:table-cell;vertical-align:middle;text-align:center">
			    <div style="font-size:14px">没有物资信息</div>
			</div>
		</div>
	</div>
</body>
</html>