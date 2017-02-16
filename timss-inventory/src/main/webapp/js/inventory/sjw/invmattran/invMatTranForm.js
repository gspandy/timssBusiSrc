var new_form = [
			{title : "入库类型", id : "tranType", type:"hidden"},
    	{title : "入库日期", id : "createdate",type : "date", rules : {required:true}, type:"hidden"},
	    {title : "采购合同号", id : "pruorderno", rules : {required:true}},
	    {title : "验收人id", id : "checkuser", type:"hidden"},
	    {title : "采购申请人", id : "checkusername",type:"label"},
	    {title : "入库总价(元)", id : "totalPrice",type:"label",type:"hidden"},
	    {title : "入库总价(含税)", id : "totalTaxPrice",type:"label"},
	    {title : "入库总价(不含税)", id : "totalNoTaxPrice",type:"label"},
	    {title : "批次", id : "lotno",type:"hidden"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8}
];

var edit_form = [
		{title : "id信息", id : "imtid", type:"hidden"},
		{title : "入库类型", id : "tranType",type:"hidden"},
	    {title : "批次", id : "lotno",type:"hidden"},
	    {title : "入库人id", id : "operuser",type:"hidden"},
	    {title : "入库人", id : "operusername"},
    	{title : "入库日期", id : "createdate",type:"date", rules : {required:true}, type:"hidden"},
		{title : "入库单号", id : "sheetno"},
	    {title : "验收人id", id : "checkuser",type:"hidden"},
	    {title : "采购申请人", id : "checkusername",type : "label"},
	    {title : "采购合同号", id : "pruorderno"},
	    {title : "入库总价(元)", id : "totalPrice",type:"label",type:"hidden"},
	    {title : "入库总价(含税)", id : "totalTaxPrice",type:"label"},
	    {title : "入库总价(不含税)", id : "totalNoTaxPrice",type:"label"},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "待办id", id : "processinstid",type:"hidden"}
];

var show_form = [
 		{title : "id信息", id : "imtid", type:"hidden"},
 		{title : "入库类型", id : "tranType",type:"hidden"},
 	    {title : "批次", id : "lotno",type:"hidden"},
 	    {title : "入库人id", id : "operuser",type:"hidden"},
 	    {title : "入库人", id : "operusername"},
     	{title : "入库日期", id : "createdate",type:"date", rules : {required:true}},
 		{title : "入库单号", id : "sheetno"},
 	    {title : "验收人id", id : "checkuser",type:"hidden"},
 	    {title : "采购申请人", id : "checkusername", type : "label"},
 	    {title : "采购合同号", id : "pruorderno", wrapMdWidth:8, type:"hidden"},
 	    {title : "采购合同", id : "pruordername", wrapMdWidth:8,
 			formatter:function(val){
 				var obj = $("#autoform").iForm("getVal");
				var text = "<a onclick=openPurOrder('"+ obj.pruorderno + "')>" + obj.pruordername + "</a>";
 				return text;
 			}
 	    },
 	    {title : "入库总价(含税)", id : "totalTaxPrice",type:"label"},
	    {title : "入库总价(不含税)", id : "totalNoTaxPrice",type:"label"},
 	    {title : "备注", id : "remark", type : "textarea", linebreak:true, wrapXsWidth:12, wrapMdWidth:8},
 	    {title : "待办id", id : "processinstid",type:"hidden"}
 ];



//编辑表单加载数据（通用方法）
function initForm(inForm){
	$("#autoform").iForm("init",{"fields":inForm,"options":{validate:true}});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		url: basePath+"inventory/invmattran/queryMatTranForm.do",
		data: {"imtid":imtid},
		dataType : "json",
		success : function(data) {
			if("" != imtid){
				var loaddata = {
						"imtid":imtid,
						"sheetno" : data.sheetno,
						"tranType" : data.tranType, 
						"createdate" : data.createdate,
						"pruorderno" : data.pruorderno, 
						"pruordername" : data.pruordername, 
						"warehouseid" : data.warehouseid,
						"lotno" : data.lotno,
						"operuser" : data.operuser,
						"operusername" : data.operusername,
						"totalPrice" : data.totalPrice,
						"remark" : data.remark,
						"processinstid" : data.processinstid,
						"checkusername" : data.checkusername,
						"checkuser":data.checkuser
					};
				$("#autoform").iForm("setVal",loaddata);
			}
			
			if(null!=data.sheetno&&""!=data.sheetno){
				$("#autoform").iForm("endEdit",["pruorderno","sheetno"]);
			}
			
			initFormStatus = $("#autoform").iForm("getVal");
			dynaCalcTotalPrice();
		}
	});
	
}

function openPurOrder(purorder_no){
	var url = basePath+ "purchase/purorder/purOrderForm.do?type=edit&sheetId=&sheetNo=" + purorder_no;
	var currTabId = FW.getCurrentTabId();
    FW.addTabWithTree({
        id : "editOrderForm" + purorder_no,
        url : url,
        name : "采购合同",
        tabOpt : {
            closeable : true,
        	afterClose : "FW.deleteTab('$arg');FW.activeTabById('" + currTabId + "');" 
        }
    });
}