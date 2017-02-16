var edit_form = [
		{title : "id信息", id : "imaid",type:"hidden"},
		{title : "申请单名称", id : "sheetname", rules : {required:true}},
		{title : "申请单号", id : "sheetno",type:"label"},
    	{title : "领料单类型", id : "applyType", rules:{required:true}, 
    		type : "combobox",
			dataType : "enum",
			enumCat : "INV_APPLY_TYPE"
    	},
	    {title : "备注", id : "remark",type : "textarea",linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "退库原因", id : "refundReason",type : "textarea", rules:{required:true},linebreak:true,wrapXsWidth:12, wrapMdWidth:8},
	    {title : "创建人id", id : "createuser",type:"hidden"}
];

	
//编辑表单加载数据（通用方法）
function initForm(){
	var options = {validate:true,labelFixWidth:160,xsWidth:4};
	if(location.href.indexOf("embed=1") > 0){
		options.labelFixWidth = 100;
		options.xsWidth = 6;
	}
	$("#autoform").iForm("init",{"fields":edit_form,"options":options});
	//加载用户表单数据
	$.ajax({
		type : "POST",
		async:false,
		url: basePath+"inventory/invmatapply/queryMatApplyForm.do",
		data: {"imaid":imaid},
		dataType : "json",
		success : function(data) {
				var loaddata = null;
				loaddata = {
						"imaid":imaid,
						"sheetname" : data.sheetname,
						"sheetno" : data.sheetno, 
						"applyType" : data.applyType,
						"remark" : data.remark,
						"createuser" : data.createuser,
						"refundReason":refundReason
					};
				if(embed == "1" || (imrsid != null && imrsid!="")){  //弹出框
					loaddata.refundReason = refundReason; //;
					$("#autoform").iForm("setVal",loaddata);
					$("#autoform").iForm("endEdit");
					$("#btn_submit").hide();
				}else{  //新建
					$("#autoform").iForm("setVal",loaddata);
					$("#autoform").iForm("endEdit",["sheetname","sheetno","applyType","remark"]);
				}
				FW.fixToolbar("#toolbar");
		}
	});
}