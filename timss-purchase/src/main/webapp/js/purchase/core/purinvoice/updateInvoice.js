//初始化数据by 合同ID
function initFormDataById( id ){
		var url = basePath + "purchase/purInvoice/queryInvoiceById.do?id=" + id;
		$.ajax({
			url : url,
			type : 'post',
			dataType : "json",
			success : function(data) {
				if( data.result == "success" ){
					$("#autoform").iForm("setVal", data.bean );
					contractId = data.bean.contractId;
					$("#autoform").iForm("endEdit",["status"]);
				}else{
					FW.error("加载信息失败！");
				}
			},complete : function(XHR, TS){
				var formData = $("#autoform").iForm("getVal");
				//供应商
				FW.showVendorInfo("supplierId","supplier");
				
				if( formData.status == "PUR_INVOICE_STATUS_2" ){
					$("#closeButtonDiv").show();
					$("#backButtonDiv, #saveButtonDiv, #editButtonDiv, #delButtonDiv, #repoButtonDiv").hide();
				}else{
					$("#closeButtonDiv, #editButtonDiv, #repoButtonDiv").show();
					$("#backButtonDiv, #saveButtonDiv, #delButtonDiv").hide();
				}
				FW.fixRoundButtons("#toolbar");
			}
		});
}

//初始化数据by 发票ID
function initDatagridById( invoiceId ){
	var url = basePath + "purchase/purInvoice/queryInvoiceItemById.do?invoiceId=" + invoiceId;
	$("#wuziDatagrid").iDatagrid("init",{
        pageSize:pageSize,//pageSize为全局变量，自动获取的
        singleSelect:true,
        fitColumns:true,
        nowrap : false,
        url : url,
        columns: wuziColumns,
		onLoadError: function(){
			//加载错误的提示 可以根据需要添加
		},
        onLoadSuccess: function(data){
        	beginEditor();
			setNoTaxSumPrice();
			//页面endEdit
			updatePageEndEdit();
			//权限
			setInvoicePriv();
			if(data && data.total==0){
				$("#grid1_empty").show();
			}
        },onClickCell : function(rowIndex, field, value) {
			if (field == 'imtNo') {
				deleteThisRow(rowIndex, field, value);
			}
		}

    });
}

//更新页面endEdit
function updatePageEndEdit(){
	$('#wuziDatagrid').datagrid("hideColumn","imtNo");
	$("#autoform").iForm("endEdit");
	$("#addDetail").hide();
	var rowDatas = $('#wuziDatagrid').datagrid("getRows");
	for( index in rowDatas ){
		$('#wuziDatagrid').datagrid("endEdit", index );
	}
}

//更新页面endEdit
function updatePageBeginEdit(){
	$('#wuziDatagrid').datagrid("showColumn","imtNo");
	$("#autoform").iForm("beginEdit");
	$("#autoform").iForm("endEdit",["status"]);
	$("#addDetail").show();
	var rowDatas = $('#wuziDatagrid').datagrid("getRows");
	for( index in rowDatas ){
		$('#wuziDatagrid').datagrid("beginEdit", index );
	}
}

//跳转到合同 by sheetId
function trunToContract(){
	var sheetId = $("#f_contractId ").val();
	var url = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetId="+sheetId;
	var sourceId = FW.getCurrentTabId();
	
	addTabWithTree( "pageId"+sheetId, "合同详细"+ sheetId, url, sourceId, "purchasing" );
}
