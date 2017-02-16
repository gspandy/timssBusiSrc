var show_form = [
 		{title : "物资退货单id信息", id : "imtid", type:"hidden"},
 		{title : "物资接收单id信息", id : "imrsid", type:"hidden"},
 		{title : "入库单号", id : "sheetno"},
 		{title : "入库类型", id : "tranType",type:"hidden"},
     	{title : "入库日期", id : "createdate",type:"date", rules : {required:true}},
 	    {title : "批次", id : "lotno",type:"hidden"},
 	    {title : "入库人id", id : "operuser",type:"hidden"},
 	    {title : "入库人", id : "operusername"},
 	    {title : "验收人id", id : "checkuser",type:"hidden"},
 	    {title : "验收人", id : "checkusername"},
 	    {title : "采购合同号", id : "pruorderno", wrapMdWidth:8},
 	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
 	    {title : "退货原因", id : "returnReason",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8,rules : {required:true}},
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
						"warehouseid" : data.warehouseid,
						"lotno" : data.lotno,
						"operuser" : data.operuser,
						"operusername" : data.operusername,
						"totalPrice" : data.totalPrice,
						"remark" : data.remark,
						"returnReason":returnReason,
						"processinstid" : data.processinstid
					};
				$("#autoform").iForm("setVal",loaddata);
			}
			
			if(null!=data.sheetno&&""!=data.sheetno){
				$("#autoform").iForm("endEdit",["pruorderno","sheetno"]);
			}
			if(null !=imrsid && ""!=imrsid){
				$("#autoform").iForm("endEdit");
				$("#btn_save").hide();
			}
			initFormStatus = $("#autoform").iForm("getVal");
			FW.fixToolbar("#toolbar");
		}
	});
}