/***
 * 页面权限
 */
function setInvoicePriv(){
	Priv.map("privMapping.PUR_INVOICE_ADD","PUR_INVOICE_ADD");
	Priv.map("privMapping.PUR_INVOICE_CLOSE","PUR_INVOICE_CLOSE");
	Priv.map("privMapping.PUR_INVOICE_BACK","PUR_INVOICE_BACK");
	
	Priv.map("privMapping.PUR_INVOICE_SAVE","PUR_INVOICE_SAVE");
	Priv.map("privMapping.PUR_INVOICE_EDIT","PUR_INVOICE_EDIT");
	Priv.map("privMapping.PUR_INVOICE_DEL","PUR_INVOICE_DEL");
	Priv.map("privMapping.PUR_INVOICE_REPO","PUR_INVOICE_REPO");
	
	Priv.apply();
	FW.fixRoundButtons("#toolbar");
}