function showReadOnlyInvoice(data){
	if(data && data.data && data.data.invoices && data.data.invoices.length){
		$('#addInvoiceWrapper').show();
		$("#invoiceListWrapper").iFold();
		initInvoice(data.data.invoices);
		$('#b-add-invoice').hide();
		dataGrid.datagrid('hideColumn',"garbage-colunms");
	}
}

function showEditableInvoice(data){
	$('#addInvoiceWrapper').show();
	$("#invoiceListWrapper").iFold();
	initInvoice(data&& data.data && data.data.invoices);
	var rows=dataGrid.datagrid('getRows');
	for(var i in rows){
		dataGrid.datagrid('beginEdit',i);
	}
	if(rows.length>0){
		$('#b-add-invoice').html('继续添加发票');
	}
}

/**
 * 以何种方式显示发票信息
 * @param opt 包含是否显示发票的信息
 * @param data 付款的所有信息
 */
function showInvoice(opt,data){
	if(!opt.readOnly){
		if(ctype=='cost'){
		    showEditableInvoice(data);
		}else{
			showReadOnlyInvoice(data);
		}
	}else if(opt.invoice){
		showEditableInvoice(data);
	}else{
		showReadOnlyInvoice(data);
	}
}
function openViewProject(){
	var projectId=$("#form1").iForm('getVal')["projectId"];
	openTab('pms/project/editProjectJsp.do?id='+projectId,'立项','pmsViewProjectTab'+projectId);
}