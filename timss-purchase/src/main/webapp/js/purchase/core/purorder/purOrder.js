var invoicePriv=function(){
	var invoice = privMapping.exampurch_invoice;
	if(""==process&&""!=processInstId&&1==invoice&&"作废"!=status){
		//审批通过且有权限
		return true;
	}else{
		return false;
	}
};

var createInvAccept=function(){
	var invoice = privMapping.acceptList_new;
	if(""==process&&""!=processInstId&&1==invoice&&"作废"!=status){
		//审批通过且有权限
		return true;
	}else{
		return false;
	}
};
var stopOrderPriv=function(){
	var stopOrder = privMapping.exampurch_stopOrder;
	if(""==process&&""!=processInstId&&1==stopOrder&&"作废"!=status){
		//审批通过且有权限
		return true;
	}else{
		return false;
	}
};
var createPay = function(){
	var exampurch_purpayBtn = privMapping.exampurch_purpayBtn;
	if(""==process && ""!=processInstId && 1==exampurch_purpayBtn&&"作废"!=status){
		//审批通过且有权限
		return true;
	}else{
		return false;
	}
};
var PurOrderPriv={
	init:function(){
		PurOrderPriv.set();
		PurOrderPriv.apply();
	},
	set:function(){//定义权限
		//编辑
		Priv.map("privMapping.exampurch_edit","exampurch_edit");
		//暂存
		Priv.map("privMapping.exampurch_save","exampurch_save");
		//提交
		Priv.map("privMapping.exampurch_approve","exampurch_approve");
		//新建
		Priv.map("privMapping.exampurch_new","exampurch_new");
		//物资接收
		Priv.map("privMapping.exampurch_itemreceive","exampurch_itemreceive");
		//删除
		Priv.map("privMapping.exampurch_delete","exampurch_delete");
		//录入发票
		Priv.map("invoicePriv()","exampurch_invoiceBtn");
		//终止合同
		Priv.map("stopOrderPriv()","exampurch_stopOrderBtn");
		//打印
		Priv.map("privMapping.exampurch_printf","exampurch_printBtn");
		//验收申请单
		Priv.map("createInvAccept()","acceptList_new_1");
		//新建付款
		Priv.map("createPay()","_exampurch_purpayBtn");
	},
	apply:function(){//应用权限
		//应用
		Priv.apply();
	}
};

//设置流程状态
function setProcessStatus(){
	if("" == process || "draft" == process || "negotiation_applydept_audit" == process){
		processStatus = "first";//首环节
		if( "draft" == process && "" != processInstId){
			processStatus = "first_save";//首环节暂存
		}else if("negotiation_applydept_audit" == process && ""!=processInstId){
			//SWF首环节暂存
			processStatus = "first_save";
		}else if("" != processInstId){
			processStatus = "over";//流程结束
		}
	}else if(process.indexOf("procurement", 0)>-1){
		processStatus = "last";//流程最后环节
	}else{
		processStatus = "process";//流程中
	}
}

//开始编辑所有行
function startEditAll(){
	var rows = $("#order_item").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#order_item").datagrid("beginEdit",i);
	}
}

//结束编辑所有行
function endEditAll(){
	var rows = $("#order_item").datagrid("getRows");
	for(var i=0;i<rows.length;i++){
		$("#order_item").datagrid("endEdit",i);
	}
}

//计算所有物资总值
function caluPrice(){
	endEditAll();
	var listData =$("#order_item").datagrid("getRows");
	var totalPrice = 0.0;
	for(var i=0;i<listData.length;i++){
		var itemnum = parseFloat(listData[i].itemnum);
		var averprice = parseFloat(listData[i].averprice);
		var tax = parseFloat(listData[i].tax);
		totalPrice = totalPrice + (itemnum * (averprice+tax));
	}
	var total = {totalPrice:totalPrice.toFixed(2)};
	$("#autoform").iForm("setVal",total);
}

//动态计算物资总价
function dynaCalcTotalPrice(){
	var rows = $("#order_item").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	var dataRows = $("#order_item").data().datagrid.data.rows;
	var totalPrice = 0;
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		
		//获取采购量
		var itemnum = row.children("td[field='itemnum']").find("input").val();
		if(typeof(itemnum) == "undefined"){
			itemnum = row.children("td[field='itemnum']").find("div").text();
		}
		itemnum = Number(itemnum);
		dataRows[i].itemnum = itemnum; 
		
		//获取单价
		var cost = row.children("td[field='cost']").find("input").val();
		if(typeof(cost) == "undefined"){
			cost = row.children("td[field='cost']").find("div").text();
		}
		
		//获取小计(endEdit之后再beginEdit会出现输入框为空的情况)
		var priceT = row.children("td[field='priceTotal']").find("div").text();
		if(null!=priceT && (null == cost || "" == cost)){
			cost = parseFloat(priceT/itemnum);
			row.children("td[field='cost']").find("input").val(_toFixed(cost,2));
		}
		cost = parseFloat(cost);
		dataRows[i].cost = cost; 
		
		//var taxRate = row.children("td[field='taxRate']").find("select[_name=taxRate_"+i+"] option:selected").val();
		var taxRate = row.children("td[field='taxRate']").find("input").val();
		if(isNaN(taxRate)){
			taxRate = row.children("td[field='taxRate']").find("div").text();
		}
		taxRate = parseFloat(taxRate*0.01);
		
		if(isNaN(itemnum) || isNaN(cost)){
			totalPrice = "非法的输入";
			break;
		}
		var lineSum = itemnum*cost;
		
		var averprice = parseFloat(cost/(1+taxRate));
		row.children("td[field='averprice']").children("div").html(_toFixed(averprice,2));
		dataRows[i].averprice = averprice;
		
		var tax = parseFloat(cost-averprice);
		row.children("td[field='tax']").children("div").html(_toFixed(tax,2));
		dataRows[i].tax = tax; 
		
		//增加显示总税费
		var taxTotal = tax*itemnum;
		row.children("td[field='taxTotal']").children("div").html(_toFixed(taxTotal,2));
		dataRows[i].taxTotal = taxTotal; 
		
		row.children("td[field='priceTotal']").children("div").html(_toFixed(lineSum,2));
		dataRows[i].priceTotal = lineSum; 
		totalPrice += lineSum;
	}
	var total = {totalPrice:totalPrice.toFixed(2)};
	$("#autoform").iForm("setVal",total);
}

//行计算
function caluRow(rowData,rowIndex){
	var itemnum = rowData.itemnum;
	var averprice = rowData.averprice;
	var tax = rowData.tax;
	var cost = parseFloat(averprice*1+tax*1).toFixed(2);
	var pt = parseFloat(cost*itemnum).toFixed(2);
	$("#order_item").datagrid("updateRow",{
		index: rowIndex,
		row: {  "cost": cost ,"priceTotal": pt }
	});
}

//提交后回调执行
function submitCallBack(){
	var totalPrice = parseFloat($("#autoform").iForm('getVal','totalPrice'));
	var curUserSiteId = Priv.secUser.siteId;  
	if(processStatus == "last"){
		if("ZJW"!=curUserSiteId || "SWF"!=curUserSiteId){
			FW.autoGenerateMatTran(sheetId);	
		}
	}
	pageClose();
}

//取消的回调执行
function cancelCallBack(){
	$("#btn-submit").button('reset');
	$("#btn-submitlist").button('reset');
	$("#btn-submit-no").button('reset');
	$("#btn-audit").button('reset');
}

//刷新当前页面
function refCurPage(){
 	isSearchMode = true;
 	$("#btn_advlocal").removeClass("active");
	$("#table_order").iDatagrid("endSearch");
 	$("#table_order").datagrid("reload");
 	setTimeout(function(){ 
 		$("#table_order").datagrid("resize"); 
 	},200);
} 

//重置Tab
function repositionTab(){
	var tab = _parent().$(".itcui-navigation-ul").find("span.navitem-name");
	var activeTabId = FW.getCurrentTabId();
	for(var i=0;i<tab.length;i++){
		if(tab[i].innerText.indexOf("申请")>-1){
			var id = tab[i].parentNode.id;
			var idArr = id.split("itcui_nav_tab_");
			FW.deleteTabById(idArr[1]);
			FW.activeTabById(activeTabId);
		}
	}
}

//税率选择统一控制
function allSelectControlCombox(val){
	 var listData =$("#order_item").datagrid("getRows");
	 for(var i=0;i<listData.length;i++){
	 	/*
		$("select[_name=taxRate_"+i+"]").find("option:selected").attr("selected", false);
	 	$("select[_name=taxRate_"+i+"] option[value='"+val+"']").attr("selected", true);
	 	var textFeild = $("select[_name=taxRate_"+i+"]").find("option:selected").text();
	 	$("select[_name=taxRate_"+i+"]").parent(".itc_object_wrap").children(".itcui_combo").children(".cb_text").html(textFeild);
	 	$("select[_name=taxRate_"+i+"]").parent(".itc_object_wrap").children(".itcui_combo").find("input").val(textFeild);
	 	*/
		if(undefined!=$("select[_name=taxRate_"+i+"]").html()){
			$("select[_name=taxRate_"+i+"]").iCombo("setVal",val); 
		}
	 	//因为上面的方法会触发下方的事件singleSelectControlCombox(val,obj) 当中也有dynaCalcTotalPrice，可以注释掉了
		//dynaCalcTotalPrice();
	 }
}

//下拉框单一控制
function singleSelectControlCombox(val,obj){
	var tRName = obj.attr("name");
	$("select[name="+tRName+"] option[value='"+val+"']").attr("selected", true);
	$("select[name="+tRName+"]").parent(".itc_object_wrap").children(".itcui_combo").find("input").val(val);
	dynaCalcTotalPrice();
}

//重置所有可编辑的字段  需要更名函数resetEditableAttr
function resetSelectAttr(){
	var rows = $("#order_item").prev(".datagrid-view2").children(".datagrid-body").find(".datagrid-row");
	for(var i=0;i<rows.length;i++){
		var row = $(rows[i]);
		row.children("td[field='taxRate']").find("select").attr("_name","taxRate_"+i);
		row.children("td[field='taxRate']").find("input").attr("name","taxRate_"+i);
		row.children("td[field='cost']").find("input").attr("name","cost_"+i);
		row.children("td[field='remark']").find("input").attr("name","remark_"+i);
		row.children("td[field='itemnum']").find("input").attr("name","itemnum_"+i);
	}
}

//检查表单中的下拉框是否编辑
function checkFormComboEditable(){
	var listData =$("#order_item").datagrid("getRows");
	if(listData.length>0){
		$("#autoform").iForm("beginEdit","taxRate");
	}else{
		$("#autoform").iForm("endEdit","taxRate");
	}
}

//显示附件
function uploadform() {
	var file = null;
	
	if("" != uploadFiles){
		file = JSON.parse(uploadFiles);
	}
	
	$("#uploadform").iForm('init', {
		"fields" : [
                    	{
                    	 id:"uploadField", 
                    	 title:" ",
                    	 type:"fileupload",
                    	 linebreak:true,
                    	 wrapXsWidth:12,
                    	 wrapMdWidth:12,
                    	 options:{
                    	     "uploader" : basePath+"upload?method=uploadFile&jsessionid="+sessId,
                    	     "delFileUrl" : basePath+"upload?method=delFile&key="+valKey,
	                    	 "downloadFileUrl" : basePath + "upload?method=downloadFile",
	                    	 "swf" : basePath + "js/purchase/common/uploadify.swf",
	                    	 "fileSizeLimit" : 10 * 1024,
	                    	 "initFiles" : file,
	                    	 "delFileAfterPost" : true
	                    	 }
                    	}
                    ],
		"options" : {
		"labelFixWidth" : 6,
		"labelColon" : false
		}
	});
}

//新建付款记录方法
function newPay(payType){
	var url = basePath + "purchase/purpay/purPayForm.do?sheetId="+sheetId+"&operType=new&payType="+payType+"&payId=";
    var suffix = new Date().getTime() + Math.floor(Math.abs(Math.random()*100));
    FW.addTabWithTree({
        id : "newPayForm" + suffix,
        url : url,
        name : "付款",
        tabOpt : {
            closeable : true,
            afterClose : "FW.deleteTab('$arg');FW.activeTabById('"+FW.getCurrentTabId()+"');FW.getFrame(FW.getCurrentTabId()).refreshPayList();"
        }
    });
}